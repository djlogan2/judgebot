package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * An instance returned from the framework when this user gets a spoof from an ICC Administrator.
 * @author David Logan
 *
 */
public class Spoof extends Level1Packet {
    private static final Logger LOG = LogManager.getLogger(Spoof.class);
    private String sAdmin = null;
    private String sCommand = null;
    private boolean parsed = false;

    /**
     * The administrator that issued the spoof.
     * @return The administrator that issued the spoof.
     */
    public String admin() {
        if (!parsed) {
            parse();
        }
        return sAdmin;
    }

    /**
     * The command that the administrator has issued on your behalf.
     * @return The command that the administrator has issued on your behalf.
     */
    public String command() {
        if (!parsed) {
            parse();
        }
        return sCommand;
    }

    private void parse() {
        Pattern p = Pattern.compile("(.*)\\s+spoofs you: (.*)");
        Matcher m = p.matcher(getParm(1));
        if (m.matches()) {
            sAdmin = m.group(1);
            sCommand = m.group(2);
        } else {
            (new Exception("Invalid data for Spoof packet. Packet data=" + getPacket())).printStackTrace();
            LOG.error("Invalid data for Spoof packet. Packet data=" + getPacket());
        }
    }
}
