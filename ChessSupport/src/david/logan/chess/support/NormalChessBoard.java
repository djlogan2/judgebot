package david.logan.chess.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class NormalChessBoard extends Board {
    
	private static final Logger LOG = LogManager.getLogger(NormalChessBoard.class);
    protected int moveNumber;
    protected int movesto50;
    protected boolean[] whiteCanCastle;
    protected boolean[] blackCanCastle;
    protected Square enpassant;
    private boolean checkinglegalmoves = false; // To prevent recursive legal move checking
    private Boolean isincheck = null;
    // TODO: can3fold and movesto50 should be in game, not board
    private boolean can3fold = false;
    private ArrayList<Square[]> legalMoveList = null;
    private HashMap<String, Integer> fold3 = new HashMap<String, Integer>();

    public NormalChessBoard() {
        setStartingPosition();
    }
    
    public NormalChessBoard(Fen fen) {
        loadFEN(fen);
    }
    
    /**
     * Clears the board so that there are no pieces on the board.
     */
    @Override
    public void clearBoard() {
        super.clearBoard();
        movesto50 = 0;
        whiteCanCastle = new boolean[] {false, false};
        blackCanCastle = new boolean[] {false, false};
        enpassant = null;
        checkinglegalmoves = false; // To prevent recursive legal move checking
        isincheck = null;
        legalMoveList = null;
    }

    /**
     * Sets the starting chess position with the pawns on the 2nd and 7th ranks and the proper pieces on the first and eight ranks.
     */
    @Override
    public void setStartingPosition() {
        try {
			loadFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		} catch (Exception e) {
		}
    }

    public boolean isLegalMove(Square from, Square to) throws Exception {
        if(checkinglegalmoves) return true;
        for(Square[] sq2 : legalMoves()) {
            if(sq2[0].equals(from) && sq2[1].equals(to))
                return true;
        }
        return false;
    }
    
    private Square travel(Square source, Square current, Direction dir) {
        if(!current.equals(source) && piecetypeAt(current) != ChessPieceEnum.NONE)
            return null; // Always stop traversal if a piece of any color is at the current square
        
        int rankdir = 0;
        int filedir = 0;
        
        switch(dir) {
        case NORTH:
            rankdir = -1;
            break;
        case NORTHEAST:
            filedir = -1;
            rankdir = -1;
            break;
        case NORTHWEST:
            filedir = 1;
            rankdir = -1;
            break;
        case SOUTH:
            rankdir = +1;
            break;
        case SOUTHEAST:
            filedir = -1;
            rankdir = +1;
            break;
        case SOUTHWEST:
            filedir = 1;
            rankdir = +1;
            break;
        case EAST:
            filedir = -1;
            break;
        case WEST:
            filedir = 1;
            break;
        }
        if(piecetypeAt(source) == ChessPieceEnum.KNIGHT) {
            if(!source.equals(current))
                return null; // We are using direction "loosely" here, but it still needs to be at the source (i.e. it can't "slide" multiple squares")
            switch(dir) {
            case NORTH:
                rankdir =  2;
                filedir = -1;
                break;
            case SOUTH:
                rankdir =  2;
                filedir =  1;
                break;
            case EAST:
                rankdir =  1;
                filedir = -2;
                break;
            case WEST:
                rankdir =  1;
                filedir =  2;
                break;
            case NORTHWEST:
                rankdir = -1;
                filedir = -2;
                break;
            case NORTHEAST:
                rankdir = -1;
                filedir =  2;
                break;
            case SOUTHWEST:
                rankdir = -2;
                filedir = -1;
                break;
            case SOUTHEAST:
                rankdir = -2;
                filedir =  1;
                break;
            }
        }
        //Square newsquare = new Square(current.rank + rankdir, current.file + filedir);
        current.rank += rankdir;
        current.file += filedir;
        
        if(current.rank < 0 || current.rank > 7) return null;
        if(current.file < 0 || current.file > 7) return null;
        
        gooddirection:
        switch(piecetypeAt(source)) {
        case NONE:
            return null;
        case KING:
            if(!isInCheck() && source.file == 4 && current.file == 6) {
               if(source.rank == 0 && current.rank == 0 && toMove == Color.WHITE) {
                   if(whiteCanCastle[KINGSIDE])
                       break gooddirection;
               } else if(source.rank == 7 && current.rank == 7 && toMove == Color.BLACK) {
                       if(blackCanCastle[KINGSIDE])
                           break gooddirection;
               }
            } else if(!isInCheck() && source.file == 4 && current.file == 2) {
                if(source.rank == 0 && current.rank == 0 && toMove == Color.WHITE) {
                    if(whiteCanCastle[QUEENSIDE])
                        break gooddirection;
                } else if(source.rank == 7 && current.rank == 7 && toMove == Color.BLACK) {
                    if(blackCanCastle[QUEENSIDE])
                        break gooddirection;
                }
            }
            if(Math.abs(source.rank - current.rank) > 1 ||
                        Math.abs(source.file - current.file) > 1) {
                return null; // The king can only move one square
            }
            break;
        case QUEEN:
            break;
        case ROOK:
            switch(dir) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                break;
            default:
                return null;
            }
            break;
        case BISHOP:
            switch(dir) {
            case NORTHWEST:
            case NORTHEAST:
            case SOUTHWEST:
            case SOUTHEAST:
                break;
            default:
                return null;
            }
            break;
        case KNIGHT:
            break;
        case PAWN:
            if(colorAt(source) == Color.WHITE) {
                switch(current.rank - source.rank) {
                case 1:
                    break;
                case 2:
                    if(source.rank != 1)
                        return null;
                    break;
                default:
                    return null;
                }
                switch(dir) {
                case SOUTH:
                    if(piecetypeAt(current) != ChessPieceEnum.NONE)
                        return null;
                    break;
                case SOUTHEAST:
                    if(current.equals(enpassant))
                        break;
                    else if(piecetypeAt(current) == ChessPieceEnum.NONE)
                        return null;
                    break;
                case SOUTHWEST:
                    if(current.equals(enpassant))
                        break;
                    else if(piecetypeAt(current) == ChessPieceEnum.NONE)
                        return null;
                    break;
                case EAST: /* Use for en-passant */
                case WEST: /* Use for en-passant */
                    return null;
                default:
                    return null;
                }
            } else {
                switch(source.rank - current.rank) {
                case 1:
                    break;
                case 2:
                    if(source.rank != 6)
                        return null;
                    break;
                default:
                    return null;
                }
                switch(dir) {
                case NORTH:
                    if(piecetypeAt(current) != ChessPieceEnum.NONE)
                        return null;
                    break;
                case NORTHEAST:
                    if(current.equals(enpassant))
                        break;
                    else if(piecetypeAt(current) == ChessPieceEnum.NONE)
                        return null;
                    break;
                case NORTHWEST:
                    if(current.equals(enpassant))
                        break;
                    else if(piecetypeAt(current) == ChessPieceEnum.NONE)
                        return null;
                    break;
                case EAST: /* Use for en-passant */
                case WEST: /* Use for en-passant */
                    return null;
                default:
                    return null;
                }
            }
            break;
        }
        if(colorAt(current) == toMove)
            return null;
        else
            return current;
    }

    private void legalMoves(Square sq, ArrayList<Square[]> possibleMoves) {
        for(Direction d : Direction.values()) {
            for(Square current = travel(sq, new Square(sq), d); current != null ; current = travel(sq, current, d)) {
                possibleMoves.add(new Square[]{new Square(sq), new Square(current)});
            }
        }
    }
    
    private ArrayList<Square[]> collectPossibleMoves() {
        ArrayList<Square[]> possibleMoves = new ArrayList<Square[]>();
        Square sq = new Square(0, 0);
        for(sq.rank = 0 ; sq.rank < 8 ; sq.rank++) {
            for(sq.file = 0 ; sq.file < 8 ; sq.file++) {
                if(piecetypeAt(sq) != ChessPieceEnum.NONE && colorAt(sq) == this.toMove) {
                    legalMoves(sq, possibleMoves);
                }
            }
        }
        return possibleMoves;
    }
    
    private Square findKing() {
        Square king = new Square();
        kingloop:
        for(king.rank = 0 ; king.rank < 8 ; king.rank++) {
            for(king.file = 0 ; king.file < 8 ; king.file++) {
                if(piecetypeAt(king) == ChessPieceEnum.KING && colorAt(king) == toMove)
                    break kingloop;
            }
        }
        return king;
    }
    
    private void removeAllChecks(ArrayList<Square[]> moveList) throws Exception {
        Square king = findKing();
        for(Iterator<Square[]> it = moveList.iterator() ; it.hasNext() ; ) {
            Square[] sq = it.next();
            NormalChessBoard newboard = clone();
            newboard.checkinglegalmoves = true; // To prevent recursive errors
            newboard.makeMove(sq[0], sq[1], ChessPieceEnum.QUEEN, true); // For this, we don't care what piece may come of a "queen"
            Square kingcheck;
            if(sq[0].equals(king)) {
                kingcheck = sq[1];
            } else {
                kingcheck = king;
            }

            for(Square[] sq2 : newboard.collectPossibleMoves()) {
                if(sq2[1].equals(kingcheck)) {
                    it.remove();
                    break;
                }
            }
        }
    }

    private void fixupCastling(ArrayList<Square[]> moveList) {
        int kcastle = -1, kmove = -1;
        int qcastle = -1, qmove = -1;
        
        for(int m = 0 ; m < moveList.size() ; m++) {
            Square[] sq = moveList.get(m);
            if(sq[0].file == 4 && board[sq[0].rank][sq[0].file].piece() == ChessPieceEnum.KING) {
                if(sq[1].file == 5) kmove = m;
                else if(sq[1].file == 6) kcastle = m;
                else if(sq[1].file == 3) qmove = m;
                else if(sq[1].file == 2) qcastle = m;
            }
        }
        
        //
        // Remove castling where moving the king over is illegal
        // (i.e. Cannot castle across check)
        //
        if(kcastle != -1 && kmove == -1)
            moveList.remove(kcastle);
        if(qcastle != -1 && qmove == -1)
            moveList.remove(qcastle);
    }
    
    private boolean isInCheck() {
        if(checkinglegalmoves)
            return false;
        
        if(isincheck != null)
            return isincheck;
        
        NormalChessBoard newboard = clone();
        Square king = findKing();
        newboard.checkinglegalmoves = true;
        
        if(toMove == Color.BLACK)
            newboard.toMove = Color.WHITE;
        else
            newboard.toMove = Color.BLACK;
        
        for(Square[] sq : newboard.collectPossibleMoves()) {
            if(sq[1].equals(king)) {
                isincheck = true;
                return true;
            }
        }
        isincheck = false;
        return false;
    }
    
    public boolean hasLegalMoves() throws Exception {
        return legalMoves().size() != 0;
    }
    
    public ArrayList<Square[]> legalMoves() throws Exception {
        if(legalMoveList != null)
            return legalMoveList;
        
        legalMoveList = collectPossibleMoves();
        removeAllChecks(legalMoveList);
        fixupCastling(legalMoveList);
        
        /*
        if(LOG.isTraceEnabled()) {
            String out = "";
            String comma = "";
            for(Square[] sq : legalMoveList) {
                out += comma + sq[0] + "-" + sq[1];
                comma = ",";
            }
            //LOG.trace("Legal moves: "+out);
        }
             */
        return legalMoveList;
    }

    @Override
    public NormalChessBoard clone() {
        NormalChessBoard newboard = new NormalChessBoard();
        super.clone(newboard);
        newboard.movesto50 = movesto50;
        newboard.moveNumber = moveNumber;
        newboard.whiteCanCastle[0] = whiteCanCastle[0];
        newboard.whiteCanCastle[1] = whiteCanCastle[1];
        newboard.blackCanCastle[0] = blackCanCastle[0];
        newboard.blackCanCastle[1] = blackCanCastle[1];
        newboard.enpassant = enpassant;
        return newboard;
    }

    /**
     * Returns whether or not the moving color is able to castle to the king side.
     * 
     * @return True if the color can castle kingside. False if it cannot.
     */
    public boolean canCastleKingside() {
        if (toMove == Color.WHITE) {
            return whiteCanCastle[KINGSIDE];
        } else if (toMove == Color.BLACK) {
            return blackCanCastle[KINGSIDE];
        } else {
            return false;
        }
    }

    /**
     * Returns whether or not the moving color is able to castle to the queen side.
     * 
     * @return True if the color can castle queenside. False if it cannot.
     */
    public boolean canCastleQueenside() {
        if (toMove == Color.WHITE) {
            return whiteCanCastle[QUEENSIDE];
        } else if (toMove == Color.BLACK) {
            return blackCanCastle[QUEENSIDE];
        } else {
            return false;
        }
    }

    public boolean canCastle(Color color, int side) {
        if(side != KINGSIDE && side != QUEENSIDE)
            return false;
        if(color == Color.WHITE) {
            return whiteCanCastle[side];
        } else if(color == Color.BLACK) {
            return blackCanCastle[side];
        } else {
            return false;
        }
    }

    /**
     * Returns true if the color whos turn it is to move is able to perform an enpassant move.
     * 
     * @return True if the color can perform an en passant. False otherwise.
     */
    public boolean canEnPassant() {
        return (enpassant != null);
    }

    /**
     * Returns the two byte rank and file values of the square for which en passant is possible.
     * 
     * @return The en passant square. null if there is no en passant.
     */
    public Square enPassantSquare() {
        return enpassant;
    }

    /**
     * 
     * @return Returns the number of moves towards the 50 move rule. A value of 10 means there are 40 moves to go. A value of 50 means a draw is available.
     */
    public int moveTo50MoveRule() {
        return movesto50;
    }

    /**
     * Returns the FEN string for the current board position.
     * 
     * @return The current FEN.
     * @throws Exception 
     */
    public Fen getFEN() throws Exception {
        return new Fen(this);
    }

    @Override
    protected void makeMove(Square from, Square to) throws Exception {
        throw new Exception("Need to call the NormalChess makeMove method");
    }

    public Move makeMove(Move move) throws Exception {
        return makeMove(move.getFromSquare(), move.getToSquare(), move.getQueeningPiece());
    }
    
    protected Move makeMove(Square from, Square to, ChessPieceEnum queeningPiece) throws Exception
    {
    	return makeMove(from, to, queeningPiece, false);
    }
    protected Move makeMove(Square from, Square to, ChessPieceEnum queeningPiece, boolean internal) throws Exception {
    	
    	char movingPiece = upperChar(board[from.rank][from.file].piece().smithPiece());
    	String fromSquare = from.toString();
    	String tosquare = to.toString();
    	String   disambiguate = "";
    	char   capturedPiece = 0;
    	char   promotedTo = 0;
    	int[]    disambiguateI = {0,1,1}; // Because we already have one piece that can move to that square on the rank and file
    	ChessPieceEnum orig = board[from.rank][from.file].piece();

    	if(!internal && orig != ChessPieceEnum.PAWN && orig != ChessPieceEnum.KING)
    	{
	    	for(Square[] sq : this.legalMoves())
	    	{
	    		if(sq[1].equals(to) && !sq[0].equals(from) && board[sq[0].rank][sq[0].file].piece() == orig)
	    		{
	    			disambiguateI[0]++;
	    			if(sq[0].file == from.file) disambiguateI[1]++;
	    			if(sq[0].rank == from.rank) disambiguateI[2]++;
	    		}
	    	}
    	}
    	
    	if(disambiguateI[0] > 0)
    	{
	    	if(disambiguateI[1] > 1 && disambiguateI[2] > 1)
	    		disambiguate = from.toString();
	    	else if(disambiguateI[1] > 1)
	    		disambiguate += from.toString().charAt(1);
	    	else
	    		disambiguate += from.toString().charAt(0);
    	}
    	
    	if(board[to.rank][to.file].piece() != ChessPieceEnum.NONE)
    		capturedPiece = board[to.rank][to.file].piece().smithPiece();
    	
    	if(queeningPiece != ChessPieceEnum.NONE)
    		promotedTo = upperChar(queeningPiece.smithPiece());
    	
        if (board[from.rank][from.file].color() != toMove) {
            throwIllegalMoveException(from, to);
        }
        
        // the move should be legal, but if we check, we would recurse through all
        // possible chess move possible from here to the end of the game...
        // That could take a while :)
        
        if(!checkinglegalmoves && !this.isLegalMove(from, to)) {
            throwIllegalMoveException(from, to);
        }
        if (board[to.rank][to.file].piece() == ChessPieceEnum.NONE && board[from.rank][from.file].piece() != ChessPieceEnum.PAWN) {
            movesto50++;
        } else {
            movesto50 = 0;
        }

        if (board[from.rank][from.file].piece() == ChessPieceEnum.KING && from.file == 4 && to.file == 6) {
            board[to.rank][5].copy(board[to.rank][7]);
            board[to.rank][7].clear();
            fireSquareChanged(to.rank, 5);
            fireSquareChanged(to.rank, 7);
            capturedPiece = 'c';
        } else if (board[from.rank][from.file].piece() == ChessPieceEnum.KING && from.file == 4 && to.file == 2) {
            board[to.rank][3].copy(board[to.rank][0]);
            board[to.rank][0].clear();
            fireSquareChanged(to.rank, 3);
            fireSquareChanged(to.rank, 0);
            capturedPiece = 'C';
        }
        
        //
        // Remove castling rights if we are capturing an opponents rook
        //
        if(board[to.rank][to.file].piece() == ChessPieceEnum.ROOK)
        {
            if(to.rank == 0 && board[to.rank][to.file].color() == Color.WHITE) {
                if(to.file == 0) {
                    whiteCanCastle[QUEENSIDE] = false;
                } else if(to.file == 7) {
                    whiteCanCastle[KINGSIDE] = false;
                }
            } else if(to.rank == 7){
                if(to.file == 0) {
                    blackCanCastle[QUEENSIDE] = false;
                } else if(to.file == 7) {
                    blackCanCastle[KINGSIDE] = false;
                }
            }
        }
        
        if(board[from.rank][from.file].piece() == ChessPieceEnum.KING) {
            if(board[from.rank][from.file].color() == Color.WHITE) {
                whiteCanCastle = new boolean[]{false,false};
            } else {
                blackCanCastle = new boolean[]{false,false};
            }
        } else if(board[from.rank][from.file].piece() == ChessPieceEnum.ROOK) {
            if(from.rank == 0 && board[from.rank][from.file].color() == Color.WHITE) {
                if(from.file == 0) {
                    whiteCanCastle[QUEENSIDE] = false;
                } else if(from.file == 7) {
                    whiteCanCastle[KINGSIDE] = false;
                }
            } else if(from.rank == 7){
                if(from.file == 0) {
                    blackCanCastle[QUEENSIDE] = false;
                } else if(from.file == 7) {
                    blackCanCastle[KINGSIDE] = false;
                }
            }
        } else if(board[to.rank][to.file].piece() == ChessPieceEnum.ROOK) {
            if(to.rank == 0 && board[to.rank][to.file].color() == Color.WHITE) {
                if(to.file == 0) {
                    whiteCanCastle[QUEENSIDE] = false;
                } else if(to.file == 7) {
                    whiteCanCastle[KINGSIDE] = false;
                }
            } else if(to.rank == 7){
                if(to.file == 0) {
                    blackCanCastle[QUEENSIDE] = false;
                } else if(to.file == 7) {
                    blackCanCastle[KINGSIDE] = false;
                }
            }
        }

        super.makeMove(from, to);
        moveNumber++;

        // a7a8Q
        if (board[to.rank][to.file].piece() == ChessPieceEnum.PAWN && (to.rank == 0 || to.rank == 7)) {
            board[to.rank][to.file].setPiece(queeningPiece);
            fireSquareChanged(to.rank, to.file);
        }
        
        if(to.equals(enpassant) && board[to.rank][to.file].piece() == ChessPieceEnum.PAWN) {
            board[from.rank][to.file].clear();
            fireSquareChanged(to.rank, to.file);
        	capturedPiece = 'E';
        }

        if (board[to.rank][to.file].piece() == ChessPieceEnum.PAWN) {
            if ((from.rank == 1 && to.rank == 3) || (from.rank == 6 && to.rank == 4)) {
                enpassant = new Square(from);
                if(board[to.rank][to.file].color() == Color.WHITE) {
                    enpassant.rank++;
                } else {
                    enpassant.rank--;
                }
                
            	Square left = null, right = null;
            	boolean pawncanep = false;
            	if(to.file != 0) left = new Square(to.rank, to.file - 1);
            	if(to.file != 7) right = new Square(to.rank, to.file + 1);
            	// We have already changed the color to move when we get here.
            	if(left != null && board[left.rank][left.file].piece() == ChessPieceEnum.PAWN && board[left.rank][left.file].color() == toMove) pawncanep = true;
            	if(right != null && board[right.rank][right.file].piece() == ChessPieceEnum.PAWN && board[right.rank][right.file].color() == toMove) pawncanep = true;
            	if(!pawncanep) enpassant = null;
            } else {
                enpassant = null;
            }
        } else
            enpassant = null;

        isincheck = null;
        isincheck = isInCheck();
        legalMoveList = null;
        if(!checkinglegalmoves)
            add3Fold();

        if(internal)
        	return null;
        
    	String longsmithmove = fromSquare + tosquare;
        if(capturedPiece != 0) longsmithmove += capturedPiece;
        if(promotedTo != 0) longsmithmove += promotedTo;
        
        String algebraicmove;
        if(capturedPiece == 'c') algebraicmove = "O-O";
        else if(capturedPiece == 'C') algebraicmove = "O-O-O";
        else
        {
        	if(movingPiece == 'P')
        	{
        		if(capturedPiece != 0)
        			algebraicmove = "" + from.toString().charAt(0);
        		else
        			algebraicmove = "";
        	}
        	else
        		algebraicmove = "" + movingPiece;
        	if(disambiguate.length() != 0)
        	{
        		if(disambiguate.length() == 2 && disambiguate.charAt(0) >= '0' && disambiguate.charAt(0) <= '8')
        		{
        			disambiguate = "" + disambiguate.charAt(1) + disambiguate.charAt(0);
        		}
        		algebraicmove += disambiguate;
        	}
        	if(capturedPiece != 0) algebraicmove += "x";
        	algebraicmove += tosquare;
        	if(promotedTo != 0)
        		algebraicmove += "=" + promotedTo;
        }
    	if(isCheckmate())
    		algebraicmove += '#';
    	else if(isincheck)
    		algebraicmove += '+';
        return new Move(longsmithmove, algebraicmove);
    }
    
    private char upperChar(char c)
    {
    	if(c >= 'a' && c <= 'z') c -= 32;
    	return c;
    }
    /**
     * Resets this board using the FEN position specified.
     * 
     * @param fen
     *            The input FEN string to set the board to.
     * @throws Exception 
     */
    @Override
    public void loadFEN(final String fen) throws Exception {
        loadFEN(new Fen(fen));
    }
    
    public void loadFEN(final Fen fen) {
        isincheck = null;
        legalMoveList = null;
        board = fen.board();
        toMove = fen.toMove();
        moveNumber = fen.moveNumber();
        movesto50 = fen.movesto50();
        whiteCanCastle = fen.whiteCanCastle();
        blackCanCastle = fen.blackCanCastle();
        enpassant = fen.enpassant();
    }
    
    private void add3Fold() {
        if(can3fold) {
            return;
        }
		try {
	        String fen;
			fen = (new Fen(this)).getShortFEN();
	        Integer i = fold3.get(fen);
	        if(i == null)
	            i = new Integer(0);
            can3fold = (++i == 3);
	        fold3.put(fen,  i);
		} catch (Exception e) {
			LOG.error("Exception " + e);
			e.printStackTrace();
		}
    }
    
    public boolean canDraw() {
        return (can3fold || movesto50 >= 50);
    }

    public int moveNumber() {
        return moveNumber;
    }

    public boolean isCheckmate() {
        try {
            return (isInCheck() && legalMoves().size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isStalemate() {
        try {
            return (!isInCheck() && legalMoves().size() == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
