package david.logan.custom.collections;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AgingList<E> {
	private static final Logger log = LogManager.getLogger(AgingList.class);
	
	synchronized public int size() {
		return itemlist.size();
	}

	synchronized public boolean isEmpty() {
		return itemlist.isEmpty();
	}

	synchronized public boolean contains(E item) {
		return itemlist.containsKey(item);
	}

	synchronized public int add(E item) {
		t newt = new t(item, secondsUntilExpire);

		aginglist.push(newt);

		log.debug("Aging list is adding " + item.toString() + " to list for " + newt.date.toString() + " for a new aging size of " + aginglist.size());
		
		if(!itemlist.containsKey(item))
			itemlist.put(item,  new ArrayList<t>());
		itemlist.get(item).add(newt);

		if(aginglist.size() == 1)
			synchronized(aginglist) {
				log.debug("Notifying the aging thread to start");
				aginglist.notify();
			}

		return itemlist.get(item).size();
	}

	synchronized public boolean remove(E item) {
		if(!itemlist.containsKey(item))
			return false;
		Iterator<t> it = itemlist.get(item).iterator();
		while(it.hasNext()) {
			aginglist.remove(it.next());
		}
		itemlist.remove(item);
		return true;
	}

	synchronized public void clear() {
		itemlist.clear();
		aginglist.clear();
	}

	synchronized private long removeOldest() {

		if(aginglist.size() == 0) {
			log.debug("removeOldest returning -1 due to empty aginglist");
			return -1;
		}
		
		t oldt = aginglist.peekLast();
		Date now = new Date();
		
		while(oldt.date.compareTo(now) <= 0) {
			log.debug("Aging list is removing " + oldt.item.toString() + " from list for " + oldt.date.toString());
			itemlist.get(oldt.item).remove(oldt);
			if(itemlist.get(oldt.item).size() == 0) {
				itemlist.remove(oldt.item);
			}
			aginglist.removeLast();
			if(aginglist.size() == 0) {
				log.debug("removeOldest return -1 because we've emptied the list");
				return -1;
			}
			oldt = aginglist.peekLast();
		}
		return oldt.date.getTime() - (new Date()).getTime();
	}
	
	public AgingList(int secondsUntilExpire) {
		this.secondsUntilExpire = secondsUntilExpire;
		expireThread = new Thread() {
			public void run() {
				while(true) {
					if(threadend) {
						log.debug("Aging thread is returning due to requested end");
						return;
					};
					try {
						long millis = removeOldest();
						log.debug("Aging thread got " + millis + " to wait");
						if(millis == -1) {
							synchronized(aginglist) {
								if(aginglist.size() == 0)
									aginglist.wait();
							}
						} else {
							Thread.sleep(millis);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};
		expireThread.setDaemon(true);
		expireThread.start();
	}
	
	private LinkedList<t> aginglist = new LinkedList<t>();
	private HashMap<E, ArrayList<t>> itemlist = new HashMap<E, ArrayList<t>>();
	private int secondsUntilExpire;
	private Thread expireThread;
	private boolean threadend = false;
	
	private class t {
		E item;
		Date date;
		public t(E item, int expiresIn) {
			this.item = item;
			this.date = new Date((new Date()).getTime() + (long)expiresIn * 1000L);
		}
		@Override public boolean equals(Object other) {
			if(other.getClass() != t.class) return false;
			@SuppressWarnings("unchecked")
			t obj = (t)other;
			if(!obj.item.equals(item)) return false;
			return date.equals(obj.date);
		}
	}

	@Override
	protected void finalize() {
		synchronized(aginglist) {
			threadend = true;
			log.debug("Notifying the aging thread to terminate");
			aginglist.notify();
		}
	}
}
