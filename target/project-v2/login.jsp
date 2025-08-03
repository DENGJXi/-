<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录页面</title>
    <style>
        /* 简单样式美化 */
        body {
            font-family: "微软雅黑", sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-box {
            background-color: #fff;
            padding: 20px 40px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .login-box h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input {
            width: 250px;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .form-group button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .form-group button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="login-box">
    <h2>用户登录</h2>
    <p style="color: red;">${errorMsg}</p>
    <!-- 表单提交到名为 LoginServlet 的 Servlet，后续需在 web.xml 或用注解配置 -->
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="username">用户名：</label>
            <input type="text" id="username" name="username" placeholder="请输入用户名" required>
        </div>
        <div class="form-group">
            <label for="password">密码：</label>
            <input type="password" id="password" name="password" placeholder="请输入密码" required>
        </div>
        <div class="form-group">
            <label for="code">验证码：</label>
            <input type="text" id="code" name="code" placeholder="请输入验证码" required>
            <span style="margin-left: 10px; color: #666;">${code}</span>
        </div>
        <div class="form-group">
            <button type="submit">登录</button>
        </div>
    </form>
</div>
</body>
</html>