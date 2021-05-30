package it.polimi.tiw.auction.beans;

import java.math.BigDecimal;

public class ClosedAuction {
	
	private int auctionId;
	private String itemName;
	private String contractorUsername;
	private String contractorAddress;
	private BigDecimal finalPrice;
	
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
	
	public String getContractorUsername() {
		return contractorUsername;
	}
	
	public void setContractorUsername(String contractorUsername) {
		this.contractorUsername = contractorUsername;
	}
	
	public String getContractorAddress() {
		return contractorAddress;
	}
	
	public void setContractorAddress(String contractorAddress) {
		this.contractorAddress = contractorAddress;
	}
	
	public BigDecimal getFinalPrice() {
		return finalPrice;
	}
	
	public void setFinalPrice(BigDecimal finalPrice) {
		this.finalPrice = finalPrice;
	}
}
