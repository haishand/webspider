package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Test {

	boolean isNumeric(String s) {
		return s.matches("-?[0-9]+.*[0-9]*");
	}
	
	public static void main(String[] args) {
		String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String dbUrl = "jdbc:sqlserver://222.195.210.27:1433;";
//		String url2 = "jdbc:sqlserver://222.195.210.27:1433;databaseName=sample;integratedSecurity=true;";
		String user = "sa";
		String passwd = "1234";
		
		Connection conn;
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dbUrl, user, passwd);
//			conn = DriverManager.getConnection(url2);
			System.out.println("conn established");
			String sql = "create database collector";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("db created");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
