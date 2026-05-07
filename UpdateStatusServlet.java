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

@WebServlet("/update-status")
public class UpdateStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Admin sends complaint ID and new status
        String idParam = request.getParameter("id");
        String status  = request.getParameter("status"); // PENDING, IN_PROGRESS, RESOLVED

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Basic input check
        if (idParam == null || status == null) {
            response.setStatus(400);
            out.println("{\"status\": \"fail\", \"message\": \"id and status are required\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);

            Connection conn = DatabaseConnection.getConnection();

            String sql = "UPDATE complaints SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                out.println("{\"status\": \"success\", \"message\": \"Status updated to " + status + "\"}");
            } else {
                out.println("{\"status\": \"fail\", \"message\": \"No complaint found with that id\"}");
            }

            stmt.close();
            conn.close();

        } catch (NumberFormatException e) {
            response.setStatus(400);
            out.println("{\"status\": \"fail\", \"message\": \"id must be a number\"}");
        } catch (Exception e) {
            response.setStatus(500);
            out.println("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}
