package edu.ucla.cs.cs144;

/** Class to represent bids in memory while parsing **/
public class Bid {

	int itemID;
	String time;
	int userID;
	int amount;
	
	Bid()
	{
		itemID = -1;
		time = null;
		userID = -1;
		amount = -1;
	}
}
