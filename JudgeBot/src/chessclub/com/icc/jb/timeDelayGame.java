package chessclub.com.icc.jb;

import java.util.Calendar;

public class timeDelayGame {
	public String white;
	public String black;
	public Calendar hit = Calendar.getInstance();
	public timeDelayGame(String white, String black, int seconds)
	{
		this.white = white;
		this.black = black;
		hit.add(Calendar.SECOND, seconds);
	}
	public int secondsToWait()
	{
		long nowMS = Calendar.getInstance().getTimeInMillis();
		long laterMS = hit.getTimeInMillis();
		long secstowait = (laterMS - nowMS) / 1000;
		if(secstowait<=0) return 0;
		else return (int)secstowait;
	}
}
