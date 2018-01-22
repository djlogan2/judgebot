package chessclub.com.enums;

import chessclub.com.icc.l2.Seek;

/**
 * The provisional status of a player.
 * @see Seek
 * @author David Logan
 *
 */
public enum ProvisionalStatus {
    /**
     * No games have been played by this player yet.
     */
    NO_GAMES,
    /**
     * The user is in provisional status.
     */
    PROVISIONAL,
    /**
     * The user has played enough games to have an established rating.
     */
    ESTABLISHED;
    /**
     * Takes the numeric value from the {@link Seek} packet and returns the enumeration.
     * @param i The numeric value from the {@link Seek} packet
     * @return  The enumeration.
     */
    public static ProvisionalStatus getStatus(final int i) {
        for (ProvisionalStatus p : ProvisionalStatus.values()) {
            if (p.ordinal() == i) {
                return p;
            }
        }
        return null;
    }
}
