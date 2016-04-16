package core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

import objs.TableObject;

import org.apache.log4j.Logger;

import utils.PropUtils;

public class UDPServer {

	public static Logger logger = Logger.getLogger(UDPServer.class);

	Worker worker;
	LinkedBlockingQueue<TableObject> queue;
	DatagramSocket ds;
	boolean isAlive = true;

	public UDPServer(int port) throws SocketException {
		queue = new LinkedBlockingQueue<TableObject>(100);
		ds = new DatagramSocket(port);
		worker = new Worker(queue);
		worker.start();
	}

	public void run() {

		// create database
		
		while (isAlive) {
			try {
				byte[] buf = new byte[1024 * 1024]; //should be defined by configuration
				DatagramPacket data = new DatagramPacket(buf, buf.length);
				ds.receive(data);

				ByteArrayInputStream bs = new ByteArrayInputStream(
						data.getData());
				ObjectInputStream os = new ObjectInputStream(bs);
				TableObject obj = (TableObject) os.readObject();
				queue.offer(obj);
//				logger.debug(obj.getTableName());
//				logger.debug(obj.getData().toString());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.debug(e);
			}

		}

	}

	public static void main(String[] args) {
		try {
			PropUtils prop = PropUtils.getInstance();
			int port = prop.getPort();

			UDPServer s = new UDPServer(port);
			logger.debug("udpserver started.");
			s.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug(e);
		}

	}
}
