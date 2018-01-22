package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * An instance returned from the framework when this user sends an "unseek" to ICC.
 * @author David Logan
 *
 */
public class Unseeking extends Level1Packet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Unseeking.class);
    private boolean error = false; 
    private int index;
    private boolean parsed = false;

    /**
     * The index number of this seek
     * @return The index number of this seek
     */
    public int index() {
        if (!parsed) {
            parse();
        }
        return index;
    }

    public String error() {
        if(error)
            return null;
        else
            return getParm(1);
    }

    private void parse() {
//        "Seeking" ad #13 removed.
        Pattern p = Pattern.compile("\"Seeking\" ad #(\\d+) removed.");
        Matcher m = p.matcher(getParm(1));
        if (m.matches()) {
            index = Integer.parseInt(m.group(1));
        } else {
            error = true;
        }
    }
}
