<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Distributed Systems Class Clicker</title>
</head>
<body>
<h2>Distributed Systems Class Clicker</h2>
<% if (request.getAttribute("choiceRegistered") != null) { %>
<p>Your choice "<%= request.getAttribute("choiceRegistered") %>" has been registered.</p>
<% } %>
<form action="submit" method="post">
  Submit your answer to the current question:<br/>
  <input type="radio" name="choice" value="A" id="A"> <label for="A">A</label><br/>
  <input type="radio" name="choice" value="B" id="B"> <label for="B">B</label><br/>
  <input type="radio" name="choice" value="C" id="C"> <label for="C">C</label><br/>
  <input type="radio" name="choice" value="D" id="D"> <label for="D">D</label><br/>
  <input type="submit" value="Submit"/>
</form>
</body>
</html>