package edu.ucla.cs.cs144;

/** Class to represent users in memory while parsing **/
public class User {
	
	int userID;
	int rating;
	String location;
	String country;
	
	User()
	{
		userID = -1;
		rating = -1;
		location = null;
		country = null;
	}
}
