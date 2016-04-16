package core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import db.DBHelper;
import objs.TableObject;
import tests.HtmlParser;

public class Worker extends Thread {

	public static Logger logger = Logger.getLogger(Worker.class);
	
	LinkedBlockingQueue<TableObject> queue;
	boolean isAlive = true;
	
	public Worker(LinkedBlockingQueue<TableObject> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		while(isAlive) {
			TableObject obj = queue.poll();
	
			try {
				if (obj != null) updateToDB(obj);
//				String table = obj.getTableName();
//				ArrayList<ArrayList<String>> data = obj.getData();
//				ArrayList<String> columns = data.get(0);
//				String createSql="create table " + table + "(";
//				String insertSql="insert into " + table + " values(";
//
//				
//				for(int i=1; i<data.size(); i++) {
//					ArrayList<String> a = data.get(i);
//				}
//				ObjectOutputStream bo = new ObjectOutputStream(new FileOutputStream(new File("test.txt"), true));
//				bo.writeObject(obj);
//				bo.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(e);
			}
		}
	}
	
	public String keepOnlyChinese(String s) {
		String ss = s;

		if (s.contains("(")) {
			ss = s.substring(0, s.indexOf('('));
		}
		return ss;
	}
	
	private void updateToDB(TableObject obj) {
		DBHelper dbHelper = DBHelper.getInstance();
		Connection conn = dbHelper.getConnection();
		HtmlParser parser = new HtmlParser();
		Statement stmt = null;
		PreparedStatement pstmt = null;
		String table = obj.getTableName();
		
		try {
			ArrayList<ArrayList<String>> data = obj.getData();
			ArrayList<String> columns = data.get(0);
			String createSql = "create table " + table + "(";
			String insertSql = "insert into " + table + " values(";
			String deleteSql = "truncate table " + table;
			for (int i = 0; i < columns.size(); i++) {
				String column = columns.get(i);
				if (i != 0) {
					createSql += ",";
					insertSql += ",";
				}
				createSql += keepOnlyChinese(column) + " varchar(100)";
				insertSql += "?";
			}
			createSql += ");";
			insertSql += ");";
			
			System.out.println(createSql);
			System.out.println(insertSql);
			
			stmt = conn.createStatement();
			pstmt = conn.prepareStatement(insertSql);
			for (int i=1; i<data.size(); i++) {
				
				ArrayList<String> rowData = data.get(i);
				for (int j=0; j<rowData.size(); j++) {
					pstmt.setString(j+1, rowData.get(j));
				}
				pstmt.addBatch();
			}
			
			if (!dbHelper.isExisted(table))  {
				stmt.executeUpdate(createSql);
			}
			stmt.executeUpdate(deleteSql);
			pstmt.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(e);
			}
		}
	}

	public static void main(String[] args) {
		
	}

}
