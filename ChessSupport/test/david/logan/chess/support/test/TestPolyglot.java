package david.logan.chess.support.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import david.logan.chess.support.Fen;
import david.logan.chess.support.PolyglotBook;

public class TestPolyglot {

	@Test
	public void testHash() {
		TestPoly tp = new TestPoly();
		try {
			assertEquals(0xd476320e4e5da44fL, tp.rhash(new Fen("K7/2Q5/1k6/3q4/4B3/6N1/5b2/7n w KQkq - 0")));
			assertEquals(0x463b96181691fc9cL, tp.rhash(new Fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")));
			assertEquals(0x823c9b50fd114196L, tp.rhash(new Fen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1")));
			assertEquals(0x0756b94461c50fb0L, tp.rhash(new Fen("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2")));
			assertEquals(0x662fafb965db29d4L, tp.rhash(new Fen("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2")));
			assertEquals(0x22a48b5a8e47ff78L, tp.rhash(new Fen("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 3")));
			assertEquals(0x652a607ca3f242c1L, tp.rhash(new Fen("rnbqkbnr/ppp1p1pp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR b kq - 0 3")));
			assertEquals(0x00fdd303c946bdd9L, tp.rhash(new Fen("rnbq1bnr/ppp1pkpp/8/3pPp2/8/8/PPPPKPPP/RNBQ1BNR w - - 0 4")));
			assertEquals(0x3c8123ea7b067637L, tp.rhash(new Fen("rnbqkbnr/p1pppppp/8/8/PpP4P/8/1P1PPPP1/RNBQKBNR b KQkq c3 0 3")));
			assertEquals(0x5c3f9b829b279560L, tp.rhash(new Fen("rnbqkbnr/p1pppppp/8/8/P6P/R1p5/1P1PPPP1/1NBQKBNR b Kkq - 0 4")));
			ArrayList<PolyglotBook.entry> list = tp.rfindkey(tp.rhash(new Fen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")));
			assertNotNull(list);
			//tp.rfindkey(0x463b96181691fc9cL);
			//System.out.println(list.size());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	public class TestPoly extends PolyglotBook {
		public TestPoly() {
			super("/Users/davidlogan/Documents/RpC-SF-0.4.bin");
		}
		public long rhash(Fen fen) { return hash(fen); }
		public ArrayList<entry> rfindkey(long key) { return findKey(key); }
	}
}
