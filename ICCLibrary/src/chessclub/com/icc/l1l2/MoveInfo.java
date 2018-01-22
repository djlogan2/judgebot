package chessclub.com.icc.l1l2;

import chessclub.com.enums.IsVariation;
import chessclub.com.icc.l2.L2MoveList;

/**
 * Instances created from a level 2 DG_MOVE_LIST packet.
 * This class isn't intended for public consumption. It is intended to be fed into a
 * {@link ICCMove} constructor. {@link ICCMove} is intended for public use.
 * @see L2MoveList
 * @see ICCMove
 * @author David Logan
 *
 */
public class MoveInfo {
    // five optional fields,
    // algebraic-move, smith-move, time, clock, and is-variation.
    // --- SMITH MOVE ---
    // This format is defined as follows:
    // <from square><to square>[<capture indicator>][<promoted to>]
    // 2 chars 2 chars 0 or 1 char 0 or 1 char

    private String algebraicmove = null;
    private String smithmove = null;
    private int time = -1;
    private int clock = -1;
    private IsVariation isvariation = null;

    /**
     * The string move information from the incoming packet data.
     * @param moveinfo  The raw string data from ICC.
     */
    public MoveInfo(final String moveinfo) {
        String[] parms = moveinfo.split("\\s+");
        algebraicmove = parms[0];
        smithmove = parms[1];
        time = Integer.parseInt(parms[2]);
        isvariation = IsVariation.getIsVariation(Integer.parseInt(parms[3]));
    }

    /**
     * Returns the algebraic notation for this move.
     * @return  the algebraic notation for this move.
     */
    public String algebraicMove() {
        return algebraicmove;
    }

    /**
     * Returns the smith notation for this move.
     * @return  the smith notation for this move.
     */
    public String smithMove() {
        return smithmove;
    }

    /**
     * Returns the number of seconds from the time it became their move to the time their move was made.
     * @return  the number of seconds from the time it became their move to the time their move was made.
     */
    public int secondsUsed() {
        return time;
    }

    /**
     * Returns the number of seconds left on the clock after this move was made, after incrementing.
     * @return  the number of seconds left on the clock after this move was made, after incrementing.
     */
    public int clockLeft() {
        return clock;
    }

    /**
     * Returns the type of move this move is, variation or mainline.
     * @return  the type of move this move is, variation or mainline.
     */
    public IsVariation isVariation() {
        return isvariation;
    }
}
