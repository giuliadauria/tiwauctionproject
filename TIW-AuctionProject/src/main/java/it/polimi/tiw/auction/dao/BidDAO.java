package it.polimi.tiw.auction.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.auction.beans.Bid;

public class BidDAO {

	private Connection connection;
	
	public BidDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Bid> findBidByAuctionId(int auctionId) throws SQLException{
		List<Bid> bidSet = new ArrayList<>();
		String query = "SELECT * FROM bid JOIN user ON userId = id WHERE auctionId = ? ORDER BY dateTime desc";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, auctionId);
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					Bid bid = new Bid();
					bid.setUsername(result.getString("username"));
					bid.setDateTime(result.getTimestamp("dateTime"));
					bid.setOffer(result.getFloat("offer"));
					bidSet.add(bid);
				}
			}
		}
		return bidSet;
	}
	
	public Bid findLastBid(int auctionId) throws SQLException {
		Bid bid = new Bid();
		String query = "SELECT * FROM bid JOIN user ON bid.userId = user.id WHERE auctionId = ? ORDER BY dateTime desc LIMIT 1";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, auctionId);
			try(ResultSet result = pstatement.executeQuery()){
				if(result.next()) {
					bid.setUsername(result.getString("username"));
					bid.setDateTime(result.getTimestamp("dateTime"));
					bid.setOffer(result.getFloat("offer"));
				}
			}
		}
		return bid;	
	}
	
	
	public void createBid(String username, int auctionId, Timestamp timestamp, float offer) throws SQLException {
		String query = "INSERT into bid (userId, auctionId, dateTime, offer) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			UserDAO userDao = new UserDAO(connection);
			int userId = userDao.findIdByUsername(username);
			//it is possible the userId to be <0, in this case the query will generate an sqlexception
			pstatement.setInt(1, userId);
			pstatement.setInt(2, auctionId);
			pstatement.setTimestamp(3, timestamp);
			pstatement.setFloat(4, offer);
			pstatement.executeUpdate();
		}
	}	
}

