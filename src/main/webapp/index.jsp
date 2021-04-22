<%--
  Created by IntelliJ IDEA.
  User: jerne
  Date: 1/20/2021
  Time: 7:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>User Sign In</title>
    <link href="https://fonts.googleapis.com/css?family=Press+Start+2P" rel="stylesheet">
    <link href="https://unpkg.com/nes.css@latest/css/nes.min.css" rel="stylesheet" />
    <style>
        html, body, pre, code, kbd, samp {
            font-family: "Press Start 2P";
        }
    </style>

</head>
<body>
<h1 class="nes-text is-primary">This is the user sign in page</h1>
<!-- <form action="https://accounts.spotify.com:443/authorize?client_id=c59275ebbf74401fa69f5936e6c43cca&response_type=code
&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FUserSignIn_war_exploded%2Fhome&scope=user-library-read+user-read-private"
      method="get">
    <label for="yourName" class="nes-">Enter your username:</label>
     <input type="text" name="yourName" size="20" class="nes-field">
    <br><br>
    <label for="password">Enter your password:</label>
    <input type="password" name="password" size="20" class="nes-field">
    <br>${message}
    <br><br>
    <input type="submit" value="login" class="nes-btn" />
</form> -->
<a href="https://accounts.spotify.com:443/authorize?client_id=c59275ebbf74401fa69f5936e6c43cca&response_type=code
&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2FUserSignIn_war_exploded%2Fhome&scope=user-library-read+user-read-private"
   class="nes-btn is-primary">Sign in</a>
</body>
</html>
