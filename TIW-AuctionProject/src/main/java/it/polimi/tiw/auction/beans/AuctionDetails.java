package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class AuctionDetails {
	
	private int auctionId;
	private int sellerId;
	private Item item;
	private Timestamp remainingTime;
	private BigDecimal initialPrice;
	private BigDecimal raise;
	private List<Bid> bidList;
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public int getSellerId() {
		return sellerId;
	}
	
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public Timestamp getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(Timestamp remainingTime) {
		this.remainingTime = remainingTime;
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
