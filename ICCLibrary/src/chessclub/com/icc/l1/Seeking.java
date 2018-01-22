package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * An instance returned from the framework when this user sends a seek to ICC.
 * @author David Logan
 *
 */
public class Seeking extends Level1Packet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Seeking.class);
    private boolean error = false; 
    private boolean empty = false;
    private int index;
    private int sentto;
    private int eligible;
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

    /**
     * The number of people this seek was sent to
     * @return The number of people this seek was sent to
     */
    public int sentto() {
        if (!parsed) {
            parse();
        }
        return sentto;
    }

    /**
     * The number of people this seek was sent to that were eligible to accept it
     * @return The number of people this seek was sent to that were eligible to accept it
     */
    public int eliglble() {
        if (!parsed) {
            parse();
        }
        return eligible;
    }
    
    public String error() {
        if(!parsed)
            parse();
        if(error)
            return null;
        else
            return getParm(1);
    }
    
    public boolean isEmpty() {
        if(!parsed)
            parse();
        return empty;
    }

    private void parse() {
        if(this.numberParms() == 1) {
            empty = true;
            return;
        }
        // TODO: This "Issuing:" line isn't being handled right now
        // TODO: Also, we need to enumerate errors in order to get away from returning text
        //        Issuing: TacticalTrainer (2000) idjit (1113) unrated wild(20) Loadgame 20 20 (adjourned)^M
        //        Your ad is #254^M
        //        (ad sent to 1132 players, of whom 1132 are eligible)^M
        //
        //        Your ad is #216
        //        (ad sent to 721 players, of whom 721 are eligible)
        //
        if(getParm(1).startsWith("Your ad is #")) {
            index = Integer.parseInt(getParm(1).substring(12));
            Pattern p = Pattern.compile("\\(ad sent to (\\d+) players, of whom (\\d+) are eligible\\)");
            Matcher m = p.matcher(getParm(2));
            if (m.matches()) {
                sentto = Integer.parseInt(m.group(1));
                eligible = Integer.parseInt(m.group(2));
            } else {
                (new Exception("Invalid data for Seek packet. Packet data=" + getPacket())).printStackTrace();
            }
        } else {
            error = true;
        }
    }
}
