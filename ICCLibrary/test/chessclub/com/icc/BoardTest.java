package chessclub.com.icc;

//import static org.junit.Assert.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import david.logan.chess.support.ChessPiece;
import david.logan.chess.support.ChessPieceEnum;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.NormalChessBoard;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.Square;
/**
 * 
 * @author David Logan
 * 
 * Test class
 *
 */
public class BoardTest {
	private class NCB extends NormalChessBoard {
		public Move makeMove(Square from, Square to, ChessPieceEnum queeningPiece) throws Exception {
			return super.makeMove(from, to, queeningPiece);
		}
	}
    private NCB board = null;
    private ChessPiece[][] startingPosition;
    private final ChessPieceEnum[] pcol = new ChessPieceEnum[]
            {ChessPieceEnum.ROOK, ChessPieceEnum.KNIGHT, ChessPieceEnum.BISHOP,
             ChessPieceEnum.QUEEN, ChessPieceEnum.KING, ChessPieceEnum.BISHOP,
             ChessPieceEnum.KNIGHT, ChessPieceEnum.ROOK};

    /**
     * 
     */
    public BoardTest() {
        Color color;
        startingPosition = new ChessPiece[8][8];
        for (int rank = 0; rank < 8; rank++) {
            if (rank < 2) {
                color = Color.WHITE;
            } else if (rank > 5) {
                color = Color.BLACK;
            } else {
                color = Color.NONE;
            }

            for (int file = 0; file < 8; file++) {
                if (color == Color.NONE) {
                    startingPosition[rank][file] = new ChessPiece(color, ChessPieceEnum.NONE);
                } else if (rank == 0 || rank == 7) {
                    startingPosition[rank][file] = new ChessPiece(color, pcol[file]);
                } else if (rank == 1 || rank == 6) {
                    startingPosition[rank][file] = new ChessPiece(color, ChessPieceEnum.PAWN);
                }
            }
        }
    }

    /**
     * 
     * @throws Exception    If an Exception occurs
     */
    @Before
    public final void setUp() throws Exception {
        board = new NCB();
    }

    /**
     * 
     * @throws Exception    If an Exception occurs
     */
    @After
    public final void tearDown() throws Exception {
        board = null;
    }

