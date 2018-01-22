package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * The instance that gets created when an ICC message is sent or received.
 * @author David Logan
 *
 */
public class Message extends Level1Packet {

    private static final Logger LOG = LogManager.getLogger(Message.class);

    private String user = null;
    private boolean sent = false;
    private boolean parsed = false;

    /**
     * The userid that sent the message or the userid that the message was sent to.
     * @return  The userid that sent the message or the userid that the message was sent to.
     */
    public String name() {
        if (!parsed) {
            parse();
        }
        return user;
    }

    /**
     * True if we sent a message to a user. False if we received a message from a user.
     * @return  True if we sent a message to a user. False if we received a message from a user.
     */
    public boolean sent() {
        if (!parsed) {
            parse();
        }
        return sent;
    }

    /**
     * True if we received a message from a user. False if we sent a message to a user.
     * @return  True if we received a message from a user. False if we sent a message to a user.
     */
    public boolean received() {
        if (!parsed) {
            parse();
        }
        return !sent;
    }

    private void parse() {
        Pattern p1 = Pattern.compile("(.*) has left a message for you.");
        Pattern p2 = Pattern.compile("Added .*the following message to (.*):");
        Matcher m = p1.matcher(getParm(1));
        if (m.matches()) {
            user = m.group(1);
            return;
        }

        m = p2.matcher(getParm(1));
        if (m.matches()) {
            sent = true;
            user = m.group(1);
            return;
        }

        (new Exception("Invalid data for Message packet. Packet data=" + getPacket())).printStackTrace();
        LOG.error("Invalid data for Message packet. Packet data=" + getPacket());
    }
}
