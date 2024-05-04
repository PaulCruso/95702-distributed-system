<%-- Author: Kaizhong Ying --%>
<%-- AndrewId: Kying --%>
<%-- Last Modified: April 1, 2024 --%>
<%-- This dashboard.jsp file aims to give user the dashboard of card collection--%>

<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hearthstone Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            padding: 20px;
            line-height: 1.6;
        }
        .container {
            max-width: 800px;
            margin: auto;
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333333;
            text-align: center;
        }
        p {
            color: #555;
        }
        .card-detail {
            border: 1px solid #ddd;
            padding: 15px;
            margin-bottom: 10px;
            border-radius: 8px;
            background: #f9f9f9;
        }
        strong {
            color: #333;
        }
        .divider {
            border: 0;
            height: 1px;
            background: #e0e0e0;
            margin: 20px 0;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to my Hearthstone Dashboard!</h1>
    <p>Total numbers of HearthStone card in collections are: <%= request.getAttribute("count") %></p>
    <p>Average cost of cards in collections is: <%= request.getAttribute("averageCost") %></p>
    <p>The most occurrence of HearthStone type is: <%= request.getAttribute("mostType") %></p>
    <hr class="divider">
    <h2>Card Collection in MongoDB:</h2>
    <%
        ArrayList<String> cardDetails = (ArrayList<String>) request.getAttribute("mongoDBString");
        if (cardDetails != null) {
            for (String details : cardDetails) {
                String[] parts = details.split("  "); // Splitting by double space as per your structure
                if (parts.length >= 7) { // Ensure there are enough parts to avoid IndexOutOfBoundsException
                    String cardName = parts[0];
                    String cardId = parts[1];
                    String cardText = parts[2];
                    String cardClass = parts[3];
                    String cardCost = parts[4];
                    String cardFlavor = parts[5];
                    String cardRarity = parts[6];
                    String cardImageUrl = parts[7]; // Handle optional image URL
                    String cardType = parts[8];
    %>
    <div class="card-detail">
        <p><strong>cardName:</strong> <%= cardName %></p>
        <p><strong>cardId:</strong> <%= cardId %></p>
        <p><strong>cardText:</strong> <%= cardText %></p>
        <p><strong>cardClass:</strong> <%= cardClass %></p>
        <p><strong>cardCost:</strong> <%= cardCost %></p>
        <p><strong>cardFlavor:</strong> <%= cardFlavor %></p>
        <p><strong>cardRarity:</strong> <%= cardRarity %></p>
        <p><strong>cardImageUrl:</strong> <%= cardImageUrl %></p>
        <p><strong>cardType:</strong> <%= cardType %></p>
    </div>
    <%
                }
            }
        }
    %>
</div>
</body>
</html>

