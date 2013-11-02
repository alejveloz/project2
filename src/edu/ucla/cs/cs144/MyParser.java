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

    static HashSet<String> writtenUserIDs;
    static HashSet<String> writtenCategoryNames;
    static LinkedHashMap<String, User> incompleteUsers;
    
    static SimpleDateFormat javaFormat;
    
    static PrintWriter itemWriter;
    static PrintWriter userWriter;
    static PrintWriter categoryWriter;
    static PrintWriter bidWriter;
    static PrintWriter itemSellerWriter;
    static PrintWriter itemCategoryWriter;
    
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
        

        /**************************************************************/
        
        
        // Get all Item elements
        Element documentRoot = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR(documentRoot, "Item");

		// Process each element
        for(int i = 0; i < items.length; i++)
        {
			// Create an Item by parsing the the "Item" element
			Item currentItem = parseItem(items[i]);
			
			// Add the resulting item to items[]
			writeItem(currentItem);
        }
        
        // Write any remaining incomplete users
        for (Map.Entry<String, User> entry : incompleteUsers.entrySet())
		{
        	writeIncompleteUser(entry.getValue());
        }
        
    }
    
  
    static Item parseItem(Element itemElement)
    {
		// Build most of Item object
    	Item item = new Item();
    	item.itemID = Integer.parseInt(itemElement.getAttribute("ItemID"));
    	item.name = getElementText(getElementByTagNameNR(itemElement, "Name"));
    	item.firstMinimumBid = strip(getElementText(getElementByTagNameNR(itemElement, "First_Bid")));
    	item.numBids = Integer.parseInt(getElementText(getElementByTagNameNR(itemElement, "Number_of_Bids")));
    	item.currentBidAmount = strip(getElementText(getElementByTagNameNR(itemElement, "Currently")));
    	item.started = createJavaDate(getElementText(getElementByTagNameNR(itemElement, "Started")));
    	item.ends = createJavaDate(getElementText(getElementByTagNameNR(itemElement, "Ends")));
    	String description = getElementText(getElementByTagNameNR(itemElement, "Description"));
    	if(description.length() > 4000)
    		description = description.substring(0, 3999);
    	item.description = description;
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
    	
    	// Write the user if they're complete and haven't been written
    	attemptWriteUser(user);
    	
    	// Write the relationship between the user and the item
    	writeItemSeller(item.itemID, user.userID);
		
    	// Create an array of category strings by parsing the "Category"s
    	Element[] categoryElements = getElementsByTagNameNR(itemElement, "Category");
    	ArrayList<String> uniqueCategoryStrings = new ArrayList<String>();
    	for(int i=0; i < categoryElements.length; i++)
    	{
    		String categoryName = getElementText(categoryElements[i]);
    		
    		if(!uniqueCategoryStrings.contains(categoryName))
    		{
    			uniqueCategoryStrings.add(categoryName);
    		}
    	}

    	// Write the unique categories for this item
    	for(int i=0; i < uniqueCategoryStrings.size(); i++)
    	{
    		String categoryName = uniqueCategoryStrings.get(i);
    	
    		// Write this category if it doesn't exist yet
    		if(!writtenCategoryNames.contains(categoryName))
    		{
    			writeCategory(categoryName);
    		}
    		
    		// Write the relationship between this category and item
    		writeItemCategory(item.itemID, categoryName);
    	}
		
		// Process Bids
    	// Hand off the bids to be parsed for Bid and User info
    	Element bidsElement = getElementByTagNameNR(itemElement, "Bids");
    	Element[] bidElements = getElementsByTagNameNR(bidsElement, "Bid");
    	if(bidElements.length > 0)
    	{
    		ArrayList<Bid> bids = parseBids(bidElements);
    		
    		// For each big, update it's ItemID and write to file
    		for(int i=0; i < bids.size(); i++)
    		{
    			bids.get(i).itemID = item.itemID;
    			writeBid(bids.get(i));
    		}
    	}
    	
    	return item;
    }
    
    static User parseSeller(Element sellerElement)
    {
    	// Create a User object
    	User user = new User();
    	
    	// Grab the "userID" and set it
    	user.userID = sellerElement.getAttribute("UserID");
    	
    	// Grab the "rating" and set it
    	user.rating = Integer.parseInt(sellerElement.getAttribute("Rating"));
 
    	// Return the User object
    	return user;
    }
    
    static ArrayList<Bid> parseBids(Element[] bidElements)
    {
    	ArrayList<Bid> bids = new ArrayList<Bid>();
    	
    	// For each bid element, process
    	for(int i=0; i<bidElements.length; i++)
    	{
    		// Parse the bid for User info and Bid info
    		Bid bid = parseBid(bidElements[i]);
    		
    		// Add the resulting Bid to the bids[] array
    		bids.add(bid);
    	}
    	
    	return bids;
    }
    
    static Bid parseBid(Element bidElement)
    {
    	// Build most of the Bid object
    	Bid bid = new Bid();
    	
    	// Grab the "time" and set it
    	bid.time = createJavaDate(getElementText(getElementByTagNameNR(bidElement, "Time")));
    	
    	// Grab the "amount" and set it
    	bid.amount = strip(getElementText(getElementByTagNameNR(bidElement, "Amount")));
    	
    	// Process Bidder object for User
    	Element bidderElement = getElementByTagNameNR(bidElement, "Bidder");
    	
    	// Create a user from the Bidder element
    	User user = parseBidder(bidderElement);
    	bid.userID = user.userID;
    	
    	// Add the user and account for conflicts
    	if(!writtenUserIDs.contains(user.userID))
    	{
    		writeUser(user);
    	}
    	
    	return bid;
    }
    
    static User parseBidder(Element bidderElement)
    {
    	// Create a User object
    	User user = new User();
    	
    	// Grab the "userID" and set it
    	user.userID = bidderElement.getAttribute("UserID");
    	
    	// Grab the "rating" and set it
    	user.rating = Integer.parseInt(bidderElement.getAttribute("Rating"));
 
    	Element locationElement = getElementByTagNameNR(bidderElement, "Location");
    	if(locationElement != null)
    		user.location = getElementText(getElementByTagNameNR(bidderElement, "Location"));

    	Element countryElement = getElementByTagNameNR(bidderElement, "Country");
    	if(countryElement != null)
    		user.country = getElementText(countryElement);
    	
    	// Return the User object
    	return user;
    }

    static void writeItem(Item item)
    {
    	itemWriter.println(item.loadString());
    	return;
    }
    
    static void attemptWriteUser(User user)
    {
    	// If the user has not been written, consider writing
    	if(!writtenUserIDs.contains(user.userID))
    	{
    		// If the user info is complete, check if previously incomplete, write
    		if(user.isComplete())
    		{
    			if(incompleteUsers.containsKey(user.userID))
    				incompleteUsers.remove(user.userID);
    			
    			writeUser(user);
    		}
    		// Otherwise, save user as incomplete (if not duplicate)
    		else
    		{
    			if(!incompleteUsers.containsKey(user.userID))
    				incompleteUsers.put(user.userID, user);	
    			
    			// TODO: If incompleteUser already exists, consider the possibility that the 
    			//       current incomplete user may have more info than the previously saved one
    		}
    	}
    }
    
    static void writeUser(User user)
    {
	    userWriter.println(user.loadString());
    	writtenUserIDs.add(user.userID);
    	return;
    }
    
    static void writeIncompleteUser(User user)
    {
    	userWriter.println(user.loadString());
    	incompleteUsers.remove(user.userID);
    	writtenUserIDs.add(user.userID);
    	return;
    }

    static void writeCategory(String categoryName)
    {
	    categoryWriter.println("\"" + customSQLEscaped(categoryName) + "\"");
    	writtenCategoryNames.add(categoryName);
    	return;
    }

    static void writeBid(Bid bid)
    {
	    bidWriter.println(bid.loadString());
    	return;
    }
    
    static void writeItemSeller(int itemID, String userID)
    {
	    itemSellerWriter.println(itemID + "," + "\"" + customSQLEscaped(userID) + "\"");
    	return;
    }
    
    static void writeItemCategory(int itemID, String categoryName)
    {
	    itemCategoryWriter.println(itemID + "," + "\"" + customSQLEscaped(categoryName) + "\"");
    	return;
    }
    
    static Date createJavaDate(String dateString)
    {
    	Date parsed = new Date();
    	
    	try {
            parsed = javaFormat.parse(dateString);
        }
        catch(ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + dateString + "\"");
        }
    	
    	return parsed;
    }
    
    static public String customSQLEscaped(String string)
	{
		return string.replace(",", "\\,").replace("\"", "\\\"");
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
        writtenUserIDs = new HashSet<String>();
        writtenCategoryNames = new HashSet<String>();
        incompleteUsers = new LinkedHashMap<String, User>();
        
        /* Initialize date format */
    	javaFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        
        /* Create and open all files for writing */
        try {
        	File csvDirectory = new File("csv"); 
        	csvDirectory.mkdirs();
			itemWriter = new PrintWriter("csv/item.csv", "UTF-8");
		    userWriter = new PrintWriter("csv/user.csv", "UTF-8");
		    categoryWriter = new PrintWriter("csv/category.csv", "UTF-8");
		    bidWriter = new PrintWriter("csv/bid.csv", "UTF-8");
		    itemSellerWriter = new PrintWriter("csv/itemseller.csv", "UTF-8");
		    itemCategoryWriter = new PrintWriter("csv/itemcategory.csv", "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Unable to create csv file");
			System.exit(2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.out.println("Unsupported encoding");
			System.exit(2);
		}
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
        
        /* Close all files */
        itemWriter.close();
        userWriter.close();
        categoryWriter.close();
        bidWriter.close();
        itemSellerWriter.close();
        itemCategoryWriter.close();
    }
}
