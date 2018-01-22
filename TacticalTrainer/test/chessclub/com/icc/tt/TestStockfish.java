package chessclub.com.icc.tt;

import chessclub.com.icc.uci.BadOption;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.Option;
import chessclub.com.icc.uci.SearchParameters;
import chessclub.com.icc.uci.UCIEngine;
import chessclub.com.icc.uci.UCIInfo;
import chessclub.com.icc.uci.UCIRequest;
import david.logan.chess.support.Fen;

public class TestStockfish implements UCIRequest {
    private Fen fen = null;
    private UCIEngine engine;
    
    public static void main(String args[]) throws Exception {
        TestStockfish m = new TestStockfish();
        m.run();
    }

    public TestStockfish() throws Exception {
    	fen = new Fen("2R5/3P1pk1/5br1/4B1p1/1P2p2p/q3P2P/5PP1/3R2K1 b - - 3 3");
    }
    private void run() {
        engine = new UCIEngine(4096, "/Users/davidlogan/Documents/icc/stockfish-231-mac/Mac/stockfish-231-64", this, true);
        engine.start();
    }

    @Override
    public void engineStarted(UCIEngine engine) {
        try {
            engine.setOption("MultiPV", "2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        engine.newPosition(fen);
        SearchParameters p = new SearchParameters();
        p.setMovetime(1000*10);
        //p.setInfinite(true);
        try {
            engine.calculate("TestStockfish 1", p);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-4);
        }
    }

    @Override
    public void engineStopping() {
        
        
    }

    @Override
    public void setUciOption(Option o) {
        
        
    }

    @Override
    public void badUciOption(BadOption o) {
        
        
    }

    @Override
    public void setUciButton(String name) {
        
        
    }

    @Override
    public void bestmove(String smithmove, String ponder_smithmove) {
        engine.quit();
    }

    @Override
    public void unknownuci(String line) {
        
        
    }

    @Override
    public void haveInfo(UCIInfo info) {
//        System.out.println(info.haveCp()+","+info.haveMultipv()+","+info.havePv()+"  "+info.originalLine());
        if(!info.haveCp())
            return;
        if(!info.haveMultipv())
            return;
        if(!info.havePv())
            return;
        String[] moves = info.getPv().split(" +");
        System.out.println("move "+ moves[0] +" cp "+info.getCp()+" multipv " + info.getMultipv());
    }

	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
	}

}
