package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Profile extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>");
        pw.println("<div class='container'>");
        String id = req.getParameter("id");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/mydb", "root", System.getenv("MYSQL_PASSWORD"))) {
                String sql = "SELECT * FROM login WHERE id = ?";
                try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                    pstmt.setString(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            pw.println("<div align=\"center\">");
                            pw.println("NAME: " + rs.getString("name") + "<br>");
                            pw.println("ADDRESS: " + rs.getString("addr") + "<br>");
                            pw.println("PHONENO: " + rs.getString("phno") + "<br>");
                            pw.println("</div>");
                        } else {
                            pw.println("Sorry, invalid ID. Please try again.<br><br>");
                            pw.println("<a href=\"profile.html\">Press here to retry</a>");
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