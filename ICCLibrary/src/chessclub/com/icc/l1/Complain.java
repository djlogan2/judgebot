package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This packet is generated when somebody complains about somebody else
 * 
 * @author David Logan
 * 
 */
public class Complain extends Level1Packet {
    private static final Logger LOG = LogManager.getLogger(Complain.class);
    private String complainer;
    private String complainee;
    private String complainfile;
    private boolean parsed = false;

    /**
     * The complainer
     * 
     * @return The person doing the complaining
     */
    public String complainer() {
        if (!parsed) {
            parse();
        }
        return complainer;
    }

    /**
     * The complainee
     * 
     * @return The person being complained about
     */
    public String complainee() {
        if (!parsed) {
            parse();
        }
        return complainee;
    }

    /**
     * The file with the complaint (i.e. "help complaint3") -- This will be "complaint3"
     * 
     * @return The complain file
     */
    public String complainfile() {
        if (!parsed) {
            parse();
        }
        return complainfile;
    }

    private void parse() {
        Pattern shout1 = Pattern.compile("Note to channel 0: (\\S+) complaining about (\\s+), see \"help (\\S+)\"");
        Matcher m = shout1.matcher(getParm(1));
        if (m.matches()) {
            complainer = m.group(1);
            complainee = m.group(2);
            complainfile = m.group(3);
        } else {
            (new Exception("Invalid data for ShoutResponse packet. Packet data=" + getPacket())).printStackTrace();
            LOG.error("Invalid data for ShoutResponse packet. Packet data=" + getPacket());
        }
        parsed = true;
    }
}
