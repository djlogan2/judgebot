package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.Titles;

/**
 * 
 * @author David Logan
 *  <p> 
 * This is the Level 2 packet for incoming changes in titles (DG_TITLES)
 *
 */
public class TitleChange extends Level2Packet {
    // 89 DG_TITLES
    // (playername titles)
    /**
     * Returns the player whos title changed
     * @return  Returns the playername whos title changed. (Usually an admin, but could be others)
     */
    public final String name() {
        return getParm(1);
    }

    /**
     * Returns the new titles
     * @return  The new titles assigned to the player
     */
    public final Titles titles() {
        return new Titles(getParm(2));
    }
}
