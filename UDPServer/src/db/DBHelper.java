package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import core.Worker;

public final class DBHelper {

	public static Logger logger = Logger.getLogger(DBHelper.class);
	
	String driver = null;
	String url = null;
	String user = null;
	String passwd = null;
	String dbName = null;
	
	public static DBHelper instance = null;
	private Connection connection = null;

	public static DBHelper getInstance() {
		if (instance == null) {
			instance = new DBHelper();
		}
		return instance;
	}

	private DBHelper() {

		try {
			Properties p = new Properties();
			p.load(new FileInputStream("database.cfg"));

			this.driver = p.getProperty("driver");
			this.url = p.getProperty("url");
			this.user = p.getProperty("user");
			this.passwd = p.getProperty("passwd");
			this.dbName = p.getProperty("database", "collector");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int executeNonQuery(String sql) {
		int result = 0;
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = getConnection();
			stmt = conn.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			free(null, stmt, null);
		} finally {
			free(null, stmt, conn);
		}

		return result;
	}

	
	public void free(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}

	public void free(Statement st) {
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}
	}

	public void free(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException err) {
			err.printStackTrace();
		}

	}

	private void free(ResultSet rs, Statement stmt, Connection conn) {
		free(rs);
		free(stmt);
		free(conn);
	}

	public Connection getConnection() {
		try {
			if (connection == null) {
				Class.forName(driver); // load db driver
				connection = DriverManager.getConnection(this.url, this.user,
						this.passwd);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		}

		return connection;
	}

	public boolean isExisted(String tableName) {
		boolean flag = false;
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rsTables = meta.getTables(null, null, tableName, null);
			if (rsTables.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		}
		
		return flag;
	}
	
	public void createDB() {
		DBHelper dbHelper = DBHelper.getInstance();
		Connection conn = dbHelper.getConnection();
		
		String checkDbSql = "select 1  from  master..sysdatabases where name='" + this.dbName + "'";
		String dbSql = "create database " + this.dbName;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(checkDbSql);
			if (rs.getRow() == 0) {
				stmt.execute(dbSql);
System.out.println("ok");
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBHelper dbHelper = DBHelper.getInstance();
		Connection conn = dbHelper.getConnection();
		dbHelper.createDB();
		try {
//			System.out.println(dbHelper.isExisted("sample"));
//			String sql = "create database sample";
//			Statement stmt = conn.createStatement();
//			stmt.executeUpdate(sql);
//			stmt.close();
			System.out.println(dbHelper.isExisted("test"));
//			String sql = "create table 电厂(用户名称 varchar(20),测点名称 varchar(20),预测最大值 varchar(20),预测最小值 varchar(20),预测总加值 varchar(20));";
			String sql = "insert into test11 values('11','22','33','44','55');";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("create table ok");
			System.out.println(dbHelper.isExisted("test"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
