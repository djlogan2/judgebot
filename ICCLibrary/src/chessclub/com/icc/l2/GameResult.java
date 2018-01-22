package chessclub.com.icc.l2;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Returns an enumerated game result for those cases when ICC returns a result string.
 * @author David Logan
 *
 */
public enum GameResult {
    /**
     * White wins the game (1-0).
     */
    WHITE_WINS("1-0"),
    /**
     * Black wins the game (0-1).
     */
    BLACK_WINS("0-1"),
    /**
     * Both players draw (1/2).
     */
    DRAW("1/2"),
    /**
     * Draw by the 50 move rule (50).
     */
    DRAW_BY_50("50"),
    /**
     * Unknown game result (?).
     */
    UNKNOWN("?"),
    /**
     * Game has been adjudicated by an ICC administrator (Adj).
     */
    ADJUDICATED("Adj"),
    /**
     * Draw agreed by both players (Agr).
     */
    DRAW_AGREED("Agr"),
    /**
     * Game courtesy aborted by black (BA).
     */
    BLACK_COURTESYABORT("BA"),
    /**
     * Game ended when black quit (BQ).
     */
    BLACK_QUIT("BQ"),
    /**
     * Game ended when white quit (WQ).
     */
    WHITE_QUIT("WQ"),
    /**
     * Game ended when loser was flagged (Fla).
     */
    FLAGGED("Fla"),
    /**
     * Game ended when user was checkmated (Mat).
     */
    CHECKMATE("Mat"),
    /**
     * Game ended in draw because there was no more mating material (NM).
     */
    NO_MATING_MATERIAL("NM"),
    /**
     * Game ended in a draw because TODO ??.
     */
    DRAW_BY_NO_TIME("NT"),
    /**
     * Draw by three position repetition rule (Rep).
     */
    DRAW_BY_REPETITION("Rep"),
    /**
     * Game won due to the loser resigning (Res).
     */
    RESIGNED("Res"),
    /**
     * Game aborted when the system was shut down with the game in progress.
     */
    ABORT_BY_SYSTEM_SHUTDOWN("SD"),
    /**
     * The game was aborted because there were not enough moves to declare a winner.
     */
    ABORT_TOO_SHORT("Sho"),
    /**
     * Draw due to a stalemate.
     */
    DRAW_BY_STALEMATE("Sta"),
    /**
     * Draw because one person ran out of time but the other player had insufficient mating material.
     */
    DRAW_BY_NO_MATE_MATERIAL("TM");
    private static final Logger LOG = LogManager.getLogger(GameResult.class);
    private String resultString;

    private GameResult(final String pResultString) {
        this.resultString = pResultString;
    }

    /**
     * Takes one of the result strings from ICC and returns one of the enumerated values.
     * @param resultString  The result string from ICC
     * @return  The enumerated value.
     */
    public static GameResult getResult(final String resultString) {
        for (GameResult r : GameResult.values()) {
            if (r.resultString.equals(resultString)) {
                return r;
            }
        }
        LOG.error("GameResult unknown result: " + resultString);
        return null;
    }
}
