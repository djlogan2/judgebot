package chessclub.com.enums;

import chessclub.com.icc.l2.Seek;

/**
 * The reason a seek was removed.
 * @author David Logan
 * @see Seek
 *
 */
public enum SeekRemovedReason {
    /**
     * This should never be returned.
     */
    UNKNOWN,
    /**
     * The person issuing the seek left.
     */
    LEFT,
    /**
     * The person issuing the seek is now playing.
     */
    PLAYING,
    /**
     * The person issuing the seek removed the ad.
     */
    REMOVED_AD,
    /**
     * The person issuing the seek replaced the ad.
     */
    REPLACED_AD,
    /**
     * The user issuing the seek is now unavailable (catch all reason).
     */
    NOT_AVAILABLE;

    /**
     * Converts an integer reason into an enumeration.
     * @param i The integer value.
     * @return  The enumeration.
     */
    public static SeekRemovedReason getReason(final int i) {
        for (SeekRemovedReason r : SeekRemovedReason.values()) {
            if (r.ordinal() == i) {
                return r;
            }
        }
        return UNKNOWN;
    }
};
