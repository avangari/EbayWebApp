package edu.ucla.cs.cs144;

import java.util.Date;

public class Bid
{
	String bidder_id;
	int bidder_rating;
	Date bid_date;
	double amount;
	String location;
	String country;

    public String getBidder_id() {
        return bidder_id;
    }

    public void setBidder_id(String bidder_id) {
        this.bidder_id = bidder_id;
    }

    public int getBidder_rating() {
        return bidder_rating;
    }

    public void setBidder_rating(int bidder_rating) {
        this.bidder_rating = bidder_rating;
    }

    public String getBid_date() {
        return bid_date.toString();
    }

    public void setBid_date(Date bid_date) {
        this.bid_date = bid_date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLocation() {
        if(location != null && !location.isEmpty() && !location.equals("null"))
            return location;
        else
            return "unavailable";
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        if(country != null && !country.isEmpty() && !country.equals("null"))
            return country;
        else
            return "unavailable";            
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
