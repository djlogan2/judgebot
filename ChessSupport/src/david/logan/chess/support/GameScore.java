package david.logan.chess.support;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * An enumeration for game scores (1-0, 0-1, etc).
 * @author David Logan
 *
 */
public enum GameScore {
    /**
     * White wins.
     */
    WHITE_WINS("1-0"),
    /**
     * Black wins.
     */
    BLACK_WINS("0-1"),
    /**
     * The game ended in a draw.
     */
    DRAW("1/2-1/2"),
    /**
     * The game was aborted (no rating change).
     */
    ABORTED("aborted"),
    /**
     * The game was adjourned.
     */
    ADJOURNED("*");
    private static final Logger LOG = LogManager.getLogger(GameScore.class);
    private String scoreString;

    private GameScore(final String pScoreString) {
        this.scoreString = pScoreString;
    }

    /**
     * Converts a game score string into its associated enumeration.
     * <table>
     * <tr><td><i>WHITE_WINS</i></td><td><b>"1-0"</b></td></tr>
     * <tr><td><i>BLACK_WINS</i></td><td><b>"0-1"</b></td></tr>
     * <tr><td><i>DRAW</i></td><td><b>"1/2-1/2"</b></td></tr>
     * <tr><td><i>ABORTED</i></td><td><b>"aborted"</b></td></tr>
     * <tr><td><i>ADJOURNED</i></td><td><b>"*"</b></td></tr>
     * </table>
     * @param scoreString   The game score string.
     * @return  The enumeration.
     */
    public static GameScore getScore(final String scoreString) {
        for (GameScore s : GameScore.values()) {
            if (s.scoreString.equals(scoreString)) {
                return s;
            }
        }
        LOG.error("GameScore unknown score string: " + scoreString);
        return null;
    }
}
