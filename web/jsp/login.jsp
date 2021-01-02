<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>login</title>
</head>
<body>

<form action="/Database_PJ_war_exploded/account/login" method="post">
    username: <input type="text" name="userName">
    password: <input type="text" name="pass">
    <input type="submit" value="submit">
    ${requestScope.message}
</form>

</body>
</html>
