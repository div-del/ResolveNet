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

@WebServlet("/add-complaint")
public class AddComplaintServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read complaint details from request
        String username    = request.getParameter("username");
        String title       = request.getParameter("title");
        String description = request.getParameter("description");
        String priority    = request.getParameter("priority");  // LOW, MEDIUM, HIGH

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = DatabaseConnection.getConnection();

            // Insert complaint into DB — status starts as PENDING
            String sql = "INSERT INTO complaints (username, title, description, priority, status) VALUES (?, ?, ?, ?, 'PENDING')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, title);
            stmt.setString(3, description);
            stmt.setString(4, priority);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                out.println("{\"status\": \"success\", \"message\": \"Complaint submitted successfully\"}");
            } else {
                out.println("{\"status\": \"fail\", \"message\": \"Could not submit complaint\"}");
            }

            stmt.close();
            conn.close();

        } catch (Exception e) {
            response.setStatus(500);
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
