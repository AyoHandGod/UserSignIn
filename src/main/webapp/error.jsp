<%--
  Created by IntelliJ IDEA.
  User: jerne
  Date: 1/21/2021
  Time: 4:40 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <p>Uh oh! Things aren't working on this page at the moment</p>
    <p>We're getting a <%= response.getStatus() %> error </p>
    <c:if test="${headerValues.get('status') == 500}">
        <p>Ran into a server issue on our end</p>
    </c:if>
    <c:if test="${header.get('status') < 500 && header.get(status) > 300}">
        <p>400 status code man</p>
    </c:if>

</body>
</html>
