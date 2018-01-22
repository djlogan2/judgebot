package david.logan.chess.support;

public class Move {

    protected ChessPieceEnum movingPiece = null;       // PGN
    protected Square fromSquare = null;                //      SMTH
    protected Square toSquare = null;                  // PGN  SMTH
    protected ChessPieceEnum capturedPiece = null;     //      SMTH
    protected ChessPieceEnum promotedTo = null;        // PGN  SMTH
    protected Character checkmate = null;              // PGN
    protected Boolean enpassant = null;                //      SMTH
    protected enum algebraicFromE {
        UNKNOWN, NONE, RANK, FILE, BOTH
    }
    protected algebraicFromE algebraicFrom = algebraicFromE.UNKNOWN;

    public Square getFromSquare() { return fromSquare; }
    public Square getToSquare() { return toSquare; }
    public ChessPieceEnum getQueeningPiece() { return promotedTo; }
    public ChessPieceEnum getCapturedPiece() { return capturedPiece; }
    public ChessPieceEnum getMovingPiece() { return movingPiece; }
    public boolean isEnPassant() { return enpassant; }
    
    @Override
    public Move clone() {
    	Move nm = new Move();
    	nm.algebraicFrom = this.algebraicFrom;
    	nm.capturedPiece = this.capturedPiece;
    	nm.checkmate = this.checkmate;
    	nm.enpassant = this.enpassant;
    	nm.fromSquare = new Square(this.fromSquare);
    	nm.movingPiece = this.movingPiece;
    	nm.promotedTo = this.promotedTo;
    	nm.toSquare = new Square(this.toSquare);
    	return nm;
    }
    
    protected Move() {
        // Subclasses will have to fill everything out themselves!
    }
    
    public Move(Move other) {
        movingPiece = other.movingPiece;
        fromSquare = other.fromSquare;
        toSquare = other.toSquare;
        capturedPiece = other.capturedPiece;
        promotedTo = other.promotedTo;
        checkmate = other.checkmate;
        enpassant = other.enpassant;
        algebraicFrom = other.algebraicFrom;
    }
    
    public Move(String longsmithmove, String algebraic) throws Exception {
        set(longsmithmove, algebraic);
    }
    
