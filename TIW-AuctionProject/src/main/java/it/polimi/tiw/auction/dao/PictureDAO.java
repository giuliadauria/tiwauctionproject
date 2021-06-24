package it.polimi.tiw.auction.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polimi.tiw.auction.beans.Picture;

public class PictureDAO {

	Connection connection;
	
	public PictureDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Picture> findPicturesByItemId(int itemId){
		List<Picture> picturesList = new ArrayList<>();
		String query = "SELECT * FROM image WHERE itemId = ?";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, itemId);
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					Picture picture = new Picture();
					picture.setItemId(result.getInt("itemId"));
					picture.setPictureUrl(result.getString("url"));
					picturesList.add(picture);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//if there are no entries corresponding to the query, null is an easier value to return and check
		if(picturesList.size() == 0) {
			return null;
		}
		else {
			return picturesList;
		}
	}
	
	public void createPicture(String url, int itemId) throws SQLException {
		String query = "INSERT into image (itemId, url) VALUES (?, ?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, itemId);
			pstatement.setString(2, url);
			pstatement.executeUpdate();
		}
	}
	
}
