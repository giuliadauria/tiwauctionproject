package it.polimi.tiw.auction.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import it.polimi.tiw.auction.beans.User;
import it.polimi.tiw.auction.dao.AuctionDAO;
import it.polimi.tiw.auction.utils.ConnectionHandler;

/**
 * Servlet implementation class CreateAuction
 */
@WebServlet("/CreateAuction")
@MultipartConfig
public class CreateAuction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	String folderPath = "";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateAuction() {
        super();
    }
    
    public void init() throws ServletException{
    	connection = ConnectionHandler.getConnection(getServletContext());
		folderPath = getServletContext().getInitParameter("outputpath");
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		Part filePart = request.getPart("file");
		String fileNameEncoded = null;
		
		if(filePart != null) {
			// We then check the parameter is valid (in this case right format)
		    String contentType = filePart.getContentType();
		    if(!contentType.equals("application/octet-stream")) {
			    if (!contentType.startsWith("image")) {
			    	System.out.println(contentType);
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "File format not permitted");
					return;
				}
			    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
			    
			    String fileNameWithoutSpaces = StringUtils.deleteWhitespace(fileName);
			    
			    //String fileNameWithoutSpaces = fileName.replaceAll(" ","");

			    //String fileNameWithoutSpaces = fileName.replaceAll("\\s+","");
			    //encode nel caso il nome del file avesse spazi o cose sbagliate
			    fileNameEncoded = URLEncoder.encode(fileNameWithoutSpaces, "utf-8");
				String outputpath = folderPath + fileNameEncoded;
				try{
					File file = new File(outputpath);
			
					try (InputStream fileContent = filePart.getInputStream()) {
						Files.copy(fileContent, file.toPath());	
					}catch(FileAlreadyExistsException e) {
					}catch (Exception e) {
						e.printStackTrace();
						response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while saving file");
						return;
					}
					
				}catch(Exception e) {
					e.printStackTrace();
					return;
				}
		    }
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
		List<String> imagesUrls = new ArrayList<>(0);
		if(fileNameEncoded != null) {
			imagesUrls.add(fileNameEncoded);
		}
		try {
			auctionDAO.createAuction(user.getUserId(), itemName, description, deadline, initialPrice, raise, imagesUrls);
		}catch(SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to create auction");
			return;
		}	
		//updating the view of sell page
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToSell";
		response.sendRedirect(path);	
		return;
	}

}
