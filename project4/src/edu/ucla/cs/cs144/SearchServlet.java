package edu.ucla.cs.cs144;

import java.lang.Integer;
import java.io.IOException;

import javax.naming.directory.SearchResult;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	response.setContentType("text/html");
    	String query = request.getParameter("q");
    	int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
    	int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
    	
    	request.setAttribute("sr",AuctionSearchClient.basicSearch(query,numResultsToSkip,numResultsToReturn));
    	
    
    	
    	request.getRequestDispatcher("/results.jsp").forward(request, response);
        
    }
}

