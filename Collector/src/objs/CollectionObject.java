package objs;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class CollectionObject {
	public static Logger logger = Logger.getLogger(CollectionObject.class);
	
	String typeName;
	ArrayList<String> urls;
	ArrayList<String> tableNames;
	ArrayList<String> filters;
	long interval;
	long count;
	
	public CollectionObject(String typeName, String urls, String filters, String tables, long interval) {
		this.typeName = typeName;
		this.urls = this.parse(urls);
		this.filters = this.parse(filters);
		this.tableNames = this.parse(tables);
		this.interval = interval;
		
		if (this.urls.size() != this.filters.size()) {
			logger.error("For type(" + this.typeName + "):" + "the url number(" + this.urls.size() + ") is not equal to filter number(" + this.filters.size() + ")");
		}
		this.count = this.urls.size() > this.filters.size()?this.urls.size():this.filters.size();
	}
	
	private ArrayList<String> parse(String s) {
		ArrayList<String> list = new ArrayList<String>();
		
		StringTokenizer st = new StringTokenizer(s, "|");
		while(st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		
		return list;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public ArrayList<String> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<String> filters) {
		this.filters = filters;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public ArrayList<String> getTableNames() {
		return tableNames;
	}
	
	
}
