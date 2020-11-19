package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "Transfer",
    urlPatterns = {"/transfer"}
)
public class TransferServlet extends HttpServlet {
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    // Retrieve transfer request parameters.
	    String accountkey = request.getParameter("accountKey");
	    String accountFrom = request.getParameter("accountFrom");
	    String accountTo = request.getParameter("accountTo");
	    String amount = request.getParameter("amount");
	    
	    // Complete transfer and retrieve new account information.
	    response.getWriter().print(AccountUtility.transfer(accountkey, accountFrom, accountTo, amount));
	}
}