package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Catalog extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>");
        pw.println("<div class='container'>");
        String title = req.getParameter("title");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb/mydb", "root", System.getenv("MYSQL_PASSWORD"))) {
                String sql = "SELECT * FROM books WHERE title = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, title);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            pw.println("<h3>Book Details</h3>");
                            pw.println("<p><strong>TITLE:</strong> " + rs.getString("title") + "</p>");
                            pw.println("<p><strong>AUTHOR:</strong> " + rs.getString("author") + "</p>");
                            pw.println("<p><strong>VERSION:</strong> " + rs.getString("version") + "</p>");
                            pw.println("<p><strong>PUBLISHER:</strong> " + rs.getString("publisher") + "</p>");
                            pw.println("<p><strong>COST:</strong> " + rs.getInt("cost") + "</p>");
                        } else {
                            pw.println("<h3>Sorry, invalid title. Please try again.</h3><br>");
                            pw.println("<a href=\"catalog.html\">Press here to retry</a>");
                        }
                    }
                }
            }
        } catch (Exception e) {
            resp.sendError(500, e.toString());
        }
        pw.println("</div>");
        pw.println("</body></html>");
    }
}