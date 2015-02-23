package edu.ucla.cs.cs144;

import java.util.LinkedList;
import java.util.Vector;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.io.PrintWriter;
import java.io.File;
import java.text.ParseException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	response.setContentType("text/html");
    	
    	String ID = request.getParameter("id");
    	String xmlString = AuctionSearchClient.getXMLDataForItemId(ID);
    	
    	
    	Item item = MyParser.loadXMLFromString(xmlString);
    	
    	request.setAttribute("item",item);
    	
    	request.getRequestDispatcher("/item.jsp").forward(request, response);
    }
    
  
}

class MyParser {
   private static int count=0;
   static DocumentBuilder builder;
   static Element root;
   
   static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
   };
   
   
   
   static class MyErrorHandler implements ErrorHandler {
       
       public void warning(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void error(SAXParseException exception)
       throws SAXException {
           fatalError(exception);
       }
       
       public void fatalError(SAXParseException exception)
       throws SAXException {
           exception.printStackTrace();
           System.out.println("There should be no errors " +
                              "in the supplied XML files.");
           System.exit(3);
       }
       
   }
   
   /* Non-recursive (NR) version of Node.getElementsByTagName(...)
    */
   static Element[] getElementsByTagNameNR(Element e, String tagName) {
       Vector< Element > elements = new Vector< Element >();
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Element && child.getNodeName().equals(tagName))
           {
               elements.add( (Element)child );
           }
           child = child.getNextSibling();
       }
       Element[] result = new Element[elements.size()];
       elements.copyInto(result);
       return result;
   }
   
   /* Returns the first subelement of e matching the given tagName, or
    * null if one does not exist. NR means Non-Recursive.
    */
   static Element getElementByTagNameNR(Element e, String tagName) {
       Node child = e.getFirstChild();
       while (child != null) {
           if (child instanceof Element && child.getNodeName().equals(tagName))
               return (Element) child;
           child = child.getNextSibling();
       }
       return null;
   }
   
   /* Returns the text associated with the given element (which must have
    * type #PCDATA) as child, or "" if it contains no text.
    */
   static String getElementText(Element e) {
       if (e.getChildNodes().getLength() == 1) {
           Text elementText = (Text) e.getFirstChild();
           return elementText.getNodeValue();
       }
       else
           return "";
   }
   
   /* Returns the text (#PCDATA) associated with the first subelement X
    * of e with the given tagName. If no such X exists or X contains no
    * text, "" is returned. NR means Non-Recursive.
    */
   static String getElementTextByTagNameNR(Element e, String tagName) {
       Element elem = getElementByTagNameNR(e, tagName);
       if (elem != null)
           return getElementText(elem);
       else
           return "";
   }
   
   
   
      
      
   
   public static Item loadXMLFromString(String xml) 
   {
	   
	   try {
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           factory.setValidating(false);
           factory.setIgnoringElementContentWhitespace(true);      
           builder = factory.newDocumentBuilder();
           builder.setErrorHandler(new MyErrorHandler());
           InputSource is = new InputSource(new StringReader(xml));
           Document doc =  builder.parse(is);
           root = doc.getDocumentElement();
       }
       catch (FactoryConfigurationError e) {
           System.out.println("unable to get a document builder factory");
           System.exit(2);
       } 
       catch (ParserConfigurationException e) {
           System.out.println("parser was unable to be configured");
           System.exit(2);
       }
	  catch(Exception e)
	   {
		  e.printStackTrace();
	   }
       
       return createItem();
   }
   
   private static Item createItem()
   {
   		Item to_return = new Item();
	    Element item = getElementByTagNameNR(root,"Item");
	   
          // to_return.Item_ID =  Integer.parseInt(item.getAttribute("ItemID"));
           to_return.name = getElementTextByTagNameNR(item,"Name");

           //change time format to SQl insertion
           try{
               to_return.started = new Date(changeTimeFormat(getElementTextByTagNameNR(item,"Started")));
               to_return.ends = new Date(changeTimeFormat(getElementTextByTagNameNR(item,"Ends")));
           }
           catch(ParseException t){
                
           }

            to_return.current_bid = Double.parseDouble(getElementTextByTagNameNR(item,"Currently"));
            to_return.first_bid =  Double.parseDouble(getElementTextByTagNameNR(item,"First_Bid"));
           /******* if the Buy price is specified, use it***/
           if(getElementTextByTagNameNR(item,"Buy_Price") != ""){
        	   to_return.buy_price = Double.parseDouble(getElementTextByTagNameNR(item,"Buy_Price")) ;
           }
           
           /********************************************/
           to_return.no_of_bids = Integer.parseInt(getElementTextByTagNameNR(item,"Number_of_Bids"));
           //allocate the size for bids array
           to_return.allocateBids();
           
           to_return.seller_id = getElementByTagNameNR(item,"Seller").getAttribute("UserID");
          

           to_return.location = getElementTextByTagNameNR(item,"Location");
           //Getting the latitude and longitude
           String latitude = getElementByTagNameNR(item,"Location").getAttribute("Latitude");
           if(latitude != "")
                   to_return.latitude = latitude;

           String longitude = getElementByTagNameNR(item,"Location").getAttribute("Longitude");
           if(longitude != "")
        	   to_return.longitude = longitude;


           String country = getElementTextByTagNameNR(item,"Country");
           to_return.country = country;

           //description
           try{
           String desc = getElementByTagNameNR(item,"Description").getFirstChild().getNodeValue(); 
           //Check for the description length and cut it down
           to_return.decription = desc;
           }
           catch(NullPointerException n){
                  to_return.decription = "";
           }	
           
           Element seller = getElementByTagNameNR(item, "Seller");
           String seller_id = seller.getAttribute("UserID");
           String seller_rating = seller.getAttribute("Rating");
           
           to_return.seller_id = seller_id;
           to_return.seller_rating = Integer.parseInt(seller_rating);
           
           
           //fill up the caetgories now.
           
        Element[] categories = getElementsByTagNameNR(item,"Category");
   		for(Element c : categories){
   		to_return.categories.add(c.getFirstChild().getNodeValue());
   			
   		}
   	
   		//fill up bids now
   		Element bids = getElementByTagNameNR(item, "Bids");
   		Element[] allBids = getElementsByTagNameNR(bids, "Bid");
        if (to_return.bids.length != allBids.length)
        {
        	to_return.bids = null;
        }
        
        int k = 0;
        try
        {
            for(Element e : allBids)
            {
                Element currBidder = getElementByTagNameNR(e,"Bidder");
                String currBidderUserID = currBidder.getAttribute("UserID");
                String currBidderRating = currBidder.getAttribute("Rating");
                String bidderLocation = getElementTextByTagNameNR(currBidder,"Location"); 
                String bidderCountry =  getElementTextByTagNameNR(currBidder,"Country");
                String currBidTime = changeTimeFormat(getElementTextByTagNameNR(e, "Time"));
                String bidAmount = getElementTextByTagNameNR(e, "Amount");
                //set the bid object 
                to_return.bids[k].bidder_id = currBidderUserID; 
                to_return.bids[k].bid_date = new Date(currBidTime);
                to_return.bids[k].amount = Double.parseDouble(bidAmount);
                to_return.bids[k].bidder_rating = Integer.parseInt(currBidderRating);
                to_return.bids[k].location = bidderLocation;
                to_return.bids[k].country = bidderCountry;
            }
        }
        catch(ParseException t){
            System.out.println(t);
        }
        
        return to_return;
   }
	   
	
   
   
  
  

    
   
   
   // changes xml date to yyyy-MM-dd HH:mm:ss
   public static String changeTimeFormat(String t) throws ParseException
   {
       DateFormat xmlDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
       DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       return newDate.format(xmlDate.parse(t));
   }
   
  
}










