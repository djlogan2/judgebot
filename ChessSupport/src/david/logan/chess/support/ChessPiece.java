package david.logan.chess.support;

/**
 *
 * @author David Logan
 * <p>
 * A simple class that contains a {@link ChessPieceEnum} and a {@link Color} object.
     * @see Color
     * @see ChessPieceEnum
 */
public class ChessPiece {
    private ChessPieceEnum piece;
    private Color color;

    /**
     * Default constructor. Set the color and the piece both to NONE
     */
    public ChessPiece() {
        piece = ChessPieceEnum.NONE;
        color = Color.NONE;
    }

    /**
     * Sets the piece and color to the specified colors.
     * @param pColor    The {@link Color} object
     * @param pPiece    The {@link ChessPieceEnum} object
     * @see Color
     * @see ChessPieceEnum
     */
    public ChessPiece(final Color pColor, final ChessPieceEnum pPiece) {
        this.color = pColor;
        this.piece = pPiece;
    }

    /**
     * Copies the chess piece data from another object to this one.
     * @param other The other chess piece object to copy from.
     */
    public final void copy(final ChessPiece other) {
        this.piece = other.piece;
        this.color = other.color;
    }

    /**
     * Sets the piece and color based upon the string byte value. The values are from the FEN standard.
     * @param pPiece    The valid character values are (white) 'P','R','N','B','K','Q' and (black) 'p','r','n','b','k','q' and (none) '-' 
     */
    public ChessPiece(final byte pPiece) {
        setPiece(pPiece);
    }

    public ChessPiece(ChessPiece chessPiece) {
        color = chessPiece.color;
        piece = chessPiece.piece;
    }

    /**
     * Returns the enumerated piece type (such as KNIGHT or NONE).
     * @return  The enumerated piece type (such as KNIGHT or NONE.)
     * @see Color
     * @see ChessPieceEnum
     */
    public final ChessPieceEnum piece() {
        return piece;
    }

    /**
     * Returns the enumerated color (WHITE, BLACK or NONE).
     * @return  The enumerated color (WHITE, BLACK or NONE.)
     * @see Color
     * @see ChessPieceEnum
     */
    public final Color color() {
        return color;
    }

    /**
     * Sets the piece type and color to NONE.
     */
    public final void clear() {
        piece = ChessPieceEnum.NONE;
        color = Color.NONE;
    }

    /**
     * Sets the piece type to a new piece without changing the color. Note that this can result in an invalid situation where
     * the piece type is a valid piece but the color is NONE.
     * @param pPiece    The new {@link ChessPieceEnum} type
     * @see Color
     * @see ChessPieceEnum
     */
    public final void setPiece(final ChessPieceEnum pPiece) {
        this.piece = pPiece;
    }

    /**
     * Sets the color of the piece without changing the piece type. Note that this can result in an invalid situation where
     * the color is a valid color but the piece type is NONE.
     * @param pColor    The new {@link Color}
     * @see Color
     * @see ChessPieceEnum
     */
    public final void setColor(final Color pColor) {
        this.color = pColor;
    }

    /**
     * Sets the piece and color based upon the string byte value. The values are from the FEN standard.
     * @param b The valid character values are (white) 'P','R','N','B','K','Q' and (black) 'p','r','n','b','k','q' and (none) '-' 
     */
    public final void setPiece(final byte b) {
        switch (b) {
        case 'P':
            piece = ChessPieceEnum.PAWN;
            color = Color.WHITE;
            break;
        case 'R':
            piece = ChessPieceEnum.ROOK;
            color = Color.WHITE;
            break;
        case 'N':
            piece = ChessPieceEnum.KNIGHT;
            color = Color.WHITE;
            break;
        case 'B':
            piece = ChessPieceEnum.BISHOP;
            color = Color.WHITE;
            break;
        case 'K':
            piece = ChessPieceEnum.KING;
            color = Color.WHITE;
            break;
        case 'Q':
            piece = ChessPieceEnum.QUEEN;
            color = Color.WHITE;
            break;
        case 'p':
            piece = ChessPieceEnum.PAWN;
            color = Color.BLACK;
            break;
        case 'r':
            piece = ChessPieceEnum.ROOK;
            color = Color.BLACK;
            break;
        case 'n':
            piece = ChessPieceEnum.KNIGHT;
            color = Color.BLACK;
            break;
        case 'b':
            piece = ChessPieceEnum.BISHOP;
            color = Color.BLACK;
            break;
        case 'k':
            piece = ChessPieceEnum.KING;
            color = Color.BLACK;
            break;
        case 'q':
            piece = ChessPieceEnum.QUEEN;
            color = Color.BLACK;
            break;
        case '-':
        default:
            piece = ChessPieceEnum.NONE;
            color = Color.NONE;
            break;
        }
    }

    public boolean none() {
        return (piece == ChessPieceEnum.NONE || color == Color.NONE);
    }
    
    @Override
    public String toString()
    {
    	return color.toString() + " " + piece.toString();
    }
}
