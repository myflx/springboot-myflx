package com.myflx.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SayServlet extends HttpServlet {
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // 获取请求参数 "message" 内容
        String message = request.getParameter("message");
        PrintWriter writer = response.getWriter();
        // 输出 "message" 参数内容
        writer.println(message);
        writer.flush();
    }
}
