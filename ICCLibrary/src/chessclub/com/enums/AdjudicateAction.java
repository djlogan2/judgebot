package chessclub.com.enums;

/**
 * 
 * @author David Logan
 *  <p> 
 *         This enumeration contains the various possible Adjudication actions. The enumerations here correspond exactly to the available parameters for the ICC "adjudicate" command.
 * 
 */
public enum AdjudicateAction {
    /**
     * The player with the white pieces will be awarded the win.
     */
    WHITE,
    /**
     * The player with the black pieces will be awarded the win.
     */
    BLACK,
    /**
     * The game is adjudicated as a draw, and draw points are assigned.
     */
    DRAW,
    /**
     * The game is adjudicated as an abort, and no ratings will change.
     */
    ABORT,
    /**
     * It is a valid parameter to the "adjudicate" ICC command, but its function is unknown.
     */
    FIRST
}
