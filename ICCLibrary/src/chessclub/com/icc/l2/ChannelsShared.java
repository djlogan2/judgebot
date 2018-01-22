package chessclub.com.icc.l2;


/**
 * 
 * @author David Logan
 *  <p> 
 * This is the level 2 packet when the library get a "channels shared" datagram.
 * The name is the user of which we are sharing the channels, and channels() is the integer list of channels we are sharing.
 *
 */
public class ChannelsShared extends Level2Packet {
    /**
     * Returns the name of the user with which you are sharing channels.
     * @return  The name of the user with which you are sharing channels.
     */
    public final String name() {
        return getParm(1);
    }

    /**
     * Returns the complete list of channels that you are sharing with the named ICC account.
     * @return  The complete list of channels that you are sharing with the named ICC account.
     */
    public final int[] channels() {
        int[] c = new int[numberParms() - 2];
        for (int x = 2; x < numberParms(); x++) {
            c[x - 2] = Integer.parseInt(getParm(x));
        }
        return c;
    }
}
