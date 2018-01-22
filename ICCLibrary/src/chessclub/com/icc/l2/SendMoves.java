package chessclub.com.icc.l2;

import chessclub.com.enums.IsVariation;
import chessclub.com.icc.l1l2.MoveInfo;

/**
 * The instance created for DG_SEND_MOVE packets.
 *     <p>five optional fields,
 *     <p>algebraic-move, smith-move, time, clock, and is-variation.
 *     <p>--- SMITH MOVE ---
 *     <p>This format is defined as follows:
 *     <p>&ltfrom square&gt&ltto square&gt[&ltcapture indicator&gt][&ltpromoted to&gt]
 *     <p>2 chars 2 chars 0 or 1 char 0 or 1 char
 *     @see MoveInfo
 * @author David Logan
 *
 */
public class SendMoves extends Level2Packet {
    /**
     * The ICC game number for this move.
     * @return  The ICC game number for this move.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }

    /**
     * Returns the algebraic notation for this move.
     * @return  the algebraic notation for this move.
     */
    public String algebraicMove() {
        return getParm(2);
    }

    /**
     * Returns the smith notation for this move.
     * @return  the smith notation for this move.
     */
    public String smithMove() {
        return getParm(3);
    }

    /**
     * Returns the number of seconds from the time it became their move to the time their move was made.
     * @return  the number of seconds from the time it became their move to the time their move was made.
     */
    public int secondsUsed() {
        return Integer.parseInt(getParm(4));
    }

    /**
     * Returns the number of seconds left on the clock after this move was made, after incrementing.
     * @return  the number of seconds left on the clock after this move was made, after incrementing.
     */
    public int clockLeft() {
        return Integer.parseInt(getParm(5));
    }

    /**
     * Returns the type of move this move is, variation or mainline.
     * @return  the type of move this move is, variation or mainline.
     */
    public IsVariation isVariation() {
        return IsVariation.getIsVariation(Integer.parseInt(getParm(6)));
    }
}
