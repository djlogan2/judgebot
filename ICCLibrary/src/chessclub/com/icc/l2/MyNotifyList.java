package chessclub.com.icc.l2;


/**
 * An instance returned from the framework when a user is added to or removed from this users notify list.
 * @author David Logan
 *
 */
public class MyNotifyList extends Level2Packet {

    /**
     * The user added or removed.
     * @return  The user added or removed.
     */
    public String user() {
        return getParm(1);
    }

    /**
     * true if they were added, false if they were removed.
     * @return  true if they were added, false if they were removed.
     */
    public boolean added() {
        return "1".equals(getParm(2));
    }
}
