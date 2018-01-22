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
public class Play extends Level1Packet {
    private static final Logger LOG = LogManager.getLogger(Play.class);
    private String me;
    private String opponent;
    private int myRating;
    private int opponentRating;
    private boolean needAccept;
    
    private boolean parsed = false;

    public String me() {
        if (!parsed) {
            parse();
        }
        return me;
    }

    public String opponent() {
        if (!parsed) {
            parse();
        }
        return opponent;
    }

    public int myrating() {
        if (!parsed) {
            parse();
        }
        return myRating;
    }

    public int opponentrating() {
        if (!parsed) {
            parse();
        }
        return opponentRating;
    }

    public boolean waitingForAccept() {
        if (!parsed) {
            parse();
        }
        return needAccept;
    }

    private void parse() {
        Pattern p = Pattern.compile("Challenge:\\s+(\\S+)\\s+\\((\\s*\\d+)\\)\\s+(\\S+)\\s+\\((\\s*\\d+)\\)\\s+(.*?)\\s+(\\d+)\\s+(\\d+)");
        
        if(numberParms() < 3) {
            //We get an empty play packet for every game started, so we know why we get empty packets.
            //LOG.error("Unknown data in Play packet: '" + getParm(1) + "'");
            return;
        }
        
        Matcher m = p.matcher(getParm(1));
        if (m.matches()) {
            this.opponent = m.group(1);
            this.opponentRating = Integer.parseInt(m.group(2).trim());
            this.me = m.group(3);
            this.myRating = Integer.parseInt(m.group(4));
        } else {
            LOG.error("Uknown data in Play packet: '" + getParm(1) + "'");
            return;
        }
        needAccept = false;
        if(getParm(2).contains("may accept"))
            needAccept = true;
//        [217 %
//         Challenge: djlogan (1784) idjit (1592) unrated Standard 20 20
//         You may accept this with "accept djlogan", decline it with
//         "decline djlogan", or propose different parameters.
//         ]
//
//         217 %^M
//         Challenge: justinchess (1762) TacticalTrainer (2000) unrated wild(20) Loadgame 20 20^M
//         Creating: justinchess (1762) TacticalTrainer (2000) unrated wild(20) Loadgame 20 20^M
//         ^GYou accept the challenge of justinchess.^M
//         "Seeking" ad #203 removed.^M
//         {Game 608 (TacticalTrainer vs. justinchess) Creating unrated wild(20) match.} *^M
    }
}
