package objs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import parser.HtmlParser;
import utils.CollectorUtils;

// collectorOjbect <-> collectionObject
public class CollectorObject implements Runnable {

	Timer timer;
	CollectionObject collection;
	LinkedBlockingQueue<TableObject> queue;

	public CollectorObject(Timer timer, CollectionObject co,
			LinkedBlockingQueue<TableObject> queue) {
		this.timer = timer;
		this.collection = co;
		this.queue = queue;
	}

	@Override
	public void run() {
		timer.schedule(new CollectorTimer(collection, this.queue), 1000,
				collection.getInterval());

	}

}

class CollectorTimer extends TimerTask {

	public static Logger logger = Logger.getLogger(CollectorTimer.class);
	CollectionObject collection;
	LinkedBlockingQueue<TableObject> queue;

	public CollectorTimer(CollectionObject collection,
			LinkedBlockingQueue<TableObject> queue) {
		this.collection = collection;
		this.queue = queue;
	}

	@Override
	public void run() {
		// int count = 0;
		// SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// String TimeString = time.format(new java.util.Date());
		// System.out.println((++count) + "--" + TimeString);
		HtmlParser parser = new HtmlParser();

		String type = null, urls = null, filters = null, tables = null;
		long interval = 0;
		int i = 0;
		try {
			type = collection.getTypeName();
			interval = collection.getInterval();
			for (i = 0; i < collection.getCount(); i++) {

				urls = collection.getUrls().get(i);
				filters = collection.getFilters().get(i);
				tables = collection.getTableNames().get(i);
				TableObject table = parser.parse(urls, filters);
				table.setTableName(tables);
				// CollectorUtils.saveToFile(table, genFileNamePrefix(type, i));
				queue.offer(table);
				// logger.debug(table.getTableName());
				// logger.debug(table.getData().toString());
			}
		} catch (IOException e) {
			logger.debug(e);
			logger.debug("i(" + i + ")" + "\n" + "type(" + type + ")" + "\n"
					+ "urls(" + urls + ")" + "\n" + "filters(" + filters + ")"
					+ "\n" + "tables(" + tables + ")");
		}

	}

	private String genFileNamePrefix(String type, int i) {
		return type + "-" + i;
	}

}