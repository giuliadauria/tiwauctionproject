package it.polimi.tiw.auction.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.util.FileItemHeadersImpl;

import it.polimi.tiw.auction.beans.Item;
import it.polimi.tiw.auction.beans.Picture;

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
				try(ResultSet result = pstatement.executeQuery();){
					if(result.next()) {
						item = new Item();
						item.setItemId(itemId);
						item.setName(result.getString("name"));
						item.setDescription(result.getString("description"));
						PictureDAO imageDao = new PictureDAO(connection);
						if(imageDao.findPicturesByItemId(itemId) == null) {
							item.setPictures(new ArrayList<Picture>(0));
						}
						else {
							item.setPictures(imageDao.findPicturesByItemId(itemId));
						}
					}
				}catch(SQLException e) {
					e.printStackTrace();
				}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return item;	
	}
	
	
	public int createItem(String name, String description, List<String> urls) throws SQLException {
		String query = "INSERT into item (name, description) VALUES (?, ?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);){
			pstatement.setString(1, name);
			pstatement.setString(2, description);
			pstatement.executeUpdate();
			ResultSet res = pstatement.getGeneratedKeys();
			int id = 0;
			if(res.next()) {
				id = (int)res.getInt(1);
			}
			if(urls.size() > 0) {
				PictureDAO pictureDAO = new PictureDAO(connection);
				for(int i = 0; i < urls.size(); i++) {
					pictureDAO.createPicture(urls.get(i), id);
				}
			}
			
			return id;
		}catch(SQLException e) {
			e.printStackTrace();
			return 1;
		}
	}
	
/*	private void renamePicturesWithItemId(int itemId) {
		ItemDAO itemDAO = new ItemDAO(connection);
		String oldUrl = null;
		try {
			oldUrl = itemDAO.findItemById(itemId).getPictures().get(0).getPictureUrl();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String query = "UPDATE image SET url = ? WHERE itemId = ?";
		String newUrl = oldUrl + itemId;
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setString(1, newUrl);
			pstatement.setInt(2, itemId);
			pstatement.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	*/
	
}
