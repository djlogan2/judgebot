package chessclub.com.enums;

import chessclub.com.icc.l2.Shout;

/**
 * The type of shout.
 * @author David Logan
 * @see Shout
 */
public enum ShoutType {
    /**
     * A regular shout.
     */
    SHOUT,
    /**
     * And "i" type shout.
     */
    I,
    /**
     * An sshout type of shout.
     */
    SSHOUT,
    /**
     * An ICC announcement type of shout.
     */
    ANNOUNCEMENT;
    
    /**
     * Take an integer value from an incoming level 2 packet and converts it to the proper enumeration.
     * @param i The integer value to convert.
     * @return  The enumeration.
     */
    public static ShoutType getType(final int i) {
        for (ShoutType t : ShoutType.values()) {
            if (t.ordinal() == i) {
                return t;
            }
        }
        return null;
    }
}
