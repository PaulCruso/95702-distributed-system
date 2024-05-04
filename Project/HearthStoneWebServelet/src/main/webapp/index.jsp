<%-- Author: Kaizhong Ying --%>
<%-- AndrewId: Kying --%>
<%-- Last Modified: April 1, 2024 --%>
<%-- This index.jsp file aims to give user a card name to search and get the dashboard --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HearthStone card collections</title>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: auto;
            background: #fff;
            padding: 20px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }
        h1 {
            color: #333;
            text-align: center;
        }
        form {
            margin-bottom: 20px;
            background: #f9f9f9;
            padding: 15px;
            border-radius: 8px;
        }
        label {
            display: block;
            margin-bottom: 10px;
            color: #666;
        }
        input[type="text"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Adds padding without increasing the width */
        }
        input[type="submit"] {
            background-color: #5c9ac0;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #4b8ca3;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Welcome to my Hearthstone web service!</h1>
    <form action="getCard" method="GET">
        <label for="cardName">Give me a HearthStone card name!</label>
        <input type="text" id="cardName" name="cardName" value="" />
        <input type="submit" value="Submit" />
    </form>
    <form action="getDashBoard" method="GET">
        <label>View my HearthStone Dashboard</label>
        <input type="submit" value="View" />
    </form>
</div>
</body>
</html>
