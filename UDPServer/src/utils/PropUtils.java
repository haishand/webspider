package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import core.UDPServer;

public class PropUtils {

	public static PropUtils instance = null;
	public int port;
	public int queueSize;
	public int bufSize;
	public static Logger logger = Logger.getLogger(PropUtils.class);
	
	public static PropUtils getInstance() {
		if (instance == null) {
			instance = new PropUtils();
		}
		return instance;
	}
	
	private PropUtils() {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("udpserver.cfg"));
			this.port = Integer.parseInt((String)prop.getProperty("port", "5555"));
			this.queueSize = Integer.parseInt((String)prop.getProperty("queuesize", "100"));
			this.bufSize = Integer.parseInt((String)prop.getProperty("bufsize", "1048576"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} 
	}
	
	public int getPort() {
		return this.port;
	}
	
	public static void main(String[] args) {
		PropUtils prop = PropUtils.getInstance();
		System.out.println(prop.getPort());
	}

}
