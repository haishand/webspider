package core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import objs.TableObject;
import utils.PropUtils;

public class Worker extends Thread {

	public static Logger logger = Logger.getLogger(Worker.class);
	LinkedBlockingQueue<TableObject> queue;
	boolean isAlive = true;
	String serverIpAddr;
	int serverPort, clientPort;
	
	public Worker(LinkedBlockingQueue<TableObject> queue) {
		this.queue = queue;
		PropUtils prop = PropUtils.getInstance();
		this.clientPort = prop.getClientPort();
		this.serverIpAddr = prop.getServerIpAddr();
		this.serverPort = prop.getServerPort();
	}
	
	@Override
	public void run() {
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(this.clientPort);
			while(isAlive) {
				TableObject obj = queue.take();
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ObjectOutputStream bo = new ObjectOutputStream(bs);
				bo.writeObject(obj);
				
				byte[] buf = bs.toByteArray();
				logger.debug(obj.getTableName());
				logger.debug(obj.getData().toString());
				DatagramPacket packet = new DatagramPacket(bs.toByteArray(), buf.length, InetAddress.getByName(this.serverIpAddr), this.serverPort);
				ds.send(packet);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			isAlive = false;
			if (ds != null) ds.close();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
