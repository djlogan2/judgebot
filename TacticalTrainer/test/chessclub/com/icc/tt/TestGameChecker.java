package chessclub.com.icc.tt;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.uci.OnlineTablebase;
import david.logan.chess.support.Fen;
import david.logan.chess.support.IllegalMoveException;
import david.logan.chess.support.PGNGame;
import david.logan.chess.support.pgn.parser.PGNParser;
import david.logan.chess.support.pgn.parser.ParseException;

public class TestGameChecker {
	private String pgn = "[Event \"\"]\n[Site \"\"]\n[Date \"2004.??.??\"]\n[Round \"?\"]\n[White \"Volkov, Sergey\"]\n[Black \"Moiseenko, Alexander\"]\n[Result \"0-1\"]\n[WhiteElo \"2630\"]\n[BlackElo \"2623\"]\n[ECO \"E24\"]\n\n1.d4 Nf6 2.c4 e6 3.Nc3 Bb4 4.f3 d5 5.a3 Bxc3+ 6.bxc3 c6 7.e4 dxe4 8.fxe4 Nxe4 9.Qg4 Nf6 10.Qxg7 Rg8 11.Qh6 c5 12.Nf3 Nc6 13.Bg5 Rg6 14.Qh4 cxd4 15.Bd3 Rxg5 16.Nxg5 dxc3 17.Rd1 Qd4 18.Qxd4 Nxd4 19.O-O Ke7 20.Bxh7 Ne2+ 21.Kf2 Nxh7 22.Nxh7 c2 23.Kxe2 cxd1=Q+ 24.Rxd1 f6 25.Rf1 f5 26.Ng5 Bd7 27.Nf3 Bc6 28.g3 Rc8 29.Ne5 Kf6 30.Nxc6 Rxc6 31.Kd3 Ra6 32.Ra1 e5 33.a4 e4+ 34.Kd4 Rd6+ 35.Kc5 Ke5 36.a5 e3 37.h4 b6+ 38.Kb5 Ke4 39.Ka6 e2 40.Re1 Ke3 41.Kxa7 bxa5 42.Kb7 Kd2 43.Rh1 e1=Q 44.Rxe1 Kxe1 45.c5 Rh6 46.c6 a4 47.c7 Rh7 48.h5 a3 49.Kb8 Rxc7 50.Kxc7 a2 51.Kd6 a1=Q 52.Ke6 Qh8 53.Kxf5 Qxh5+ 54.Kf4 Kf2 55.g4 Qg6 56.g5 Qf7+ 57.Ke5 Kg3 58.Ke4 Kg4 59.Ke5 Qg6 60.Kd4 Qxg5 61.Ke4 Qf5+ 62.Kd4 Kf4 63.Kc3 Qd7 64.Kc4 Ke4 65.Kc3 Qd3+ 66.Kb4 Qc2 67.Kb5 Kd5 68.Kb6 Qc6+ 69.Ka7 Qb5 70.Ka8 Kc6 71.Ka7 Qb7# 0-1";
	private String tbfen = "8/8/6q1/8/5KP1/8/5k2/8 w - - 1 56";
	private String compfen = "8/p7/Kp1r4/P4p2/2P4P/4k1P1/4p3/4R3 w - - 2 41";
	private ArrayList<Tactic> tacticList = new ArrayList<Tactic>();
	private String errorMessage;
	@Test
	public void test() {
		testPGN tp = new testPGN(pgn);
		synchronized(tp) {
			if(!tp.finished)
				try {
					tp.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		fail("Not done");
	}

	public class Tactic {
		public Tactic(Fen fen, String moves) {
			this.fen = fen;
			this.moves = moves;
		}
		public Fen fen;
		public String moves;
	};
	public class testPGN extends GameChecker {
		private PGNGame pgngame;
		public boolean finished = false;
		
		david.logan.chess.support.MoveList.moveIterator moveIterator = null;
		public testPGN(String game) {
			super(4096, "/Users/davidlogan/Documents/stockfish-5-mac/Mac/stockfish-5-sse42", "./Book.bin", 2, 20, 30);
	        PGNParser parser = null;
	        parser = new PGNParser(new StringReader(game));
	        @SuppressWarnings("unused")
	        ArrayList<PGNGame> gamelist = null;
	        try {
	            gamelist = parser.parsefile();
	            pgngame = gamelist.get(0);
	            moveIterator = pgngame.iterator();
	            analyzeGame();
	        } catch (ParseException e) {
	        	e.printStackTrace();
	        	errorMessage = e.getMessage();
	        	finished = true;
	        	synchronized(this) {
	        		notify();
	        	}
	        } catch (IllegalMoveException e) {
	        	e.printStackTrace();
	        	errorMessage = e.getMessage();
	        	finished = true;
	        	synchronized(this) {
	        		notify();
	        	}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	errorMessage = e.getMessage();
	        	finished = true;
	        	synchronized(this) {
	        		notify();
	        	}
	        }
	        return;
		}

		@Override
		public void getnextmove()  {
			if(!moveIterator.hasNext())
				endofgame();
			else
				move(new ICCMove(moveIterator.next()));
		}

		@Override
		public void endofgame() {
			shutdown();
			finished = true;
			synchronized(this) {
				this.notify();
			}
		}

		@Override
		public void goodtactic(Fen fen, String moves, boolean winning) {
			tacticList.add(new Tactic(fen, moves));
		}

		@Override
		public void badtactic(Fen fen, String moves) {
		}

		@Override
		public void haveOnlineTablebase(OnlineTablebase otb) {
		}
	};
}
