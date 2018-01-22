package chessclub.com.icc.l2;

import david.logan.chess.support.GameScore;

/**
 * The instance created when a tournament game ends.
 * @author David Logan
 *
 */
public class TourneyGameEnded extends Level2Packet {
    /**
     * The event this game is connected to.
     * @return The event this game is connected to.
     */
    public String eventLabel() {
        return getParm(1);
    }

    /**
     * The white player.
     * @return The white player.
     */
    public String white() {
        return getParm(2);
    }

    /**
     * The black player.
     * @return The black player.
     */
    public String black() {
        return getParm(3);
    }

    /**
     * The game id (presumably).
     * @return The game id (presumably)
     */
    public int id() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * The {@link GameScore}.
     * @return The {@link GameScore}
     */
    public GameScore result() {
        return GameScore.getScore(getParm(5));
    }

    /**
     * The text version of the result.
     * @return The text version of the result.
     */
    public String resultString() {
        return getParm(5);
    }
}
