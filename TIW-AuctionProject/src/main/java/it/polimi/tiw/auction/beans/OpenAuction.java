package it.polimi.tiw.auction.beans;

import java.util.concurrent.TimeUnit;

public class OpenAuction {

	private int auctionId;
	private String name;
	private String seller;
	private String itemName;
	private float bestOffer;
	private String remainingTime;
	
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
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public float getBestOffer() {
		return bestOffer;
	}
	
	public void setBestOffer(float bestOffer) {
		this.bestOffer = bestOffer;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
