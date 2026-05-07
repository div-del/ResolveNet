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
import java.sql.Statement;

@WebServlet("/view-complaints")
public class ViewComplaintsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Optional: filter by username (for regular user) or get all (for admin)
        String username = request.getParameter("username"); // null means admin wants all

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            Connection conn = DatabaseConnection.getConnection();

            ResultSet rs;

            if (username != null && !username.isEmpty()) {
                // Show only this user's complaints
                String sql = "SELECT * FROM complaints WHERE username = ? ORDER BY id DESC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                rs = stmt.executeQuery();
            } else {
                // Admin — show all complaints
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM complaints ORDER BY id DESC");
            }

            // Build JSON array manually (simple — no external libraries)
            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) json.append(",");
                first = false;

                json.append("{");
                json.append("\"id\": ")         .append(rs.getInt("id"))                   .append(",");
                json.append("\"username\": \"") .append(rs.getString("username"))  .append("\",");
                json.append("\"title\": \"")    .append(rs.getString("title"))     .append("\",");
                json.append("\"description\": \"").append(rs.getString("description")).append("\",");
                json.append("\"priority\": \"") .append(rs.getString("priority"))  .append("\",");
                json.append("\"status\": \"")   .append(rs.getString("status"))    .append("\"");
                json.append("}");
            }

            json.append("]");

            out.println(json.toString());
            rs.close();
            conn.close();

        } catch (Exception e) {
            response.setStatus(500);
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
