package edu.ucla.cs.cs144;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		try{
		// instantiate the search engine
		SearchEngine se = new SearchEngine();
		
		
		TopDocs topDocs = se.performSearch(query); 

		// obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
		ScoreDoc[] hits = topDocs.scoreDocs;
		System.out.println("the number of matches found is  :"+ hits.length);
		
		int len = (hits.length > numResultsToSkip+numResultsToReturn) ? numResultsToSkip+numResultsToReturn : hits.length;
		
		//if the query returns less than the num results to skip results, we just return a null;
		if(hits.length < numResultsToSkip)
		{
			return null;
		}
		
		SearchResult[] sr = new SearchResult[numResultsToReturn+numResultsToSkip];
		
		// retrieve each matching document from the ScoreDoc arry
		for (int i = numResultsToSkip,j=0; i < len; i++,j++) {
			Document doc= se.getDocument(hits[i].doc);
					    
		    String itemID = doc.get("Item_ID");
		    String name = doc.get("Name");
		    sr[j] = new SearchResult(itemID, name);
		  
		}
		
		
		return sr;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		
		try{
			// instantiate the search engine
			SearchEngine se = new SearchEngine();
			
				LinkedList<Integer> list = new LinkedList<Integer>();
				HashMap<String, String> hash = new HashMap<String,String>();
	    	    Connection conn = DbManager.getConnection(true);
	    	    Statement statement = conn.createStatement(); 
			
			TopDocs topDocs = se.performSearch(query); 

			// obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
			ScoreDoc[] hits = topDocs.scoreDocs;
			System.out.println("the number of matches found from the keyword query is  :"+ hits.length);
			
			//a list of items returned from the keyword query
			StringBuilder items = new StringBuilder("(");
			for(int i=0; i<hits.length; i++)
			{
				Document doc =  se.getDocument(hits[i].doc);
				String itemID = doc.get("Item_ID");
				String name = doc.get("Name");
				hash.put(itemID,name);
			    items.append(itemID);
			    items.append(',');
				
			}
			items.deleteCharAt(items.length()-1);
			items.append(")");
			
			//at this point, the items string has list of item ids (id1,id2,id3.....);
			//now we can execute the query and get the results.
			String polygon = "'POLYGON (("+region.getLx()+" "+region.getLy()+","+region.getLx()+" "+region.getRy()+","+region.getRx()+" "+region.getRy()+","+region.getRx()+" "+region.getLy()+","+region.getLx()+" "+region.getLy()+"))'" ;  
    	    ResultSet itemset = statement.executeQuery("Select Item_ID from Items_Coord where MBRContains(GeomFromText("+polygon+"),Coordinates) and Item_Id in "+items+" ");
			while(itemset.next())
			{
				int id = itemset.getInt("Item_ID");
				list.addLast(id);				
			}
			
			int return_length = list.size();
			//if the query returns less than the num results to skip results, we just return a null;
			if(return_length <= numResultsToSkip)
			{
				return null;
			}
			
			for(int i=0;i<numResultsToSkip;i++)
			{
				list.remove(list.size()-1);
			}
			
			int len = (list.size() > numResultsToReturn)? numResultsToReturn:list.size();
			SearchResult[] sr = new SearchResult[len];
			for(int i=0;i<len;i++)
			{
				String id = Integer.toString(list.get(i));
				String name = hash.get(id);
				sr[i] = new SearchResult(id,name);
			}
			return sr;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String getXMLDataForItemId(String itemId) 
	{
		Connection conn = null;
		try
		{
			conn = DbManager.getConnection(true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		String temp = "", name = "", started = "", ends = "", currently = "$", firstBid = "$", buyPrice = "$", 
		numOfBids = "", sellerId = "", location = "", locationTwo = "", country = "", description = "", 
		rating = "", bidderId = "";
		Float longitude = null, latitude = null;
		StringBuilder xml = new StringBuilder("");
		String queryItems = "select * from Items where Item_ID ='" + itemId + "'";
		String queryCategories = "select * from Categories where Item_ID='" + itemId + "'";
		String queryBids = "select * from Bids where Item_ID='" + itemId + "'";
		String sellerQuery;

        try
        {
			Statement s = conn.createStatement();
			ResultSet results = s.executeQuery(queryItems);

			while (results.next()) 
			{
				name = results.getString("Name").replace("<","&lt;").replace(">","&gt;");
				started = results.getString("Started").toString();
				ends = results.getTimestamp("Ends").toString();
				currently += String.format("%.2f", results.getFloat("Currently"));
				firstBid += String.format("%.2f", results.getFloat("First_Bid"));
				buyPrice += String.format("%.2f", results.getFloat("Buy_Price"));
				numOfBids = Integer.toString(results.getInt("No_of_Bids"));
				sellerId = results.getString("Seller_ID");
				description = results.getString("Description").toString().replace("<","&lt;").replace(">","&gt;");
				longitude = results.getFloat("Longitude");
				latitude = results.getFloat("Latitude");
				location = results.getString("Location");
			}
			xml.append("<Item ItemID=\""+itemId.toString()+"\">\n");
			xml.append(getTag("Name", name));

			results = s.executeQuery(queryCategories);

			while (results.next()) 
			{
				xml.append(getTag("Category", results.getString("Category").replace("<","&lt;").replace(">","&gt;")));
			}
			xml.append(getTag("Currently", currently));

			if (buyPrice.compareTo("$0.00") != 0)
				xml.append(getTag("Buy_Price", buyPrice));

			xml.append(getTag("First_Bid", firstBid));
			xml.append(getTag("Number_of_Bids", numOfBids));			
			xml.append("<Bids>\n");

			results = s.executeQuery(queryBids);

			while (results.next()) 
			{
				try
				{
					Statement sTwo = conn.createStatement();
					ResultSet resultsTwo = sTwo.executeQuery("select * from Users where User_ID ='"+results.getString("User_ID")+"'");
					while(resultsTwo.next())
					{
						rating = resultsTwo.getString("Bidder_Rating");
						locationTwo = resultsTwo.getString("Location");
						country = resultsTwo.getString("Country");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				xml.append("<Bid>\n");
				xml.append("<Bidder Rating=\"");
				xml.append(rating);
				xml.append("\" UserID=\"");
				xml.append(results.getString("User_ID"));
				xml.append("\">\n");
				xml.append(getTag("Location", locationTwo));
				xml.append(getTag("Country", country));
				xml.append("</Bidder>\n");
				xml.append(getTag("Time", changeTimeFormat(results.getTimestamp("Time").toString())));
				xml.append(getTag("Amount", "$"+String.format("%.2f", results.getFloat("Amount"))));
			}
			xml.append("</Bids>\n");

			sellerQuery = "select * from Users where User_ID='" + sellerId + "'";
			results = s.executeQuery(sellerQuery);

			while (results.next()) 
			{
				rating = Integer.toString(results.getInt("Seller_Rating"));
				if(!Float.toString(longitude).equals("0.0") && !Float.toString(latitude).equals("0.0"))
				{
					xml.append("<Location Latitude=\""+latitude+"\" Longitude=\""+longitude+"\">"+location+"</Location>\n");
				}
				else
					xml.append(getTag(("Location"), location));
				xml.append(getTag(("Country"), results.getString("Country")));
			}
			xml.append(getTag(("Started"), changeTimeFormat(started)));
			xml.append(getTag(("Ends"), changeTimeFormat(ends)));
			xml.append("<Seller Rating=\""+rating+"\" UserID=\""+sellerId+"\"/>\n");
			xml.append(getTag(("Description"), description));
			xml.append("</Item>");
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
        return xml.toString().replace("&","&amp;");
	}
	
	public String getTag(String tag, String value)
	{
		StringBuilder temp = new StringBuilder("");
		temp.append("<");
		temp.append(tag);
		temp.append(">");
		temp.append(value);
		temp.append("</");
		temp.append(tag);
		temp.append(">\n");
		return temp.toString();
	}

	public static String changeTimeFormat(String t)
    {
        DateFormat xmlDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
        	return xmlDate.format(newDate.parse(t));
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        return null;
    }
	
	public String echo(String message) {
		return message;
	}

}

class SearchEngine {
    private IndexSearcher searcher = null;
    private QueryParser parser = null;

    /** Creates a new instance of SearchEngine */
    public SearchEngine() throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1"))));
        parser = new QueryParser("content", new StandardAnalyzer());
    }
    
    public TopDocs performSearch(String queryString)
    throws IOException, ParseException {
        Query query = parser.parse(queryString);        
        return searcher.search(query,19532);
    }

    public Document getDocument(int docId)
    throws IOException {
        return searcher.doc(docId);
    }
}

