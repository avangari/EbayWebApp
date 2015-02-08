package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


class Item {
	int ID;
	String name;
	String categories;
	String description;
	
	Item(){}
	
	public void setID(int id)
	{
		ID = id;
	}
	
	public void setName(String name ){
		
		this.name = name;
	}
	
	public void setCategory(String category){
	       this.categories = category;
	}
	
	public void setDescription(String desc)
	{
		description = desc;
	}
	
	public int getID(){
		return ID;
	}
	
	public String getName(){
		return name;
	}
	
	public String getCategory(){
		return categories;
	}
	
	public String getDescription()
	{
		return description;
	}
}

public class Indexer 
{
    /** Creates a new instance of Indexer */
    public Indexer() 
    {
	
    }
 
    public void rebuildIndexes() 
    {
        Connection conn = null;
        // create a connection to the database to retrieve Items from MySQL
    	try 
        {
    	    conn = DbManager.getConnection(true);
    	} 
        catch (SQLException ex) 
        {
    	    System.out.println(ex);
    	}
 	    HashMap<Integer, Item> map = new HashMap<Integer, Item> ();
	    Statement s = conn.createStatement();
            ResultSet itemSet = s.executeQuery("select i.Item_ID, i.Name, i.Description, c.Category from Items i, Categories c where 			i.Item_ID = c.Item_ID order by Item_ID");
  	   
	     int id;
             String name;
             String description;
             String c ;
 	     
     	    while(itemset.next())
	  {
 	     	
           id = itemset.getInt("Item_ID");
           name = itemset.getString("Name");
           description = itemset.getString("Description");
           c = itemset.getString("Category");
           if(!map.containsKey(id) )
	   {
             Item i = new Item();
             i.setName(name);
             i.setID(id);
             i.setDescription(description);
             i.setCategory(c);
             map.put(id,i);
           }
           else
           {
             Item i = map.get(id);
             String s = i.getCategory();
             i.setCategory(s+" "+c);
             i.put(id,i);
           }
            
           }

           System.out.println(map.size());  
           /*for(int i : map.keySet())
           {
                  
             Item item = map.get(i);
             //indexItem(item);
            }
            */
     	   
        // close the database connection
    	try 
        {
    	    conn.close();
    	} 
        catch (SQLException ex) 
        {
    	    System.out.println(ex);
    	}
    }    

    /*public void indexItem(Item item) throws IOException 
    {
        Directory indexDir = FSDirectory.open(new File("index-directory"));
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
        IndexWriter indexWriter = new IndexWriter(indexDir, config);
        Document doc = new Document();
        StringBuilder itemCategories = new StringBuilder("");
        String allInfo;

        doc.add(new Field("Item_ID", item.getID(), Field.Store.YES));
        doc.add(new Field("Item_Name", item.getName(), Field.Store.YES));
        doc.add(new Field("Description", item.getDescription(), Field.Store.YES);
        
        for(String s: item.getCategories)
        {
            itemCategories.append(s);
            itemCategories.append(" ");
        }

        doc.add(new Field("Categories", itemCategories.toString(), Field.Store.YES);
       
        allInfo = hotel.getName() + " " + hotel.getCity() + " " + hotel.getDescription();
        doc.add(new Field("All_Info", allInfo, Field.Store.YES, Field.Index.TOKENIZED));

        writer.addDocument(doc);
    }*/

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
