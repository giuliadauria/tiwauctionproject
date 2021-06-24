package it.polimi.tiw.auction.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import it.polimi.tiw.auction.beans.AuctionDetails;
import it.polimi.tiw.auction.dao.AuctionDAO;
import it.polimi.tiw.auction.utils.ConnectionHandler;

/**
 * Servlet implementation class GetAuctionDetails
 */
@WebServlet("/GetAuctionDetails")
public class GetAuctionDetails extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;
	String folderPath = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAuctionDetails() {
        super();
    }
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");

		connection = ConnectionHandler.getConnection(getServletContext());
		folderPath = getServletContext().getInitParameter("outputpath");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		// get and check params
		Integer auctionId = null;
		try {
			auctionId = Integer.parseInt(request.getParameter("auctionid"));
		} catch (NumberFormatException | NullPointerException e) {
			// only for debugging e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param values");
			return;
		}
		AuctionDAO auctionDAO = new AuctionDAO(connection);
		AuctionDetails auctionDetails = null;
		try {
			auctionDetails = auctionDAO.findAuctionDetailsById(auctionId);
			if (auctionDetails == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource not found");
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to recover auction details");
			return;
		}
		if(auctionDetails.getItem().getPictures().size() > 0) {
			//String fileName = URLDecoder.decode(auctionDetails.getItem().getPictures().get(0).getPictureUrl(), "UTF-8");
			File file = new File(folderPath, auctionDetails.getItem().getPictures().get(0).getPictureUrl());
			if (!file.exists() || file.isDirectory()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not present");
				return;
			}
		}
		String path = "/WEB-INF/Details.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("closeerror", request.getParameter("closeerror"));
		ctx.setVariable("biderror", request.getParameter("biderror"));
		ctx.setVariable("auctionDetails", auctionDetails);
		templateEngine.process(path, ctx, response.getWriter());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
