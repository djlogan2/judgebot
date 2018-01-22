package chessclub.com.icc.tt;

import static org.junit.Assert.*;

import org.junit.Test;

import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.NormalChessBoard;

public class TestAlgebraicMoves {
	@Test
	public void test() {
		testme("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", "e2e4", "e4");
		testme("rnbqkbnr/pppppppp/8/6N1/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", "g5f3", "N5f3");
		testme("rnbqkbnr/pppppppp/8/6N1/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", "g1f3", "N1f3");
		testme("rnbqkbnr/pppppppp/8/6N1/8/5n2/PPPPPPPP/RNBQKBNR w - - 0 1", "g5f3", "N5xf3");
		testme("rnbqkbnr/pppppppp/8/6N1/8/5n2/PPPPPPPP/RNBQKBNR w - - 0 1", "g1f3", "N1xf3");
		testme("rnbqkbnr/pppppppp/8/4N3/8/5n2/PPPPPPPP/RNBQKBNR w - - 0 1", "e5f3", "Nexf3");
		testme("rnbqkbnr/pppppppp/8/4N3/8/5n2/PPPPPPPP/RNBQKBNR w - - 0 1", "g1f3", "Ngxf3");
		testme("rnbqkbnr/pppppppp/8/4N3/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", "e5f3", "Nef3");
		testme("rnbqkbnr/pppppppp/8/4N3/8/8/PPPPPPPP/RNBQKBNR w - - 0 1", "g1f3", "Ngf3");
		testme("8/8/1N1N3k/8/7K/8/1N1N4/8 w - - 0 1", "b6c4", "Nb6c4");
		testme("8/8/1N1N4/8/7K/4k3/1N1N4/8 w - - 0 1", "b6c4", "Nb6c4+");
		testme("8/8/1N1N4/4K3/8/4k3/1N1N3R/8 w - - 0 1", "b6c4", "Nb6c4#");
		testme("8/8/8/8/3pPp1k/8/7K/8 b - e3 0 1", "d4e3", "dxe3");
		testme("8/8/8/8/3pPp1k/8/7K/8 b - e3 0 1", "f4e3", "fxe3");
		testme("8/8/8/8/3pPp1k/8/3K4/8 b - e3 0 1", "d4e3", "dxe3+");
		testme("8/8/8/8/3pPp1k/8/3K4/8 b - e3 0 1", "f4e3", "fxe3+");
		testme("3k4/8/8/8/8/8/8/R3K3 w Q - 0 1", "e1c1", "O-O-O+");
		testme("3k4/7R/8/7B/8/8/2R5/R3K3 w Q - 0 1", "e1c1", "O-O-O#");
		testme("3k4/7R/8/7B/8/8/2R5/R3K3 w - - 0 1", "c2c7", "Rcc7");
		testme("3k4/7R/8/7B/8/8/2R5/R3K3 w - - 0 1", "h7c7", "Rhc7");
		testme("3k4/8/R7/7B/7R/8/8/R3K3 w - - 0 1", "a6a4", "R6a4");
		testme("3k4/8/R7/7B/7R/8/8/R3K3 w - - 0 1", "a1a4", "R1a4");
		testme("3k4/8/R7/7B/7R/8/8/R3K3 w - - 0 1", "h4a4", "Rha4");
		testme("5k2/7K/1q3q2/8/8/8/1q3q2/8 b - - 0 1", "b6d4", "Qb6d4");
		testme("5k2/8/1q3q2/8/8/3K4/1q3q2/8 b - - 0 1", "b6d4", "Qb6d4#");
		testme("5k2/8/1q3q2/8/6K1/8/1q3q2/8 b - - 0 1", "b6d4", "Qb6d4+");
		testme("4r3/3P2k1/8/7K/8/8/8/8 w - - 0 1", "d7e8N", "dxe8=N+");
		testme("4r2R/3P2k1/5R1R/7K/8/8/8/8 w - - 0 1", "d7e8N", "dxe8=N#");
	}
	private void testme(String fen, String move, String expect)
	{
		try {
			NormalChessBoard board = new NormalChessBoard(new Fen(fen));
			Move superMove = board.makeMove(new Move(move, true));
			assertEquals(expect, superMove.getAlgebraicMove());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Fen " + "x" + " Move " + move + " expecting " + expect + " crashed");
		}
		
	}
}
