/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chessclub.com.icc.l1l2;


import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.SendMoves;
import david.logan.chess.support.Move;

/**
 * This class instance represents a particular move number in a move list.
 * It could also contain entire sub variations of moves.
 * @author David Logan
 */
public class ICCMove extends Move {

    private int msec;
    
    public ICCMove(SendMoves p) throws Exception {
        super(p.smithMove(), p.algebraicMove());
    }
    public ICCMove(Move move) {
        super(move);
    }
    /**
     * Constructor for a {@link MoveInfo} instance.
     * @param p a {@link MoveInfo} instance.
     * @throws Exception 
     */
    public ICCMove(final MoveInfo p) throws Exception {
        super(p.smithMove(), p.algebraicMove());
    }

    @SuppressWarnings("unused")
    private ICCMove() {
        super(null);
    }

    public void set(Msec p) {
        msec= p.msec();
    }
    public int getMsec() {
        return msec;
    }

//    public ICCMove(final String algmove, final String smithmove) throws Exception {
//        super(smithmove, algmove);
//    }
}
