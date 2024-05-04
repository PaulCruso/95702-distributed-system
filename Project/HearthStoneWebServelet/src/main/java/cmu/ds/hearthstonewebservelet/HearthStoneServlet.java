package cmu.ds.hearthstonewebservelet;
/**
 * HearthStoneServlet.java
 * Author: Kaizhong Ying
 * Andrew ID: kying
 * Last Modified: April 2, 2024
 *
 * A servlet that handles requests for Hearthstone card data and dashboard analytics. It supports two main operations:
 * fetching data for a specific card and displaying it, and retrieving and displaying analytics for the card dataset.
 * The servlet responds to two types of GET requests, distinguished by their path:
 * /getCard - Fetches data for a specific card by its name. If the card is found, it is added to the MongoDB
 * database, and the card data is returned in JSON format to be displayed on the result.jsp page.
 * /getDashBoard - Performs analytics on the stored card data and presents results such as average cost,
 * most common type of card, and total count on the dashboard.jsp page.
 */

import java.io.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "HearthStoneServlet", value = {"/getDashBoard", "/getCard"})
public class HearthStoneServlet extends HttpServlet {

    /**
     * Initializes the servlet. This method is called by the servlet container to indicate to a servlet that the
     * servlet is being placed into service.
     */
    @Override
    public void init() {}

    /**
     * Handles the GET requests by routing to the appropriate process based on the request path.
     * It supports fetching Hearthstone card data and generating dashboard analytics.
     *
     * @param request  The {@link HttpServletRequest} object that contains the request the client has made to the servlet.
     * @param response The {@link HttpServletResponse} object that contains the response the servlet sends to the client.
     * @throws IOException      If an input or output exception occurs.
     * @throws ServletException If the request for the GET could not be handled.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Determine the request's servlet path to decide the action.
        String path = request.getServletPath();
        // Initialize the variable that will hold the name of the next view (JSP page).
        String nextView = "";

        // Check if the path indicates a request to get a specific card's data.
        if (path.contains("getCard")) {
            // Retrieve the card name from the request parameters.
            String cardName = request.getParameter("cardName");

            // Check if the cardName parameter is missing or if the specified card does not exist.
            if (cardName == null || HearthStoneModel.getCard(cardName) == null) {
                // Direct the user to the result page without fetching the card.
                nextView = "result.jsp";
            } else {
                // The card exists, add it to the MongoDB and retrieve its JSON representation.
                HearthStoneModel.addMongoDB(cardName);
                // Fetch the latest MongoDB JSON data for the added card.
                String result = HearthStoneModel.getMongoDBJson();
                // Set the JSON result as an attribute to be accessed in the JSP.
                request.setAttribute("result", result);
                // Direct the user to the result page to view the card data.
                nextView = "result.jsp";
            }
        } else if (path.contains("getDashBoard")) {
            // The request is for the dashboard analytics.

            // Perform the analytics on the stored data.
            HearthStoneModel.analytic();
            // Set attributes for the request with the analytics data to be displayed on the dashboard.
            request.setAttribute("mongoDBString", HearthStoneModel.getMongoDBString());
            request.setAttribute("averageCost", HearthStoneModel.getAverageCost());
            request.setAttribute("mostType", HearthStoneModel.getMostType());
            request.setAttribute("count", HearthStoneModel.getCount());
            // Direct the user to the dashboard page to view the analytics.
            nextView = "dashboard.jsp";
        }
        // Get the request dispatcher for the next view and forward the request and response objects.
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }

}