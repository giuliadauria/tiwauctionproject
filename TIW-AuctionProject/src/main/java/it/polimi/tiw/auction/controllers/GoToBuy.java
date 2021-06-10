package it.polimi.tiw.auction.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import it.polimi.tiw.auction.utils.ConnectionHandler;
import it.polimi.tiw.auction.beans.OpenAuction;
import it.polimi.tiw.auction.beans.User;
import it.polimi.tiw.auction.beans.WonAuction;
import it.polimi.tiw.auction.dao.AuctionDAO;

/**
 * Servlet implementation class GoToBuy
 */
@WebServlet("/GoToBuy")
public class GoToBuy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection = null;
	
	 /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToBuy() {
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
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
				String loginpath = getServletContext().getContextPath() + "/index.html";
				HttpSession session = request.getSession();
				if (session.isNew() || session.getAttribute("user") == null) {
					response.sendRedirect(loginpath);
					return;
				}
				AuctionDAO auctionDAO = new AuctionDAO(connection);
				List<OpenAuction> openAuctions = new ArrayList<OpenAuction>();
				String keyword = (String) session.getAttribute("keyword");
				if (keyword == null) {
					keyword = "";
				}

				try {
					openAuctions = auctionDAO.findAuctionByKeyword(keyword);
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to find open auctions");
					return;
				}
				
				List<WonAuction> wonAuctions = new ArrayList<WonAuction>();

				try {
					User user = (User) session.getAttribute("user");
					wonAuctions = auctionDAO.findWonAuctionByContractor(user.getUserId());
				} catch (SQLException e) {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to find won auctions");
					return;
				}

				// Redirect to the Home page and add missions to the parameters
				String path = "/WEB-INF/Buy.html";
				ServletContext servletContext = getServletContext();
				final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
				ctx.setVariable("keyword", keyword);
				ctx.setVariable("openAuctions", openAuctions);
				ctx.setVariable("wonAuctions", wonAuctions);
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
