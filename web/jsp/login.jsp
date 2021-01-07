<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>login</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
<div class="col-md-4" style="margin-left: 50%; margin-top: 300px">
    <form action="/Database_PJ_war_exploded/account/login" method="post" style="opacity: 50%">
        <div class="form-group">
            <label for="loginRole"><h3>Role</h3></label>
            <select name="type" id="loginRole" class="form-control" style="max-width: 300px">
                <option value="doctor">主治医生</option>
                <option value="chief nurse">护士长</option>
                <option value="ward nurse">病房护士</option>
                <option value="emergency nurse">急救护士</option>
            </select>
        </div>
        <div class="form-group">
            <label for="loginName"><h3>Username</h3></label>
            <input class="input-group" type="text" name="name" id="loginName" style="max-width: 300px; height: 40px" placeholder="username">
        </div>
        <div class="form-group">
            <label for="loginPass"><h3>Password</h3></label>
            <input class="input-group" type="text" name="password" id="loginPass" style="max-width: 300px; height: 40px" placeholder="password">
        </div>
        <div class="form-group">
            <button class="btn-default" type="submit" value="submit" style="width: 80px; height: 40px; margin-top: 30px;">Login</button>
        </div>
    </form>
    <div>${requestScope.message}</div>
</div>
</body>
<style>
    body{
        background: no-repeat url("https://i.loli.net/2021/01/06/Ip6RCEMBkWNYbmS.png");
        background-size: 100% 100%;
    }
</style>
</html>
