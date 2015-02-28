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
import edu.ucla.cs.cs144.Bid;


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

public class ItemServlet extends HttpServlet implements Servlet 
{
    public ItemServlet() {}
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //response.setContentType("text/html");
        String ID = request.getParameter("itemId");
        String xmlString = AuctionSearchClient.getXMLDataForItemId(ID);
        Item item = MyParser.loadXMLFromString(xmlString);
        request.setAttribute("item",item);
        request.getRequestDispatcher("/item.jsp").forward(request, response);
    }  
}

class MyParser 
{
    private static int count=0;
    static DocumentBuilder builder;
    static Element root;
    static Document doc;

    public static Item loadXMLFromString(String xml) 
    {
        try 
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            doc = builder.parse(is);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return createItem();
    }
    
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    // changes xml date to yyyy-MM-dd HH:mm:ss
    public static Date changeTimeFormat(String t) throws ParseException
    {
        DateFormat xmlDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return newDate.parse(newDate.format(xmlDate.parse(t)));
    }

    static Element[] getElementsByTagNameNR(Element e, String tagName) 
    {
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
   
   private static Item createItem()
   {
        Item to_return = new Item();
        Element item = doc.getDocumentElement();
        
        to_return.setName(doc.getElementsByTagName("Name").item(0).getTextContent());
        String id = item.getAttribute("ItemID");
        
        to_return.setItem_ID(Integer.parseInt(id));
        //change time format to SQl insertion
        try{
            to_return.setStarted(changeTimeFormat(doc.getElementsByTagName("Started").item(0).getTextContent()));
            to_return.setEnds(changeTimeFormat(doc.getElementsByTagName("Ends").item(0).getTextContent()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        to_return.setCurrent_bid(Double.parseDouble(doc.getElementsByTagName("Currently").item(0).getTextContent().substring(1)));
        to_return.setFirst_bid(Double.parseDouble(doc.getElementsByTagName("First_Bid").item(0).getTextContent().substring(1)));
        
        /******* if the Buy price is specified, use it***/
        if(doc.getElementsByTagName("Buy_Price").item(0) != null)
        {
            if(Double.parseDouble(doc.getElementsByTagName("Buy_Price").item(0).getTextContent().substring(1)) >= 0.0)
                to_return.setBuy_price(Double.parseDouble(doc.getElementsByTagName("Buy_Price").item(0).getTextContent().substring(1)));
        }
        to_return.setNo_of_bids(Integer.parseInt(doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent()));
        to_return.setSeller_id(((Element)doc.getElementsByTagName("Seller").item(0)).getAttribute("UserID"));
        to_return.setSeller_rating(Integer.parseInt(((Element)doc.getElementsByTagName("Seller").item(0)).getAttribute("Rating")));  

        to_return.setLocation(doc.getElementsByTagName("Location").item(0).getTextContent());
        
        //Getting the latitude and longitude
        String latitude = getElementByTagNameNR(item,"Location").getAttribute("Latitude");
        if(latitude != null)
           to_return.setLatitude(latitude);

        String longitude = getElementByTagNameNR(item,"Location").getAttribute("Longitude");
        if(longitude != null )
            to_return.setLongitude(longitude);

        String country = doc.getElementsByTagName("Country").item(0).getTextContent();
        to_return.setCountry(country);

        //description
        try
        {
            String desc = doc.getElementsByTagName("Description").item(0).getTextContent();
            //Check for the description length and cut it down
            to_return.setDecription(desc);
        }
        catch(NullPointerException n)
        {
            to_return.setDecription("");
        }    
           
        //fill up the caetgories
        NodeList categories = doc.getElementsByTagName("Category");  
        for(int i = 0; i < categories.getLength(); i++)
        {
            to_return.addToCategories(categories.item(i).getTextContent());
        }
        
        Element rootBid = (Element)doc.getElementsByTagName("Bids").item(0);
        Element[] bids = (Element[])getElementsByTagNameNR(rootBid, "Bid");
        if(bids.length > 0)
        {
            for(Element e: bids)
            {
                Bid temp = new Bid();
                String currBidderUserID = ((Element)e.getElementsByTagName("Bidder").item(0)).getAttribute("UserID"); 
                String currBidderRating = ((Element)e.getElementsByTagName("Bidder").item(0)).getAttribute("Rating"); 
                
                Element rootBidder = (Element)e.getElementsByTagName("Bidder").item(0);
                Element[] bidLocation = (Element[])getElementsByTagNameNR(rootBidder, "Location");
                Element[] bidCountry = (Element[])getElementsByTagNameNR(rootBidder, "Country");

                String bidderLocation = bidLocation[0].getTextContent();
                String bidderCountry =  bidCountry[0].getTextContent();
                
                String bidAmount = ((Element)e.getElementsByTagName("Amount").item(0)).getTextContent().substring(1);
                Date currBidTime = null;
                try
                {
                    currBidTime = changeTimeFormat( ((Element)e.getElementsByTagName("Time").item(0)).getTextContent() );
                   
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                //set the bid object 
                temp.setBidder_id(currBidderUserID); 
                temp.setBid_date(currBidTime);
                temp.setAmount(Double.parseDouble(bidAmount));
                temp.setBidder_rating(Integer.parseInt(currBidderRating));
                temp.setLocation(bidderLocation);
                temp.setCountry(bidderCountry);
                
                to_return.addToBids(temp);
            }
        }
        to_return.sortBids();
        return to_return;
   }
}