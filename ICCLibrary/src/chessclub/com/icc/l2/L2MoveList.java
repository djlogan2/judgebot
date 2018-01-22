package chessclub.com.icc.l2;

import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.l1l2.MoveInfo;
import david.logan.chess.support.Move;

/**
 * This class isn't for public use.
<p>25 DG_MOVE_LIST
<p>Loading of move list.
<p>If this variable is set, I get one of these DGs when:
<ol>
<li>I start observing a game.</li>
<li>I start playing or examining a game (though in this
     case it usually just looks like (gamenumber *), unless
     I'm resuming an adjourned game or it's a wild game.</li>
<li>I use the "refresh &ltgame&gt" command.</li>
<li>I use the "copygame" command.</li>
<li>An examiner edits the position of a game I'm observing
     or examining.</li>
    </ol>
     (gamenumber initial-position {white-move1-info}{black-move1-info}{white-move2-info} ...)
 * @author David Logan
 *
 */
public class L2MoveList extends Level2Packet {
    /**
     * The ICC game number.
     * @return  The ICC game number.
     */
    public int gamenumber() {
        return Integer.parseInt(getParm(1));
    }

    /**
     *  Initial-position is either "*" in the case of the normal initial
    position, or 64 characters indicating what occupies (-PNBRQKpnbrqk)
    each square, in lexigraphic order (a8 b8 ... h1).
     * @return  The initial position string.
     */
    public String initialposition() {
        return getParm(2);
    }

    private boolean parsed = false;
    private Move[] movelist = null;

    private void parse() throws Exception {
        movelist = new Move[numberParms() - 3];
        for (int x = 3; x < numberParms(); x++) {
            movelist[x - 3] = new ICCMove(new MoveInfo(getParm(x)));
        }
        parsed = true;
    }

    /**
     * The array of moves.
     * @return  The array of moves.
     * @throws Exception 
     */
    public Move[] movelist() throws Exception {
        if (!parsed) {
            parse();
        }
        return movelist;
    }
}
