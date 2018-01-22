package chessclub.com.icc.l1;

import chessclub.com.enums.CN;


/**
 * For returning error strings from level 1 packets. I may not leave this this way.
 * <p>But for right now, the intent is that the handler for issuing a command will get this error packet
 * If there is an error in returned from the command.
 * <p>This also doesn't happen for many cases right now. I am also working to expand how command returns
 * are handled, which is why I may not be using this class much longer.
 * @author David Logan
 *
 */
public class L1ErrorOnly extends Level1Packet {
    /**
     * True if an error message was discovered.
     * @return  true or false
     */
    public boolean hasError() {
        if(this.getType() == CN.SET) {
            if("I can't assign that value to that variable.".equals(getParm(1)))
                return true;
            else
                return false;
        }
        if (numberParms() <= 1 || getParm(1).length() == 0) {
            return false;
        }
        if(getParm(1).startsWith("Sending takeback ")) {
            return false;
        }
        if(getParm(1).contains(" taking back ")) {
            return false;
        }
        if (getParm(1).startsWith("(told ") && getParm(1).endsWith(" people)")) {
            return false;
        }
        if (getParm(1).startsWith("You are no longer examining game ")) {
            return false;
        }
        if (getParm(1).startsWith("You already have admin status.")) {
            return false;
        }
        if (getParm(1).startsWith("You now have admin status.")) {
            return false;
        }
        if (getParm(1).startsWith("You no longer have admin status.")) {
            return false;
        }
        return true;
    }

    /**
     * The english error text.
     * @return  The english error text.
     */
    public String errortext() {
        return getParm(1);
    }
}
