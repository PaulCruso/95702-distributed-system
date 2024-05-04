<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<html>
<head>
    <title>Results</title>
</head>
<body>
<h2>Distributed Systems Class Clicker</h2>
<% Map<String, Integer> results = (Map<String, Integer>) request.getAttribute("results");
    if (results.get("A") == 0 && results.get("B") == 0 && results.get("C") == 0 &&results.get("D") == 0 ) { %>
<p>There are currently no results.</p>
<% } else { %>
    The Result from the surveys are as follows: <br/><br/>

    <% for (Map.Entry<String, Integer> entry : results.entrySet()) { %>

        <%= entry.getKey() %>:
        <%= entry.getValue() %><br/>
    <% } %>

<% } %>
</body>
</html>
