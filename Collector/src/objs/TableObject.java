package objs;

import java.io.Serializable;
import java.util.ArrayList;

public class TableObject implements Serializable{
	String tableName;
	
	private static final long serialVersionUID = 339139033874323868L;
	private ArrayList<ArrayList<String>> data = null;
	
	public TableObject() {
		data = new ArrayList<ArrayList<String>>();
	}

	public ArrayList<ArrayList<String>> getData() {
		return data;
	}

	public void setData(ArrayList<ArrayList<String>> data) {
		this.data = data;
	}

	public void setTableName(String name) {
		this.tableName = name;
	}

	public String getTableName() {
		return this.tableName;
	}
	
}
