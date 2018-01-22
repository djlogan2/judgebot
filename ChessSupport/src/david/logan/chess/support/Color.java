package david.logan.chess.support;

/**
 * 
 * @author David Logan
 * <p>
 * Simple enumeration to enumerate a chess piece color and a "to move" color. The colors can be WHITE, BLACK or NONE.
 *
 */
public enum Color {
    /**
     * No color.
     */
    NONE,
    /**
     * Black.
     */
    BLACK,
    /**
     * White.
     */
    WHITE;
    /**
     * Returns the enumerated color from the ordinal.
     * @param ord   The ordinal (0-2)
     * @return      The enumerated color.
     */
    public static Color getColor(final int ord) {
        for (Color c : Color.values()) {
            if (c.ordinal() == ord) {
                return c;
            }
        }
        return null;
    }

    /**
     * Returns the opposite color passed in.
     * @param c The current color
     * @return  The opposite color
     */
    public static Color getOpposite(final Color c) {
        if (c == WHITE) {
            return BLACK;
        } else {
            return WHITE;
        }
    }

    public Color other() {
        if(this == Color.WHITE)
            return Color.BLACK;
        else
            return Color.WHITE;
    }
}
