import java.util.Collection;
import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chessclub.com.icc.tt.GameChecker;
import chessclub.com.icc.tt.ICCBean;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import chessclub.com.icc.uci.OnlineTablebase;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;


public class TemporaryReviewTactics extends GameChecker implements Runnable {
    private static ApplicationContext applicationContext;
    private static IDatabaseService serv;
    private static ICCBean bean;
    
    private Iterator<TacticsTable> iii = null;
    private TacticsTable currentTactic = null;
    

    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("batch-test-context.xml");
        bean = (ICCBean) applicationContext.getBean("icc");
        serv = (IDatabaseService) applicationContext.getBean("databaseService");
        TemporaryReviewTactics trt = new TemporaryReviewTactics();
        trt.run();
    }
    
    public TemporaryReviewTactics() {
    	super(4096, bean.getUciProgram(), bean.getUciBookFile(), bean.getUciThreads(), bean.getuciNotInTacticTime(), bean.getuciInTacticTime());
    }

    @Override
    public void run() {
        Collection<TacticsTable> tactics = serv.getNullTactics();
        iii = tactics.iterator();
        startNextTactic();
    }

    private void startNextTactic() {
        if(!iii.hasNext()) {
        	shutdown();
            return;
        }
        
	        try {
	    		do {
	    			currentTactic = iii.next();
	    		} while(currentTactic != null && currentTactic.getTomove() != currentTactic.getFen().toMove() && iii.hasNext());
			} catch (Exception e1) {
				e1.printStackTrace();
	        	shutdown();
	            return;
			}
		
		if(currentTactic == null) {
        	shutdown();
            return;
		}
		
        currentTactic.setgood(false);
        
    	try {
    		String firstMove = null;
    		if(currentTactic.getTomove() != currentTactic.getFen().toMove())
    			firstMove = currentTactic.getMoves().getMove(0).getShortSmithMove();
			analyzeGame(currentTactic.getFenString(), firstMove);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
        
    }

	@Override
	public void getnextmove() {
		nomoremoves();
	}

	@Override
	public void endofgame() {
		serv.good(currentTactic);
		startNextTactic();
	}

	@Override
	public void goodtactic(Fen fen, String moves, boolean winning) {
		String[] splitmoves = moves.split(" +");
		int ourmovecount = currentTactic.getMoves().halfMoveCount();
		int theirmovecount = splitmoves.length;
		boolean good = false;
		
		if(ourmovecount <= theirmovecount  || // The tactic could have gone longer, but a previous tactic computation ended too early is still a good tactic
		   (ourmovecount % 2 == 0 && (theirmovecount + 1 == ourmovecount))       // We end on a computer move the tactic doesn't have
		) good = true;
		if(!good) return;
		
		try {
			for(int x = 0 ; x < Math.min(ourmovecount, theirmovecount) ; x++) {
				if(!currentTactic.getMoves().getMove(x).getShortSmithMove().equals(splitmoves[x]))
					return;
			};
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		currentTactic.setgood(true);
	}

	@Override
	protected Move getpreferredlosersmove(MoveList tactic, Move m1, Move m2, Move m3) {
		Move tmove = currentTactic.getMoves().getMove(tactic.halfMoveCount());
		if(tmove == null)    return m3;
		if(tmove.equals(m1)) return m1;
		if(tmove.equals(m2)) return m2;
		return m3;
	}

	@Override
	public void badtactic(Fen fen, String moves) {
	}

	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
	}
}
