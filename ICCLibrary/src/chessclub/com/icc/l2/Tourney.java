package chessclub.com.icc.l2;


/**
 * The instance returned from the framework for each tournament packet received from ICC.
 * @author David Logan
 *
 */
public class Tourney extends Level2Packet {
    private static final char GUESTSCANWATCH = 0x01;
    private static final char JOINNEWWINDOW  = 0x02;
    private static final char WATCHNEWWINDOW = 0x04;
    private static final char INFONEWWINDOW  = 0x08;
    
    /**
     * The index number of the tournament entry.
     * @return The index number of the tournament entry.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * true if guests can watch this event.
     * @return true if guests can watch this event.
     */
    public boolean guestsCanWatch() {
        return ((getParm(2).charAt(0) & GUESTSCANWATCH) == GUESTSCANWATCH);
    }

    /**
     * true if the event creator thinks we need to create a new window when joining.
     * @return true if the event creator thinks we need to create a new window when joining.
     */
    public boolean joinNewWindow() {
        return ((getParm(2).charAt(0) & JOINNEWWINDOW) == JOINNEWWINDOW);
    }

    /**
     * true if the event creator thinks we need to create a new window when watching.
     * @return true if the event creator thinks we need to create a new window when watching.
     */
    public boolean watchNewWindow() {
        return ((getParm(2).charAt(0) & WATCHNEWWINDOW) == WATCHNEWWINDOW);
    }

    /**
     * true if the event creator thinks we need to create a new window when displaying event information.
     * @return true if the event creator thinks we need to create a new window when displaying event information.
     */
    public boolean infoNewWindow() {
        return ((getParm(2).charAt(0) & INFONEWWINDOW) == INFONEWWINDOW);
    }

    /**
     * (Usually if not always) english text description string.
     * @return (Usually if not always) english text description string.
     */
    public String description() {
        return getParm(3);
    }

    /**
     * The ICC command issued in order to join.
     * @return The ICC command issued in order to join.
     */
    public String joincommand() {
        return getParm(4);
    }

    /**
     * The ICC command issued in order to watch.
     * @return The ICC command issued in order to watch.
     */
    public String watchcommand() {
        return getParm(5);
    }

    /**
     * The ICC command issued in order to get event info.
     * @return The ICC command issued in order to get event info.
     */
    public String infocommand() {
        return getParm(6);
    }

    /**
     * After joining, this is the confirm text that is displayed with an "OK"/"Cancel" button,
     * joining only after an "OK".
     * <p>If confirm text is null, then there is no confirmation prior to joining.
     * @return The text or null
     */
    public String confirmtext() {
        return getParm(7);
    }
}
