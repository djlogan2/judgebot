package chessclub.com.icc.uci;

public interface UCIRequest {
    public void engineStarted(UCIEngine engine);
    public void engineStopping();
    public void setUciOption(Option o);
    public void badUciOption(BadOption o);
    public void setUciButton(String name);
//    public void readyok();
//    public void currentMove(String smithMove, int moveNumber);
//    public void nodes(int nodes, int nps, int msec);
//    public void depth1(int depth);
//    public void depth(int depth, int score, int msec, int nodes, int nps, String moveList);
//    public void matein(int moves);
    public void bestmove(String smithmove, String ponder_smithmove);
    public void haveOnlineTablebase(OnlineTablebase otb);
    public void unknownuci(String line);
//    public void depth(int d);
//    public void seldepth(int s);
//    public void time(int t);
//    public void nodes(int n);
//    public void pv(String p);
//    public void multipv(int m);
//    public void cp(int c);
//    public void mate(int m);
//    public void lowerbound();
//    public void upperbound();
//    public void currmove(String c);
//    public void currmovenumber(int c);
//    public void hashfull(int h);
//    public void nps(int n);
//    public void tbhits(int t);
//    public void cpuload(int c);
//    public void string(String s);
//    public void refutation(String r);
//    public void line(String l);
    public void haveInfo(UCIInfo info);
}
