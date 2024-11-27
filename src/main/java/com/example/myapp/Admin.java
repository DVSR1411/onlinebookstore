package com.example.myapp;
import java.sql.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Admin extends HttpServlet {
    private static String pass = System.getenv("MYSQL_PASSWORD");
    private static final String HEAD = "<html><head><link rel='stylesheet' type='text/css' href='static/style.css'></head><body>";
    private static final String DIV = "<div class='container'>";
    private static final String END = "</div></body></html>";
    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("login".equals(action)) {
            validateLogin(req, resp);
        } else if ("addBook".equals(action)) {
            addBook(req, resp);
        } else if ("viewPurchases".equals(action)) {
            viewPurchases(resp);
        }
    }
    private void validateLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        pw.println(HEAD);
        pw.println(DIV);
        String id = req.getParameter("id");
        String pwd = req.getParameter("pwd");
        if ("root".equals(id) && pass.equals(pwd)) {
            pw.println("Valid login ID<br><br>");
            pw.println("<a href=\"dashboard.html\">Admin Dashboard</a>");
        } else {
            pw.println("Sorry, invalid ID. Please try again.<br><br>");
            pw.println("<a href=\"login.html\">Click here to retry login</a>");
        }
        pw.println(END);
    }
    private void addBook(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        pw.println(HEAD);
        pw.println(DIV);
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String version = req.getParameter("version");
        String publisher = req.getParameter("publisher");
        int cost = Integer.parseInt(req.getParameter("cost"));
        String sql = "INSERT INTO books (title, author, version, publisher, cost) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); 
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, version);
            pstmt.setString(4, publisher);
            pstmt.setInt(5, cost);
            pstmt.executeUpdate();
            pw.println("<h3>Book added successfully!</h3>");
        } catch (Exception e) {
            resp.sendError(500, "Error adding book: " + e.getMessage());
        }
        pw.println(END);
    }
    private void viewPurchases(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(HEAD);
        out.println(DIV);
        out.println("<h3>All Purchases</h3><table border='1'><tr><th>User ID</th><th>Title</th><th>Amount</th></tr>");
        String sql = "SELECT id, title, amount FROM details";
        
        try (Connection con = getConnection(); 
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                out.println("<tr><td>" + rs.getString("id") + "</td><td>" + rs.getString("title") + "</td><td>" + rs.getInt("amount") + "</td></tr>");
            }
        } catch (Exception e) {
            resp.sendError(500, "Error retrieving purchases: " + e.getMessage());
        }
        out.println("</table>");
        out.println(END);
    }
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://mysqldb/mydb", "root", pass);
    }
}