    /**
     * 
     */
    @Test
    public final void testBoard() {
        assertEquals(true, board.canCastle(Color.WHITE, NormalChessBoard.KINGSIDE));
        assertEquals(true, board.canCastle(Color.WHITE, NormalChessBoard.QUEENSIDE));
        assertEquals(true, board.canCastle(Color.BLACK, NormalChessBoard.KINGSIDE));
        assertEquals(true, board.canCastle(Color.BLACK, NormalChessBoard.QUEENSIDE));
        assertEquals(Color.WHITE, board.colorToMove());
        assertEquals(false, board.canEnPassant());
        assertNull(board.enPassantSquare());
        assertEquals(1, board.moveNumber());
        assertEquals(0, board.moveTo50MoveRule());
        Square square = new Square(0,0);
        for (square.rank = 0; square.rank < 8; square.rank++) {
            for (square.file = 0; square.file < 8; square.file++) {
                assertEquals("Expected " + this.startingPosition[square.rank][square.file].color() + " at " + square + " but it was " + board.colorAt(square), this.startingPosition[square.rank][square.file].color(), board.colorAt(square));
                assertEquals("Expected " + this.startingPosition[square.rank][square.file].piece() + " at " + square + " but it was " + board.piecetypeAt(square), this.startingPosition[square.rank][square.file].piece(), board.piecetypeAt(square));
            }
        }
        try {
            board.makeMove(new Square("e2"), new Square("e4"), null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            board.makeMove(new Square("e8"), new Square("d8"), null);
            fail("Should not have succeeded");
            board.makeMove(new Square("d4"), new Square("d5"), null);
            fail("Should not have succeeded");
        } catch (Exception e) {
        }
    }

    /**
     * 
     */
    @Test
    public final void testClearBoard() {
        board.clearBoard();
        assertEquals(false, board.canCastle(Color.WHITE, NormalChessBoard.KINGSIDE));
        assertEquals(false, board.canCastle(Color.WHITE, NormalChessBoard.QUEENSIDE));
        assertEquals(false, board.canCastle(Color.BLACK, NormalChessBoard.KINGSIDE));
        assertEquals(false, board.canCastle(Color.BLACK, NormalChessBoard.QUEENSIDE));
        assertEquals(Color.NONE, board.colorToMove());
        assertEquals(false, board.canEnPassant());
        assertNull(board.enPassantSquare());
        assertEquals(1, board.moveNumber());
        assertEquals(0, board.moveTo50MoveRule());
        Square square = new Square();
        for (square.rank = 0; square.rank < 8; square.rank++) {
            for (square.file = 0; square.file < 8; square.file++) {
                assertEquals("Expected " + Color.NONE + " at " + square + " but it was " + board.colorAt(square), Color.NONE, board.colorAt(square));
                assertEquals("Expected " + ChessPieceEnum.NONE + " at " + square + " but it was " + board.piecetypeAt(square), ChessPieceEnum.NONE, board.piecetypeAt(square));
            }
        }
    }
    @Test
    public final void testFENstrings() {
        try {
			board.loadFEN("r1b2rkq/pppp1pbp/2n3n1/3N4/2B2Q2/1P6/P1PP2PP/B3RR1K w - - 0 1");
		} catch (Exception e) {
			fail(e.getMessage());
		}
    }
    /**
     * 
     */
    @Test
    public final void testLoadFEN() {
        try {
			board.loadFEN("8/8/6p1/N3Ppk1/P5P1/2P2Kb1/8/8 w - f6 0 49");
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
        assertEquals(false, board.canCastle(Color.WHITE, NormalChessBoard.KINGSIDE));
        assertEquals(false, board.canCastle(Color.WHITE, NormalChessBoard.QUEENSIDE));
        assertEquals(false, board.canCastle(Color.BLACK, NormalChessBoard.KINGSIDE));
        assertEquals(false, board.canCastle(Color.BLACK, NormalChessBoard.QUEENSIDE));
        assertEquals(Color.WHITE, board.colorToMove());
        assertEquals(true, board.canEnPassant());
        assertEquals(new Square("f6"), board.enPassantSquare());
        assertEquals(97, board.moveNumber());
        assertEquals(0, board.moveTo50MoveRule());
        try {
            assertTrue(board.isLegalMove(new Square("e5"), new Square("f6")));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        // board.loadFEN("8/8
        int row = 7;
        int col = 0;
        for (; row >= 6; row--) {
            for (col = 0; col < 8; col++) {
                checkPiece(row, col, Color.NONE, ChessPieceEnum.NONE);
            }
        }
        // board.loadFEN(" 6p1/
        col = 0;
        for (col = 0; col < 6; col++) {
            checkPiece(row, col, Color.NONE, ChessPieceEnum.NONE);
        }
        checkPiece(row, col++, Color.BLACK, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        // board.loadFEN(" N4pk1
        row--;
        col = 0;
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.KNIGHT);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.BLACK, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.BLACK, ChessPieceEnum.KING);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        // board.loadFEN(" P3P1P1
        row--;
        col = 0;
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        // board.loadFEN("       2P2Kb1/8/8 w - f6 0 49");
        row--;
        col = 0;
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.PAWN);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        checkPiece(row, col++, Color.WHITE, ChessPieceEnum.KING);
        checkPiece(row, col++, Color.BLACK, ChessPieceEnum.BISHOP);
        checkPiece(row, col++, Color.NONE, ChessPieceEnum.NONE);
        // board.loadFEN(" 8/8
        row--;
        col = 0;
        for (; row >= 0; row--) {
            for (col = 0; col < 8; col++) {
                checkPiece(row, col, Color.NONE, ChessPieceEnum.NONE);
            }
        }
    }

    private void checkPiece(final int rank, final int file, final Color color, final ChessPieceEnum piece) {
        Square square = new Square(rank, file);
        assertEquals("Expected " + color + " at square " + square + " but it was " + board.colorAt(square), color, board.colorAt(square));
        assertEquals("Expected " + piece + " at square " + square + " but it was " + board.piecetypeAt(square), piece, board.piecetypeAt(square));
    }

    /**
     * 
     */
    @Test
    public final void testSetStartingPosition() {
        board.clearBoard();
        board.setStartingPosition();
        assertEquals(true, board.canCastle(Color.WHITE, NormalChessBoard.KINGSIDE));
        assertEquals(true, board.canCastle(Color.WHITE, NormalChessBoard.QUEENSIDE));
        assertEquals(true, board.canCastle(Color.BLACK, NormalChessBoard.KINGSIDE));
        assertEquals(true, board.canCastle(Color.BLACK, NormalChessBoard.QUEENSIDE));
        assertEquals(Color.WHITE, board.colorToMove());
        assertEquals(false, board.canEnPassant());
        assertNull(board.enPassantSquare());
        assertEquals(1, board.moveNumber());
        assertEquals(0, board.moveTo50MoveRule());
        Square square = new Square();
        for (square.rank = 0; square.rank < 8; square.rank++) {
            for (square.file = 0; square.file < 8; square.file++) {
                assertEquals("Expected " + this.startingPosition[square.rank][square.file].color() + " at square.rank " + square.rank + ",square.file " + square.file + " but it was " + board.colorAt(square), this.startingPosition[square.rank][square.file].color(), board.colorAt(square));
                assertEquals("Expected " + this.startingPosition[square.rank][square.file].piece() + " at square.rank " + square.rank + ",square.file " + square.file + " but it was " + board.piecetypeAt(square), this.startingPosition[square.rank][square.file].piece(), board.piecetypeAt(square));
            }
        }
    }

    /**
     * 
     */
    @Test
    public final void testGetRowCol() {
        final String[][] squares = new String[][] {{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8"}, {"b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"}, {"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8"},
                {"d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"}, {"e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8"}, {"f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8"}, {"g1", "g2", "g3", "g4", "g5", "g6", "g7", "g8"},
                {"h1", "h2", "h3", "h4", "h5", "h6", "h7", "h8"}};
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                assertEquals(new Square(rank, file).toString(), squares[file][rank]);
            }
        }
    }