    protected void set(String longsmithmove, String algebraic) throws Exception {
        if(longsmithmove != null)
            set(longsmithmove, false);
        
        if(algebraic == null)
            return;
        
        boolean castle = false;
        if (algebraic.startsWith("O-O-O")) {
            movingPiece = ChessPieceEnum.KING;
            promotedTo = ChessPieceEnum.NONE;
            capturedPiece = ChessPieceEnum.NONE;
            algebraicFrom = algebraicFromE.NONE;
            castle = true;
        } else if (algebraic.startsWith("O-O")) {
            movingPiece = ChessPieceEnum.KING;
            promotedTo = ChessPieceEnum.NONE;
            capturedPiece = ChessPieceEnum.NONE;
            algebraicFrom = algebraicFromE.NONE;
            castle = true;
        }

        int cur = algebraic.length() - 1;

        if(promotedTo == null)
            promotedTo = ChessPieceEnum.NONE;
        checkmate = 0;
        
        if(algebraic.charAt(cur) == '+' || algebraic.charAt(cur) == '#') {
            checkmate = algebraic.charAt(cur--);
        }
        
        if(castle)
            return;
        
        if(algebraic.charAt(cur-1) == '=') {
            switch(algebraic.charAt(cur)) {
            case 'Q': promotedTo = ChessPieceEnum.QUEEN; break;
            case 'R': promotedTo = ChessPieceEnum.ROOK; break;
            case 'N': promotedTo = ChessPieceEnum.KNIGHT; break;
            case 'B': promotedTo = ChessPieceEnum.BISHOP; break;
            }
            cur -= 2;
        }
        
        cur --;
        toSquare = new Square(algebraic.substring(cur,  cur + 2));
        cur--;
        
        if(cur == -1) {
            movingPiece = ChessPieceEnum.PAWN;
            algebraicFrom = algebraicFromE.NONE;
            return;
        }
        
        if(algebraic.charAt(cur) == 'x')
            cur--;
        else
            capturedPiece = ChessPieceEnum.NONE;
        
        algebraicFrom = algebraicFromE.NONE;
        if(algebraic.charAt(cur) >= '0' && algebraic.charAt(cur) <= '8') {
        	if(fromSquare == null)
        		fromSquare = new Square("z" + algebraic.charAt(cur));
        	algebraicFrom = algebraicFromE.RANK;
        	cur--;
        }
        
        if(algebraic.charAt(cur) >= 'a' && algebraic.charAt(cur) <= 'h') {
    		if(fromSquare == null)
    			fromSquare = new Square("" + algebraic.charAt(cur) + "0");
    		//else
    		//	fromSquare.rank = (algebraic.charAt(cur) - 'a' );
        	if(algebraicFrom == algebraicFromE.NONE)
        		algebraicFrom = algebraicFromE.FILE;
        	else
        		algebraicFrom = algebraicFromE.BOTH;
            cur--;
        }
        
        if(cur == -1) {
            movingPiece = ChessPieceEnum.PAWN;
            return;
        }

        if(cur != 0)
            throw new Exception("Unknown error processing PGN move "+ algebraic +", cur expected 0 but was "+cur);
        
        switch(algebraic.charAt(0)) {
        case 'R': movingPiece = ChessPieceEnum.ROOK; break;
        case 'N': movingPiece = ChessPieceEnum.KNIGHT; break;
        case 'B': movingPiece = ChessPieceEnum.BISHOP; break;
        case 'Q': movingPiece = ChessPieceEnum.QUEEN; break;
        case 'K': movingPiece = ChessPieceEnum.KING; break;
        default: movingPiece = ChessPieceEnum.PAWN;
        }
    }
    /**
     * 
     * @param smithmove The SMITH move string
     * @param shortsmith True if this is a "short SMITH" ... in other words, nothing more than from square, to square, promote char. If this is false, this is a "long SMITH", which means it has capture information, castling, and en passant in addition to capture indicator.
     * @throws Exception 
     */
    public Move(String smithmove, boolean shortsmith) throws Exception {
        set(smithmove, shortsmith);
    }
    
    protected void set(String smithmove, boolean shortsmith) throws Exception {
        fromSquare = new Square(smithmove.substring(0, 2));
        toSquare = new Square(smithmove.substring(2, 4));
        
        if(shortsmith) {
            if(smithmove.length() > 5) throw new Exception("A short smith move can have at most 5 characters!");
            if(smithmove.length() == 4) {
                promotedTo = ChessPieceEnum.NONE;
                return;
            }
            switch(smithmove.charAt(4)) {
            case 'R':
            case 'r': promotedTo = ChessPieceEnum.ROOK; break;
            case 'N':
            case 'n': promotedTo = ChessPieceEnum.KNIGHT; break;
            case 'B':
            case 'b': promotedTo = ChessPieceEnum.BISHOP; break;
            case 'Q':
            case 'q': promotedTo = ChessPieceEnum.QUEEN; break;
            default:
                throw new Exception("Unable to determine what this promoting piece character is supposed to be: '"+smithmove.charAt(4)+"'");
            }
            return;
        }
        
        capturedPiece = ChessPieceEnum.NONE;
        promotedTo = ChessPieceEnum.NONE;
        enpassant = false;
        
        for(int x = 4 ; x < smithmove.length() ; x++)
            switch(smithmove.charAt(x)) {
            case 'N':
                promotedTo = ChessPieceEnum.KNIGHT;
                break;
            case 'B':
                promotedTo = ChessPieceEnum.BISHOP;
                break;
            case 'R':
                promotedTo = ChessPieceEnum.ROOK;
                break;
            case 'Q':
                promotedTo = ChessPieceEnum.QUEEN;
                break;
            case 'E':
                movingPiece = ChessPieceEnum.PAWN;
                enpassant = true;
            case 'p':
                capturedPiece = ChessPieceEnum.PAWN;
                break;
            case 'n':
                capturedPiece = ChessPieceEnum.KNIGHT;
                break;
            case 'b':
                capturedPiece = ChessPieceEnum.BISHOP;
                break;
            case 'r':
                capturedPiece = ChessPieceEnum.ROOK;
                break;
            case 'q':
                capturedPiece = ChessPieceEnum.QUEEN;
                break;
            case 'k':
                capturedPiece = ChessPieceEnum.KING;
                break;
            case 'c':
                capturedPiece = ChessPieceEnum.NONE;
                movingPiece = ChessPieceEnum.KING;
                promotedTo = ChessPieceEnum.NONE;
                enpassant = false;
                algebraicFrom = algebraicFromE.NONE;
                break;
            case 'C':
                capturedPiece = ChessPieceEnum.NONE;
                movingPiece = ChessPieceEnum.KING;
                promotedTo = ChessPieceEnum.NONE;
                enpassant = false;
                algebraicFrom = algebraicFromE.NONE;
                break;
            }
    }
    
