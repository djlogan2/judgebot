package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.Titles;

/**
 * 
 * @author David Logan
 *  <p> 
 * This is the Level 2 packet for incoming ICC qtells
 *
 */
public class ChannelQTell extends Level2Packet {
    // 82 DG_CHANNEL_QTELL
    // (channel name titles ^Y{qtell-string^Y})
    /**
     * Returns the channel that this qtell was sent to.
     * @return  Returns the channel that this qtell was sent to.
     */
    public final int channel() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * Returns the name of the ICC account that sent the qtell to the channel.
     * @return  The name of the ICC account that sent the qtell to the channel.
     */
    public final String name() {
        return getParm(2);
    }

    /**
     * Returns the {@link Titles} object associated with the ICC account that sent the qtell to the channel.
     * @return  The {@link Titles} object associated with the ICC account that sent the qtell to the channel.
     */
    public final Titles titles() {
        return new Titles(getParm(3));
    }

    /**
     * Returns the qtell text that was sent to the channel.
     * @return  The qtell text that was sent to the channel.
     */
    public final String text() {
        return getParm(4);
    }
}
