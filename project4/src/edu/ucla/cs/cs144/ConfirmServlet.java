package edu.ucla.cs.cs144;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ConfirmServlet extends HttpServlet implements Servlet {
       
    public ConfirmServlet() {}

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    
    response.setContentType("text/html");
    HttpSession session = request.getSession(true);
    // date and time information 
    Calendar cal = Calendar.getInstance();
    cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
    request.setAttribute("buy_time", sdf.format(cal.getTime()).toString()); 



    String str_credit_card = request.getParameter("credit");
    
    try
    {
      Integer credit_card = Integer.parseInt(str_credit_card);
      request.setAttribute("credit_card",credit_card);
      request.setAttribute("error",false);
    }
    catch(NumberFormatException e)
    {
      request.setAttribute("error",true);
    }

    request.getRequestDispatcher("/confirm.jsp").forward(request, response);

  }

  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }
}
