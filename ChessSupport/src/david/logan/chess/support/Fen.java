package david.logan.chess.support;


public class Fen {
    public final static int KINGSIDE = 0;
    public final static int QUEENSIDE = 1;
    private final static int COLUMNS = 8;
    private final static int ROWS = 8;
    protected ChessPiece _board[][] = new ChessPiece[8][8];
    protected String shortfen;
    protected String fen;
    private Color toMove;
    protected boolean[] whiteCanCastle = new boolean[]{false,false};
    protected boolean[] blackCanCastle = new boolean[]{false, false};
    private Square enpassant = null;
    private int movesto50;
    private int moveNumber;

    private static char[][] PIECELETTER = new char[][] {new char[] {'-', '-', '-', '-', '-', '-', '-'}, new char[] {'-', 'k', 'q', 'b', 'n', 'r', 'p'}, new char[] {'-', 'K', 'Q', 'B', 'N', 'R', 'P'}};
    public Fen(NormalChessBoard board) throws Exception {
        
        for(int rank = 0 ; rank < ROWS ; rank++) {
            for(int file = 0 ; file < COLUMNS ; file++) {
                    _board[rank][file] = new ChessPiece(board.board[rank][file]);
            }
        }
        shortfen = "";
        byte b;
        Square sq = new Square();
        for (sq.rank = ROWS - 1; sq.rank >= 0; sq.rank--) {
            b = 0;
            for (sq.file = 0; sq.file < COLUMNS; sq.file++) {
                if (board.piecetypeAt(sq) == ChessPieceEnum.NONE) { //board[rank][file].piece() == ChessPieceEnum.NONE) {
                    b++;
                } else {
                    if (b != 0) {
                        shortfen += (char)(b + '0');
                        b = 0;
                    }
                    try {
                    shortfen += PIECELETTER[board.colorAt(sq).ordinal()][board.piecetypeAt(sq).ordinal()];
                    } catch(Exception e) {
                        System.out.println("Stop here");
                    }
                }
            }
            if (b != 0) {
                shortfen += (char)(b + '0');
                b = 0;
            }
            if (sq.rank != 0) {
                shortfen += '/';
            }
        }
    
        this.toMove = board.toMove;
        if (board.toMove == Color.WHITE) {
            shortfen += " w";
        } else {
            shortfen += " b";
        }
    
        whiteCanCastle[KINGSIDE] = board.canCastle(Color.WHITE, KINGSIDE); 
        whiteCanCastle[QUEENSIDE] = board.canCastle(Color.WHITE, QUEENSIDE); 
        blackCanCastle[KINGSIDE] = board.canCastle(Color.BLACK, KINGSIDE); 
        blackCanCastle[QUEENSIDE] = board.canCastle(Color.BLACK, QUEENSIDE);
        
        checkCastling();
        
        String castle = "";
        if (board.canCastle(Color.WHITE, KINGSIDE)) {
            castle += 'K';
        }
        if (board.canCastle(Color.WHITE, QUEENSIDE)) {
            castle += 'Q';
        }
        if (board.canCastle(Color.BLACK, KINGSIDE)) {
            castle += 'k';
        }
        if (board.canCastle(Color.BLACK, QUEENSIDE)) {
            castle += 'q';
        }
        if (castle.length() == 0) {
            castle = "-";
        }

        shortfen += ' ' + castle;
        if (board.enPassantSquare() == null) {
            shortfen += " -";
        } else {
            shortfen += ' ' + board.enPassantSquare().toString();
            enpassant = new Square(board.enPassantSquare());
        }
        
        fen = shortfen;
        fen += ' ' + Integer.toString(board.moveTo50MoveRule());
        fen += ' ' + Integer.toString(((board.moveNumber()-1)/2)+1);
        movesto50 = board.moveTo50MoveRule();
        moveNumber = ((board.moveNumber()-1)/2)+1;
    }
    
    public Fen(Fen other) {
    	for(int x = 0 ; x < 8 ; x++) {
    		for(int y = 0 ; y < 8 ; y++) {
    			_board[x][y] = other._board[x][y];
    		}
    	}
    	shortfen = other.shortfen;
    	fen = other.fen;
    	toMove = other.toMove;
    	whiteCanCastle[0] = other.whiteCanCastle[0];
    	whiteCanCastle[1] = other.whiteCanCastle[1];
    	blackCanCastle[0] = other.blackCanCastle[0];
    	blackCanCastle[1] = other.blackCanCastle[1];
    	enpassant = other.enpassant;
    	movesto50 = other.movesto50;
    	moveNumber = other.moveNumber;
    }
    
