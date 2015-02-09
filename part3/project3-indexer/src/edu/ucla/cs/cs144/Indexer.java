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
	private int ID;
	private String name;
	private String categories;
	private String description;
	
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
	
	public String getID_string(){
		String value = Integer.toString(ID);
		return value;
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
	private IndexWriter indexWriter = null;
    /** Creates a new instance of Indexer */
    public Indexer() 
    {
	
    }
 
    public void rebuildIndexes() 
    {
    	//erase existing index
    	try{
    	 getIndexWriter();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    	 
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

        try
        {   
            Statement s = conn.createStatement();
            ResultSet itemSet = s.executeQuery("select i.Item_ID, i.Name, i.Description, c.Category from Items i, Categories c where 			i.Item_ID = c.Item_ID order by Item_ID");
  	   
	     int id;
             String name;
             String description;
             String c ;
 	     
     	    while(itemSet.next())
     	    {
 	     	
           id = itemSet.getInt("Item_ID");
           name = itemSet.getString("Name");
           description = itemSet.getString("Description");
           c = itemSet.getString("Category");
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
             Item now_item = map.get(id);
             String item_category = now_item.getCategory();
             now_item.setCategory(item_category+" "+c);
             map.put(id,now_item);
           }
         }
         
       }   
        
         catch (SQLException ex){
            System.out.println("SQLException caught");
            System.out.println("---");
            while ( ex != null ){
                System.out.println("Message   : " + ex.getMessage());
                System.out.println("SQLState  : " + ex.getSQLState());
                System.out.println("ErrorCode : " + ex.getErrorCode());
                System.out.println("---");
                ex = ex.getNextException();
            }
       }

          
           
           //index each of the items
           for(int i : map.keySet())
           {
                  
             Item item = map.get(i);
             indexItem(item);
            }
            
     	   
        // close the database connection
    	try 
        {
    	    conn.close();
    	} 
        catch (SQLException ex) 
        {
    	    System.out.println(ex);
    	}
    	
    	//erase existing index
    	try{
    	 closeIndexWriter();
    	}
    	catch(IOException e)
    	{
    		e.printStackTrace();
    	}
    }    

    public void indexItem(Item item)  
    {
    	
    	try {
    		System.out.println(item.getID());
        	
        	IndexWriter writer = getIndexWriter();
            Document doc = new Document();
            doc.add(new StringField("Item_ID", item.getID_string(), Field.Store.YES));
            doc.add(new StringField("Name", item.getName(), Field.Store.YES));
           
            String fullSearchableText = item.getName() + " " + item.getCategory()+ " " + item.getDescription();
            doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
            
            //write the document
            writer.addDocument(doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    public IndexWriter getIndexWriter() throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }    

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
       
    }   
}
