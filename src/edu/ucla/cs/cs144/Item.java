package edu.ucla.cs.cs144;

/** Class to represent items in memory while parsing **/
public class Item {
	
	int itemID;
	String name;
	int buyPrice;
	int firstMinimumBid;
	String started;
	String ends;
	String description;
	int numBids;
	int currentBidAmount;
	int userID;  //for database purposes
	int categoryIDs[]; //for database purposes
	
	Item()
	{
		itemID = -1;
		name = null;
		buyPrice = -1;
		firstMinimumBid = -1;
		started = null;
		ends = null;
		description = null;
		numBids = -1;
		currentBidAmount = -1;
		userID = -1;
	}
}
