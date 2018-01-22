package chessclub.com.icc.uci;

import static org.junit.Assert.*;

import org.junit.Test;

import david.logan.chess.support.Fen;

public class TestOnlineTablebase {
	public class testGame {
		
	};
	@Test
	public void test() {
		OnlineTablebase ot = new OnlineTablebase();
		try {
			ot.GetTablebaseMoves(new Fen("8/8/p6p/1p6/8/2k5/5K2/8 b - - 3 52"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		fail("Not yet implemented");
	}
}
