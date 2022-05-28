package javaCode;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/postWifi", "/postHistory"}) // "/postWifi"
public class HelloServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	 
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String x = request.getParameter("locationX");
        String y = request.getParameter("locationY");        
        
        Double dx = Double.valueOf(x);
        Double dy = Double.valueOf(y);
        DBManager.InsertHistoryDB(dx, dy);
        System.out.println("ok");
        
        response.sendRedirect("index.jsp");
        
        
    }
 
}