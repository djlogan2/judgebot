package chessclub.com.icc.l2;


/**
 * Similar to the rating keys, this is the wild rating key instances.
 * ICC sends one of these for each wild type for future use by using only the index number.
 * @author David Logan
 *
 */
public class WildKey extends Level2Packet {
    /**
     * The index number of the wild type.
     * @return The index number of the wild type.
     */
    public int index() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * The english text description of the wild type.
     * @return The english text description of the wild type.
     */
    public String name() {
        return getParm(2);
    }
}
