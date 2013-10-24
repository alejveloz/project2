package edu.ucla.cs.cs144;

/** Class to represent bids in memory while parsing **/
public class Bid {

	int itemID;
	String time;
	String userID;
	String amount;
	
	Bid()
	{
		itemID = -1;
		time = null;
		userID = null;
		amount = null;
	}
}
