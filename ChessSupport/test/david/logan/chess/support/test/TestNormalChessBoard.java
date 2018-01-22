package david.logan.chess.support.test;

import static org.junit.Assert.*;

import org.junit.Test;

import david.logan.chess.support.Board;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.NormalChessBoard;

public class TestNormalChessBoard {

	@Test
	public void testCastlingFromBeginning() {
		NormalChessBoard ncb = new NormalChessBoard();
		try {
			String[] moves = new String[]{"e2e4", "e7e5", "g1f3", "g8f6", "f1c4", "f8c5", "b2b4", "b7b5", "b4c5", "b5c4","c5c6", "c4c3","c1a3","c8a6","d2d3","d7d6", "b1c3","b8c6","c3d5","c6d4","d5c7","d8c7"};
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
			assertArrayEquals(new boolean[]{true,true}, ncb.getFEN().blackCanCastle());
			assertArrayEquals(new boolean[]{true,true}, ncb.getFEN().whiteCanCastle());
			for(int x = 0 ; x < moves.length ; x++)
			{
				ncb.makeMove(new Move(moves[x], true));
				assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
				assertEquals(true,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
				assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
				assertEquals(true,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
				assertArrayEquals(new boolean[]{true,true}, ncb.getFEN().blackCanCastle());
				assertArrayEquals(new boolean[]{true,true}, ncb.getFEN().whiteCanCastle());
			}
		} catch (Exception e) {
			fail("Test failed");
		}
	}
	@Test
	public void testCastlingFromFen()
	{
		try {
			NormalChessBoard ncb = new NormalChessBoard(new Fen("r2qk2r/p1N2ppp/b2p1n2/4p3/3nP3/B2P1N2/P1P2PPP/R2QK2R b KQkq - 0 1"));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
			ncb.makeMove(new Move("d8c7", true));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
			ncb = new NormalChessBoard(new Fen("rn1qkb1r/2p1pppp/5n2/1p6/3P4/1p2PN2/3N1PPP/R1BQK2R w KQkq - 0 10"));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
			ncb.makeMove(new Move("a1a8", true));
			assertEquals(true,  ncb.canCastle(Color.BLACK, Board.KINGSIDE));
			assertEquals(false,  ncb.canCastle(Color.BLACK, Board.QUEENSIDE));
			assertEquals(true,  ncb.canCastle(Color.WHITE, Board.KINGSIDE));
			assertEquals(false,  ncb.canCastle(Color.WHITE, Board.QUEENSIDE));
		} catch (Exception e) {
			fail("Test failed");
		}
	}
}
