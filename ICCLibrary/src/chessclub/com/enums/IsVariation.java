package chessclub.com.enums;

/**
 * The variation enumeration for the move list packet data.
 * @author David Logan
 *
 */
public enum IsVariation {
    /**
     * Main line, part of a DG_POSITION_BEGIN (either play or examine mode).
     */
    MOVE_INITIAL,
    /**
     * Main line, play mode.
     */
    MOVE_PLAYED,
    /**
     * Main line, examine mode, the result of "forward 1", for example.
     */
    MOVE_FORWARD,
    /**
     * A move in examine mode, make what of it you will.
     */
    MOVE_EXAMINE,
    /**
     * A move in examine mode, intended to overwrite a previous line. (Not implemented yet).
     */
    MOVE_OVERWRITE,
    /**
     * A move in examine mode, intended to be the new main line. (Not implemented yet).
     */
    MOVE_MAINLINE,
    /**
     * A move in examine mode, intended to be a side line. (Not implemented yet).
     */
    MOVE_SIDELINE; 
    
    /**
     * Converts an integer variation value into its associated enumeration.
     * @param i The numeric value from the packet.
     * @return  The enumeration.
     */
    public static IsVariation getIsVariation(final int i) {
        for (IsVariation v : IsVariation.values()) {
            if (v.ordinal() == i) {
                return v;
            }
        }
        return null;
    }
}
