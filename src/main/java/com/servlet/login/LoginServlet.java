
package com.servlet.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // Query to check if the user exists in the database
    private static final String LOGIN_QUERY = "SELECT * FROM users WHERE EmailId=? AND Password=?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get PrintWriter
        PrintWriter pw = res.getWriter();
        // Set Content type
        res.setContentType("text/html");

        String EmailId = req.getParameter("EmailId");
        String Password = req.getParameter("Password");

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql:///medicom", "root", "system");
                PreparedStatement ps = con.prepareStatement(LOGIN_QUERY)) {
            ps.setString(1, EmailId);
            ps.setString(2, Password);

            // Execute query to check if user exists
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // User exists, authentication successful
            	String fullName = rs.getString("FirstName") + " " + rs.getString("LastName");
            	req.getSession().setAttribute("fullName", fullName);
                res.sendRedirect("index2.html");
            } else {
                // User not found or incorrect credentials
                pw.println("Invalid credentials. Please try again.");
            }
        } catch (SQLException se) {
            pw.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            pw.println(e.getMessage());
            e.printStackTrace();
        }

        // Close the stream
        pw.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Redirect GET requests to the login page or handle them as needed.
    }
}
