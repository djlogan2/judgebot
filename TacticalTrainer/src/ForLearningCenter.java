import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.tt.GameChecker;
import chessclub.com.icc.tt.ICCBean;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.UCIInfo;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList.moveIterator;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.PGNGame;


public class ForLearningCenter extends GameChecker {
    public ForLearningCenter(int bufferSize, int nThreads, int inTacticTime, int notInTacticTime, String uciProgram) {
		super(bufferSize, uciProgram, null, nThreads, notInTacticTime, inTacticTime);
	}
	private static final Logger log = LogManager.getLogger(ForLearningCenter.class);
	private static ApplicationContext applicationContext;
    private static IDatabaseService serv;
    private static ICCBean bean;
    
    private Iterator<TacticsTable> iii = null;
    private TacticsTable currentTactic = null;
    private moveIterator moveIterator = null;
	
    public static void main(String[] args) throws Exception {
        applicationContext = new ClassPathXmlApplicationContext("batch-test-context.xml");
        bean = (ICCBean) applicationContext.getBean("icc");
        serv = (IDatabaseService) applicationContext.getBean("databaseService");
        ForLearningCenter flc = new ForLearningCenter(4096, bean.getUciThreads(), bean.getuciInTacticTime(), bean.getuciNotInTacticTime(), bean.getUciProgram());
        flc.run();
    }
    
    public void run() throws Exception {
    	Collection<TacticsTable> tactics = serv.getAllTactics();
    	//Collection<TacticsTable> tactics = new java.util.ArrayList<TacticsTable>();
    	//tactics.add(serv.getTactic(47158));
        iii = tactics.iterator();
        startNextTactic();
		_engine.join();
		//};
    }

    private String[] linevalue = new String[3];
    
	@Override
	public void haveInfo(UCIInfo info) {
		super.haveInfo(info);
		if(!info.haveMultipv()) return;
		int idx = info.getMultipv() - 1;
		String score;
		if(info.haveMate())
			score = "M" + info.getMate();
		else
			score = "NM";
		linevalue[idx] = score;

	}


	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
		if(otb.getScore(0) > 0)
			linevalue[0] = "" + 'M' + otb.getScore(0);
		return;
	}
	
	private void startNextTactic()
	{
		log.trace("startNextTactic");
		currentTactic = null;
		linevalue = new String[3];
		while(currentTactic == null || !currentTactic.getgood() || !currentTactic.getWinning())
		{
			if(!iii.hasNext())
			{
				_engine.quit();
				return;
			}
			currentTactic = iii.next();
			moveIterator = currentTactic.getMoves().iterator();
		}

		try {
			String firstMove = null;
			if(currentTactic.getTomove() != currentTactic.getFen().toMove())
				firstMove = moveIterator.next().getShortSmithMove();
			analyzeGame(currentTactic.getFen().getFEN(), firstMove);

			return;
		} catch (Exception e) {
			e.printStackTrace();
			_engine.quit();
			return;
		}
	}

	@Override
	public void getnextmove() {
		log.trace("getnextmove");
		// This routine gets called as long as GameChecker wants more moves
		// If we are out of moves by this point, the tactic is potentially good,
		// but it wants more. There are more tactical moves that could have been made.
		try {
			if(moveIterator.hasNext())
			{
				move(new ICCMove(moveIterator.next()));
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			_engine.quit();
			System.exit(0);
		}
		nomoremoves();
	}

	@Override
	public void endofgame() {
		log.trace("endofgame");
		startNextTactic();
	}

	@Override
	public void goodtactic(Fen fen, String moves, boolean winning) {
		log.trace("goodtactic");
		NormalChessGame ncg;
		try {
			ncg = new NormalChessGame(currentTactic.getFen());
			moveIterator i = currentTactic.getMoves().iterator();
			while(i.hasNext()) ncg.makeMove(i.next());
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		// We should get here after sending all of the moves to the GameChecker
		String description;
		try {
			int nMove = currentTactic.getMoves().halfMoveCount();
			if(currentTactic.getTomove() == currentTactic.getFen().toMove())
				nMove++;
			nMove /= 2;
			//w, bl: b w (b w)
			//b, wl: w b (w b)
			//w, nl: w (b w)
			//b, nl: b (w b)
			
			log.trace("linevalue is " + linevalue[0] + "," + linevalue[1] + "," + linevalue[2]);
			log.trace("gameboard.isCheckmate()=" + gameboard.isCheckmate());
			
			if(ncg.isCheckmate())
			{
				linevalue[0] = String.format("M%d",  nMove);
				description = "Mate in " + nMove;
			}
			else if(linevalue[0] == null) // We got a best move from the tablebase, so haveinfo was never called
			{
				log.error("linevalue is null, skipping writing this tactic");
				return;
			}
			else if(linevalue[0].charAt(0) == 'M')
			{
				int pmi = Integer.parseInt(linevalue[0].substring(1)) + nMove;
				linevalue[0] = String.format("PM%d",  pmi);
				description = "Partial mate in " + pmi;
			}
			else
			{
				linevalue[0] = "NM";
				description = "Material gain in " + nMove;
			}
			
			String filename = linevalue[0] + "_";
			int intro = 0;
			if(currentTactic.getTomove() == currentTactic.getFen().toMove())
				filename += "nl_";
			else
			{
				filename += "l_";
				intro = 1;
			}
			
			filename += currentTactic.getTomove().toString();
			
			PGNGame game = new PGNGame();
			game.addTag("FEN", currentTactic.getFen().getFEN());
			game.addTag("Event", "?");
			game.addTag("Site",  "?");
			game.addTag("Date",  DateFormat.getDateInstance().format(new Date()));
			game.addTag("Round",  "?");
			if(currentTactic.getTomove() == Color.WHITE)
			{
				game.addTag("White",  description);
				game.addTag("Black",  "?");
			}
			else
			{
				game.addTag("White",  "?");
				game.addTag("Black",  description);
			}
			game.addTag("Result", "*");
			game.addTag("SetUp", "1");
			game.addTag("PlyCount",  "" + currentTactic.getMoves().halfMoveCount());
			game.addTag("TTID",  "" + currentTactic.getId());
			game.addInitialComment("\"intro\":" + intro + ",\"rating\":" + currentTactic.getRating());
			moveIterator = currentTactic.getMoves().iterator();
			NormalChessGame ngame = new NormalChessGame(currentTactic.getFen());
			while(moveIterator.hasNext())
			{
				Move superMove = ngame.makeMove(moveIterator.next());
				game.addmove(null, superMove.getAlgebraicMove(), null, null, null);
			}

			filename += ".pgn";
			try
			{
				System.out.println("Opening " + filename);
				FileWriter fw = new FileWriter("/home/djlogan/tacticaltrainer/" + filename, true);
				System.out.println("Writing " + game.pgnToString());
				fw.write(game.pgnToString());
				System.out.println("Closing " + filename);
				fw.close();
			}
			catch(IOException e)
			{
				System.out.println("Had trouble writing to " + filename + ": " + e.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void badtactic(Fen fen, String moves) {
		serv.updateGoodAndComment(currentTactic.getId(), false, "Stockfish 7 failed this");
	}
}
