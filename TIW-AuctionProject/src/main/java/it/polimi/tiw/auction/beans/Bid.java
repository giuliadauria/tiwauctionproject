package it.polimi.tiw.auction.beans;

import java.sql.Timestamp;

public class Bid {

	private String username;
	private int auctionId;
	private Timestamp dateTime;
	private float offer;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public Timestamp getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}
	
	public float getOffer() {
		return offer;
	}
	
	public void setOffer(float offer) {
		this.offer = offer;
	}

}
