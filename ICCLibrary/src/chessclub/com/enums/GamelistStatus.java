package chessclub.com.enums;

/**
 * An enumeration for game status.
 * @author David Logan
 *
 */
public enum GamelistStatus {
    /**
     * The game was a win for a player.
     */
    WIN,
    /**
     * The game was a draw for a player.
     */
    DRAW,
    /**
     * The game was adjourned for some reason.
     */
    ADJOURNED,
    /**
     * The game was aborted for some reason.
     */
    ABORT;
    /**
     * Given a number from a packet, returns the correct enumeration.
     * @param i A game status ID
     * @return  The associated enumerated value.
     */
    public static GamelistStatus getStatus(final int i) {
        for (GamelistStatus s : GamelistStatus.values()) {
            if (s.ordinal() == i) {
                return s;
            }
        }
        return null;
    }
}
