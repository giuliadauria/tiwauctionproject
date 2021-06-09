package it.polimi.tiw.auction.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.auction.beans.AuctionDetails;
import it.polimi.tiw.auction.beans.ClosedAuction;
import it.polimi.tiw.auction.beans.OpenAuction;
import it.polimi.tiw.auction.beans.User;
import it.polimi.tiw.auction.beans.WonAuction;

public class AuctionDAO {

	private Connection connection;
	
	public AuctionDAO(Connection connection) {
		this.connection = connection;
	}	
	
	public AuctionDetails findAuctionDetailsById(int auctionId) throws SQLException {
		AuctionDetails auction = null;
		String query = "SELECT * FROM auction WHERE id = ?";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			pstatement.setInt(1, auctionId);
			try(ResultSet result = pstatement.executeQuery()){
				if(result.next()) {
					auction = new AuctionDetails();
					auction.setAuctionId(result.getInt("id"));
					auction.setName(result.getString("name"));
					UserDAO userDAO = new UserDAO(connection);
					auction.setSeller(userDAO.findUsernameById(result.getInt("sellerId")));
					BidDAO bidDAO = new BidDAO(connection);
					auction.setBidList(bidDAO.findBidByAuctionId(result.getInt("id")));
					ItemDAO itemDAO = new ItemDAO(connection);
					auction.setItem(itemDAO.findItemById(result.getInt("itemId")));
					auction.setInitialPrice(result.getFloat("initialPrice"));
					auction.setRaise(result.getFloat("raise"));
					Timestamp now = Timestamp.from(Instant.now());
					auction.setRemainingTime(result.getTimestamp("deadline").getTime()-now.getTime());
					auction.setLongRemainingTime(result.getTimestamp("deadline").getTime()-now.getTime());
				}
			}
		}
		return auction;
	}
	
	public List<OpenAuction> findOpenAuction() throws SQLException{
		List<OpenAuction> openAuctionList = new ArrayList<>();
		String query = "SELECT * FROM auction WHERE deadline > ? ORDER BY deadline asc";
		try(PreparedStatement pstatement = connection.prepareStatement(query);){
			Timestamp now = Timestamp.from(Instant.now());
			pstatement.setTimestamp(1, now);
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					OpenAuction openAuction = new OpenAuction();
					openAuction.setAuctionId(result.getInt("id"));
					openAuction.setName(result.getString("name"));
					UserDAO userDAO = new UserDAO(connection);
					openAuction.setSeller(userDAO.findUsernameById(result.getInt("sellerId")));
					BidDAO bidDAO = new BidDAO(connection);
					openAuction.setBestOffer(bidDAO.findLastBid(result.getInt("id")).getOffer());
					ItemDAO itemDAO = new ItemDAO(connection);
					openAuction.setItemName((itemDAO.findItemById(result.getInt("itemId")).getName()));
					//openAuction.setItemName(result.getString("name"));
					openAuction.setRemainingTime(result.getTimestamp("deadline").getTime()-now.getTime());
					openAuctionList.add(openAuction);
				}
			}
		}
		return openAuctionList;
	}
	
	public List<OpenAuction> findOpenAuctionBySeller(int sellerId) throws SQLException{
		List<OpenAuction> openAuctionList = new ArrayList<OpenAuction>();
		String query = "SELECT * FROM auction WHERE sellerId = ? AND deadline > ? ORDER BY deadline asc";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			Timestamp now = Timestamp.from(Instant.now());
			pstatement.setInt(1, sellerId);
			pstatement.setTimestamp(2, now);
			try(ResultSet result = pstatement.executeQuery();){
				while(result.next()) {
					OpenAuction openAuction = new OpenAuction();
					openAuction.setAuctionId(result.getInt("id"));
					openAuction.setName(result.getString("name"));
					BidDAO bidDAO = new BidDAO(connection);
					openAuction.setBestOffer(bidDAO.findLastBid(result.getInt("id")).getOffer());
					ItemDAO itemDAO = new ItemDAO(connection);
					openAuction.setItemName((itemDAO.findItemById(result.getInt("itemId")).getName()));
					openAuction.setRemainingTime(result.getTimestamp("deadline").getTime() - now.getTime());
					openAuctionList.add(openAuction);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return openAuctionList;
	}
	
	public List<ClosedAuction> findClosedAuctionBySeller(int sellerId) throws SQLException{
		List<ClosedAuction> closedAuctionList = new ArrayList<>();
		String query = "SELECT FROM (closedAuction JOIN item ON itemId = id) JOIN bid ON (contractorId, id, finalPrice) = (userId, AuctionId, offer) GROUP BY sellerId = ? ";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, sellerId);
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					ClosedAuction closedAuction = new ClosedAuction();
					closedAuction.setAuctionId(result.getInt("id"));
					UserDAO userDAO = new UserDAO(connection);
					User contractor = userDAO.findUserById(result.getInt("contractorId"));
					closedAuction.setContractorUsername(contractor.getUsername());
					closedAuction.setContractorAddress(contractor.getAddress());
					ItemDAO itemDAO = new ItemDAO(connection);
					closedAuction.setItemName((itemDAO.findItemById(result.getInt("itemId")).getName()));
					closedAuction.setFinalPrice(result.getFloat("offer"));
					closedAuctionList.add(closedAuction);
				}
			}
		}	
		return closedAuctionList;
	}	
				
	public List<OpenAuction> findAuctionByKeyword(String keyword) throws SQLException{
		List<OpenAuction> foundAuctionList = new ArrayList<>();
		String query = "SELECT auction.* FROM auction JOIN item ON auction.itemId = item.id WHERE (item.name LIKE CONCAT( '%',?,'%')) OR (item.description LIKE CONCAT( '%',?,'%')) ORDER BY deadline asc";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setString(1, keyword);
			pstatement.setString(2, keyword);
			Timestamp now = Timestamp.from(Instant.now());
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					OpenAuction auctionFound = new OpenAuction();
					auctionFound.setAuctionId(result.getInt("id"));
					auctionFound.setName(result.getString("name"));
					UserDAO userDAO = new UserDAO(connection);
					auctionFound.setSeller(userDAO.findUsernameById(result.getInt("sellerId")));
					ItemDAO itemDAO = new ItemDAO(connection);
					auctionFound.setItemName(itemDAO.findItemById(result.getInt("itemId")).getName());
					BidDAO bidDAO = new BidDAO(connection);
					auctionFound.setBestOffer(bidDAO.findLastBid(result.getInt("id")).getOffer());
					auctionFound.setRemainingTime(result.getTimestamp("deadline").getTime() - now.getTime());					
					foundAuctionList.add(auctionFound);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return foundAuctionList;
	}
	
	public List<WonAuction> findWonAuctionByContractor(int idContractor) throws SQLException{
		List<WonAuction> wonAuctionList = new ArrayList<>();
		String query = "SELECT * FROM closedAuction JOIN bid ON (id, contractorId, finalPrice) = (auctionId, userId, offer) WHERE contractorId = ? ORDER BY bid.dateTime desc";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, idContractor);
			try(ResultSet result = pstatement.executeQuery()){
				while(result.next()) {
					WonAuction wonAuction = new WonAuction();
					wonAuction.setAuctionId(result.getInt("id"));
					ItemDAO itemDAO = new ItemDAO(connection);
					wonAuction.setItem(itemDAO.findItemById(result.getInt("itemId")));
					wonAuction.setFinalPrice(result.getFloat("finalPrice"));
					UserDAO userDAO = new UserDAO(connection);
					wonAuction.setSellerUsername(userDAO.findUsernameById(result.getInt("sellerId")));
					wonAuctionList.add(wonAuction);
				}
			}
		}
		return wonAuctionList;
	}
	
	public void createAuction(int sellerId, String nameItem, String description, Timestamp deadline, float initialPrice, float raise) throws SQLException {
		//prevedo che dopo aver creato l'item e l'asta, se si vuole si possono aggiungere delle foto
		ItemDAO itemDAO = new ItemDAO(connection);
		int itemId = itemDAO.createItem(nameItem, description);	
		String query = "INSERT into auction (itemId, name, deadline, initialPrice, raise, sellerId) VALUES (?, ?, ?, ?, ?, ?)";
		try(PreparedStatement pstatement = connection.prepareStatement(query)){
			pstatement.setInt(1, itemId);
			pstatement.setString(2, nameItem);
			pstatement.setTimestamp(3, deadline);
			pstatement.setFloat(4, initialPrice);
			pstatement.setFloat(5, raise);
			pstatement.setInt(6, sellerId);
			pstatement.executeUpdate();
			//if in some way i need to return the auctionId check on createItem method how to do it
		}catch(SQLException e) {
			e.printStackTrace();
			return;
		}
	}	
	
	public void closeAuction(int idAuction) throws SQLException {
		AuctionDetails auctionToClose = findAuctionDetailsById(idAuction);
		//not so sure this really works		
		if(auctionToClose.getLongRemainingTime() < 0) {
			String query = "INSERT into closedAuction (id, itemId, sellerId, initialPrice, raise, contractorId, finalPrice) VALUES (?, ?, ?, ?, ?, ?, ?) ";
			try(PreparedStatement pstatement = connection.prepareStatement(query)){
				pstatement.setInt(1, auctionToClose.getAuctionId());
				pstatement.setInt(2, auctionToClose.getItem().getItemId());
				UserDAO userDAO = new UserDAO(connection);
				int sellerId = userDAO.findIdByUsername(auctionToClose.getName());
				pstatement.setInt(3, sellerId);
				pstatement.setFloat(4, auctionToClose.getInitialPrice());
				pstatement.setFloat(5, auctionToClose.getRaise());
				BidDAO bidDAO = new BidDAO(connection);
				pstatement.setInt(6, userDAO.findIdByUsername(bidDAO.findLastBid(idAuction).getUsername()));
				pstatement.setFloat(7, bidDAO.findLastBid(idAuction).getOffer());
				String deletingQuery  = "DELETE FROM openAuction WHERE id = ?";
				try(PreparedStatement pstatementdelete = connection.prepareStatement(deletingQuery)){
					pstatementdelete.setInt(1, idAuction);
					pstatementdelete.executeUpdate();
				}
			}
		}	
		else {
			throw new SQLException("You cannot close this auction yet");
		}
	}

}










