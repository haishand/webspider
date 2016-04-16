package parser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import objs.TableObject;
import utils.CollectorUtils;

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
						int len = vbs.length;
						for (int i = 0; i < vbs.length; i++) {
							if (vbs[i] == 63) { // skip it
								--len;
								continue;
							}
							bs[i] = vbs[i];
						}
						v = new String(bs, 0, len);
						
						
						// deal with <input language="javascript" ... value="xxxx" ...>
						Pattern pattern = Pattern.compile("value=\"[\\d|\\.]*\"");
						Matcher matcher = pattern.matcher(td.html());
						String v2 = "";
						if (matcher.find()) {
//System.out.println(matcher.group());
							v2 = matcher.group();
							v2 = v2.substring(v2.indexOf("\"")+1, v2.lastIndexOf("\""));
System.out.println(v2);
						}
						rowData.add(v);
					}
				}
				if (haveMet)
					table.getData().add(rowData);
			}
		}

		return table;
	}

	private void print(String v) {
		byte[] bytes = v.getBytes();
		if (bytes[0] == 63)
			System.out.print("*");
		System.out.println(v + "=");
		for (byte b : bytes) {
			System.out.print(b);
		}
		System.out.println();
	}

	public static String keepOnlyChinese(String s) {
		String ss = s;

		if (s.contains("(")) {
			ss = s.substring(0, s.indexOf('('));
		}
		return ss;
	}

	public static void main(String[] args)
			throws BadHanyuPinyinOutputFormatCombination {
		HtmlParser parser = new HtmlParser();
		System.out.println(keepOnlyChinese("预测最小值(Mwh)"));
		try {
			TableObject obj = parser.parse("http://127.0.0.1/test3.html",
					"用户名称");
//			obj.setTableName("test");
//			// CollectorUtils.saveToFile(obj, "test");
//			ArrayList<ArrayList<String>> data = obj.getData();
//			String table = obj.getTableName();
//			ArrayList<String> columns = data.get(0);
//			String createSql = "create table " + table + "(";
//			for (int i = 0; i < columns.size(); i++) {
//				String column = columns.get(i);
//				if (i != 0)
//					createSql += ",";
//				createSql += keepOnlyChinese(column) + " varchar(20)";
//			}
//			createSql += ");";
//			System.out.println(createSql);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