    @Test
    public void test1() {
        NCB board = new NCB();
        try {
			board.loadFEN("r1bqk1nr/pp2bppp/2n5/3p4/3N4/1BP5/PP3PPP/RNBQ1RK1 b kq - 0 18");
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
        try {
            board.makeMove(new Square("a8"), new Square("b8"), null);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(board.canCastle(Color.BLACK, NormalChessBoard.KINGSIDE));
        assertFalse(board.canCastle(Color.BLACK, NormalChessBoard.QUEENSIDE));
    }

    @Test
    public void test2() {
        NCB board = new NCB();
        try {
			board.loadFEN("8/P3r1pp/8/5k2/1P2bb1P/4r3/4P3/R2K1BR1 w - - 1 69");
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
        try {
            board.makeMove(new Square("a7"), new Square("a8"), ChessPieceEnum.ROOK);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(ChessPieceEnum.NONE, board.piecetypeAt(new Square("a7")));
        assertEquals(ChessPieceEnum.ROOK, board.piecetypeAt(new Square("a8")));
        assertEquals(Color.WHITE, board.colorAt(new Square("a8")));
    }

    @Test
    public void test3() {
        NCB board = new NCB();
        try {
			board.loadFEN("r6r/6PP/kp5Q/p1p5/P1P5/1P6/5PK1/8 w - - 0 95");
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
        try {
            board.makeMove(new Square("g7"), new Square("h8"), ChessPieceEnum.QUEEN);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals(ChessPieceEnum.NONE, board.piecetypeAt(new Square("g7")));
        assertEquals(ChessPieceEnum.QUEEN, board.piecetypeAt(new Square("h8")));
        assertEquals(Color.WHITE, board.colorAt(new Square("h8")));
    }
    
    @Test
    public void test4() {
        String fenstring = "8/p7/P7/1p1p4/1P1P1p2/1K4k1/8/2q1q3 w - - 0 53";
        Fen fen = null;
		try {
			fen = new Fen(fenstring);
		} catch (Exception e) {
			fail(e.getMessage());
		}
        assertEquals(Color.WHITE, fen.toMove());
        assertFalse(fen.blackCanCastle()[0]);
        assertFalse(fen.blackCanCastle()[1]);
        assertFalse(fen.whiteCanCastle()[0]);
        assertFalse(fen.whiteCanCastle()[1]);
        assertNull(fen.enpassant());
        assertEquals(0, fen.movesto50());
        assertEquals(105, fen.moveNumber());
    }
    
    @Test
    public void testcanwinontime() {
        String fenstring = "5k2/1N6/1p6/8/p7/3pK3/8/8 b - - 1 5";
        NormalChessGame game = new NormalChessGame();
        try {
			game.loadFEN(fenstring);
		} catch (Exception e) {
			fail(e.getMessage());
		}
        assertTrue(game.canWinOnTime(Color.BLACK));
        assertFalse(game.canWinOnTime(Color.WHITE));
    }
    
    @Test
    public void testNormalChess() {
        Fen fen = null;
		try {
			fen = new Fen("r1bq1rk1/2p2pp1/pP1bp2B/3pn3/8/1PN3Q1/P3PPPP/R3KB1R b KQq - 2 90");
		} catch (Exception e1) {
			fail(e1.getMessage());
		}
        NCB tempgame = new NCB();
        tempgame.loadFEN(fen);
        try {
            tempgame.makeMove(new Square("e5"), new Square("g6"), null);
            tempgame.makeMove(new Square("g3"), new Square("g5"), null);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not have had an exception: "+e.getMessage());
        }
    }
}
