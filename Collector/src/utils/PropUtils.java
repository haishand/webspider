package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import objs.CollectionObject;

public class PropUtils {
	public static Logger logger = Logger.getLogger(PropUtils.class);
	
	private static PropUtils instance = null;
	private int propTypesNum = 0;
	ArrayList<CollectionObject> collections;
	
	// server ip/port
	String ServerIpAddr;
	int serverPort;
	
	// client port
	int clientPort;
	
	public static PropUtils getInstance() {
		if (instance == null) {
			instance = new PropUtils();
		}
		return instance;
	}
	
	private PropUtils() {
		collections = new ArrayList<CollectionObject>();
		Properties prop = new Properties();
		
		try {
			prop.load(new FileInputStream("connection.cfg"));
			this.ServerIpAddr = prop.getProperty("serverip", "127.0.0.1");
			this.serverPort = Integer.parseInt(prop.getProperty("serverport", "5555"));
			this.clientPort = Integer.parseInt(prop.getProperty("clientport", "6666"));
			prop.clear();
			
			prop.load(new FileInputStream("collector.cfg"));
			propTypesNum = prop.size()/5;
			CollectionObject co;
			for (int i=0; i<propTypesNum; i++) {
				String typeName = (String)prop.get("type" + i);
				String url = (String)prop.get("url" + i);
				String value = (String)prop.get("filter" + i);
				String filter = new String(value.getBytes("ISO-8859-1"), "GBK");
				String v2 = (String)prop.get("table" + i);
				String tables = new String(v2.getBytes("ISO-8859-1"), "GBK");
				long interval = 1000 * 60 * Long.parseLong((String)prop.get("interval" + i));
				co = new CollectionObject(typeName, url, filter, tables, interval);
				collections.add(co);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			logger.debug(e);
		}
	}

	public ArrayList<CollectionObject> getCollections() {
		return collections;
	}

	
	public static void main(String[] args) {
	}

	public int getClientPort() {
		return this.clientPort;
	}

	public String getServerIpAddr() {
		return this.ServerIpAddr;
	}

	public int getServerPort() {
		return this.serverPort;
	}
}
