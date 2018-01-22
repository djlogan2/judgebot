package david.logan.chess.support;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

// 11 11 11  1  1  1  1  1 11 11 11 11 11 11 11 11 - 24 bits - 48 bits both colors 92 bits locations + "can moves" (11.5 bytes)
//  R  N  B  Q  K KB KN KR Pa Pb Pc Pd Pe Pf Pg Ph
// 00 01 10 11 - Promoted piece, two bits
//  R  N  B  Q
// Pa Pb Pc Pd Pe Pf Pg Ph - 16 bits for promoted pieces (2 bytes for both colors)
public abstract class Board {

    private static final Logger LOG = LogManager.getLogger(Board.class);
    protected static final int ROWS = 8;
    protected static final int COLUMNS = 8;

    public static final int KINGSIDE = 0;
    public static final int QUEENSIDE = 1;

    protected ChessPiece[][] board;
    protected Color toMove;

    protected boolean fireEvents = true;

    abstract public void loadFEN(String fen) throws Exception;
    abstract public Fen getFEN() throws Exception;
    abstract public void setStartingPosition();

    public Board() {
        toMove = Color.NONE;
        board  = new ChessPiece[ROWS][COLUMNS];
    }

    /**
     * Clears the board so that there are no pieces on the board.
     */
    public void clearBoard() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clearBoard()");
        }
        toMove = Color.NONE;
        for (int rank = 0; rank < ROWS; rank++) {
            for (int file = 0; file < COLUMNS; file++) {
                board[rank][file] = new ChessPiece(Color.NONE, ChessPieceEnum.NONE);
            }
        }
    }

    /**
     * Returns the color of the piece at the designated rank and file.
     * 
     * @param square
     *            The square in question. See {@link Square}
     * @return Returns Color.WHITE, Color.BLACK or Color.NONE
     */
    public Color colorAt(Square square) {
        return board[square.rank][square.file].color();
    }

    /**
     * Returns the color whos turn it is to move.
     * 
     * @return Color.WHITE, Color.BLACK or Color.NONE
     */
    public Color colorToMove() {
        return toMove;
    }

    /**
     * Returns the type of chess piece at the specified rank and file.
     * 
     * @param rank
     *            The 0-7 rank number.
     * @param file
     *            The 0-7 file number.
     * @return The {@link ChessPiece}. Color and Piece will both be 'NONE' if there is no piece at this square.
     */
    public ChessPiece pieceAt(Square square) {
        return board[square.rank][square.file];
    }

    /**
     * Returns just the type of piece (such as KNIGHT) at the specified rank and file.
     * 
     * @param square
     *            The square in question. See {@link Square}
     * @return The chess piece type (i.e. KNIGHT.) 'NONE' if there is no piece. (See: {@link ChessPieceEnum})
     */
    public ChessPieceEnum piecetypeAt(final Square square) {
        return board[square.rank][square.file].piece();
    }

    protected void makeMove(Square from, Square to) throws IllegalMoveException, Exception {
        if (board[from.rank][from.file].color() != toMove) {
            LOG.error("makeMove is asking for an illegal move");
        }

        board[to.rank][to.file].copy(board[from.rank][from.file]);
        board[from.rank][from.file].clear();
        fireSquareChanged(to.rank, to.file);
        fireSquareChanged(from.rank, from.file);

        toMove = (toMove == Color.WHITE ? Color.BLACK : Color.WHITE);
    }

    protected void throwIllegalMoveException(Square from, Square to) throws IllegalMoveException {
        try {
			throw new IllegalMoveException( getFEN().getFEN(), from, to);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalMoveException( "INVALID FEN", from, to);
		}
    }
    /**
     * Returns the raw piece count on the board for the requested color. It returns the widely accepted piece values:
     * <ul>
     * <li><b>1</b> for a pawn</li>
     * <li><b>3</b> for a knight or a bishop</li>
     * <li><b>5</b> for a rook</li>
     * <li><b>9</b> for a queen</li>
     * </ul>
     * 
     * @param c
     *            The requested color.
     * @return The integer sum of the pieces on the board.
     */
    public int pieceCount(final Color c) {
        int count = 0;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                int c2 = board[rank][file].piece().pieceValue();
                if (board[rank][file].color() != c) {
                    c2 = -c2;
                }
                count += c2;
            }
        }
        return count;
    }
    
    /**
     * Used for subclasses to clone themselves with. This method will copy the base data
     * @param newboard -- An instance of a new subclassed "Board"
     */
    protected void clone(Board newboard) {
        for(int rank = 0 ; rank < 8 ; rank++) {
            for(int file = 0 ; file < 8 ; file++) {
                newboard.board[rank][file] = new ChessPiece(board[rank][file]);
            }
        }
        newboard.toMove = toMove;
        return;
    }
    
    private ArrayList<BoardChangeListener> listeners = new ArrayList<BoardChangeListener>();
    public void addBoardChangeListener(BoardChangeListener l) {
        listeners.add(l);
    }
    public void removeBoardChangeListener(BoardChangeListener l) {
        listeners.remove(l);
    }
    protected void fireSquareChanged(int rank, int file) {
        if(fireEvents)
            fireSquareChanged(new Square(rank, file));
    }
    protected void fireSquareChanged(Square square) {
        if(fireEvents)
            for(BoardChangeListener bcl : listeners)
                bcl.squareChanged(square);
    }
    protected void fireBoardChanged() {
        if(fireEvents)
            for(BoardChangeListener bcl : listeners)
                bcl.boardChanged();
    }

    private int materialcount(Color color, boolean countpawns) {
        int materialcount = 0;
        for(int rank = 0 ; rank < ROWS ; rank++)
            for(int file = 0 ; file < COLUMNS ; file++) {
                if(board[rank][file] != null && board[rank][file].color() == color) {
                    switch(board[rank][file].piece()) {
                    case PAWN:
                        if(countpawns)
                            materialcount++;
                        break;
                    case ROOK:
                        materialcount += 5;
                        break;
                    case KNIGHT:
                    case BISHOP:
                        materialcount += 3;
                        break;
                    case QUEEN:
                        materialcount += 9;
                        break;
                    default:
                        break;
                    }
                }
            }
        return materialcount;
    }

    public int materialcount(Color color) {
        return materialcount(color, true);
    }
    public int nopawnmaterialcount(Color color) {
        return materialcount(color, false);
    }
}
