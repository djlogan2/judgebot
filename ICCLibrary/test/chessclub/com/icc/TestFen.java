package chessclub.com.icc;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import david.logan.chess.support.Fen;

public class TestFen {

	@Test
	public void testFenString() {
		try {
			for (String line : Files.readAllLines(Paths.get("/Users/davidlogan/Documents/workspace/judgebot/TacticalTrainer/fens.txt"))) {
			    @SuppressWarnings("unused")
				Fen fen = new Fen(line);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
