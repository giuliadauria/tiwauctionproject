package it.polimi.tiw.auction.beans;

public class WonAuction {
	
	private int auctionId;
	private String itemName;
	private float finalPrice;
	private String sellerUsername;
	
	public int getAuctionId() {
		return auctionId;
	}
	
	public void setAuctionId(int auctionId) {
		this.auctionId = auctionId;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
