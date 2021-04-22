<%@ page import="network.ApiConnectorInterface" %>
<%@ page import="network.ApiConnectorInterfaceImpl" %>
<%@ page import="data.UserDAO" %>
<%@ page import="data.User" %><%--
  Created by IntelliJ IDEA.
  User: jerne
  Date: 1/20/2021
  Time: 10:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test Page</title>
    <link href="https://fonts.googleapis.com/css?family=Press+Start+2P" rel="stylesheet">
    <link href="https://unpkg.com/nes.css@latest/css/nes.min.css" rel="stylesheet" />
    <style>
        html, body, pre, code, kbd, samp {
            font-family: "Press Start 2P";
        }
    </style>
</head>
<body>
    <p>Just another Test page</p>
    <p><%= request.getParameter("yourName")%></p>
    <%! UserDAO data = new UserDAO(); %>
    <%! User user; %>
    <%= user = data.checkLogin(request.getParameter("yourName"), request.getParameter("password")) %>
    <p><%= user.getName() %></p>

</body>
</html>
