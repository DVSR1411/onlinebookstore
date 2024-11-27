package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Reg extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>");
        pw.println("<div class='container'>");
        String name = req.getParameter("name");
        String addr = req.getParameter("addr");
        String phno = req.getParameter("phno");
        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/mydb", "root", System.getenv("MYSQL_PASSWORD"));
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id FROM login")) {
                boolean flag = false;
                while (rs.next()) {
                    if (id.equals(rs.getString(1))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    pw.println("Sorry, invalid ID already exists. Please try again with a new ID.<br><br>");
                    pw.println("<a href=\"reg.html\">Click here to retry registration</a>");
                } else {
                    String sql = "INSERT INTO login (name, addr, phno, id, pwd) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, addr);
                        pstmt.setString(3, phno);
                        pstmt.setString(4, id);
                        pstmt.setString(5, pwd);
                        pstmt.executeUpdate();
                    }
                    pw.println("Your details are entered successfully.<br><br>");
                    pw.println("<a href=\"login.html\">Click here to login</a>");
                }
            }
        } catch (Exception e) {
            resp.sendError(500, e.toString());
        }
        pw.println("</div>");
        pw.println("</body></html>");
    }
}