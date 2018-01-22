package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.Titles;

/**
 * The instance returned after user logon.
 * @author David Logan
 *
 */
public class WhoAmI extends Level2Packet {

    /**
     * Our own userid.
     * @return Our own userid.
     */
    public String userid() {
        return getParm(1);
    }

    /**
     * Our own titles.
     * @return Our own titles.
     */
    public Titles titles() {
        return new Titles(getParm(2));
    }
}
