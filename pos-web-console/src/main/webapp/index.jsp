<%@ page language="java" pageEncoding="utf-8"%>
<%
    response.addHeader("Access-Control-Allow-Origin", "*");
    response.addHeader("Cache-Control", "no-cache");
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>
        鹦鹉美家 - PosConsole Service
    </title>
</head>
<body>
<center style="margin-top: 100px;">
    <h1>
        <img id="img"  alt="" width="250px" src="load.gif">
        <br/>
        <br/>
        <span id="info">
鹦鹉美家 - PosConsole Service运行中...
</span>
        <a href="help" target="_blank"> 查看API</a>
    </h1>
</center>
</body>
</html>
