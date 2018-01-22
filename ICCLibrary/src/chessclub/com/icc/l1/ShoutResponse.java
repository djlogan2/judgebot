package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * The response data that comes back from a shout.
 * @author David Logan
 *
 */
public class ShoutResponse extends Level1Packet {
    private static final Logger LOG = LogManager.getLogger(ShoutResponse.class);
    private int shoutedTo = 0;
    private boolean areListening = true;
    private boolean parsed = false;
    
    /**
     * The number of people that heard your shout.
     * @return  The number of people that heard your shout.
     */
    public int shoutCount() { 
        if (!parsed) {
            parse();
        }
        return shoutedTo;
    }
    
    /**
     * True or false depending on whether or not you are listening to shouts (and thus saw your own or any possible responses).
     * @return  true if you will see your shout and other peoples responses to your shout. false if you are not watching shouts.
     */
    public boolean isListening() {
        if (!parsed) {
            parse();
        }
        return areListening;
    }
    
    private void parse() {
        Pattern shout1 = Pattern.compile("\\(shouted to (\\d+) people\\)( \\(You're not listening to shouts.\\))?");
        Matcher m = shout1.matcher(getParm(1));
        if (m.matches()) {
            shoutedTo = Integer.parseInt(m.group(1));
            if (m.groupCount() == 2) {
                areListening = false;
            }
        } else {
            (new Exception("Invalid data for ShoutResponse packet. Packet data=" + getPacket())).printStackTrace();
            LOG.error("Invalid data for ShoutResponse packet. Packet data=" + getPacket());
        }
        parsed = true;
    }
}
