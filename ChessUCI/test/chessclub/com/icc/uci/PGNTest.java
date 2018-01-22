package chessclub.com.icc.uci;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.util.ArrayList;

import org.junit.Test;

import david.logan.chess.support.Fen;
import david.logan.chess.support.IllegalMoveException;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.PGNGame;
import david.logan.chess.support.MoveList.moveIterator;
import david.logan.chess.support.pgn.parser.PGNParser;
import david.logan.chess.support.pgn.parser.ParseException;

public class PGNTest {
//    @Test
//    public void pgnTest() throws Exception {
//        FileInputStream f;
//        PGNParser parser = null;
//        try {
//            f = new FileInputStream("/Users/davidlogan/Documents/icc/ICCLibrary/test/test.pgn");
//            parser = new PGNParser(f);
//        } catch (FileNotFoundException e) {
//            fail("Unable to find file");
//        }
//        @SuppressWarnings("unused")
//        ArrayList<PGNGame> gamelist = null;
//        try {
//            gamelist = parser.parsefile();
//        } catch (ParseException e) {
//            fail(e.getMessage());
//        }
//        return;
//    }
    String game = "[Event \"ICC w20 20 10 u\"]\r[Site \"Internet Chess Club\"]\r[Date \"2013.01.16\"]\r[Round \"-\"]\r[White \"idjit\"]\r" +
    "[Black \"TrainingBot\"]\r[Result \"1-0\"]\r[ICCResult \"Black checkmated\"]\r[WhiteElo \"1113\"]\r[BlackElo \"1600\"]\r[Time \"23:28:50\"]\r" +
    "[TimeControl \"1200+10\"]\r[SetUp \"1\"]\r[FEN \"r1b2rkq/pppp1pbp/2n3n1/3N4/2B2Q2/1P6/P1PP2PP/B3RR1K w - - 0 1\"]\r\r" +
    "1. Ne7+ Ncxe7 2. Bxf7+ Rxf7 3. Qxf7# {Black checkmated} 1-0\r\r";
    String game2 = "[Event \"ICC 5 0 u\"]\n" +
                    "[Site \"Internet Chess Club\"]\n" +
                    "[Date \"2011.08.05\"]\n" +
                    "[Round \"-\"]\n" +
                    "[White \"idjit\"]\n" +
                    "[Black \"XACTO\"]\n" +
                    "[Result \"0-1\"]\n" +
                    "[ICCResult \"White forfeits on time\"]\n" +
                    "[WhiteElo \"1196\"]\n" +
                    "[BlackElo \"1406\"]\n" +
                    "[Opening \"King's pawn opening\"]\n" +
                    "[ECO \"B07\"]\n" +
                    "[NIC \"VO.17\"]\n" +
                    "[Time \"00:33:18\"]\n" +
                    "[TimeControl \"300+0\"]\n" +
                    "\n" +
                    "1. e4 {0:04:58} d6 {0:05:00} 2. Nf3 {0:04:56} g6 {0:04:59} 3. d4 {0:04:54}\n" +
                    "Bg7 {0:04:59} 4. Be3 {0:04:51} Nf6 {0:04:58} 5. Bd3 {0:04:49} Bg4 {0:04:58}\n" +
                    "6. O-O {0:04:45} Qd7 {0:04:56} 7. Nc3 {0:04:37} h5 {0:04:56} 8. Be2\n" +
                    "{0:04:35} Nc6 {0:04:55} 9. Qd2 {0:04:25} O-O-O {0:04:52} 10. Rad1 {0:04:22}\n" +
                    "Kb8 {0:04:50} 11. h3 {0:04:02} Bxh3 {0:04:48} 12. gxh3 {0:03:58} Qxh3\n" +
                    "{0:04:46} 13. Nb5 {0:02:52} a6 {0:04:34} 14. d5 {0:02:25} axb5 {0:04:25} 15.\n" +
                    "dxc6 {0:02:23} bxc6 {0:04:24} 16. Qa5 {0:02:05} Kb7 {0:04:18} 17. Qa7+\n" +
                    "{0:01:59} Kc8 {0:04:17} 18. Nd4 {0:01:33} Kd7 {0:04:03} 19. Nxb5 {0:01:16}\n" +
                    "Qg4+ {0:03:55} 20. Bxg4+ {0:01:10} hxg4 {0:03:50} 21. Qxc7+ {0:01:00} Ke6\n" +
                    "{0:03:49} 22. Qxc6 {0:00:55} Rc8 {0:03:43} 23. Nd4+ {0:00:47} Ke5 {0:03:39}\n" +
                    "24. Qb5+ {0:00:31} Kxe4 {0:03:33} 25. Qd3+ {0:00:27} Ke5 {0:03:24} 26. f4+\n" +
                    "{0:00:24} gxf3 {0:03:22} 27. Nxf3+ {0:00:18} Ke6 {0:03:12} 28. Ng5+\n" +
                    "{0:00:16} Kd7 {0:03:08} 29. Qb5+ {0:00:14} Rc6 {0:03:07} 30. Qb7+ {0:00:13}\n" +
                    "Rc7 {0:03:07} 31. Qb5+ {0:00:06} Rc6 {0:03:04} 32. Qb7+ {0:00:03} Rc7\n" +
                    "{0:03:02} {White forfeits on time} 0-1\n";
    @Test
    public void testgame() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }

    @Test
    public void testgame2() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game2));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    
    @Test
    public void testgame3() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game3));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    
    @Test
    public void testgame4() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game4));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    
    
    @Test
    public void testgame5() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game5));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    @Test
    public void testgame6() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game6));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    @Test
    public void testgame7() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game7));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    @Test
    public void testgame8() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game8));
        @SuppressWarnings("unused")
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return;
    }
    
    @Test
    public void testgame8b() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game8));
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        game8 = gamelist.get(0).pgnToString();
        
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        return;
    }
    
    
    @Test
    public void testgame9() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game9));
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        NormalChessGame ncg = new NormalChessGame();
        String where = "";
        
        for(moveIterator mi = gamelist.get(0).iterator() ; mi.hasNext();) {
        	david.logan.chess.support.Move m = mi.next();
        	try {
				ncg.makeMove(m);
				if(ncg.canEnPassant()) {
					System.out.println("Making move " + m);
					System.out.println("Yep, can en-passant on move " + ncg.moveNumber());
					Fen testfen = new Fen(ncg);
					NormalChessGame tempgame = new NormalChessGame();
					tempgame.loadFEN(testfen);
					assertTrue(tempgame.canEnPassant());
//					tempgame.makeMove(m);
				};
			} catch (Exception e) {
				fail(where + " failed: " + e.getMessage());
			}
        	
        }
    }
    
    @Test
    public void testgame10() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game10));
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        game8 = gamelist.get(0).pgnToString();
        
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        return;
    }
    
    @Test
    public void testgame10b() {
        PGNParser parser = null;
        parser = new PGNParser(new StringReader(game10));
        ArrayList<PGNGame> gamelist = null;
        try {
            gamelist = parser.parsefile();
        } catch (ParseException e) {
            fail(e.getMessage());
        } catch (IllegalMoveException e) {
            fail(e.getMessage());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
        NormalChessGame ncg = new NormalChessGame();
        String where = "";
        
        for(moveIterator mi = gamelist.get(0).iterator() ; mi.hasNext();) {
        	david.logan.chess.support.Move m = mi.next();
        	try {
				ncg.makeMove(m);
				if(ncg.canEnPassant()) {
					System.out.println("Making move " + m);
					System.out.println("Yep, can en-passant on move " + ncg.moveNumber());
					Fen testfen = new Fen(ncg);
					NormalChessGame tempgame = new NormalChessGame();
					tempgame.loadFEN(testfen);
					assertTrue(tempgame.canEnPassant());
//					tempgame.makeMove(m);
				};
			} catch (Exception e) {
				fail(where + " failed: " + e.getMessage());
			}
        	
        }
    }
    
//    @Test
//    public void testreverse() throws Exception {
//        FileInputStream f;
//        PGNParser parser = null;
//        try {
//            f = new FileInputStream("/Users/davidlogan/Documents/icc/ICCLibrary/test/test.pgn");
//            parser = new PGNParser(f);
//        } catch (FileNotFoundException e) {
//            fail("Unable to find file");
//        }
//        ArrayList<PGNGame> gamelist = null;
//        try {
//            gamelist = parser.parsefile();
//        } catch (ParseException e) {
//            fail(e.getMessage());
//        }
//        
//        @SuppressWarnings("unused")
//        String reverse = gamelist.get(0).pgnToString();
//        return;
//    }
    String game3 = "[Event \"ICC 10 0\"]\n" +
"[Site \"Internet Chess Club\"]\n" +
"[Date \"2013.02.05\"]\n" +
"[Round \"-\"]\n" +
"[White \"amontice\"]\n" +
"[Black \"wethomas5200\"]\n" +
"[Result \"*\"]\n" +
"[ICCResult \"Game adjourned when White disconnected\"]\n" +
"[WhiteElo \"894\"]\n" +
"[BlackElo \"1011\"]\n" +
"[Opening \"Queen's pawn game, Darooha variation \"]\n" +
"[ECO \"D02\"]\n" +
"[NIC \"QP.08\"]\n" +
"[Time \"22:02:32\"]\n" +
"[TimeControl \"600+0\"]\n" +
"\n" +
"1. d4 {0:09:57} d5 {0:09:59} 2. Nf3 {0:09:49} Nf6 {0:09:58} 3. c3 {0:09:26}\n" +
"Nc6 {0:09:50} 4. e3 {0:09:14} e6 {0:09:47} 5. Be2 {0:09:09} Bd6 {0:09:44} 6.\n" +
"Nbd2 {0:09:01} O-O {0:09:40} 7. O-O {0:08:57} e5 {0:09:39} 8. b4 {0:08:47}\n" +
"exd4 {0:09:37} 9. exd4 {0:08:44} Re8 {0:09:34} 10. Re1 {0:08:39} Bf5\n" +
"{0:09:23} 11. Nh4 {0:08:17} Ne7 {0:09:10} 12. Nxf5 {0:08:01} Nxf5 {0:09:06}\n" +
"13. Bg4 {0:07:57} Nh6 {0:09:01} 14. Bf3 {0:07:30} Rxe1+ {0:08:48} 15. Qxe1\n" +
"{0:07:25} c6 {0:08:47} 16. Nb3 {0:07:20} Qc7 {0:08:44} 17. Bd2 {0:07:00}\n" +
"Bxh2+ {0:08:38} 18. Kf1 {0:06:42} Bf4 {0:08:25} 19. Qc1 {0:06:21} Bxd2\n" +
"{0:08:23} 20. Qxd2 {0:06:19} Re8 {0:08:18} 21. Re1 {0:06:17} Rxe1+ {0:08:15}\n" +
"22. Kxe1 {0:06:12} Qe7+ {0:08:11} 23. Kf1 {0:05:25} Ne4 {0:08:06} 24. Qe2\n" +
"{0:05:13} f5 {0:07:56} 25. Nc5 {0:05:05} b6 {0:07:52} 26. Nxe4 {0:04:57}\n" +
"fxe4 {0:07:48} 27. Bg4 {0:04:42} Nxg4 {0:07:39} 28. Qxg4 {0:04:40} g5\n" +
"{0:07:30} 29. a4 {0:04:07} Kg7 {0:07:29} 30. a5 {0:03:55} b5 {0:07:27} 31.\n" +
"g3 {0:03:49} h6 {0:07:21} 32. Qh5 {0:03:40} Qf6 {0:07:13} 33. Kg2 {0:03:37}\n" +
"Qg6 {0:06:49} 34. Qg4 {0:03:32} Qf7 {0:06:30} 35. a6 {0:03:20} Kf6 {0:06:10}\n" +
"36. f4 {0:02:51} Qe6 {0:05:54} 37. Qxe6+ {0:02:17} Kxe6 {0:05:52} 38. g4\n" +
"{0:02:11} gxf4 {0:05:48} 39. Kf2 {0:01:52} Kf6 {0:05:47} 40. Kg2 {0:01:32}\n" +
"Kg5 {0:05:45} 41. Kf2 {0:01:26} Kxg4 {0:05:44} 42. Ke2 {0:01:25} h5\n" +
"{0:05:43} 43. Kd2 {0:01:18} Kg3 {0:05:43} 44. c4 {0:01:15} h4 {0:05:37} 45.\n" +
"cxd5 {0:01:12} cxd5 {0:05:36} 46. Kc3 {0:01:07} h3 {0:05:34} 47. Kb3\n" +
"{0:01:05} h2 {0:05:33} 48. Ka3 {0:00:48} h1=Q {0:05:31} 49. Ka2 {0:00:38}\n" +
"Qc1 {0:05:25} 50. Kb3 {0:00:36} e3 {0:05:24} 51. Ka2 {0:00:32} e2 {0:05:23}\n" +
"52. Kb3 {0:00:30} e1=Q {0:05:19} {Game adjourned when White disconnected} *\n";
    String game4 = "[Event \"ICC 5 0\"]\n" +
"[Site \"Internet Chess Club\"]\n" +
"[Date \"2013.02.06\"]\n" +
"[Round \"-\"]\n" +
"[White \"benefico29\"]\n" +
"[Black \"lotswife\"]\n" +
"[Result \"*\"]\n" +
"[ICCResult \"Game adjourned when White disconnected\"]\n" +
"[WhiteElo \"1496\"]\n" +
"[BlackElo \"1421\"]\n" +
"[Opening \"Benoni defense\"]\n" +
"[ECO \"A56\"]\n" +
"[NIC \"OI.06\"]\n" +
"[Time \"10:38:11\"]\n" +
"[TimeControl \"300+0\"]\n" +
"\n" +
"1. d4 {0:04:58} Nf6 {0:04:58} 2. c4 {0:04:58} c5 {0:04:53} 3. Nc3 {0:04:58}\n" +
"cxd4 {0:04:51} 4. Qxd4 {0:04:56} Nc6 {0:04:50} 5. Qd1 {0:04:55} d6 {0:04:49}\n" +
"6. e3 {0:04:55} g6 {0:04:48} 7. Nf3 {0:04:55} Bg7 {0:04:46} 8. Be2 {0:04:55}\n" +
"Bg4 {0:04:36} 9. h3 {0:04:55} Bd7 {0:04:30} 10. a3 {0:04:53} a5 {0:04:26}\n" +
"{Game adjourned when White disconnected} *\n";
    
String game5 = "Event \"ICC 5 0\"]\n" +
            "[Site \"Internet Chess Club\"]\n" +
            "[Date \"2013.04.02\"]\n" +
            "[Round \"-\"]\n" +
            "[White \"Anjogui\"]\n" +
            "[Black \"jaiom\"]\n" +
            "[Result \"*\"]\n" +
            "[ICCResult \"Game adjourned when White disconnected\"]\n" +
            "[WhiteElo \"1227\"]\n" +
            "[BlackElo \"1117\"]\n" +
            "[Opening \"Sicilian defense\"]\n" +
            "[ECO \"B32\"]\n" +
            "[NIC \"SI.32\"]\n" +
            "[Time \"18:59:43\"]\n" +
            "[TimeControl \"300+0\"]\n" +
            "\n"+
            "1. e4 c5 2. Nf3 Nc6 3. d4 cxd4 4. Nxd4 Qb6 5. Nb3 Nf6 6. Be3 Nxe4 7. Bxb6\n" +
            "axb6 8. f3 Nf6 9. Bc4 e6 10. O-O Ra4 11. Bd3 Bd6 12. Nc3 Rh4 13. g3 Rh5 14.\n" +
            "Ne4 Nxe4 15. Bxe4 Be7 16. f4 O-O 17. Qxh5 g6 18. Qf3 Bf6 19. c3 d5 20. Bc2\n" +
            "d4 21. Be4 dxc3 22. Bxc6 cxb2 23. Rab1 bxc6 24. Qxc6 Ba6 25. Rf2 Rd8 26.\n" +
            "Rfxb2 Bd3 27. Rd1 b5 28. Rbd2 {Game adjourned when White disconnected} *";
String game6 = "[Event \"ICC 5 0\"]\n" +
"[Site \"Internet Chess Club\"]\n" +
"[Date \"2013.04.06\"]\n" +
"[Round \"-\"]\n" +
"[White \"oldchessmoves\"]\n" +
"[Black \"nels-1932\"]\n" +
"[Result \"*\"]\n" +
"[ICCResult \"Game adjourned when White disconnected\"]\n" +
"[WhiteElo \"1400\"]\n" +
"[BlackElo \"1253\"]\n" +
"[Opening \"QGA: Mannheim variation\"]\n" +
"[ECO \"D23\"]\n" +
"[NIC \"QP.08\"]\n" +
"[Time \"08:03:28\"]\n" +
"[TimeControl \"300+0\"]\n" +
"\n" +
"1. d4 d5 2. c4 Nf6 3. Nf3 dxc4 4. Qa4+ Bd7 5. Qxc4 e6 6. Bg5 Be7 7. Nc3 a6\n" +
"8. e4 b5 9. Qb3 Nc6 10. Bd3 Na5 11. Qc2 h6 12. Bf4 Rc8 13. Rd1 c5 14. O-O\n" +
"cxd4 15. Nxd4 b4 16. Bxa6 b3 17. axb3 Rc5 18. b4 Rh5 19. Be2 Rxh2 20. Kxh2\n" +
"O-O 21. bxa5 Qa8 22. a6 Bc5 23. Nf3 Qa7 24. Ne5 Be8 25. b4 Bb6 26. b5 Nh7\n" +
"27. Nc6 Qa8 28. Ne7+ Kh8 29. Bf3 Nf6 {Game adjourned when White\n" +
"disconnected} *";

String game7 = "Event \"ICC 15 10\"]\n" +
                "[Site \"Internet Chess Club\"]\n" +
                "[Date \"2013.04.30\"]\n" +
                "[Round \"-\"]\n" +
                "[White \"Rimawis\"]\n" +
                "[Black \"vampiro\"]\n" +
                "[Result \"1-0\"]\n" +
                "[ICCResult \"Black resigns\"]\n" +
                "[WhiteElo \"1831\"]\n" +
                "[BlackElo \"1393\"]\n" +
                "[Opening \"Philidor: Nimzovich (Jaenisch) variation\"]\n" +
                "[ECO \"C41\"]\n" +
                "[NIC \"KP.08\"]\n" +
                "[Time \"11:21:33\"]\n" +
                "[TimeControl \"900+10\"]\n" +
                        "\n" +
                "1. e4 e5 2. Nf3 d6 3. d4 Nf6 4. Nc3 Bg4 5. dxe5 dxe5 6. Qxd8+ Kxd8 7. Nxe5\n" +
                "Be6 8. Bg5 h6 9. O-O-O+ Ke8 10. Bxf6 gxf6 11. Nd3 c6 12. f4 Bd6 13. g3 a5\n" +
                "14. Bg2 Ke7 15. Rhe1 b5 16. Nd5+ cxd5 17. exd5 {Black resigns} 1-0";

String game8 = "[Event \"ICC 1 0\"]\n" +
        "[Site \"Internet Chess Club\"]\n" +
                "[Date \"2013.05.14\"]\n" +
                "[Round \"-\"]\n" +
                "[White \"DanMason\"]\n" +
                "[Black \"Pawnwreck\"]\n" +
                "[Result \"0-1\"]\n" +
                "[ICCResult \"White forfeits on time\"]\n" +
                "[WhiteElo \"1727\"]\n" +
                "[BlackElo \"1831\"]\n" +
                "[Opening \"Sicilian: Grand Prix attack\"]\n" +
                "[ECO \"B21\"]\n" +
                "[NIC \"SI.48\"]\n" +
                "[Time \"22:44:17\"]\n" +
                "[TimeControl \"60+0\"]\n" +
                        "\n" +
                "1. e4 c5 2. f4 Nc6 3. Nf3 g6 4. c3 Bg7 5. e5 d6 6. d4 dxe5 7. d5 e4 8. dxc6\n" +
                "exf3 9. Qxd8+ Kxd8 10. Be3 c4 11. Na3 fxg2 12. Bxg2 Rb8 13. O-O-O+ Kc7 14.\n" +
                "f5 bxc6 15. Bf4+ e5 16. Bg3 f6 17. Nxc4 Ne7 18. Nd6 Rd8 19. Nf7 Rxd1+ 20.\n" +
                "Rxd1 Nxf5 21. Bf2 Be6 22. Bxa7 Rb7 23. Bc5 Bxf7 24. a4 Kc8 25. b4 Bd5 26.\n" +
                "Bh3 Kb8 27. a5 Rb5 28. Bf1 Rb7 29. a6 Rd7 30. a7+ Ka8 31. Ba6 Nd6 32. c4\n" +
                "Nxc4 33. Bxc4 Rc7 34. Bxd5 cxd5 35. Rxd5 f5 36. Rd8+ Kb7\n" +
                "{White forfeits on time}\n" +
                "0-1";
String game9 =  "[Event \"ICC 1 0\"]\n" +
				"[Site \"Internet Chess Club\"]\n" +
				"[Date \"2014.09.25\"]\n" +
				"[Round \"-\"]\n" +
				"[White \"zagor\"]\n" +
				"[Black \"dainiusk\"]\n" +
				"[Result \"0-1\"]\n" +
				"[ICCResult \"White checkmated\"]\n" +
				"[WhiteElo \"1777\"]\n" +
				"[BlackElo \"1793\"]\n" +
				"[Opening \"Modern defense: Averbakh system, Kotov variation\"]\n" +
				"[ECO \"A40\"]\n" +
				"[NIC \"KF.03\"]\n" +
				"[Time \"04:24:44\"]\n" +
				"[TimeControl \"60+0\"]\n"+
				"\n" +
				"1. d4 {0:00:57} g6 {0:01:00} 2. c4 {0:00:56} Bg7 {0:01:00} 3. Nc3 {0:00:56}\n" +
				"d6 {0:00:59} 4. e4 {0:00:56} Nc6 {0:00:59} 5. f3 {0:00:55} Nxd4 {0:00:58} 6.\n" +
				"Nge2 {0:00:53} Nxe2 {0:00:57} 7. Bxe2 {0:00:52} Bd7 {0:00:57} 8. Bd2\n"+
				"{0:00:51} e5 {0:00:56} 9. Be3 {0:00:51} Ne7 {0:00:56} 10. Qd2 {0:00:51} Nc6\n" +
				"{0:00:56} 11. h4 {0:00:50} h5 {0:00:55} 12. O-O-O {0:00:49} Qc8 {0:00:54}\n" +
				"13. Nd5 {0:00:48} Ne7 {0:00:53} 14. Kb1 {0:00:47} Nxd5 {0:00:52} 15. cxd5\n" +
				"{0:00:47} b6 {0:00:49} 16. Rc1 {0:00:47} Qd8 {0:00:48} 17. Rc2 {0:00:47} Rc8\n" +
				"{0:00:47} 18. Rhc1 {0:00:46} a5 {0:00:47} 19. Ba6 {0:00:45} c5 {0:00:43} 20.\n" +
				"Bxc8 {0:00:42} Qxc8 {0:00:42} 21. a3 {0:00:38} f5 {0:00:41} 22. b4 {0:00:37}\n" +
				"axb4 {0:00:40} 23. axb4 {0:00:37} f4 {0:00:39} 24. Bf2 {0:00:35} Kf7\n" +
				"{0:00:37} 25. bxc5 {0:00:35} bxc5 {0:00:36} 26. Rb2 {0:00:34} Qa8 {0:00:34}\n" +
				"27. Ra2 {0:00:31} Rb8+ {0:00:33} 28. Ka1 {0:00:30} Qb7 {0:00:31} 29. Rb1\n" +
				"{0:00:29} Qxb1# {0:00:30} {White checkmated} 0-1\n";

String game10 = "[Event \"ICC 1 1\"]\n" +
"[Site \"Internet Chess Club\"]\n" +
"[Date \"2014.12.20\"]\n" +
"[Round \"-\"]\n" +
"[White \"Nadtkins\"]\n" +
"[Black \"StrongBach\"]\n" +
"[Result \"1-0\"]\n" +
"[ICCResult \"Black checkmated\"]\n" +
"[WhiteElo \"1989\"]\n" +
"[BlackElo \"1896\"]\n" +
"[Opening \"Queen's pawn: Chigorin variation\"]\n" +
"[ECO \"D00\"]\n" +
"[NIC \"QP.08\"]\n" +
"[Time \"12:53:38\"]\n" +
"[TimeControl \"60+1\"]\n" +
"\n" +
"1. d4 {0:00:59} d5 {0:01:01} 2. Nc3 {0:01:00} Qd6 {0:01:02} 3. g3 {0:00:59}\n" +
"Bf5 {0:01:03} 4. a3 {0:00:58} Qd7 {0:01:04} 5. Bf4 {0:00:54} h6 {0:01:04} 6.\n" +
"Bg2 {0:00:54} e6 {0:01:05} 7. h4 {0:00:55} Bd6 {0:01:06} 8. e3 {0:00:53} Ne7\n" +
"{0:01:07} 9. Nge2 {0:00:53} Rg8 {0:01:08} 10. Qd2 {0:00:53} Na6 {0:01:09}\n" +
"11. O-O-O {0:00:53} Ng6 {0:01:10} 12. Kb1 {0:00:53} Nxf4 {0:01:11} 13. exf4\n" +
"{0:00:53} O-O-O {0:01:12} 14. Nc1 {0:00:50} Nb8 {0:01:13} 15. Nb3 {0:00:50}\n" +
"Qe7 {0:01:14} 16. Rhe1 {0:00:46} Qf6 {0:01:15} 17. Bf1 {0:00:45} Bg4\n" +
"{0:01:16} 18. Be2 {0:00:45} Bf5 {0:01:17} 19. Bd3 {0:00:44} Bg4 {0:01:18}\n" +
"20. Be2 {0:00:45} Bf5 {0:01:19} 21. Na4 {0:00:42} Nc6 {0:01:20} 22. Nac5\n" +
"{0:00:42} Rh8 {0:01:21} 23. Ka2 {0:00:40} Rhf8 {0:01:22} 24. c3 {0:00:39}\n" +
"Qe7 {0:01:23} 25. Nd3 {0:00:39} Be4 {0:01:24} 26. f3 {0:00:39} Bf5 {0:01:25}\n" +
"27. Bf1 {0:00:39} Rfe8 {0:01:26} 28. Ne5 {0:00:38} Nb8 {0:01:27} 29. Nc1\n" +
"{0:00:32} f6 {0:01:28} 30. Ned3 {0:00:31} Nc6 {0:01:29} 31. Re3 {0:00:28}\n" +
"Qf7 {0:01:30} 32. Rde1 {0:00:28} g5 {0:01:31} 33. fxg5 {0:00:27} Bxg3\n" +
"{0:01:32} 34. gxf6 {0:00:20} Bxe1 {0:01:33} 35. Qxe1 {0:00:19} Qxf6\n" +
"{0:01:34} 36. Nc5 {0:00:19} Qf7 {0:01:35} 37. Bb5 {0:00:15} Rd6 {0:01:36}\n" +
"38. Qd1 {0:00:12} Qh5 {0:01:37} 39. Qa4 {0:00:11} Qxh4 {0:01:38} 40. Nxb7\n" +
"{0:00:09} Kxb7 {0:01:39} 41. Qa6+ {0:00:10} Kb8 {0:01:40} 42. Bxc6 {0:00:10}\n" +
"Rxc6 {0:01:41} 43. Qxc6 {0:00:10} Qe7 {0:01:42} 44. Nb3 {0:00:09} Bg6\n" +
"{0:01:43} 45. Nc5 {0:00:09} Bb1+ {0:01:44} 46. Kxb1 {0:00:09} Qh7+ {0:01:45}\n" +
"47. Ka2 {0:00:09} Qb1+ {0:01:46} 48. Kxb1 {0:00:10} Rd8 {0:01:47} 49. Qb7#\n" +
"{0:00:10} {Black checkmated} 1-0\n";

}
