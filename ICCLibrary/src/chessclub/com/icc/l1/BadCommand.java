package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * 
 * @author David Logan
 *  <p> 
 *         The BadCommand packet captures data from an incoming level 1 packet and returns the result of an invalid command. Since the vast majority of ICC is still unstructured text, this will probably be a work in progress for years.
 * 
 */
public class BadCommand extends Level1Packet {

    private static final Logger LOG = LogManager.getLogger(BadCommand.class);
    private String sCommand = null;
    private boolean parsed = false;

    /**
     * Returns the command string that was invalid. It's not parsed or anything, so whatever was sent in will be the value of this string.
     * @return  The bad command string
     */
    public final String command() {
        if (!parsed) {
            parse();
        }
        return sCommand;
    }

    private void parse() {
        if(numberParms() < 2)
            return;
        
        Pattern p = Pattern.compile("No such command \\((.*)\\)\\.");
        Matcher m = p.matcher(getParm(1));
        if (m.matches()) {
            sCommand = m.group(1);
            return;
        }
        p = Pattern.compile(".*\"\\((.*?)\\)\".*");
        m = p.matcher(getParm(1));
        if (m.matches()) {
            sCommand = m.group(1);
            return;
        }

        p = Pattern.compile("\"(.*)\" does not match any appropriate list name. Possible choices are:");
        m = p.matcher(getParm(1));
        if (m.matches()) {
            sCommand = m.group(1);
            return;
        }

        if(this.numberParms() == 3) {
            p = Pattern.compile("You can use \"(.*)\" if you're registered.  See \"help register\".");
            m = p.matcher(getParm(2));
            if (m.matches()) {
                sCommand = m.group(1);
                return;
            }
        }

        p = Pattern.compile("E1 User \\(.*\\) of login kind User cannot do the command set");
        m = p.matcher(getParm(1));
        if (m.matches()) {
            sCommand = "set";
            return;
        }

        if(getParm(1).equals("Admin authenticate Failed!")) {
            sCommand = "admin";
            return;
        }

        (new Exception("Invalid data for CN_BAD packet. Packet data=" + getPacket())).printStackTrace();
        LOG.error("Invalid data for CN_BAD packet. Packet data=" + getPacket());
    }
}
