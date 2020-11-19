package com.risonstudio.projects;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "GetAccount",
    urlPatterns = {"/getaccount"}
)
public class RetrieveAccountServlet extends HttpServlet {
	@Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response) 
	      throws IOException {

	    response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");

	    String accountkey = request.getParameter("accountKey");
	    response.getWriter().print(AccountUtility.getAccount(accountkey));
	}
}
