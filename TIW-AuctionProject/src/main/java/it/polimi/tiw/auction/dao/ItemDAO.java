package it.polimi.tiw.auction.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.auction.beans.Item;

public class ItemDAO {

	private Connection connection;
	
	public ItemDAO(Connection connection) {
		this.connection = connection;
	}
	
	public Item findItemById(int itemId) throws SQLException{
		Item item = null;
		String query = "SELECT * FROM item WHERE id = ?";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
				pstatement.setInt(1, itemId);
				try(ResultSet result = pstatement.executeQuery()){
					if(result.next()) {
						item = new Item();
						item.setItemId(itemId);
						item.setName(result.getString("name"));
						item.setDescription(result.getString("description"));
						PictureDAO imageDao = new PictureDAO(connection);
						item.setPictures(imageDao.findPicturesByItemId(itemId)); //this could be null, what to do?
					}
				}
		}
		 return item;	
	}
	
	
	public int createItem(String name, String description) throws SQLException {
		String query = "INSERT into item (name, description) VALUES (?, ?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setString(1, name);
			pstatement.setString(2, description);
			pstatement.executeUpdate();
			//not really sure this works
			return pstatement.getResultSet().getInt("id");
		}
	}
	
	
}
