package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "Deposit",
    urlPatterns = {"/deposit"}
)
public class DepositServlet extends HttpServlet {
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    // Retrieve deposit request parameters.
	    String accountkey = request.getParameter("accountKey");
	    String accountTo = request.getParameter("accountTo");
	    String amount = request.getParameter("amount");
	    
	    // Complete deposit and retrieve new account information.
	    response.getWriter().print(AccountUtility.deposit(accountkey, accountTo, amount));
	}
}