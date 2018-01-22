package david.logan.chess.support.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList.moveIterator;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.PGNGame;
import david.logan.chess.support.pgn.parser.PGNParser;


public class TestChessBoard {

	@Test
	public void testMakeMoveMove() {
        PGNParser parser = null;
        PGNGame game = null;
        parser = new PGNParser(new StringReader(pgn));
        ArrayList<PGNGame> gamelist = null;
        try {
			gamelist = parser.parsefile();
			game = gamelist.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
        NormalChessGame ncg = new NormalChessGame();
        moveIterator i = game.iterator();
        while(i.hasNext()) {
        	Move m = i.next();
        	System.out.println(m);
        	try {
				ncg.makeMove(m);
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
        	try {
				@SuppressWarnings("unused")
				Fen f = new Fen(ncg.getFEN().getFEN());
			} catch (Exception e) {
				e.printStackTrace();
				fail(e.getMessage());
			}
        }
	}

	private String pgn = "[Event \"ICC 15 0\"]\n" +
			"[Site \"Internet Chess Club\"]\n" +
			"[Date \"2015.05.22\"]\n" +
			"[Round \"-\"]\n" +
			"[White \"Antillian103\"]\n" +
			"[Black \"steve985\"]\n" +
			"[Result \"0-1\"]\n" +
			"[ICCResult \"White resigns\"]\n" +
			"[WhiteElo \"1197\"]\n" +
			"[BlackElo \"1343\"]\n" +
			"[Opening \"Ruy Lopez: old Steinitz defense\"]\n" +
			"[ECO \"C62\"]\n" +
			"[NIC \"RL.02\"]\n" +
			"[Time \"21:09:00\"]\n" +
			"[TimeControl \"900+0\"]\n" +
			"\n" +
			"1. e4 {0:15:00} e5 {0:15:00} 2. Nf3 {0:14:51} Nc6 {0:14:58} 3. Bb5 {0:14:48}\n" +
			"d6 {0:14:56} 4. d4 {0:14:37} exd4 {0:14:52} 5. Bc4 {0:14:28} Bg4 {0:14:43}\n" +
			"6. Bxf7+ {0:13:51} Kxf7 {0:14:37} 7. Ng5+ {0:13:50} Ke8 {0:14:22} 8. Qxg4\n" +
			"{0:13:43} Be7 {0:14:18} 9. Ne6 {0:13:36} Qc8 {0:13:50} 10. Qxg7 {0:13:10}\n" +
			"Bf6 {0:13:29} 11. Qf8+ {0:12:56} Kd7 {0:13:27} 12. Qf7+ {0:12:48} Nge7\n" +
			"{0:13:18} 13. Qxf6 {0:12:36} Nb4 {0:13:15} 14. Nxd4 {0:12:18} Rf8 {0:13:08}\n" +
			"15. Qe6+ {0:12:13} Ke8 {0:13:00} 16. Bg5 {0:12:04} Qxe6 {0:12:51} 17. Nxe6\n" +
			"{0:11:41} Nxc2+ {0:12:16} 18. Kd2 {0:11:36} Nxa1 {0:12:14} 19. Nxc7+\n" +
			"{0:11:34} Kd7 {0:12:08} 20. Nxa8 {0:11:30} Rxa8 {0:12:05} 21. Nc3 {0:11:26}\n" +
			"Rg8 {0:12:02} 22. f4 {0:11:18} Nc6 {0:11:41} 23. Rxa1 {0:11:11} Nd4\n" +
			"{0:11:40} 24. Nd5 {0:10:57} Rg6 {0:11:24} 25. Kd3 {0:10:43} Ne6 {0:11:19}\n" +
			"26. h4 {0:10:20} Nc5+ {0:11:12} 27. Kd4 {0:09:58} h6 {0:11:10} 28. Ne7\n" +
			"{0:09:43} Rg7 {0:10:53} 29. Nf5 {0:09:21} Ne6+ {0:10:35} 30. Kd5 {0:09:15}\n" +
			"Rg8 {0:10:07} 31. Bxh6 {0:08:46} Rxg2 {0:09:58} 32. Nxd6 {0:08:36} Rxb2\n" +
			"{0:09:47} 33. f5 {0:08:27} Nc7+ {0:09:29} 34. Ke5 {0:08:16} Rh2 {0:09:16}\n" +
			"35. Re1 {0:07:55} Rxh4 {0:09:11} 36. Nxb7 {0:07:45} Rxh6 {0:08:58} 37. Nc5+\n" +
			"{0:07:35} Ke8 {0:08:45} 38. f6 {0:07:19} Kf7 {0:08:40} 39. Nd7 {0:07:04} Ne8\n" +
			"{0:08:37} 40. Kf4 {0:06:37} Nxf6 {0:08:32} 41. Ne5+ {0:06:32} Ke6 {0:08:15}\n" +
			"42. Nc6 {0:06:23} Rh4+ {0:07:50} 43. Ke3 {0:06:15} Rxe4+ {0:07:28} 44. Kf2\n" +
			"{0:06:08} Rxe1 {0:07:23} 45. Kxe1 {0:06:06} a6 {0:07:19} 46. Nd8+ {0:06:02}\n" +
			"Kd5 {0:07:11} 47. Nb7 {0:05:46} Kc4 {0:07:07} 48. Nd6+ {0:05:41} Kb4\n" +
			"{0:07:04} 49. Nc8 {0:05:33} Ka3 {0:06:57} 50. Nd6 {0:05:25} Kxa2 {0:06:55}\n" +
			"51. Nc4 {0:05:21} Kb3 {0:06:52} 52. Na5+ {0:05:16} Kb4 {0:06:37} 53. Nc6+\n" +
			"{0:05:08} Kc5 {0:06:23} 54. Ne5 {0:05:03} a5 {0:06:13} 55. Nd3+ {0:05:01}\n" +
			"Kc4 {0:06:05} 56. Nb2+ {0:04:58} Kb3 {0:06:01} 57. Nd3 {0:04:56} a4\n" +
			"{0:05:57} 58. Nc1+ {0:04:54} Kc2 {0:05:47} 59. Na2 {0:04:49} a3 {0:05:41}\n" +
			"60. Ke2 {0:04:43} Kb2 {0:05:37} 61. Nb4 {0:04:38} Nd5 {0:05:14} 62. Nd3+\n" +
			"{0:04:33} Kc3 {0:05:02} 63. Nc1 {0:04:30} Kc2 {0:04:58} 64. Na2 {0:04:23}\n" +
			"Nc3+ {0:04:55} 65. Nxc3 {0:04:13} Kxc3 {0:04:52} 66. Kd1 {0:04:11} Kb2\n" +
			"{0:04:49} 67. Kd2 {0:04:09} a2 {0:04:48} 68. Kd1 {0:04:07} a1=Q+ {0:04:45}\n" +
			"69. Kd2 {0:04:06} Qa3 {0:04:43} 70. Kd1 {0:04:03} Qd3+ {0:04:38} 71. Ke1\n" +
			"{0:04:01} Qc2 {0:04:27} 72. Kf1 {0:03:57} Kc3 {0:04:25} 73. Ke1 {0:03:55}\n" +
			"Kd3 {0:04:22} 74. Kf1 {0:03:52} Ke3 {0:04:19} 75. Kg1 {0:03:48} Kf3\n" +
			"{0:04:15} {White resigns} 0-1\n";

}
