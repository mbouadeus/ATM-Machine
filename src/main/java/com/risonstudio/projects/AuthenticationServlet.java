package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@WebServlet(
    name = "Authentication",
    urlPatterns = {"/authenticate"}
)
public class AuthenticationServlet extends HttpServlet {
	
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    // Retrieve authentication parameters.
	    String customerID = request.getParameter("customerID");
	    String pin = request.getParameter("pin");
	    
	    // Authenticate user and retrieve account key.
	    String accountKey = AccountUtility.authenticate(customerID, pin);
	    
	    response.getWriter().print(accountKey);
	  }
}
