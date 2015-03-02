package edu.ucla.cs.cs144;

import java.lang.Integer;
import java.io.IOException;

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
        String query = request.getParameter("q").trim();
        int numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
        int numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"))+1;
        SearchResult[] results = AuctionSearchClient.basicSearch(query,numResultsToSkip,numResultsToReturn);
        SearchResult[] modifiedResults = null;
        if(results.length == 0)
        {
            request.setAttribute("no_result",true);
        }
        
        else
        {
            request.setAttribute("no_result",false);
            if(results.length > numResultsToReturn-1)
            {
               request.setAttribute("toskip", true);
               modifiedResults = new SearchResult[results.length-1];
               System.arraycopy(results, 0, modifiedResults, 0, results.length-1);
            }
            else
            {
                request.setAttribute("toskip", false);
                modifiedResults = new SearchResult[results.length];
                System.arraycopy(results, 0, modifiedResults, 0, results.length);
            }

        }
        request.setAttribute("sr",modifiedResults);
        request.getRequestDispatcher("/results.jsp").forward(request, response);
    }
}

