package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Login extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>");
        pw.println("<div class='container'>"); // Open the container div
        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/mydb", "root", System.getenv("MYSQL_PASSWORD"))) {
                String sql = "SELECT pwd FROM login WHERE id = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            if (pwd.equals(rs.getString("pwd"))) {
                                pw.println("Valid login ID<br><br>");
                                pw.println("<a href=\"profile.html\">User Profile</a><br>");
                                pw.println("<a href=\"catalog.html\">Books Catalog</a><br>");
                                pw.println("<a href=\"order.html\">Order Confirmation</a>");
                            } else {
                                pw.println("Sorry, invalid password. Please try again.<br><br>");
                                pw.println("<a href=\"login.html\">Click here to retry login</a>");
                            }
                        } else {
                            pw.println("Sorry, invalid ID. Please try again.<br><br>");
                            pw.println("<a href=\"login.html\">Click here to retry login</a>");
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