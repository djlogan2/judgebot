package chessclub.com.icc.l1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.enums.AdjudicateAction;

/**
 * The class that has the response when an ICC Administrator adjudicates another users game.
 * @author David Logan
 *
 */
public class GameAdjudicated extends Level1Packet {
    // Adjourned game [gorato--Shima777] adjudicated in favor of gorato.
    // Left the following message for gorato:
    // --JudgeBot (13:28 31-Jan-11 EST): Adjourned game [gorato--Shima777] adjudicated
    // \ in favor of gorato.
    // Left the following message for Shima777:
    // --JudgeBot (13:28 31-Jan-11 EST): Adjourned game [gorato--Shima777] adjudicated
    // \ in favor of gorato.
    private static final Logger LOG = LogManager.getLogger(GameAdjudicated.class);
    private String white;
    private String black;
    private String winner;
    private String loser;
    private boolean parsed = false;
    private AdjudicateAction action = null;

    /**
     * The adjudication action that the administrator performed.
     * @return  The adjudication action that the administrator performed.
     */
    public AdjudicateAction action() {
        if (!parsed) {
            parse();
        }
        return this.action;
    }

    /**
     * The declared winner of the game.
     * @return  The declared winner of the game.
     */
    public String winner() {
        if (!parsed) {
            parse();
        }
        return this.winner;
    }

    /**
     * The declared loser of the game.
     * @return  The declared loser of the game.
     */
    public String loser() {
        if (!parsed) {
            parse();
        }
        return this.loser;
    }

    /**
     * True if the game was successfully adjudicated.
     * @return  True if the game was successfully adjudicated.
     */
    public boolean wasAdjudicated() {
        if (!parsed) {
            parse();
        }
        return (this.action != null);
    }

    /**
     * The white player.
     * @return  The white player.
     */
    public String white() {
        if (!parsed) {
            parse();
        }
        return this.white;
    }

    /**
     * The black player.
     * @return  The black player.
     */
    public String black() {
        if (!parsed) {
            parse();
        }
        return this.black;
    }

    private void parse() {
        Pattern p = Pattern.compile("Adjourned game \\[(.*)--(.*)\\] adjudicated (in favor of (.*)|aborted|as a draw)\\.");
        Matcher m = p.matcher(getParm(1));
        if (m.matches()) {
            white = m.group(1);
            black = m.group(2);

            if ("aborted".equals(m.group(3))) {
                action = AdjudicateAction.ABORT;
            } else if ("as a draw".equals(m.group(3))) {
                action = AdjudicateAction.DRAW;
            } else {
                winner = m.group(4);
                if (white.equals(winner)) {
                    action = AdjudicateAction.WHITE;
                    loser = black;
                } else {
                    action = AdjudicateAction.BLACK;
                    loser = white;
                }
            }
        } else {
            p = Pattern.compile("There is no adjourned game between (.*) and (.*)\\.");
            m = p.matcher(getParm(1));
            if (m.matches()) {
                white = m.group(1);
                black = m.group(2);
            } else {
                (new Exception("Invalid data for GameAdjudicated packet. Packet data=" + getPacket())).printStackTrace();
                LOG.error("Invalid data for GameAdjudicated packet. Packet data=" + getPacket());
                return;
            }
        }
        parsed = true;
    }

}
