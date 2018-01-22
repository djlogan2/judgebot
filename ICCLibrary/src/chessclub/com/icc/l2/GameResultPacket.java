package chessclub.com.icc.l2;

import david.logan.chess.support.GameScore;

/**
 * The class that contains data from the DG_GAME_RESULT packet when an active game (gamenumber) ends.
 * @author David Logan
 *
 */
public class GameResultPacket extends Level2Packet {
    // 13 DG_GAME_RESULT
    // Form: (gamenumber become-examined game_result_code score_string2 description-string ECO)
    /**
     * The game number.
     * @return  The game number.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * True if this user is expected to go right into examining the game. 
     * @return  True if this user is expected to go right into examining the game.
     */
    public boolean becomesexamined() {
        return "1".equals(getParm(2));
    }

    /**
     * The result of the game.
     * @see GameResult
     * @return  A GameResult
     */
    public GameResult gameresult() {
        return GameResult.getResult(getParm(3));
    }

    /**
     * Another type of abbreviated result.
     * @see GameScore
     * @return  The GameScore enumeration.
     */
    public GameScore gamescore() {
        return GameScore.getScore(getParm(4));
    }

    /**
     * An english description of the end of game result. e.g. "White Checkmated".
     * @return  An english description of the end of game result.
     */
    public String description() {
        return getParm(5);
    }

    /**
     * The ECO code for this game.
     * @return The ECO code for this game.
     */
    public String eco() {
        return getParm(6);
    }
}
