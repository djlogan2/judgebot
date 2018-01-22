package david.logan.chess.support;


public class PGNMove extends Move {

    public int getFromFile() {
        if(fromSquare != null && fromSquare.file >= 0 && fromSquare.file < 8)
            return fromSquare.file;
        else
            return -1;
    }
    public int getFromRank() {
        if(fromSquare != null && fromSquare.rank >= 0 && fromSquare.rank < 8)
            return fromSquare.rank;
        else
            return -1;
    }
    
    public PGNMove(Color toMove, String pgnmove) throws Exception {
        super(null, pgnmove);
        
        if (pgnmove.startsWith("O-O-O")) {
            if (toMove == Color.WHITE) {
                set("e1c1C", pgnmove);
                return;
            } else {
                set("e8c8C", pgnmove);
                return;
            }
        } else if (pgnmove.startsWith("O-O")) {
            if (toMove == Color.WHITE) {
                set("e1g1c", pgnmove);
                return;
            } else {
                set("e8g8c", pgnmove);
                return;
            }
        }
    }

    public void finishMove(Square fromSquare, ChessPieceEnum taken, boolean ep) throws Exception {
        this.fromSquare = fromSquare;
        this.capturedPiece = taken;
        this.enpassant = ep;
    }
}
