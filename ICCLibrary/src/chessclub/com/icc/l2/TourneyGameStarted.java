package chessclub.com.icc.l2;


/**
 * Instance created when a tournament game is started.
 * <p>The server assumes it's a tournament game if the tourney variable is not null.
 * @author David Logan
 *
 */
public class TourneyGameStarted extends Level2Packet {
    /**
     * The event label--in other words, the contents of the "tourney" variable.
     * @return The event label--in other words, the contents of the "tourney" variable.
     */
    public String eventlabel() {
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
     * The tournament id.
     * @return The tournament id.
     */
    public int id() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * The ICC game number.
     * @return The ICC game number.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(5));
    }
}
