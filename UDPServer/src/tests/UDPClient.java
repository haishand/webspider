package tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import objs.TableObject;

public class UDPClient {

	public static void main(String[] args) {
		try {
			DatagramSocket ds = new DatagramSocket(6666);
			TableObject obj = new TableObject();
			populate(obj);
			
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ObjectOutputStream bo = new ObjectOutputStream(bs);
			bo.writeObject(obj);
			
			byte[] buf = bs.toByteArray();
			DatagramPacket packet = new DatagramPacket(bs.toByteArray(), buf.length, InetAddress.getByName("127.0.0.1"), 5555);
			ds.send(packet);
			ds.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void populate(TableObject obj) {
		ArrayList<String> al = new ArrayList<String>();
		for (int i=0; i<10; i++) {
			al.add(""+i);
		}
		obj.getData().add(al);
	}
}
