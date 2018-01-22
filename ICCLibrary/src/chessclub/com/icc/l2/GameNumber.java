package chessclub.com.icc.l2;


/**
 * Class to return Game Number items from ICC packets.
 * @author David Logan
 */
public class GameNumber extends Level2Packet {
    /**
     * Returns the game number.
     * @return  The game number.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }
}
