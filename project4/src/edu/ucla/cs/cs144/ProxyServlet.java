package edu.ucla.cs.cs144;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");

         
        String query_input = request.getParameter("q");
        
        String url =  "http://google.com/complete/search?output=toolbar&";
        //String charset = "UTF-8";
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        
        
        String query = String.format("q=%s", URLEncoder.encode(query_input, charset));
        
        URLConnection connection = new URL(url + query).openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response_google = (InputStream)connection.getInputStream();
        BufferedReader br = new BufferedReader( new InputStreamReader(response_google) );
        String xmlResponse = "";
        String sCurrentLine;
        while ((sCurrentLine = br.readLine()) != null)
        {
            xmlResponse = xmlResponse + sCurrentLine;
        }
        
        PrintWriter out = response.getWriter();
        out.print(xmlResponse);
        
    }
}
