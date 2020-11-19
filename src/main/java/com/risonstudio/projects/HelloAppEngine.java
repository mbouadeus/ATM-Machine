package com.risonstudio.projects;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
    name = "HelloAppEngine",
    urlPatterns = {"/hello"}
)
public class HelloAppEngine extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) 
      throws IOException {

    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8");

    String name = request.getParameter("name");


    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Entity person = new Entity("Person");
    
    person.setProperty("name", name);
    datastore.put(person);
    
    response.getWriter().print("Hello " + name + ", from App Engine!\r\n");
  }
}