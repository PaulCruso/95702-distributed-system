<%-- Author: Kaizhong Ying --%>
<%-- Last Modified: Feb 3, 2024 --%>
<%-- This index.jsp file aims to get a text and hash function --%>
<%-- return the hexadecimal text and as base 64 notation --%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hash Function Calculator</title>
</head>
<body>
<h1>Hash Function Calculator</h1>
<p>Enter text and select a hash function to compute its MD5 or SHA-256 hash.</p>

<%-- this form get the required text and hash function--%>
<form action="text" method="get">
    <label for="input">Text:</label><br>
    <input type="text" id="input" name="input" required><br><br>
    <label for="type">Choose a hash function:</label><br>
    <input type="radio" id="md5" name="type" value="MD5" checked>
    <label for="md5">MD5</label><br>
    <input type="radio" id="sha256" name="type" value="SHA-256">
    <label for="sha256">SHA-256</label><br><br>
    <input type="submit" value="Compute Hash">
</form>
<%-- get the four attributes from ComputeHashes class --%>
<%-- Seperate the output into "MD5" and "SHA-256" two conditions --%>
<%
    String input = (String) request.getAttribute("input");
    String type = (String) request.getAttribute("hash function");
    String hex = (String) request.getAttribute("hex text");
    String base64 = (String) request.getAttribute("base64");

    if (input != null && type != null && hex != null && base64 != null) {
        if ("MD5".equals(type)){
%>
            <h1>Result of MD5</h1>
            <p><strong>Text: </strong> <%= input %> </p>
            <p><strong>Hexadecimal: </strong> <%= hex %></p>
            <p><strong>Base64: </strong> <%= base64 %></p>
<%      }else{ %>
            <h1>Result of SHA-256</h1>
            <p><strong>Text: </strong> <%= input %> </p>
            <p><strong>Hexadecimal: </strong> <%= hex %></p>
            <p><strong>Base64: </strong> <%= base64 %></p>

      <%}
    }
%>
</body>
</html>