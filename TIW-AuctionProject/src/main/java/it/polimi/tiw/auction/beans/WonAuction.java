package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;

public class WonAuction {
	
	private int auctionId;
	private Item item;
	private float finalPrice;
	private String sellerUsername;
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public float getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(float finalPrice) {
		this.finalPrice = finalPrice;
	}
	
	public String getSellerUsername() {
		return sellerUsername;
	}
	
	public void setSellerUsername(String sellerUsername) {
		this.sellerUsername = sellerUsername;
	}

}
