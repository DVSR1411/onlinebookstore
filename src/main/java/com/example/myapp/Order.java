package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Order extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>");
        pw.println("<div class='container'>");
        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");
        String title = req.getParameter("title");
        String count1 = req.getParameter("no");
        String cno = req.getParameter("cno");
        int count = Integer.parseInt(count1);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://mysqldb:3306/mydb", "root", System.getenv("MYSQL_PASSWORD"))) {
                String loginSql = "SELECT pwd FROM login WHERE id = ?";
                try (PreparedStatement loginStmt = con.prepareStatement(loginSql)) {
                    loginStmt.setString(1, id);
                    try (ResultSet rs = loginStmt.executeQuery()) {
                        if (!rs.next() || !pwd.equals(rs.getString(1))) {
                            pw.println("SORRY INVALID ID TRY AGAIN<br><br>");
                            pw.println("<a href=\"order.html\">Press HERE to RETRY</a>");
                            return;
                        }
                    }
                }
                String costSql = "SELECT cost FROM books WHERE title = ?";
                try (PreparedStatement costStmt = con.prepareStatement(costSql)) {
                    costStmt.setString(1, title);
                    try (ResultSet rs1 = costStmt.executeQuery()) {
                        if (rs1.next()) {
                            int x = rs1.getInt("cost");
                            int amount = count * x;
                            pw.println("AMOUNT: " + amount + "<br><br>");
                            String orderSql = "INSERT INTO details (id, title, amount, cno) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement orderStmt = con.prepareStatement(orderSql)) {
                                orderStmt.setString(1, id);
                                orderStmt.setString(2, title);
                                orderStmt.setInt(3, amount);
                                orderStmt.setString(4, cno);
                                orderStmt.executeUpdate();
                                pw.println("YOUR ORDER has been taken<br>");
                            }
                        } else {
                            pw.println("SORRY INVALID TITLE TRY AGAIN<br><br>");
                            pw.println("<a href=\"order.html\">Press HERE to RETRY</a>");
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