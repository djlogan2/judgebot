package chessclub.com.icc.l2;

import chessclub.com.enums.SeekRemovedReason;

/**
 * Instance returned from the framework when a seek is removed from the list.
 * @author David Logan
 *
 */
public class SeekRemoved extends Level2Packet {
    /**
     * This is the index of the seek.
     * @return The index of the seek.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The {@link SeekRemovedReason} the seek was removed.
     * @return  The {@link SeekRemovedReason} the seek was removed. 
     */
    public SeekRemovedReason reason() {
        return SeekRemovedReason.getReason(Integer.parseInt(getParm(2)));
    }
}
