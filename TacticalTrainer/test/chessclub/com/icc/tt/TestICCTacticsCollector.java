package chessclub.com.icc.tt;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.Test;

import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.tt.entity.CompletedTactic;
import chessclub.com.icc.tt.entity.GameToCheck;
import chessclub.com.icc.tt.entity.TacticUserStat;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.entity.TacticsUser;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;

public class TestICCTacticsCollector extends ICCTacticsCollector {
	
	private static SendMoves sm;
	private static ICCBean bean;
	private static IDatabaseService dbs;
	private static Object done = new Object();
	
	static {
		bean = mock(ICCBean.class);
		when(bean.getUciThreads()).thenReturn(3);
		when(bean.getuciInTacticTime()).thenReturn(45);
		when(bean.getuciNotInTacticTime()).thenReturn(15);
		
		sm = mock(SendMoves.class);
		when(sm.smithMove()).thenReturn("f1h1", "h4g3", "h1g1", "g3f2", "g1g6", "e6g6");
		
		dbs = new testDBS();
	}
	
	public TestICCTacticsCollector() {
		super(null, dbs, bean, 4096, "/Users/davidlogan/Documents/stockfish-5-mac/Mac/stockfish-5-sse42", null);
	}

	@Test
	public void letsTest() {
		try {
			analyzeGame("5k2/8/4Q1P1/8/7K/8/8/5r2 b - - 0", null);
			synchronized(done) {done.wait();}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Override
	public boolean atend() {
		return false;
	}
	
	@Override
	public void forward(int count) {
		sendMoves(sm);
	}
	
	@Override
	public void kibitz(String text) {}

	private static class testDBS implements IDatabaseService {

		@Override
		public TacticsUser getUser(String username, int uniqueid, int rating) {
			return null;
		}

		@Override
		public TacticsTable getRandomTactic(Collection<Integer> ineligible_ids,
				TacticsUser user, Color tomove, int rating) {
			return null;
		}

		@Override
		public void solvedTactic(CompletedTactic ct) {
		}

		@Override
		public DatabaseStatistics getStatistics() {
			return null;
		}

		@Override
		public Collection<TacticsTable> getAllTactics() {
			return null;
		}

		@Override
		public void good(TacticsTable incoming_tt) {
			
		}

		@Override
		public String getMessage(Locale locale, String code) {
			return null;
		}

		@Override
		public int getRanking(int uid) {
			return 0;
		}

		@Override
		public int getRanking(TacticsUser user) {
			return 0;
		}

		@Override
		public Collection<TacticsTable> getNullTactics() {
			return null;
		}

		@Override
		public GameToCheck getNextGameToCheck() {
			return null;
		}

		@Override
		public void saveGameToCheck(String who, int gameid) {
			
		}

		@Override
		public void saveGametoCheck(String who, Fen fen) {
		}

		@Override
		public boolean hasBeenBothered(String who) {
			return false;
		}

		@Override
		public Collection<String> getAllUsers() {
			return null;
		}

		@Override
		public int saveTactic(Fen fen, String moves, Color tacticWinner, boolean winning) {
			System.out.println("fen=" + fen + " , moves=" + moves + ", color=" + tacticWinner);
			return 0;
		}

		@Override
		public Collection<TacticsTable> getNullWinningTactics() {
			return null;
		}

		@Override
		public void winning(TacticsTable incoming_tt) {
		}

		@Override
		public void updateFen(int id, Fen fen) {
		}

		@Override
		public ArrayList<TacticUserStat> rankByRating(int mingames, String who) {
			return null;
		}

		@Override
		public TacticsTable getTactic(int id) {
			return null;
		}

		@Override
		public void updateGoodAndComment(int id, boolean good, String comment) {
		}

		@Override
		public Integer[][] ratingDetail(String name) {
			return null;
		}
	};
}
