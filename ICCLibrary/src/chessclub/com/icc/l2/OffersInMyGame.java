package chessclub.com.icc.l2;

public class OffersInMyGame extends Level2Packet {
    // 21 DG_OFFERS_IN_MY_GAME                                                                                                                                                                                                  
    // Form: (gamenumber wdraw bdraw wadjourn badjourn wabort babort wtakeback btakeback)
    public int gameNumber() {
        return Integer.parseInt(getParm(1));
    }

    public boolean whiteDraw() {
        return "1".equals(getParm(2));
    }
    public boolean blackDraw() {
        return "1".equals(getParm(3));
    }
    public boolean whiteAdjourn() {
        return "1".equals(getParm(4));
    }
    public boolean blackAdjourn() {
        return "1".equals(getParm(5));
    }
    public boolean whiteAbort() {
        return "1".equals(getParm(6));
    }
    public boolean blackAbort() {
        return "1".equals(getParm(7));
    }
    public int whiteTakeback() {
        return Integer.parseInt(getParm(8));
    }
    public int blackTakeback() {
        return Integer.parseInt(getParm(9));
    }
}
