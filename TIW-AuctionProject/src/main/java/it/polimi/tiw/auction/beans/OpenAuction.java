package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class OpenAuction {

	private int auctionId;
	private String sellerId;
	private String itemName;
	private BigDecimal bestOffer;
	//private Timestamp remainingTime;
	private String remainingTime;
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public String getSellerId() {
		return sellerId;
	}
	
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public BigDecimal getBestOffer() {
		return bestOffer;
	}
	
	public void setBestOffer(BigDecimal bestOffer) {
		this.bestOffer = bestOffer;
	}
	
	/*public Timestamp getRemainingTime() {
		return remainingTime;
	}
	
	public void setRemainingTime(Timestamp remainingTime) {
		this.remainingTime = remainingTime;
	}*/
	
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
}
