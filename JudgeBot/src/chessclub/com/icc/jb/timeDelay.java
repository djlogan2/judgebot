package chessclub.com.icc.jb;

import java.util.LinkedList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.icc.jb.ifac.timeDelayHandlerInterface;

public class timeDelay extends Thread {

    private static final Logger log = LogManager.getLogger(timeDelay.class);
    private static timeDelay instance = null;

    public static timeDelay getInstance() { return instance; }
    
    static {
        instance = new timeDelay();
        instance.start();
    }
    
	private static LinkedList<timeDelayGame> gameList = new LinkedList<timeDelayGame>();
	private static boolean kill = false;
	private static timeDelayHandlerInterface ifac;
	private static int maxSize = 0;
	private static int _timeDelay = 0;

	synchronized public void setInterface(timeDelayHandlerInterface ifac) {
	    timeDelay.ifac = ifac;
	}
	synchronized public void setTimeDelay(int _timeDelay) {
	    timeDelay._timeDelay = _timeDelay;
	}
//	public timeDelay(int timeDelay, timeDelayHandlerInterface ifac)
//	{
//	    this.timeDelay = timeDelay;
//		this.ifac = ifac;
//	}
	synchronized public void run()
	{
		if(log.isDebugEnabled())
			log.debug("timeDelay is running");
		while(!kill)
		{
			int toWait = timeToWait();
			
			if(log.isDebugEnabled())
				log.debug("timeDelay has toWait="+toWait);
			
			if(toWait > 0)
				try {
					if(log.isDebugEnabled())
						log.debug("timeDelay is waiting "+toWait+" seconds");
					this.wait(toWait*1000);
				} catch (InterruptedException e1) {
					log.error("timeDelay encountered an error trying a timed wait ("+toWait+"): "+e1.getMessage());
				}
			
			String[] nextGame = getNext();
			
			if(log.isDebugEnabled())
				log.debug("timeDelay nextGame="+nextGame);
			
			if(nextGame == null)
				try {
					if(log.isDebugEnabled())
						log.debug("timeDelay no nextGame! Waiting for notify");
					wait();
				} catch (InterruptedException e) {
					log.error("timeDelay encountered an error trying an infinite wait: "+e.getMessage());
				}
			else
			{
				if(log.isDebugEnabled())
					log.debug("timeDelay sending "+nextGame[0]+"/"+nextGame[1]+" to handler");
				ifac.timeDelayExpired(nextGame[0], nextGame[1]);
			}
		}
	}

	public void shutdown()
	{
		kill = true;
	}
	
	synchronized public void addWait(String white, String black)
	{
		if(_timeDelay == 0)
		{
			if(log.isDebugEnabled())
				log.debug("timeDelay sending game immediately due to time delay of zero");
			ifac.timeDelayExpired(white, black);
		}
		else
		{
			if(log.isDebugEnabled())
				log.debug("timeDelay putting "+white+"/"+black+" on the stack");
			boolean notify = (gameList.size() == 0);
			gameList.add(new timeDelayGame(white, black, _timeDelay));
			int newSize = gameList.size();
			if(timeDelay.maxSize < newSize)
				maxSize = newSize;
			if(notify)
			{
				if(log.isDebugEnabled())
					log.debug("timeDelay notify sent");
				this.notify();
			}
		}
	}
	public int timeToWait()
	{
		if(gameList.isEmpty()) return -1;
		timeDelayGame g = gameList.peek();
		return g.secondsToWait();
	}
	synchronized public String[] getNext()
	{
		if(gameList.isEmpty()) return null;
		timeDelayGame g = gameList.pop();
		if(log.isDebugEnabled())
			log.debug("timeDelay returning "+g.white+"/"+g.black);
		return new String[] {g.white, g.black};

	}
	
	synchronized public void removeWait(String white, String black)
	{
		if(log.isDebugEnabled())
			log.debug("timeDelay trying to remove "+white+"/"+black+" from the stack");
		for(timeDelayGame g : gameList)
		{
			if(g.white.equals(white)&&g.black.equals(black))
			{
				gameList.remove(g);
				return;
			}
		}
	}
	
	// WARNING: This isn't synchronized! It's not for use with adding or removing. It's only use is for
	// quiescing to empty before deleting!
	public boolean isEmpty() { return gameList.isEmpty(); }
	public int queueSize() { return timeDelay.gameList.size(); }
	public int maxQueueSize() { return timeDelay.maxSize; }
    public int getTimeDelay() { return _timeDelay; }
}
