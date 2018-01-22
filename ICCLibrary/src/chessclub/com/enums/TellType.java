package chessclub.com.enums;

import chessclub.com.icc.l2.PersonalTell;

/**
 * The type of tell in a {@link PersonalTell} instance.
 * @author David Logan
 *
 */
public enum TellType {
    // The type is 0 for "say", 1 for "tell", 2 for "ptell" (from bughouse
    // partner), 3 would be qtell if we sent qtells this way (which
    // we don't, they go with DG_PERSONAL_QTELL), and 4 for "atell"
    // (from admin or helper).
    /**
     * A "say" in a game.
     */
    SAY,
    /**
     * A regular tell.
     */
    TELL,
    /**
     * A bughouse "ptell" type of tell.
     */
    PTELL,
    /**
     * A tournament "qtell" type of tell.
     */
    QTELL,
    /**
     * A helper "atell" type of tell.
     */
    ATELL;
    /**
     * Converts the integer value from the data packet to the appropriate enumeration.
     * @param i The integer data from the packet.
     * @return  The enumeration.
     */
    public static TellType getTellType(final int i) {
        for (TellType t : TellType.values()) {
            if (t.ordinal() == i) {
                return t;
            }
        }
        return null;
    }
}
