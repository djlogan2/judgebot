package chessclub.com.icc.l2;

import chessclub.com.enums.ProvisionalStatus;
import chessclub.com.icc.l1l2.Titles;
import david.logan.chess.support.Color;

/**
 * An instance that gets sent from the framework whenever the user gets a seek (DG_SEEK).
 * @author David Logan
 *
 */
public class Seek extends Level2Packet {
    // 50 DG_SEEK
    // Level-2 form of "seeking" messages, useful for maintaining
    // the sought list at the client.
    //
    // Form: (index name titles rating provisional-status wild rating-type time
    // inc rated color minrating maxrating autoaccept formula fancy-time-control)
    // 123 happyface {} 2242 2 0 Standard 20 0 1 -1 2242 9999 0 1 {}
    /**
     * An index number unique to each seek request.
     * <p>(Don't forget seek requests can last quite a while.)
     * @return An index number unique to each seek request.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The user that issued the seek.
     * @return The user that issued the seek.
     */
    public String name() {
        return getParm(2);
    }

    /**
     * The {@link Titles} held by the user issuing the seek.
     * @return The {@link Titles} held by the user issuing the seek.
     */
    public Titles titles() {
        return new Titles(getParm(3));
    }

    /**
     * The rating of the user that issued the seek.
     * @return The rating of the user that issued the seek.
     */
    public int rating() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * The {@link ProvisionalStatus} of the user issuing the seek.
     * @return The {@link ProvisionalStatus} of the user issuing the seek.
     */
    public ProvisionalStatus provisionalstatus() {
        return ProvisionalStatus.getStatus(Integer.parseInt(getParm(5)));
    }

    /**
     * The games wild type for the seek.
     * @return The games wild type for the seek.
     */
    public int wild() {
        return Integer.parseInt(getParm(6));
    }

    /**
     * The type of game (i.e. the type of rating) in English text.
     * @return The type of game (i.e. the type of rating) in English text.
     */
    public String ratingtype() {
        return getParm(7);
    }

    
    /**
     * The initial time in second for the seek.
     * @return The initial time in second for the seek.
     */
    public int time() {
        return Integer.parseInt(getParm(8));
    }

    /**
     * The increment value for the seek.
     * @return The increment value for the seek.
     */
    public int inc() {
        return Integer.parseInt(getParm(9));
    }

    /**
     * true if the seek is for a rated game.
     * @return true if the seek is for a rated game.
     */
    public boolean rated() {
        return "1".equals(getParm(10));
    }

    /**
     * The {@link Color} if the seek requests a certain color. {@link Color#NONE} if no color was requested.
     * @return The {@link Color} if the seek requests a certain color. {@link Color#NONE} if no color was requested.
     */
    public Color color() {
        switch (Integer.parseInt(getParm(11))) {
        case -1:
            return Color.NONE;
        case 0:
            return Color.BLACK;
        case 1:
            return Color.WHITE;
        default:
            return null;
        }
    }

    /**
     * The minimum rating requested by the issuer of the seek.
     * @return The minimum rating requested by the issuer of the seek.
     */
    public int minrating() {
        return Integer.parseInt(getParm(12));
    }

    /**
     * The maximum rating requested by the issuer of the seek.
     * @return The maximum rating requested by the issuer of the seek.
     */
    public int maxrating() {
        return Integer.parseInt(getParm(13));
    }

    /**
     * true if the issuer of the seek will automatically accept the game.
     * @return true if the issuer of the seek will automatically accept the game.
     */
    public boolean autoaccept() {
        return "1".equals(getParm(14));
    }

    /**
     * true if the issuer of the seek will apply his formula against the acceptor of the seek before the seek is accepted.
     * @return true if the issuer of the seek will apply his formula against the acceptor of the seek before the seek is accepted.
     */
    public boolean formula() {
        return "1".equals(getParm(15));
    }

    /**
     * fancy-time-control is usually null ({}), but if it isn't, see the notes
     * of {@link GameStarted#fancytimecontrol()} for info on what it means.  It overrides
     * the time and inc if present, but those can still be used to estimate
     * etime and we'll try to keep them vaguely close to the real TC.
     * @return A string that contains the fancy time control if one exists.
     */
    public String fancytime() {
        return getParm(16);
    }
}
