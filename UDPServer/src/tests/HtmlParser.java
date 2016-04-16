package tests;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db.DBHelper;
import objs.TableObject;

public class HtmlParser {

	public HtmlParser() {
	}

	public TableObject parse(String url, String filter) throws IOException {
		TableObject table = new TableObject();

		ArrayList<String> rowData;
		Document doc = Jsoup.connect(url).get();

		Elements tbls = doc.getElementsByTag("TABLE");
		for (Element tbl : tbls) {
			boolean haveMet = false;
			Elements trs = tbl.getElementsByTag("TR");

			for (Element tr : trs) {
				Elements tds = tr.getElementsByTag("TD");
				rowData = new ArrayList<String>();
				for (Element td : tds) {
					if (!haveMet && !td.text().equals(filter)) {
						continue;
					}

					haveMet = true;

					if (haveMet) {
						String v = td.text().trim();
						// print(v);
						// if (v.length() == 1 && v.getBytes()[0] == 63) {
						// // System.out.println(v.length());
						// v=""; // deal with special character
						// }
						// else if (v.length() != 0 &&
						// v.getBytes()[v.length()-1] == 63) {
						// byte[] bs = new byte[v.length()-1];
						// for (int i=0; i<v.length()-1; i++) {
						// bs[i] = v.getBytes()[i];
						// }
						// v = new String(bs);
						// }
						byte[] vbs = v.getBytes();
						byte[] bs = new byte[vbs.length];
						for (int i = 0; i < vbs.length; i++) {
							if (vbs[i] == 63) { // special character like '?', skip it
								continue;
							}
							bs[i] = vbs[i];
						}
						v = new String(bs);
						rowData.add(v);
					}
				}
				if (haveMet)
					table.getData().add(rowData);
			}
		}

		return table;
	}

//	private void print(String v) {
//		byte[] bytes = v.getBytes();
//		if (bytes[0] == 63)
//			System.out.print("*");
//		System.out.println(v + "=");
//		for (byte b : bytes) {
//			System.out.print(b);
//		}
//		System.out.println();
//	}

	// should be enhanced to deal with other characters like [,], etc.
	public static String keepOnlyChinese(String s) {
		String ss = s;

		if (s.contains("(")) {
			ss = s.substring(0, s.indexOf('('));
		}
		return ss;
	}

	public static void main(String[] args) throws SQLException {
		DBHelper dbHelper = DBHelper.getInstance();
		Connection conn = dbHelper.getConnection();
		HtmlParser parser = new HtmlParser();
		Statement stmt = conn.createStatement();

//		System.out.println(keepOnlyChinese("预测最小值(Mwh)"));
		try {
			TableObject obj = parser.parse("http://127.0.0.1/test3.html",
					"用户名称");
			obj.setTableName("test13");
			// CollectorUtils.saveToFile(obj, "test");
			ArrayList<ArrayList<String>> data = obj.getData();
			String table = obj.getTableName();
			ArrayList<String> columns = data.get(0);
			String createSql = "create table " + table + "(";
//			String insertSql = "insert into " + table + "(";
			String insertSql = "insert into " + table + " values(";
			String deleteSql = "truncate table " + table;
			String values = " values(";
			for (int i = 0; i < columns.size(); i++) {
				String column = columns.get(i);
				if (i != 0) {
					createSql += ",";
					insertSql += ",";
//					values += ",";
				}
				createSql += keepOnlyChinese(column) + " varchar(100)";
//				insertSql += keepOnlyChinese(column);
//				values += "?";
				insertSql += "?";
			}
			createSql += ");";
			insertSql += ");";
//			values += ");";
//			insertSql += ")";
//			insertSql += values;
			
			
			System.out.println(createSql);
			System.out.println(insertSql);
			
			PreparedStatement pstmt = conn.prepareStatement(insertSql);
			for (int i=1; i<data.size(); i++) {
				
				ArrayList<String> rowData = data.get(i);
				for (int j=0; j<rowData.size(); j++) {
					pstmt.setString(j+1, rowData.get(j));
				}
				pstmt.addBatch();
			}
			
			stmt.executeUpdate(deleteSql);
//			stmt.executeUpdate(createSql);
//			pstmt.executeBatch();

			
			pstmt.close();
			stmt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
