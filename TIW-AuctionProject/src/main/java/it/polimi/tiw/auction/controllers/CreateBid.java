package it.polimi.tiw.auction.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.auction.utils.ConnectionHandler;
import it.polimi.tiw.auction.beans.User;
import it.polimi.tiw.auction.dao.AuctionDAO;
import it.polimi.tiw.auction.dao.BidDAO;

/**
 * Servlet implementation class CreateBid
 */
@WebServlet("/CreateBid")
public class CreateBid extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateBid() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
		connection = ConnectionHandler.getConnection(servletContext);
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}

		// Get and parse all parameters from request
		boolean isBadRequest = false;
		BidDAO bidDAO = new BidDAO(connection);
		AuctionDAO auctionDAO = new AuctionDAO(connection); 
		Float bid = null;
		Integer auctionId = null;
		try {
			bid = Float.parseFloat(request.getParameter("bid"));
			auctionId = Integer.parseInt(request.getParameter("auctionId"));
			float lastBid = 0;
			float initialPrice = 0;
			float raise = 0;
			try {
				lastBid = bidDAO.findLastBid(auctionId).getOffer();
				initialPrice = auctionDAO.findAuctionDetailsById(auctionId).getInitialPrice();
				raise = auctionDAO.findAuctionDetailsById(auctionId).getRaise();
			}catch(Exception e) {
				//e.printStackTrace();
			}
			isBadRequest = bid<=0 || bid<lastBid+raise || bid < initialPrice;
		} catch (NumberFormatException | NullPointerException e) {
			isBadRequest = true;
			e.printStackTrace();
		}
		if (isBadRequest) {
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/GetAuctionDetails?auctionid=" + auctionId + "&biderror=true";
			response.sendRedirect(path);
			return;
		}

		// Create bid in DB
		User user = (User) session.getAttribute("user");
		try {
			bidDAO.createBid(user.getUsername(), auctionId, Timestamp.from(Instant.now()), bid);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to add bid");
			return;
		}

		// return the user to the right view
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GetAuctionDetails?auctionid=" + auctionId;
		response.sendRedirect(path);
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
