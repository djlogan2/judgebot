package chessclub.com.icc.l2;


/**
 * Instance created when a DG_PARTNERSHIP packet is sent from ICC.
 * @author David Logan
 *
 */
public class Partnership extends Level2Packet {
    // 44 DG_PARTNERSHIP
    // (name name forming)
    /**
     * One of the two names forming (or breaking) a partnership.
     * @return  An ICC username.
     */
    public String name1() {
        return getParm(1);
    }

    /**
     * One of the two names forming (or breaking) a partnership.
     * @return  An ICC username.
     */
    public String name2() {
        return getParm(2);
    }

    /**
     * True if they are forming a partnership, false if they are dissolving one.
     * @return  True if they are forming a partnership, false if they are dissolving one.
     */
    public boolean forming() {
        return "1".equals(getParm(3));
    }

    /**
     * True if they are dissolving a partnership, false if they are creating one.
     * @return  True if they are dissolving a partnership, false if they are creating one.
     */
    public boolean breaking() {
        return !forming();
    }
}
