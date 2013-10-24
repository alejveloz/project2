package edu.ucla.cs.cs144;
import java.util.ArrayList;

/** Class to represent items in memory while parsing **/
public class Item {
	
	int itemID;
	String name;
	String buyPrice; // Consider converting to different representation 
	String firstMinimumBid; // Consider converting to different representation 
	String started;
	String ends;
	String description;
	int numBids;
	String currentBidAmount; // Consider converting to different representation 
	String userID;  //for database purposes
	
	Item()
	{
		itemID = -1;
		name = null;
		buyPrice = null;
		firstMinimumBid = null;
		started = null;
		ends = null;
		description = null;
		numBids = -1;
		currentBidAmount = null;
		userID = null;
	}
}
