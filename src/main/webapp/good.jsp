<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>商品列表</title>
    <style>
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background-color: #f2f2f2; }
    </style>
</head>
<body>
<h1>商品列表</h1>
<a href="menu">返回菜单</a>

<c:if test="${not empty error}">
    <p style="color:red;">${error}</p>
</c:if>

<c:if test="${not empty products}">
    <table>
        <tr>
            <th>商品编号</th>
            <th>商品名称</th>
            <th>规格</th>
            <th>单价</th>
            <th>库存数量</th>
        </tr>
        <c:forEach items="${products}" var="p">
            <tr>
                <td>${p.goodID}</td>
                <td>${p.goodName}</td>
                <td>${p.goodSize}</td>
                <td>${p.goodPrice}</td>
                <td>${p.goodNum}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
</body>
</html>