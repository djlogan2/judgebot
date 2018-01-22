package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * A packet that gets returned from the framework when a user gets logged out.
 * @author David Logan
 *
 */
public class LoggedOut extends Level1Packet {
    // idjit has arrived. You can't both be logged in. Sorry.
    private static final Logger LOG = LogManager.getLogger(LoggedOut.class);
    private boolean duplicateLogin = false;
    private boolean parsed = false;

    /**
     * True if this users was kicked out due to the same username logging in a second time.
     * @return  true if this user was kicked out due to the same user logging in again.
     */
    public boolean duplicateLogin() {
        if (!parsed) {
            parse();
        }
        return this.duplicateLogin;
    }

    /**
     * Returns one of the list of logout messages.
     * @param x The message number to get
     * @return  The message or null if you have exceeded the number of messages
     */
    public String getLogoutMessage(final int x) {
        if (x < numberParms()) {
            return getParm(x);
        }
        return null;
    }
    
    private void parse() {
        Pattern p = Pattern.compile("\\S+ has arrived\\.  You can't both be logged in\\.  Sorry\\.");
        for (int x = 0; x < numberParms(); x++) {
            Matcher m = p.matcher(getParm(x));
            if (m.matches()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("DUPLICATE KICK: " + getParm(x));
                }
                duplicateLogin = true;
            }
        }
        parsed = true;
    }

}
