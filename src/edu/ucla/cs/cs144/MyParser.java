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
    
    static LinkedHashMap<Integer, Item> itemMap;
    static LinkedHashMap<Integer, User> userMap;
    static LinkedHashMap<Integer, String> categoryIDMap;
    static LinkedHashMap<String, Integer> categoryStringMap;
    static ArrayList<Bid> bidsArray;
    
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
        
        
        // Get all Item elements
        Element documentRoot = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR(documentRoot, "Item");

		// Process each element
        for(int i = 0; i < items.length; i++)
        {
			// Create an Item by parsing the the "Item" element
			Item currentItem = parseItem(items[i]);
			
			// Add the resulting item to items[]
			addItem(currentItem);
        }
        
        /**************************************************************/
        
    }
    
  
    static Item parseItem(Element itemElement)
    {
		// Build most of Item object
    	Item item = new Item();
    	item.itemID = Integer.parseInt(itemElement.getAttribute("ItemID"));
    	item.name = getElementText(getElementByTagNameNR(itemElement, "Name"));
    	item.firstMinimumBid = getElementText(getElementByTagNameNR(itemElement, "First_Bid"));
    	item.numBids = Integer.parseInt(getElementText(getElementByTagNameNR(itemElement, "Number_of_Bids")));
    	item.currentBidAmount = getElementText(getElementByTagNameNR(itemElement, "Currently"));
    	item.started = getElementText(getElementByTagNameNR(itemElement, "Started"));
    	item.ends = getElementText(getElementByTagNameNR(itemElement, "Ends"));
    	item.description = getElementText(getElementByTagNameNR(itemElement, "Description"));
    	Element buyPriceElement = getElementByTagNameNR(itemElement, "Buy_Price");
    	if(buyPriceElement != null)
    	{
    		item.buyPrice = strip(getElementText(buyPriceElement));
    	}
			
		// Build User object for seller	
    	User user = parseSeller(getElementByTagNameNR(itemElement, "Seller"));
    	item.userID = user.userID;
    	String location = getElementText(getElementByTagNameNR(itemElement, "Location"));
    	String country = getElementText(getElementByTagNameNR(itemElement, "Country"));
    	user.location = location;
    	user.country = country;
    	
    	// Add the user and account for conflicts
    	addUser(user);
		
    	// Create an array of category strings by parsing the "Category"s
    	Element[] categoryElements = getElementsByTagNameNR(itemElement, "Category");
    	if(categoryElements.length > 0)
    	{
    		// Get an array of the equivalent category ID's
    		int categoryIDs[] = getCategoryIDs(categoryElements);
    		item.categoryIDs = categoryIDs;
    	}
		
		// Process Bids
    	// Hand off the bids to be parsed for Bid and User info
    	Element bidsElement = getElementByTagNameNR(itemElement, "Bids");
    	Element[] bidElements = getElementsByTagNameNR(bidsElement, "Bid");
    	if(bidElements.length > 0)
    	{
    		ArrayList<Bid> bids = parseBids(bidElements);
    		
    		// For each big, update it's ItemID
    		for(int i=0; i < bids.size(); i++)
    		{
    			bids.get(i).itemID = item.itemID;
    		}
    		
    		// Add the found bids to the global bid ArrayList
    		bidsArray.addAll(bids);
    	}
    	
    	return item;
    }
    
    static User parseSeller(Element seller)
    {
    	User user = new User();
    	// Create a User object
    	// Grab the "userID" and set it
    	// Grab the "rating" and set it
 
    	// Return the User object
    	return user;
    }
    
    static ArrayList<Bid> parseBids(Element[] categoryElements)
    {
    	ArrayList<Bid> bids = new ArrayList<Bid>();
    	
    	// For each bid element, process
    	// Parse the bid for User info and Bid info
    	parseBid();
    	// Add the resulting Bid to the bids[] array
    	
    	return bids;
    }
    
    static void parseBid()
    {
    	// Build most of the Bid object
    	// Grab the "time" and set it
    	// Grab the "amount" and set it
    	
    	// Process Bidder object for User
    	// Create a user from the Bidder element
    	User user = parseBidder();
    	
    	// Add the user and account for conflicts
    	addUser(user);
    			
    	// Set the remaining fields of the Bid object
    	// Set the Bid item's "userID"
    }
    
    static User parseBidder()
    {
    	// Create a User object
    	User user = new User();
    	
    	// Grab the "userID" and set it
    	// Grab the "rating" and set it
    	
    	return user;
    }
    
    static int[] getCategoryIDs(Element[] categoryElements)
    {
    	// Create a return array
    	int categoryIDs[] = {0, 1, 2, 3};
    	
    	// Process each Category string
    		// Check for category in string->id map
    		// If it exists
    			// Add it to the return array
    		// If it doesn't
    			// Create a new id (increment) 
    			// Add  pair to string->id, id->string maps
    			// Add new id to return array
    	
    	// Return the array
    	return categoryIDs;
    }
    
    static void addItem(Item item)
    {
    	// If the item doesn't exist in itemMap, add it
    	if(!itemMap.containsKey(item.itemID))
    		itemMap.put(item.itemID, item);
    }
    
    static void addUser(User user)
    {
    	// If the user doesn't exist in userMap
    	if(!userMap.containsKey(user.userID))
    	{
    		// Add them
    		userMap.put(user.userID, user);
    	}
    	// If the user does exist in userMap
    	else
    	{
    		// Grab the existing User
    		User existingUser = userMap.get(user.userID);
    		
    		// Merge the two Users to create a new user
    		User newUser = mergeUsers(existingUser, user);
    		
    		// Add the new users
    		userMap.put(newUser.userID, newUser);
    	}
    		
    }
    
    static User mergeUsers(User user1, User user2)
    {
    	User newUser = new User();
    	
    	// Compare field by field
    	// Update first User to gain any missing attributes
    	// Print any conflicts (should not occur)
    	return newUser;
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
        
        /* Initialize containers */
        itemMap = new LinkedHashMap<Integer, Item>();
        userMap = new LinkedHashMap<Integer, User>();
        categoryIDMap = new LinkedHashMap<Integer, String>();
        categoryStringMap = new LinkedHashMap<String, Integer>();
        bidsArray = new ArrayList<Bid>();
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
