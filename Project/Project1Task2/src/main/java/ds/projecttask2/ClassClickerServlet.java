/**
 * Author: Kaizhong Ying
 * Last Modified: Feb 4, 2024
 *
 * This program is the servlet of class clicker
 * it gives two pages /submit and /getResults
 * the doGet method record the number of click
 * the doPost gives user an interaction surface to submit the choice
 * the program illustrates the record and interaction of MVC
 */
package ds.projecttask2;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "ClassClicker", urlPatterns = {"/submit", "/getResults"})
public class ClassClickerServlet extends HttpServlet {
    private final ClassClickerModel clickerModel = new ClassClickerModel();

    /**
     * Record the results of users click each option and display
     * Reset the option after user open /getResults website
     *
     * @param request The HttpServletRequest given by the user request
     * @param response The response generated from the request
     * @throws IOException  if the request does not exist
     * @throws ServletException if the response can not be generated
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("/getResults".equals(request.getServletPath())) {
            request.setAttribute("results", clickerModel.getResults());
            request.getRequestDispatcher("results.jsp").forward(request, response);
            clickerModel.clearResults();
        } 
    }

    /**
     * Give user an interface to submit their option
     * It will continue recording unless user change the urlPatterns
     *
     * @param request The HttpServletRequest given by the user request
     * @param response The response generated from the request
     * @throws ServletException if the request does not exist
     * @throws IOException if the response can not be generated
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String choice = request.getParameter("choice");
        if (choice.equals("A") || choice.equals("B") || choice.equals("C") || choice.equals("D")) {
            clickerModel.recordChoice(choice);
            request.setAttribute("choiceRegistered", choice);
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

}