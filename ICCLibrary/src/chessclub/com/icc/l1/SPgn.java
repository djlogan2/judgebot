package chessclub.com.icc.l1;

import java.io.StringReader;

import david.logan.chess.support.IllegalMoveException;
import david.logan.chess.support.PGNGame;
import david.logan.chess.support.pgn.parser.PGNParser;
import david.logan.chess.support.pgn.parser.ParseException;

public class SPgn extends Level1Packet {
    private PGNGame game;
    @Override
    protected void initialize(final String pPacket) {
        super.initialize(pPacket);
        PGNParser parser = new PGNParser(new StringReader(pPacket.split("(\r\n)+", 2)[1]));
        try {
            game = parser.pgngame();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public PGNGame getGame() { return game; }
}
