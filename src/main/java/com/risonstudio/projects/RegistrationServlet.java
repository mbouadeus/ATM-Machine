package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@WebServlet(
    name = "Registration",
    urlPatterns = {"/register"}
)
public class RegistrationServlet extends HttpServlet {
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    // Retrieve registration parameters.
	    String name = request.getParameter("name");
	    String pin = request.getParameter("pin");
	    String initialCheckingBalance = request.getParameter("initialCheckingBalance");
	    String initialSavingsBalance = request.getParameter("initialSavingsBalance");

	    // Register new user and retrieve account key.
	    String accountKey = AccountUtility.register(name, pin, initialCheckingBalance, initialSavingsBalance);
	    
	    if (accountKey == null) response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    response.getWriter().print(accountKey);
	  }
}
