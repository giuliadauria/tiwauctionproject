package it.polimi.tiw.auction.beans;

import java.util.List;

import it.polimi.tiw.auction.beans.Picture;

public class Item {

	private int itemId;
	private String name;
	private String description;
	private List<Picture> pictures;
	
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<Picture> getPictures() {
		return pictures;
	}
	
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
}
