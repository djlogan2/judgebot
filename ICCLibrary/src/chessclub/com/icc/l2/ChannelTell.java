package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.Titles;

/**
 * 
 * @author David Logan
 *  <p> 
 * This is the level 2 packet for incoming ICC channel tells.
 *
 */
public class ChannelTell extends Level2Packet {
    // 28 DG_CHANNEL_TELL
    // The level 2 version of a channel tell. If this is on, the
    // normal form is inhibited.
    //
    // Form: (channel playername titles ^Y{tell string^Y} type)
    /**
     * Returns the channel that the tell was sent to.
     * @return  The channel that the tell was sent to
     */
    public final int channel() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * Returns the name of the ICC account that sent this channel tell.
     * @return  The name of the ICC account that sent this channel tell
     */
    public final String name() {
        return getParm(2);
    }

    /**
     * Returns the {@link Titles} object for the ICC account that sent this channel tell.
     * @return  The {@link Titles} object for the ICC account that sent this channel tell.
     */
    public final Titles titles() {
        return new Titles(getParm(3));
    }

    /**
     * Returns the channel tell text sent by the ICC account.
     * @return  The channel tell text sent by the ICC account.
     */
    public final String text() {
        return getParm(4);
    }

    /**
     * Returns true if this was sent to the channel as a normal tell as opposed to an "atell".
     * @return  <b>true</b> if the channel tell was sent as a normal tell. <b>false</b> if the channel tell was sent as an atell.
     */
    public final boolean normaltell() {
        return "1".equals(getParm(5));
    }

    /**
     * Returns true if this was sent to the channel as a an atell as opposed to a normal tell.
     * @return  <b>true</b> if the channel tell was sent as an atell. <b>false</b> if the channel tell was sent as a normal tell.
     */
    public final boolean atell() {
        return "4".equals(getParm(5));
    }
}
