package chessclub.com.icc.uci;

import java.util.ArrayList;

public class UCIMoves {

    private ArrayList<String> moveList = new ArrayList<String>();

    public final String[] getAllSmithMoves() {
        String[] ret = new String[moveList.size()];
        ret = moveList.toArray(ret);
        return ret;
    }

    public final void addMove(final String move) {
        moveList.add(move);
    }
}
