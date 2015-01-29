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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

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


class MyParser {
    private static int count=0;
    private static HashMap<String, User> allUsers = new HashMap();
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
    
    static class User
    {
        private String userID;
        private String sellerRating;
        private String bidderRating;
        private String location;
        private String country;
        
        public User()
        {
            userID = "\\N";
            sellerRating = "\\N";
            bidderRating = "\\N";
            location = "\\N";
            country = "\\N";
        }
        
        public String getUserID(){return userID;}
        public String getSellerRating(){return sellerRating;}
        public String getBidderRating(){return bidderRating;}
        public String getLocation(){return location;}
        public String getCountry(){return country;}
        
        public void setUserID(String s){userID = s;}
        public void setSellerRating(String s){sellerRating = s;}
        public void setBidderRating(String s){bidderRating = s;}
        public void setLocation(String s){location = s;}
        public void setCountry(String s){country = s;}
    }
    
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

        createCategories(root);
        createItems(root);
        createBids(root);
        createUsers(root);
    }
    
    /* to create the items load file*/
    public static void createItems(Element root)
    {
    	StringBuilder str = new StringBuilder("");
    	int i=1;
    	
    	Element[] items = getElementsByTagNameNR(root,"Item");
    	for(Element e : items)
        {
            str.append(e.getAttribute("ItemID")+columnSeparator);
            str.append(getElementTextByTagNameNR(e,"Name")+columnSeparator);

            //change time format to SQl insertion
            try{
                str.append(changeTimeFormat(getElementTextByTagNameNR(e,"Started"))+columnSeparator);
                str.append(changeTimeFormat(getElementTextByTagNameNR(e,"Ends"))+columnSeparator);
            }
            catch(ParseException t){
                    System.out.println(t);
            }

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

            String location = getElementTextByTagNameNR(e,"Location");
            str.append(location+columnSeparator);

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


            String country = getElementTextByTagNameNR(e,"Country");
            str.append(country+columnSeparator);

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
        writeToFile("items.csv", str.toString());
    }
  
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
    	writeToFile("categories.csv", str.toString());
    }

    public static void createBids(Element root)
    {
        Element[] items = getElementsByTagNameNR(root, "Item");
        String itemID;
        Element bids; 
        Element[] allBids;
        int numOfBids;
        StringBuilder str = new StringBuilder("");
        
        for(Element item : items)
        {
            bids = getElementByTagNameNR(item, "Bids");
            allBids = getElementsByTagNameNR(bids, "Bid");
            itemID = item.getAttribute("ItemID");
            numOfBids = allBids.length;

            try
            {
                for(Element e : allBids)
                {
                    Element currBidder = getElementByTagNameNR(e,"Bidder");
                    String currBidderUserID = currBidder.getAttribute("UserID");
                    String currBidTime = changeTimeFormat(getElementTextByTagNameNR(e, "Time"));
                    String bidAmount = strip(getElementTextByTagNameNR(e, "Amount"));
                    str.append(itemID+columnSeparator+currBidderUserID+columnSeparator+currBidTime+columnSeparator+bidAmount);
                    str.append("\n");
                }
            }
            catch(ParseException t){
                System.out.println(t);
            }
        }
        // Write to file/call load function here
        writeToFile("bids.csv", str.toString());
    }

   // CREATE LOAD FILE FOR USERS
    public static void createUsers(Element root)
    {
        Element[] items = getElementsByTagNameNR(root, "Item");
        Element[] bids;
        Element seller, bid, currBidder;
        String sellerUserID, bidderID, sellerRating, bidderRating,sellerLocation, bidderLocation, sellerCountry, bidderCountry, temp;
        
        for(Element item: items)
        {
            seller = getElementByTagNameNR(item, "Seller");
            sellerUserID = seller.getAttribute("UserID");
            sellerRating = seller.getAttribute("Rating");

            addToMap(sellerUserID, sellerRating, "\\N", "\\N","\\N", true);
            // Write to file/load the seller information
            try{
                bid = getElementByTagNameNR(item, "Bids");
                bids = getElementsByTagNameNR(bid, "Bid");
            }
            catch(NullPointerException t){
                return;
            }

            for(Element e : bids)
            {
                currBidder = getElementByTagNameNR(e, "Bidder");
                bidderID = currBidder.getAttribute("UserID");
                bidderRating = currBidder.getAttribute("Rating");
                try{
                bidderLocation = getElementTextByTagNameNR(currBidder, "Location");
                bidderCountry = getElementTextByTagNameNR(currBidder, "Country");
                }
                catch(NullPointerException t){
                        bidderLocation = "\\N";
                        bidderCountry = "\\N";
                }
                addToMap(bidderID,"\\N",bidderRating, bidderLocation, bidderCountry, false);

            }
        }
        
        StringBuilder str = new StringBuilder("");
    	for(String userID : allUsers.keySet())
        {
            User u = allUsers.get(userID);
            str.append(buildUserString(u));
    	}
        writeToFile("users.csv", str.toString());
    }

    public static void addToMap(String userID, String sellerRating,String bidderRating, String location, String country, boolean isSeller)
    {
        User u;
        if(isSeller)
        {
            if(!allUsers.containsKey(userID))
            {
                u = new User();
                u.setUserID(userID); u.setSellerRating(sellerRating); u.setBidderRating(bidderRating); u.setLocation(location); u.setCountry(country);
                allUsers.put(userID, u);
            }
            else
            {
                u = allUsers.get(userID);
                if(u.getSellerRating().equals("\\N"))
                    u.setSellerRating(sellerRating);
            }
        }

        if(!isSeller)
        {
            if(!allUsers.containsKey(userID))
            {
                u = new User();
                u.setUserID(userID); u.setSellerRating(sellerRating); u.setBidderRating(bidderRating); u.setLocation(location); u.setCountry(country);
                allUsers.put(userID, u);
            }
            else
            {
                u = allUsers.get(userID);
                if(u.getBidderRating().equals("\\N"))
                    u.setBidderRating(bidderRating);
            }
        }
    }
    
    public static void writeToFile(String fileName, String input)
    {
        FileWriter f;
        try 
        {
            f = new FileWriter(fileName,true);

            f.write(input);
            f.flush();
            f.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    public static String buildUserString(User u)
    {
        StringBuilder str = new StringBuilder("");
        str.append(u.userID);
        str.append(columnSeparator);
        str.append(u.sellerRating);
        str.append(columnSeparator);
        str.append(u.bidderRating);
        str.append(columnSeparator);
        str.append(u.location);
        str.append(columnSeparator);
        str.append(u.country);
        str.append(columnSeparator);
        str.append("\n");
        return str.toString();
    }
    
    // changes xml date to yyyy-MM-dd HH:mm:ss
    public static String changeTimeFormat(String t) throws ParseException
    {
        DateFormat xmlDate = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        DateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return newDate.format(xmlDate.parse(t));
    }
          
    
    public static void main (String[] args) 
    {
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