    public Fen(String fen) throws Exception {
        this.shortfen = fen;
        this.fen = fen;
        
        int rank = ROWS - 1;
        int file = 0;

        Square sq = new Square();
        for (sq.rank = 0; sq.rank < ROWS; sq.rank++) {
            for (sq.file = 0; sq.file < COLUMNS; sq.file++) {
                _board[sq.rank][sq.file] = new ChessPiece(Color.NONE, ChessPieceEnum.NONE);
            }
        }

        String[] fensplit = fen.split("\\s+");
        for (byte b : fensplit[0].getBytes()) {
            if (b >= '1' && b <= '8') {
                file += (b - '1') + 1;
            } else if (b == '/') {
            	if(file != 8)
            		throw new Exception("Invalid FEN String");
                file = 0;
                rank--;
            } else {
            	if(rank >= ROWS || rank < 0 || file >= COLUMNS || file < 0)
            		throw new Exception("Invalid FEN String");
                _board[rank][file] = new ChessPiece(b);
                file++;
            }
        }
        
        if(file != 8 || rank != 0)
        	throw new Exception("Invalid FEN String");
    
        switch (fensplit[1].charAt(0)) {
        case 'w':
            toMove = Color.WHITE;
            break;
        case 'b':
            toMove = Color.BLACK;
            break;
        default:
        	throw new Exception("Invalid FEN String");
        }
    
        for (byte b : fensplit[2].getBytes()) {
            switch (b) {
            case 'K':
                whiteCanCastle[KINGSIDE] = true;
                break;
            case 'Q':
                whiteCanCastle[QUEENSIDE] = true;
                break;
            case 'k':
                blackCanCastle[KINGSIDE] = true;
                break;
            case 'q':
                blackCanCastle[QUEENSIDE] = true;
            case '-':
                break;
            default: 
            	throw new Exception("Invalid FEN String");
            }
        }
        if (!"-".equals(fensplit[3])) {
        	checkEnPassant(new Square(fensplit[3]));
        }
        try {
	        movesto50 = Integer.parseInt(fensplit[4]);
	        if(fensplit.length == 6)
	        	moveNumber = Integer.parseInt(fensplit[5]) * 2 - (toMove == Color.WHITE ? 1 : 0);
	        else
	        	moveNumber = 1;
        } catch(Exception e) {
        	throw new Exception("Invalid FEN String");
        }
        checkCastling();
    }

    protected void checkCastling() throws Exception {
        if(_board[0][4].piece() != ChessPieceEnum.KING || _board[0][4].color() != Color.WHITE) {
        	if(whiteCanCastle[KINGSIDE] || whiteCanCastle[QUEENSIDE]) throw new Exception("Invalid FEN String");
        } else {
        	if(_board[0][0].piece() != ChessPieceEnum.ROOK || _board[0][0].color() != Color.WHITE)
            	if(whiteCanCastle[QUEENSIDE]) throw new Exception("Invalid FEN String");
        	if(_board[0][7].piece() != ChessPieceEnum.ROOK || _board[0][7].color() != Color.WHITE)
            	if(whiteCanCastle[KINGSIDE]) throw new Exception("Invalid FEN String");
        }
        
        if(_board[7][4].piece() != ChessPieceEnum.KING || _board[7][4].color() != Color.BLACK) {
        	if(blackCanCastle[KINGSIDE] || blackCanCastle[QUEENSIDE]) throw new Exception("Invalid FEN String");
        } else {
        	if(_board[7][0].piece() != ChessPieceEnum.ROOK || _board[7][0].color() != Color.BLACK)
            	if(blackCanCastle[QUEENSIDE]) throw new Exception("Invalid FEN String");
        	if(_board[7][7].piece() != ChessPieceEnum.ROOK || _board[7][7].color() != Color.BLACK)
            	if(blackCanCastle[KINGSIDE]) throw new Exception("Invalid FEN String");
        }
    }
    
    private void checkEnPassant(Square ep) {
    	int rank = ep.rank;
    	ChessPiece p = null;
    	if(this.toMove == Color.WHITE) rank--; else rank++;
    	if(ep.file != 0) p = _board[rank][ep.file-1];
    	if(p != null && p.color() == toMove && p.piece() == ChessPieceEnum.PAWN) {
    		enpassant = ep;
    		return;
    	}
    	if(ep.file != 7) p = _board[rank][ep.file+1];
    	if(p != null && p.color() == toMove && p.piece() == ChessPieceEnum.PAWN) {
    		enpassant = ep;
    		return;
    	}
    }
    
    public void clearboard() {
        movesto50 = 0;
        whiteCanCastle = new boolean[] {false, false};
        blackCanCastle = new boolean[] {false, false};
        enpassant = null;
    }

    public int movesto50() {
        return movesto50;
    }
    
    public boolean[] whiteCanCastle() {
        return whiteCanCastle;
    }
    
    public boolean[] blackCanCastle() {
        return blackCanCastle;
    }

    public Square enpassant() {
        return enpassant;
    }
    
    public String getFEN() {
        return fen;
    }
    
    public String getShortFEN() {
        return shortfen;
    }
    
    @Override
    public String toString() {
        return shortfen;
    }

    public Color toMove() {
        return toMove;
    }

    public ChessPiece[][] board() {
        ChessPiece[][] newBoard = new ChessPiece[8][8];
        
        for(int rank = 0 ; rank < ROWS ; rank++) {
            for(int file = 0 ; file < COLUMNS ; file++) {
                    newBoard[rank][file] = new ChessPiece(_board[rank][file]);
            }
        }
        
        return newBoard;
    }

    public int moveNumber() {
        return moveNumber;
    }
    
    public int pieceCount() {
    	int pc = 0;
        for(int rank = 0 ; rank < ROWS ; rank++) {
            for(int file = 0 ; file < COLUMNS ; file++) {
            	if(_board[rank][file].piece() != ChessPieceEnum.NONE)
            		pc++;
            }
        }
        return pc;
    }
}
