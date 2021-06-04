package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuctionDetails {
	
	private int auctionId;
	private String seller;
	private Item item;
	private String remainingTime;
	private BigDecimal initialPrice;
	private BigDecimal raise;
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

	public BigDecimal getInitialPrice() {
		return initialPrice;
	}
	
	public void setInitialPrice(BigDecimal initialPrice) {
		this.initialPrice = initialPrice;
	}
	
	public BigDecimal getRaise() {
		return raise;
	}
	
	public void setRaise(BigDecimal raise) {
		this.raise = raise;
	}
	
	public List<Bid> getBidList() {
		return bidList;
	}
	
	public void setBidList(List<Bid> bidList) {
		this.bidList = bidList;
	}
}
