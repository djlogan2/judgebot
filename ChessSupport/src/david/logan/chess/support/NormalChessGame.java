package david.logan.chess.support;

import java.util.Iterator;

/**
 * The board plus move list and variations, including the ability to move back and forth
 * @author davidlogan
 *
 */
public class NormalChessGame extends NormalChessBoard {
    protected class ncbmove extends Move {
        public int movesto50;
        public boolean[] whiteCanCastle;
        public boolean[] blackCanCastle;
        public ncbmove(Move move, int movesto50, boolean[] whiteCanCastle, boolean[] blackCanCastle) {
            super(move);
            this.movesto50 = movesto50;
            this.whiteCanCastle = whiteCanCastle.clone();
            this.blackCanCastle = blackCanCastle.clone();
        }
    }
    private MoveList moveList;
    private MoveList.moveIterator moveIterator;

    public NormalChessGame() {
        moveList = new MoveList();
        moveIterator = moveList.iterator();
    }
    
    public NormalChessGame(Fen fen)
    {
    	super(fen);
        moveList = new MoveList();
        moveIterator = moveList.iterator();
    }
    
    public Move makeMove(Move move) throws Exception {
    	Move superMove = super.makeMove(move);
    	moveList.add(superMove);
    	return superMove;
    }
    
    public MoveList getMoveList() { return moveList; }

    public boolean hasForward() {
        return (moveIterator != null && moveIterator.hasNext());
    }
    public boolean hasBackward() {
        return (moveIterator != null && moveIterator.hasPrev());
    }

    public boolean forward() {
        
        if(moveIterator == null || !moveIterator.hasNext())
            return false;
        ncbmove move = (ncbmove) moveIterator.forward();
        
        try {
            makeMove(move);
            movesto50 = move.movesto50;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public Move lastMove() {
        return moveIterator.lastmove();
    }
    
    public Move nextMove() {
        return moveIterator.peeknext();
    }
    
    public void start() {
        fireEvents = false;
        moveIterator = moveList.iterator();
        while(moveIterator.hasPrev()) moveIterator.backward();
        fireEvents = true;
        fireBoardChanged();
    }
    
    public void end() {
        fireEvents = false;
        while(moveIterator.hasNext())
            moveIterator.forward();
        fireEvents = true;
        fireBoardChanged();
    }
    public boolean backward() {
        if(!moveIterator.hasPrev())
            return false;
        try {
            ncbmove move = unmakeMove();
            movesto50 = move.movesto50;
            whiteCanCastle = move.whiteCanCastle.clone();
            blackCanCastle = move.blackCanCastle.clone();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    public boolean hasVariation() {
//        if(movePointer == moveList.size())
//            return false;
//        Move[] var = moveList.get(movePointer).move.getVariations();
//        return (var != null && var.length != 0);
//    }
    
    protected ncbmove unmakeMove() {
    	ncbmove m = (ncbmove)moveList.removeLastMove();
    	if(m == null) {
    		return null;
    	}
        ChessPieceEnum queeningPiece = m.getQueeningPiece();
        ChessPieceEnum capturedPiece = m.getCapturedPiece();
        Square ep = null; //= m.getEnPassantSquare();
        Square from = m.getToSquare();
        Square to = m.getFromSquare();
        
        if(m.isEnPassant()) {
            ep = new Square(from.rank, to.file);
        }
        
        if(queeningPiece != null) {
            board[to.rank][to.file].setColor(board[from.rank][from.file].color());
            board[to.rank][to.file].setPiece(ChessPieceEnum.PAWN);
            board[from.rank][from.file].clear();
            fireSquareChanged(to);
            fireSquareChanged(from);
            toMove = toMove.other();
            return m;
        }
        
        if(m.isKingsideCastle()) {
            board[to.rank][4].copy(board[from.rank][6]);
            board[to.rank][7].copy(board[to.rank][5]);
            board[to.rank][5].clear();
            board[to.rank][6].clear();
            fireSquareChanged(to.rank, 4);
            fireSquareChanged(to.rank, 5);
            fireSquareChanged(to.rank, 6);
            fireSquareChanged(to.rank, 7);
            toMove = toMove.other();
            return m;
        }
        
        if(m.isQueensideCastle()) {
            board[to.rank][4].copy(board[from.rank][2]);
            board[to.rank][0].copy(board[to.rank][3]);
            board[to.rank][2].clear();
            board[to.rank][3].clear();
            fireSquareChanged(to.rank, 0);
            fireSquareChanged(to.rank, 2);
            fireSquareChanged(to.rank, 3);
            fireSquareChanged(to.rank, 4);
            toMove = toMove.other();
            return m;
        }
        
        board[to.rank][to.file].copy(board[from.rank][from.file]);
        
        if(capturedPiece != null) {
            board[from.rank][from.file].setPiece(capturedPiece);
            board[from.rank][from.file].setColor(board[from.rank][from.file].color().other());
        } else
            board[from.rank][from.file].clear();

        fireSquareChanged(from);
        fireSquareChanged(to);
        
        if(ep != null) {
            board[to.rank][from.file].setPiece(ChessPieceEnum.PAWN);
            board[to.rank][from.file].setColor(board[to.rank][to.file].color().other());
            fireSquareChanged(to.rank, from.file);
        }
        
        toMove = toMove.other();
        return m;
    }
    
    public void loadGame(PGNGame pgnGame) throws Exception {
        fireEvents = false;

        if(pgnGame.hasFEN())
            loadFEN(pgnGame.getFEN());
        else
            setStartingPosition();
        
        for(Iterator<Move> i = moveList.iterator() ; i.hasNext() ; ) {
            Move move = i.next();
            moveList.add(new ncbmove(move, movesto50, whiteCanCastle, blackCanCastle));
            makeMove(move);
        }
        fireEvents = true;
        fireBoardChanged();
    }
    
    public boolean canWinOnTime(Color color) {
        ChessPiece piece = null;
        for (int rank = 0; rank < ROWS; rank++) {
            for (int file = 0; file < COLUMNS; file++) {
                if(board[rank][file].color() == color) {
                    switch(board[rank][file].piece()) {
                    case KING: break;
                    case KNIGHT:
                    	// Two knights doesn't mean we can win on time
                    	if(piece.piece() == ChessPieceEnum.KNIGHT)
                    		break;
                    case BISHOP:
                        // If there is more than K + one piece on the board, the color can win on time
                        if(piece != null)
                            return true;
                        piece = board[rank][file];
                        break;
                        // If the piece isn't a K, N, or B, we can win on time
                    default:
                        return true;
                    }
                }
            }
        }
        return false; // This means we have no piece, meaning the color has only a K
        // or we have one piece, which means we have K+N or K+B only on the board
    }
}
