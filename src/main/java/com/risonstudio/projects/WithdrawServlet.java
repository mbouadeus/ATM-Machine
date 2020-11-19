package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "Withdraw",
    urlPatterns = {"/withdraw"}
)
public class WithdrawServlet extends HttpServlet {
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    // Retrieve withdraw request parameters.
	    String accountkey = request.getParameter("accountKey");
	    String accountFrom = request.getParameter("accountFrom");
	    String amount = request.getParameter("amount");
	    
	    // Complete withdraw and retrieve new account information.
	    response.getWriter().print(AccountUtility.withdraw(accountkey, accountFrom, amount));
	}
}