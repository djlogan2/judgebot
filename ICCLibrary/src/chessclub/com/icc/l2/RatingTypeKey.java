package chessclub.com.icc.l2;


/**
 * This is the rating type key instance that gets returned from a DG_RATING_TYPE_KEY packet.
 * <p>This tells us what ratings we are getting back from ICC in various other packets.
 * @author David Logan
 *
 */
public class RatingTypeKey extends Level2Packet {
    /**
     * The index number.
     * @return  The index number.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The english rating text.
     * @return  The english rating text.
     */
    public String text() {
        return getParm(2);
    }
}
