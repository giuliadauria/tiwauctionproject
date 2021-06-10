package it.polimi.tiw.auction.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.auction.dao.AuctionDAO;
import it.polimi.tiw.auction.utils.ConnectionHandler;

/**
 * Servlet implementation class CloseAuction
 */
@WebServlet("/CloseAuction")
public class CloseAuction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection;
	TemplateEngine templateEngine;
       
    public CloseAuction() {
        super();
    }
    
    public void init() throws ServletException{
    	ServletContext servletContext = getServletContext();
    	ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
    	templateResolver.setTemplateMode(TemplateMode.HTML);
    	this.templateEngine = new TemplateEngine();
    	this.templateEngine.setTemplateResolver(templateResolver);
    	templateResolver.setSuffix(".html");
    	connection = ConnectionHandler.getConnection(getServletContext());
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

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
		boolean cantClose = false;
		int auctionId = 0;
		try {
			auctionId = Integer.parseInt(request.getParameter("auctionId"));
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
		AuctionDAO auctionDAO = new AuctionDAO(connection);	
		try {
			auctionDAO.closeAuction(auctionId);
		}catch(SQLException e) {
			//if you cannot close an auction, a message appears
			cantClose = true;
		}
		if(cantClose) {
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/GetAuctionDetails?auctionid=" + auctionId + "&closeerror=true";
			response.sendRedirect(path);
			return;
		}
		//Redirect to the Sell page and add the closed action to the closed auction list
		else {
			String ctxpath = getServletContext().getContextPath();
			String path = ctxpath + "/GoToSell";
			response.sendRedirect(path);
			return;
		}	
	}
	
	
	
	
	
}
