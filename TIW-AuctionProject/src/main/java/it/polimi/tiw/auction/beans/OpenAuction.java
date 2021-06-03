package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class OpenAuction {

	private int auctionId;
	private String sellerId;
	private String itemName;
	private BigDecimal bestOffer;
	//private Timestamp remainingTime;
	private long remainingTime;
	
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
	
	public long getRemainingTime() {
	return remainingTime;
	}

	public void setRemainingTime(long remainingTime) {
		this.remainingTime = remainingTime;
	}
}
