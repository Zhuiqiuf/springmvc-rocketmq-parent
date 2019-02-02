<%--
  User: jackie
  Date: 2018/11/23
  Time: 11:25
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>登录</title>
</head>
<body>
<center>

    <div style="margin: 100px 0 0 0">
        <p style="color: red;">${msg }</p>
        <p>用户登录</p>
    </div>
    <div>
        <!--
        <form action="login" method="post">
    -->
        <form action="${pageContext.request.contextPath }/login"
              method="post">
            用户名:<input type="text" name="username" value="${username}"><br />
            密&nbsp;码:<input type="password" name="password"><br /> <br />
            &nbsp;<input type="submit" value="登陆">&nbsp; <input
                type="reset" value="重置" />
        </form>
    </div>
</center>
</body>
</html>