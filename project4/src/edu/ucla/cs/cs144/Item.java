package edu.ucla.cs.cs144;

import java.util.LinkedList;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import edu.ucla.cs.cs144.Bid;

public class Item
{
    private int Item_ID;
    private String name;
    private LinkedList<String> categories;
    private double current_bid;
    private double buy_price;
    private double first_bid;
    private int no_of_bids;
    private LinkedList<Bid> bids;
    private String latitude;
    private String longitude;
    private String location;
    private String country;
    private Date started;
    private Date ends;
    private String seller_id;
    private int seller_rating;
    private String decription;
    
    public Item()
    {
        categories = new LinkedList<String>();
        latitude = "";
        longitude = "";
        buy_price = -1.0;
        bids = new LinkedList();
    }

    public int getBidLength()
    {
    	return bids.size();
    }

    public int getCategoryLength()
    {
        return categories.size();
    }

    public int getItem_ID() {
        return Item_ID;
    }

    public void setItem_ID(int Item_ID) {
        this.Item_ID = Item_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<String> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<String> categories) {
        this.categories = categories;
    }

    public void addToCategories(String value)
    {
    	this.categories.add(value);
    }

    public void addToBids(Bid b)
    {
    	this.bids.add(b);
    }

    public double getCurrent_bid() {
        return current_bid;
    }

    public void setCurrent_bid(double current_bid) {
        this.current_bid = current_bid;
    }

    public double getBuy_price() {
        return buy_price;
    }

    public void setBuy_price(double buy_price) {
        this.buy_price = buy_price;
    }

    public double getFirst_bid() {
        return first_bid;
    }

    public void setFirst_bid(double first_bid) {
        this.first_bid = first_bid;
    }

    public int getNo_of_bids() {
        return no_of_bids;
    }

    public void setNo_of_bids(int no_of_bids) {
        this.no_of_bids = no_of_bids;
    }

    public LinkedList<Bid> getBids() {
        Collections.sort(this.bids);
        return bids;
    }

    public void setBids(LinkedList<Bid> bids) {
        this.bids = bids;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStarted() {
        return (new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa").format(this.started));
    }

    public void setStarted(Date started) {
        this.started = started;
    }

    public String getEnds() {
        return (new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa").format(this.ends));
    }

    public void setEnds(Date ends) {
        this.ends = ends;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getSeller_rating() {
        return seller_rating;
    }

    public void setSeller_rating(int seller_rating) {
        this.seller_rating = seller_rating;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }
    
    // public void sortBids()
    // {
    // 	Collections.sort(bids, new Comparator<Bid>() {
    // 		  public int compare(Bid o1, Bid o2) {
    // 		      return o1.getBid_date().compareTo(o2.getBid_date());
    // 		  }
    // 		});
    // }

    public String printAll()
    {
        String bidInfo = "";
        for(Bid b : bids)
        {
            bidInfo += "<br/>"+b.bidder_id+"<br/>"+b.bidder_rating+"<br/>"+b.bid_date.toString()+"<br/>$"+b.amount+"<br/>"+b.location+"<br/>"+b.country+"<br/>";
        }
        return name+"<br/>"+current_bid+"<br/>"+first_bid+"<br/>"+no_of_bids+"<br/>"+location+"<br/>"+country+"<br/>"+started.toString()+"<br/>"+ends.toString()+"<br/>"+seller_id+"<br/>"+seller_rating+"<br/>"+decription+"<br/>"+bidInfo;
    }
}