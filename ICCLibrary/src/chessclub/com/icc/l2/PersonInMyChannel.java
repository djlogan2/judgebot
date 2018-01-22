package chessclub.com.icc.l2;


/**
 * Instance created when a person enters or leaves a channel that this user is in.
 * @author David Logan
 *
 */
public class PersonInMyChannel extends Level2Packet {
    /**
     * The channel number that some other ICC member has entered or left.
     * @return  The channel number that some other ICC member has entered or left.
     */
    public int channel() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The user that entered or left the channel.
     * @return  The user that entered or left the channel.
     */
    public String name() {
        return getParm(2);
    }

    /**
     * true if they entered, false if they left.
     * @return  true if they entered, false if they left.
     */
    public boolean entered() {
        return "1".equals(getParm(3));
    }
}
