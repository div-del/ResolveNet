package com.resolvenet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read username and password from request
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User found — login success
                String role = rs.getString("role");
                out.println("{\"status\": \"success\", \"role\": \"" + role + "\", \"username\": \"" + username + "\"}");
            } else {
                // Wrong credentials
                response.setStatus(401);
                out.println("{\"status\": \"fail\", \"message\": \"Invalid username or password\"}");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            response.setStatus(500);
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