    @Override
    public String toString() {
        return "Move " + movingPiece + " moves from " + fromSquare + " to " + toSquare + " taking " + capturedPiece + " promoting to " + promotedTo;
    }

    public boolean isKingsideCastle() {
        if(fromSquare.equals("e1") && toSquare.equals("g1") && movingPiece == ChessPieceEnum.KING)
            return true;
        if(fromSquare.equals("e8") && toSquare.equals("g8") && movingPiece == ChessPieceEnum.KING)
            return true;
        return false;
    }
    public boolean isQueensideCastle() {
        if(fromSquare.equals("e1") && toSquare.equals("c1") && movingPiece == ChessPieceEnum.KING)
            return true;
        if(fromSquare.equals("e8") && toSquare.equals("c8") && movingPiece == ChessPieceEnum.KING)
            return true;
        return false;
    }
    /**
     * Returns the algebraic notation for this move.
     * @return  the algebraic notation for this move.
     * @throws Exception if this move does not have the appropriate algebraic move construction information 
     */
    public String getAlgebraicMove() throws Exception {
        String ret = "";
        if(fromSquare == null | toSquare == null)  throw new Exception("Algebraic notation is unknown ('from or to square' is UNKNOWN')");
        if(movingPiece == null) throw new Exception("Algebraic notation is unknown ('moving piece is UNKNOWN'");
        
        if(isKingsideCastle())
            return "O-O" + (checkmate == null || checkmate == 0x00 ? "" : checkmate);
        if(isQueensideCastle())
            return "O-O-O" + (checkmate == null || checkmate == 0x00 ? "" : checkmate);
        
        switch(movingPiece) {
        case KING: ret = "K"; break;
        case QUEEN: ret = "Q"; break;
        case ROOK: ret = "R"; break;
        case KNIGHT: ret = "N"; break;
        case BISHOP: ret = "B"; break;
        case PAWN: break;
        case NONE:
            throw new Exception("Algebraic notation is unknown ('moving piece' is NONE')");
        }

        if(algebraicFrom == null) throw new Exception("Algebraic notation is unknown ('from' is UNKNOWN)");
        switch(algebraicFrom) {
        case UNKNOWN: throw new Exception("Algebraic notation is unknown ('from' is UNKNOWN)");
        case NONE: break;
        case FILE: ret += fromSquare.toString().charAt(0); break;
        case RANK: ret += fromSquare.toString().charAt(1); break;
        case BOTH: ret += fromSquare.toString(); break;
        }
        
        if(capturedPiece == null) throw new Exception("Algebraic notation is unknown ('captured piece' is UNKNOWN')");
        if(capturedPiece != ChessPieceEnum.NONE)
            ret += "x";
        
        ret += toSquare.toString();
        
        if(promotedTo == null) throw new Exception("Algebraic notation is unknown ('promoted piece' is UNKNOWN')");
        switch(promotedTo) {
        case ROOK: ret += "=R"; break;
        case BISHOP: ret += "=B"; break;
        case KNIGHT: ret += "=N"; break;
        case QUEEN: ret += "=Q"; break;
        case NONE: break;
        case KING:
        case PAWN:
        default:
            throw new Exception("Error...");
        }
        
        if(checkmate == null) throw new Exception("Algebraic notation is unknown ('check/mate' is UNKNOWN')");
        if(checkmate != 0)
            ret += checkmate;
        
        return ret;
    }

