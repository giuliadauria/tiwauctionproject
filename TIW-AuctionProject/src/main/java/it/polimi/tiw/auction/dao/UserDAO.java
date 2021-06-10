package it.polimi.tiw.auction.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.auction.beans.User;

public class UserDAO {
	
	private Connection connection;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
	public User checkUser(String username, String password) throws SQLException {
		String query = "SELECT  * FROM user  WHERE username = ? AND password =?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			pstatement.setString(2, password);
			try(ResultSet result = pstatement.executeQuery()){
				if (!result.isBeforeFirst()) { // no results, credential check failed
					return null;
				}	
				else {
					result.next();
					User user = new User();
					user.setUserId(result.getInt("id"));
					user.setUsername(result.getString("username"));
					user.setPassword(result.getString("password"));
					user.setAddress(result.getString("address"));
					return user;
				}
			}
		}
	}
	
	public User findUserById(int userId) throws SQLException{
		String query = "SELECT * FROM user WHERE id = ?";
		User user = new User();
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			try(ResultSet result = pstatement.executeQuery()){
				if (!result.isBeforeFirst()) { // no results, credential check failed
					return null;
				}
				else {
					result.next();
					user.setUserId(userId);
					user.setUsername(result.getString("username"));
					user.setPassword(result.getString("password"));
					user.setAddress(result.getString("address"));
					return user;
				}
			}	
		}
	}
	
	public String findUsernameById(int userId) throws SQLException{
		String query = "SELECT username FROM user WHERE id = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			try(ResultSet result = pstatement.executeQuery()){
				if (!result.isBeforeFirst()) { // no results, credential check failed
					return null;
				}
				else {
					result.next();
					return result.getString("username");
				}
			}	
		}
	}	

	
	public int findIdByUsername(String username) throws SQLException{
		String query = "SELECT id FROM user WHERE username = ?";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, username);
			try(ResultSet result = pstatement.executeQuery()){
				if (!result.isBeforeFirst()) { // no results, credential check failed
					return -1;
				}
				else {
					result.next();
					return result.getInt("id");
				}
			}	
		}
	}
}
