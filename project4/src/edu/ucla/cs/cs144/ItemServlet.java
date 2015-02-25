// package edu.ucla.cs.cs144;

// import java.util.LinkedList;
// import java.util.Vector;
// import java.io.IOException;
// import java.io.StringReader;
// import java.util.Date;
// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
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
import org.w3c.dom.Text;
import org.xml.sax.ErrorHandler;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        
        String ID = request.getParameter("itemId");
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
   static Document doc;
   
   public static Item loadXMLFromString(String xml) 
   {
       
       try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = builder.parse(is);
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

        to_return.name = doc.getElementsByTagName("Name").item(0).getTextContent();

        //change time format to SQl insertion
        try{
            to_return.started = new Date(changeTimeFormat(doc.getElementsByTagName("Started").item(0).getTextContent()));
            to_return.ends = new Date(changeTimeFormat(doc.getElementsByTagName("Ends").item(0).getTextContent()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        to_return.current_bid = Double.parseDouble(doc.getElementsByTagName("Currently").item(0).getTextContent().substring(1));
        to_return.first_bid =  Double.parseDouble(doc.getElementsByTagName("First_Bid").item(0).getTextContent().substring(1));
        /******* if the Buy price is specified, use it***/
        // if(doc.getElementsByTagName("Buy_Price").item(0).getTextContent() != null)
        //     to_return.buy_price = Double.parseDouble(doc.getElementsByTagName("Buy_Price").item(0).getTextContent().substring(1));
        
        to_return.no_of_bids = Integer.parseInt(doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent());

        //allocate the size for bids array
        to_return.allocateBids();

        //NodeList sellers = doc.getElementsByTagName("Seller");
        to_return.seller_id = ((Element)doc.getElementsByTagName("Seller").item(0)).getAttribute("UserID");
        to_return.seller_rating = Integer.parseInt(((Element)doc.getElementsByTagName("Seller").item(0)).getAttribute("Rating"));  

        to_return.location = doc.getElementsByTagName("Location").item(0).getTextContent();
        
        //Getting the latitude and longitude
        String latitude = ((Element)doc.getElementsByTagName("Location").item(0)).getAttribute("Latitude");
        if(latitude != "")
           to_return.latitude = latitude;

        String longitude = ((Element)doc.getElementsByTagName("Location").item(0)).getAttribute("Longitude");
        if(longitude != "")
            to_return.longitude = longitude;


        String country = doc.getElementsByTagName("Country").item(0).getTextContent();
        to_return.country = country;

        //description
        try
        {
            String desc = doc.getElementsByTagName("Description").item(0).getTextContent();
            //Check for the description length and cut it down
            to_return.decription = desc;
        }
        catch(NullPointerException n){
              to_return.decription = "";
        }    
           
        //fill up the caetgories now.
        
        NodeList categories = doc.getElementsByTagName("Category");  
        for(int i = 0; i < categories.getLength(); i++)
        {
            to_return.categories.add(categories.item(i).getTextContent());
        }
    
        // //fill up bids now
        // Element bids = getElementByTagNameNR(item, "Bids");
        // Element[] allBids = getElementsByTagNameNR(bids, "Bid");
        // if (to_return.bids.length != allBids.length)
        // {
        //     to_return.bids = null;
        // }
        
        // int k = 0;
        // try
        // {
        //     for(Element e : allBids)
        //     {
        //         Element currBidder = getElementByTagNameNR(e,"Bidder");
        //         String currBidderUserID = currBidder.getAttribute("UserID");
        //         String currBidderRating = currBidder.getAttribute("Rating");
        //         String bidderLocation = getElementTextByTagNameNR(currBidder,"Location"); 
        //         String bidderCountry =  getElementTextByTagNameNR(currBidder,"Country");
        //         String currBidTime = changeTimeFormat(getElementTextByTagNameNR(e, "Time"));
        //         String bidAmount = getElementTextByTagNameNR(e, "Amount");
        //         //set the bid object 
        //         to_return.bids[k].bidder_id = currBidderUserID; 
        //         to_return.bids[k].bid_date = new Date(currBidTime);
        //         to_return.bids[k].amount = Double.parseDouble(bidAmount);
        //         to_return.bids[k].bidder_rating = Integer.parseInt(currBidderRating);
        //         to_return.bids[k].location = bidderLocation;
        //         to_return.bids[k].country = bidderCountry;
        //     }
        // }
        // catch(ParseException t){
        //     System.out.println(t);
        // }
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