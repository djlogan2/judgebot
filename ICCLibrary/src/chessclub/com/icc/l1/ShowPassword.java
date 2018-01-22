package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This packet is generated when somebody does a "showpassword"
 * 
 * @author David Logan
 * 
 */
public class ShowPassword extends Level1Packet {
    private static final Logger LOG = LogManager.getLogger(ShowPassword.class);
    private String asker;
    private String user;
    private boolean parsed = false;

    /**
     * The asker
     * 
     * @return The person doing the "showpassword"
     */
    public String asker() {
        if (!parsed) {
            parse();
        }
        return asker;
    }

    /**
     * The user
     * 
     * @return The person whos password has been obtainee
     */
    public String user() {
        if (!parsed) {
            parse();
        }
        return user;
    }

    private void parse() {
        Pattern shout1 = Pattern.compile("(\\S+) did show-password (\\S+).");
        Matcher m = shout1.matcher(getParm(1));
        if (m.matches()) {
            asker = m.group(1);
            user = m.group(2);
        } else {
            (new Exception("Invalid data for ShowPassword packet. Packet data=" + getPacket())).printStackTrace();
            LOG.error("Invalid data for ShowPassword packet. Packet data=" + getPacket());
        }
        parsed = true;
    }
}
