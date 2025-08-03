<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>商品管理系统</title>
</head>
<body>
<h1>商品管理菜单</h1>
<ul>
    <li><a href="query">商品查询</a></li>
    <li><a href="add">商品录入</a></li>
    <li><a href="delete">商品删除</a></li>
    <li><a href="updateStock">库存管理</a></li>
    <li><a href="/">返回首页</a></li>
</ul>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>
</body>
</html>