package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {

	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("value=\"[\\d|\\.]*\"");
		Matcher matcher = pattern.matcher("<a href=\"index.html\">ึ๗าณ</a>");
		if (matcher.find()) {
			System.out.println(matcher.group(0));
		}
		
	}

}
