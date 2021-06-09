package it.polimi.tiw.auction.beans;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuctionDetails {
	
	private int auctionId;
	private String name;
	private String seller;
	private Item item;
	private String remainingTime;
	private long remainingLongTime;
	private float initialPrice;
	private float raise;
	private List<Bid> bidList;
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public String getSeller() {
		return seller;
	}
	
	public void setSeller(String seller) {
		this.seller = seller;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public String getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		long days = TimeUnit.MILLISECONDS.toDays(remainingTime);
		long hours = TimeUnit.MILLISECONDS.toHours(remainingTime)-days*24;
		long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime)-days*24*60-hours*60;
		long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime)-days*24*60*60-hours*60*60-minutes*60;
		this.remainingTime = (days+"d "+hours+"h "+minutes+"m "+seconds+"s");
	}
	
	public long getLongRemainingTime() {
		return this.remainingLongTime;
	}
	
	public void setLongRemainingTime(long longRemainingTime) {
		this.remainingLongTime = longRemainingTime;
	}

	public float getInitialPrice() {
		return initialPrice;
	}
	
	public void setInitialPrice(float initialPrice) {
		this.initialPrice = initialPrice;
	}
	
	public float getRaise() {
		return raise;
	}
	
	public void setRaise(float raise) {
		this.raise = raise;
	}
	
	public List<Bid> getBidList() {
		return bidList;
	}
	
	public void setBidList(List<Bid> bidList) {
		this.bidList = bidList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
