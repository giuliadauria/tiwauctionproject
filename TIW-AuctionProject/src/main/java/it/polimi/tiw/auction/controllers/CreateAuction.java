package it.polimi.tiw.auction.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.tiw.auction.beans.User;
import it.polimi.tiw.auction.dao.AuctionDAO;
import it.polimi.tiw.auction.utils.ConnectionHandler;

/**
 * Servlet implementation class CreateAuction
 */
@WebServlet("/CreateAuction")
public class CreateAuction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAuction() {
        super();
    }
    
    public void init() throws ServletException{
    	connection = ConnectionHandler.getConnection(getServletContext());
    }

	
	/*protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}*/

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";					
			response.sendRedirect(loginpath);
			return;
		}	
		//get and parse all parameters from request
		boolean isBadRequest = false;
		Timestamp now = Timestamp.from(Instant.now());
		String itemName = null;
		String description = null;
		Timestamp deadline = null;
		Float initialPrice = null;
		Float raise = null;
		try {
			itemName = StringEscapeUtils.escapeJava(request.getParameter("itemName"));
			description = StringEscapeUtils.escapeJava(request.getParameter("description"));
			deadline = Timestamp.valueOf(request.getParameter("deadline"));
			initialPrice = Float.valueOf(request.getParameter("initialPrice"));
			raise = Float.valueOf(request.getParameter("raise"));
			isBadRequest = (raise < 0) || (initialPrice < 0) || (description.isEmpty()) || (itemName.isEmpty())
					|| (deadline.before(now));
		}catch (NumberFormatException | NullPointerException e) {
			isBadRequest = true;
			e.printStackTrace();
		}
		if(isBadRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		//Create auction in DB
		User user = (User) session.getAttribute("user");
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		try {
			auctionDAO.createAuction(user.getUserId(), itemName, description, deadline, initialPrice, raise);
		}catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create auction");
			return;
		}	
		//updating the view of sell page
		
		
		
		//return the user to the right view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToSell";
		response.sendRedirect(path);	
	}

}
