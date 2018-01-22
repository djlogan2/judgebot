package david.logan.chess.support;

/**
 * 
 * @author David Logan
 * <p>
 * This is just a small enumeration that converts chess piece types into an enumerated value.
 *
 */
public enum ChessPieceEnum {
    /**
     * No piece.
     */
    NONE(0, ' '),
    /**
     * A king.
     */
    KING(999, 'k'),
    /**
     * A queen.
     */
    QUEEN(9, 'q'),
    /**
     * A bishop.
     */
    BISHOP(3, 'b'),
    /**
     * A knight.
     */
    KNIGHT(3, 'n'),
    /**
     * A rook.
     */
    ROOK(5, 'r'),
    /**
     * A pawn.
     */
    PAWN(1, 'p');
    private int value;
    private char smithPiece;
    /**
     * The constructor that saves the numerical piece value.
     * @param v The numerical piece value.
     */
    ChessPieceEnum(final int v, final char s) {
        value = v;
        smithPiece = s;
    }

    /**
     * Returns the widely accepted numerical piece value (1 for pawn, 9 for queen).
     * This returns the arbitrary value 999 for a king.
     * @return  The numerical value of the piece.
     */
    public int pieceValue() {
        return value;
    }
    
    public char smithPiece() {
    	return smithPiece;
    }
}
