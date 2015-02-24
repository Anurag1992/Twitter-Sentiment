/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Twitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;

/**
 *
 * @author 007
 */
@WebServlet(name = "Response", urlPatterns = {"/Response"})
public class Response extends HttpServlet {
         
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException, ClassNotFoundException, JSONException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
      BayesianClassifier bob = new BayesianClassifier();       
      TweetHandler twat = new TweetHandler();
      String keyword = request.getParameter("keyword");
      System.out.println(keyword);
      List<Status> tweets =  twat.getTweets(keyword);
      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8"); 
      response.getWriter().write("<table class =\"pure-table\"><thead><td>The Tweets</td><td>Associated Sentiment</td></thead><tbody>");
       
      int pos = 0,neg=0;   
    for(Status tweet : tweets)
         {
            
             
             int result = bob.classifier(tweet.getText());
            
            
             switch(result)
             {
                 case 0 : {
                          neg++;
                          response.setContentType("text/html");  
                          response.setCharacterEncoding("UTF-8"); 
                          response.getWriter().write("<tr class = \"negative\"><td>"+tweet.getText()+"</td><td>Negative</td></tr>");
                          break;
                         }
               
                 case 4 :{ 
                          pos++;
                          response.setContentType("text/html");  
                          response.setCharacterEncoding("UTF-8"); 
                          response.getWriter().write("<tr class =\"positive\"><td>"+tweet.getText()+"</td><td>Positive</td></tr>");
                          break;    
                         }
             }
            
         }
        
    
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8"); 
        response.getWriter().write("<input type ='hidden' id = 'pos' value = "+pos+">");
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8"); 
        response.getWriter().write("<input type ='hidden' id = 'neg' value = "+neg+">");
       
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8"); 
        response.getWriter().write("</tbody></table>");                  
        
       


        } finally {            
            out.close();
        }
    }

     
   

   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
             try {
                 try {
                     processRequest(request, response);
                 } catch (JSONException ex) {
                     Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
                 }
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
             }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
             try {
                 try {
                     processRequest(request, response);
                 } catch (JSONException ex) {
                     Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
                 }
             } catch (ClassNotFoundException ex) {
                 Logger.getLogger(Response.class.getName()).log(Level.SEVERE, null, ex);
             }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