    /**
     * Returns the smith notation for this move.
     * <p>  This format is defined as follows:
    <p>&ltfrom square&gt&ltto square&gt[&ltcapture indicator&gt][&ltpromoted to&gt]
    <p>    2 chars     2 chars       0 or 1 char       0 or 1 char
    <p>
    <p>The capture indicator is one of pnbrqkEcC.
    <p>It indicates the type of piece captured.  "c" indicates a short castling
    move (in which case the coordinates are for the king's movements), and C
    indicates a long castling move.  A "E" indicates an en-passant capture.
    If it's not a capture, or castling move, the field is empty.
    <p>The promotion information is one of "NBRQ", indicating the promoted piece.
    <p>example:  <ul><li>e4g5p   is a N move from e4 to g5 capturing a pawn.</li>
          <li>f7f8Q   is a queening move</li>
          <li>f7g8nQ  is a pawn move capturing a knight and queening.</li>
    </ul>
    <p>One advantage of this system that it's easy to parse, and it's
    reversible--you can walk forward and backward through the move
    list only knowing the current position at a given time.
    The notation is an "improvement" of a notation suggested
    by Warren Smith.
    <p>Note: bughouse plunks are the same in all move formats, e.g. "Q@f7+"
    Mystery kriegspiel moves are either "?" or of the form "?xb1".
    
     * @return  the smith notation for this move.
     * @throws Exception 
     */
    public String getSmithMove() throws Exception {
        String ret = fromSquare.toString() + toSquare.toString();

        if(isKingsideCastle())
            ret += 'c';
        else if(isQueensideCastle())
            ret += 'C';
        else if(enpassant == null)
            throw new Exception("Smith notation is unknown ('en passant' is UNKNOWN)");
        else if(enpassant)
            ret += 'E';
        else {
            if(capturedPiece == null) throw new Exception("Smith notation is unknown ('captured piece' is UNKNOWN)");
            switch(capturedPiece) {
            case PAWN: ret += 'p'; break;
            case KNIGHT: ret += 'n'; break;
            case BISHOP: ret += 'b'; break;
            case ROOK: ret += 'r'; break;
            case QUEEN: ret += 'q'; break;
            case NONE:
                break;
            case KING:
            default:
                throw new Exception("Error...");
            }
        }
        
        if(promotedTo == null) throw new Exception("Smith notation is unknown ('promoted piece' is UNKNOWN')");
        switch(promotedTo) {
        case ROOK: ret += 'R'; break;
        case BISHOP: ret += 'B'; break;
        case KNIGHT: ret += 'N'; break;
        case QUEEN: ret += 'Q'; break;
        case NONE: break;
        case KING:
        case PAWN:
        default:
            throw new Exception("Error...");
        }
        
        return ret;
    }
    
    public String getShortSmithMove() throws Exception {
        String ret = fromSquare.toString() + toSquare.toString();

        if(promotedTo == null) throw new Exception("Smith notation is unknown ('promoted piece' is UNKNOWN')");
        switch(promotedTo) {
        case ROOK: ret += 'r'; break;
        case BISHOP: ret += 'b'; break;
        case KNIGHT: ret += 'n'; break;
        case QUEEN: ret += 'q'; break;
        case NONE: break;
        case KING:
        case PAWN:
        default:
            throw new Exception("Error...");
        }
        
        return ret;
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof String) {
            try {
                if(getShortSmithMove().equals(other)) return true;
                if(getSmithMove().equals(other)) return true;
                if(getAlgebraicMove().equals(other)) return true;
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            
        } else if(other instanceof Move) {
            if(!((Move)other).promotedTo.equals(this.promotedTo))
                return false;
            return ((Move)other).fromSquare.equals(this.fromSquare) && ((Move)other).toSquare.equals(this.toSquare);
        } else {
            return false;
        }
    }
}
