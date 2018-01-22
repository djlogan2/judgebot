package chessclub.com.icc.l2;

import chessclub.com.enums.L2;

/**
 * The instance returned from the framework when a level 2 setting is changed.
 * @author David Logan
 *
 */
public class Set2 extends Level2Packet {
    // 124 DG_SET2
    // (dg-code off/on)
    /**
     * The level 2 code that was turned off or turned on.
     * @return The level 2 code that was turned off or turned on.
     */
    public L2 code() {
        return L2.getL2(Integer.parseInt(getParm(1)));
    }

    /**
     * true if the setting was turned on.
     * @return true if the setting was turned on.
     */
    public boolean on() {
        return "1".equals(getParm(2));
    }

    /**
     * true if the setting was turned off.
     * @return true if the setting was turned off.
     */
    public boolean off() {
        return !on();
    }
}
