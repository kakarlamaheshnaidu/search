package com.jspidersbtm.searchapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class SearchController extends HttpServlet{
	private Connection con;
	private PreparedStatement pstmt;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String qry = "select * from jspidersdb.student where stream=?";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con  = DriverManager.getConnection("jdbc:mysql://localhost:3306?user=root&password=root");
			pstmt = con.prepareStatement(qry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String stream = req.getParameter("st");
		StringBuilder sb = new StringBuilder();
		int i = 0;

		sb.append("<html><body>"
				+ "<table border=\"2\">"
				+ "<tr><th bgcolor=\"#ff66ff\"> Id </th>"
				+ "<th bgcolor=\"#ff66ff\"> name </th>"
				+ "<th bgcolor=\"#ff66ff\"> percentage </th></tr>"
				+ "");


		try {
			pstmt.setString(1, stream);
			boolean res = pstmt.execute();
			ResultSet rs = pstmt.getResultSet();

			while (rs.next()) {
				i= i+1;
				int id = rs.getInt("id");
				String name = rs.getString("name");
				double perc = rs.getDouble("perc");

				sb.append("<tr>"
						+ "<td>"+ id +"</td>"
						+ "<td>"+ name +"</td>"
						+ "<td>"+ perc +"</td>"
						+ "</tr>");
			}
			sb.append( "</table>"
					+ "</body></html>");

			if (i == 0) {
				// no data found
				resp.sendRedirect("error.html");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.println(sb);
	}
}
