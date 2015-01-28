/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
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
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element root = doc.getDocumentElement();
        
        System.out.println(root.getNodeName());
        
        
        //createCategories(root);
        createItems(root);
        /**************************************************************/
        
    }
    /**********************************************/
    /* to create the items load file*/
 public static void createItems(Element root){
    	
    	StringBuilder str = new StringBuilder("");
    	int i=1;
    	
    	Element[] items = getElementsByTagNameNR(root,"Item");
    	for(Element e : items){
    		str.append(e.getAttribute("ItemID")+columnSeparator);
    		str.append(getElementTextByTagNameNR(e,"Name")+columnSeparator);
    		str.append(getElementTextByTagNameNR(e,"Started")+columnSeparator);
    		str.append(getElementTextByTagNameNR(e,"Ends")+columnSeparator);
    		str.append( strip ( getElementTextByTagNameNR(e,"Currently") ) + columnSeparator);
    		str.append( strip ( getElementTextByTagNameNR(e,"First_Bid") ) + columnSeparator);
    		/******* if the Buy price is specified, use it***/
    		if(getElementTextByTagNameNR(e,"Buy_Price") != ""){
    		str.append( strip ( getElementTextByTagNameNR(e,"Buy_Price") ) + columnSeparator);
    		}
    		else 
    			str.append("\\N"+columnSeparator);
    		/********************************************/
    		str.append(getElementTextByTagNameNR(e,"Number_of_Bids")+columnSeparator);
    		
    		String seller_id = getElementByTagNameNR(e,"Seller").getAttribute("UserID");
    		str.append(seller_id+columnSeparator);
    		
    		
    		
    		
    		
    		//Getting the latitude and longitude
    		String latitude = getElementByTagNameNR(e,"Location").getAttribute("Latitude");
    		if(latitude != "")
    			str.append(latitude+columnSeparator);
    		else 
    			str.append("\\N"+columnSeparator);
    		
    		String longitude = getElementByTagNameNR(e,"Location").getAttribute("Longitude");
    		if(longitude != "")
    			str.append(longitude+columnSeparator);
    		else 
    			str.append("\\N"+columnSeparator);
    		
    		
    		
    		//description
    		try{
    		String desc = getElementByTagNameNR(e,"Description").getFirstChild().getNodeValue(); 
    		//Check for the description length and cut it down
    		if(desc.length()>4000){
    			String desc1 = desc.substring(0, 4000);
    			str.append(desc1+"\n");
    		}
    		else	
    			str.append(desc+"\n");
    		}
    		catch(NullPointerException n){
    			str.append("\\N"+"\n");
    		}
    		
    		
    		
    		
    		
    	}
    	
    	
    	FileWriter f;
		try {
			f = new FileWriter("/home/naren/items.csv");
			
			f.write(str.toString());
			f.flush();
	    	f.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	 
    }
  
    /*******************************************************/
    // to create the load file for the categories table
    public static void createCategories(Element root) {
    	StringBuilder str = new StringBuilder("");
    	
    	Element[] items = getElementsByTagNameNR(root,"Item");
    	for(Element e : items){
    		Element[] categories = getElementsByTagNameNR(e,"Category");
    		for(Element c : categories){
    			str.append(e.getAttribute("ItemID")+columnSeparator+c.getFirstChild().getNodeValue()+"\n");
    			
    		}
    		
    	}
    	
    	
    	FileWriter f;
		try {
			f = new FileWriter("/home/naren/categories.csv");
			
			f.write(str.toString());
			f.flush();
	    	f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    }
    /*******************************************************************/

    // changes xml date to yyyy-MM-dd HH:mm:ss
    public static String changeTimeFormat(String t) throws ParseException
    {
        DateFormat xmlDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return newDate.format(xmlDate.parse(t));
    }

    public static void createBids(Element item) throws ParseException
    {
        String itemID;
        Element bids; 
        Element[] allBids;
        int numOfBids;

        bids = getElementByTagNameNR(item, "Bids");
        allBids = getElementsByTagNameNR(bids, "Bid");
        itemID = item.getAttribute("ItemID");
        numOfBids = allBids.length;

        for(Element e : allBids)
        {
            Element currBidder = getElementByTagNameNR(e, "Bidder");
            String currBidderUserID = currBidder.getAttribute("UserID");
            String currBidTime = changeTimeFormat(getElementTextByTagNameNR(e, "Time"));
            String bidAmount = strip(getElementTextByTagNameNR(e, "Amount"));
            System.out.println(currBidderUserID + " " + currBidTime + " " + bidAmount);
            
            // Write to file/call load function here
        }
    }

    public static void createUsers(Element item)
    {
        Element[] bids;
        Element seller, bid, currBidder;
        String sellerUserID, bidderID, sellerRating, bidderRating, 
                sellerLocation, bidderLocation, sellerCountry, bidderCountry;
        
        sellerLocation = getElementText(getElementByTagNameNR(item, "Location"));
        sellerCountry = getElementText(getElementByTagNameNR(item, "Country"));
        seller = getElementByTagNameNR(item, "Seller");
        sellerUserID = seller.getAttribute("UserID");
        sellerRating = seller.getAttribute("SellerRating");
        
        // Write to file/load the seller information
        
        bid = getElementByTagNameNR(item, "Bid");
        bids = getElementsByTagNameNR(bid, "Bids");
        
        for(Element e : bids)
        {
            currBidder = getElementByTagNameNR(e, "Bidder");
            bidderID = currBidder.getAttribute("UserID");
            bidderRating = currBidder.getAttribute("Rating");
            bidderLocation = getElementTextByTagNameNR(currBidder, "Location");
            bidderCountry = getElementTextByTagNameNR(currBidder, "Country");
            
            // Write to file/load the bidder information 
        }
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
