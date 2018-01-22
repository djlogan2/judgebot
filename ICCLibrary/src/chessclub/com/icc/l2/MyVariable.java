package chessclub.com.icc.l2;

import chessclub.com.enums.L2;

/**
 * Returned in response to DG_MY_VARIABLE and DG_MY_STRING_VARIABLE packets.
 * @author David Logan
 *
 */
public class MyVariable extends Level2Packet {
    /**
     * The variable name.
     * @return  The variable name.
     */
    public String variableName() {
        return getParm(1);
    }

    /**
     * The variables string value.
     * @return  The variables string value.
     */
    public String stringValue() {
        return getParm(2);
    }

    /**
     * The variables integer variable. If the data was returned as a
     * "DG_MY_STRING_VARIABLE", a zero is returned from this function even if
     * the data in the string could be converted to a number.
     * @return  The integer value of the variable or zero if the variable is intended to be a string.
     */
    public int intValue() {
        if (type() == L2.MY_STRING_VARIABLE) {
            return 0;
        } else {
            return Integer.parseInt(getParm(2));
        }
    }
}
