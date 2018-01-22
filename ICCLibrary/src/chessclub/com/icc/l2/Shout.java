package chessclub.com.icc.l2;

import chessclub.com.enums.ShoutType;
import chessclub.com.icc.l1l2.Titles;

/**
 * The instance returned from the framework when a shout is received.
 * @author David Logan
 *
 */
public class Shout extends Level2Packet {
    // 32 DG_SHOUT
    // The level 2 form of shout, sshout and announce. Inhibits
    // normal reception of these.
    //
    // Form: (playername titles type ^Y{shout string^Y})
    /**
     * The name of the user that issued the shout.
     * @return The name of the user that issued the shout.
     */
    public String name() {
        return getParm(1);
    }

    /**
     * The {@link Titles} held by the person that issued the shout.
     * @return The {@link Titles} held by the person that issued the shout.
     */
    public Titles titles() {
        return new Titles(getParm(2));
    }

    // The type is 0 for regular shouts, 1 for "I" type shouts, 2
    // for sshouts and 3 for announcements.
    /**
     * The {@link ShoutType} of the shout.
     * @return The {@link ShoutType} of the shout.
     */
    public ShoutType shouttype() {
        return ShoutType.getType(Integer.parseInt(getParm(3)));
    }

    /**
     * true if the type of shout is just a regular "shout xxx" type of shout.
     * @return true if the type of shout is just a regular "shout xxx" type of shout.
     */
    public boolean shout() {
        return shouttype() == ShoutType.SHOUT;
    }

    /**
     * true if the type of shout is an "i xxx" type of shout.
     * @return true if the type of shout is an "i xxx" type of shout.
     */
    public boolean i() {
        return shouttype() == ShoutType.I;
    }

    /**
     * true if the type of shout is an "sshout xxx" type of shout.
     * @return true if the type of shout is an "sshout xxx" type of shout.
     */
    public boolean sshout() {
        return shouttype() == ShoutType.SSHOUT;
    }

    /**
     * true if the shout type is an ICC announcement type of shout.
     * @return true if the shout type is an ICC announcement type of shout.
     */
    public boolean announcement() {
        return shouttype() == ShoutType.ANNOUNCEMENT;
    }

    /**
     * The text of the shout.
     * @return The text of the shout.
     */
    public String text() {
        return getParm(4);
    }
}
