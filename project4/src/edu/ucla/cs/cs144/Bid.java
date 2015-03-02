package edu.ucla.cs.cs144;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class Bid implements Comparable<Bid>
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
        return (new SimpleDateFormat("EEE, MMM d yyyy, hh:mm aaa").format(this.bid_date));
    }

    public Date getBidDate()
    {
        return this.bid_date;
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

    @Override
    public int compareTo(Bid b) 
    {
        if (this.getBid_date() == null || b.getBid_date() == null)
          return 0;

        return this.getBidDate().compareTo(b.getBidDate());
    }
}
