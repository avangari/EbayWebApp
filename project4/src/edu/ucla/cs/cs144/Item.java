package edu.ucla.cs.cs144;

import java.util.LinkedList;
import java.util.Date;
import edu.ucla.cs.cs144.Bid;

public class Item{
	int Item_ID;
	String name;
	LinkedList<String> categories;
	double current_bid;
	double buy_price;
	double first_bid;
	int no_of_bids;
	Bid[] bids;
	String latitude;
	String longitude;
	String location;
	String country;
	Date started;
	Date ends;
	String seller_id;
	int seller_rating;
	String decription;
	
	public Item()
	{
		categories = new LinkedList<String>();
		latitude = "";
		longitude = "";
		buy_price = -1;
	}
	
	public void allocateBids()
	{
		this.bids = new Bid[no_of_bids];
	}
	
	public String getName()
	{
		return name;
	}

	public String printAll()
	{
		return name+"<br/>"+current_bid+"<br/>"+first_bid+"<br/>"+no_of_bids+"<br/>"+location+"<br/>"+country+"<br/>"+started.toString()+"<br/>"+ends.toString()+"<br/>"+seller_id+"<br/>"+seller_rating+"<br/>"+decription;
	}
}


