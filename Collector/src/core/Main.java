package core;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.LinkedBlockingQueue;

import objs.CollectionObject;
import objs.CollectorObject;
import objs.TableObject;

import org.apache.log4j.Logger;

import utils.PropUtils;


public class Main implements Runnable {


	public static Logger logger = Logger.getLogger(Main.class);
	
	private Timer timer = new Timer();	// to keep multiple collection tasks in one task queue
	
	ArrayList<CollectorObject> collectors;
	ArrayList<Thread> threadTasks = new ArrayList<Thread>();
	LinkedBlockingQueue<TableObject> queue;
	Worker worker;
	
	public ArrayList<CollectorObject> getCollectors() {
		return collectors;
	}

	public ArrayList<Thread> getThreadTasks() {
		return threadTasks;
	}

	public Main() {
		init();
	}
	
	private void init() {
		queue = new LinkedBlockingQueue<TableObject>(100);
		worker = new Worker(queue);
		worker.start();
		
		collectors = new ArrayList<CollectorObject>();
		PropUtils prop = PropUtils.getInstance();
		for (CollectionObject obj : prop.getCollections()) {
			CollectorObject collector = new CollectorObject(timer, obj, queue);
			collectors.add(collector);
		}
	}

	@Override
	public void run() {
		
		for (CollectorObject c : collectors) {
			Thread t = new Thread(c);
			threadTasks.add(t);
			t.start();
		}
	}

	public static void main(String[] args) {
		final Main m = new Main();
		Thread th = new Thread(m);
		th.setDaemon(true);
		th.start();
		logger.debug("Collector started!");
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				
				// Release resources
				m.timer.cancel();
				for (Thread th : m.getThreadTasks()) {
					th.stop();
				}
				logger.error("Collector exit!");
			}
		});
	}
}

