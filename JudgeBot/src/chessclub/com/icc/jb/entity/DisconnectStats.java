package chessclub.com.icc.jb.entity;

public class DisconnectStats {
	public class _d
	{
		public int won;
		public int lost;
		public int drawn;
		public int aborted;
		public int manual;
	};
	public _d disconnected;
	public _d victim;
	
	public DisconnectStats()
	{
		disconnected = new _d();
		victim = new _d();
	}
}
