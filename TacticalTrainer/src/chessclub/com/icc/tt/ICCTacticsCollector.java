package chessclub.com.icc.tt;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IGameStartedResult;
import chessclub.com.icc.handler.interfaces.IMyGame;
import chessclub.com.icc.handler.interfaces.ISFen;
import chessclub.com.icc.handler.interfaces.IWhoAmI;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.Sfen;
import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.OffersInMyGame;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.TakeBackOccurred;
import chessclub.com.icc.l2.WhoAmI;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.UCIInfo;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;
import david.logan.chess.support.NormalChessBoard;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.PGNGame;
import david.logan.chess.support.MoveList.moveIterator;

public class ICCTacticsCollector extends GameChecker implements
    IAcceptGenericPackets, IWhoAmI,
                IGameStartedResult, IMyGame, ISFen {
    private static final double WEIGHT = 1800.0; // Try to weight games to this rating or above
    private static final Logger log = LogManager.getLogger(ICCTacticsCollector.class);
    private ICCInstance icc;
    @SuppressWarnings("unused")
    private String whoami;
    
    //*********** THESE ARE THE VARIABLES USED TO WATCH FOR ENDING GAMES FOR TACTICAL POSITIONS **************
    private Integer analyzedgameid = null;
    private HashMap<Integer, GameStarted> gamelist = new HashMap<Integer, GameStarted>();
    //****************************************** END ****************************************

    @SuppressWarnings("unused")
	private int whiteRating;
    @SuppressWarnings("unused")
	private int blackRating;
    private int currentHalfMove;
    private Move firstMove = null;
    private Fen firstFen = null;
    @SuppressWarnings("unused")
    private MessageSource messageSource;
    private IDatabaseService databaseService;
    @SuppressWarnings("unused")
    private ICCBean iccBean;
    private String[] linevalue = new String[3];

    private ICCTacticsCollector() {
    	super(0, null, null, 0, 0, 0);
    }
    
    public ICCTacticsCollector(MessageSource messageSource, IDatabaseService databaseService, ICCBean iccBean, int iBufferSize, String uciProgram, String uciBookFile) {
    	super(iBufferSize, uciProgram, uciBookFile, iccBean.getUciThreads(), iccBean.getuciNotInTacticTime(), iccBean.getuciInTacticTime() );
        this.messageSource = messageSource;
        this.databaseService = databaseService;
        this.iccBean = iccBean;
    }
    
    public void run() {
    	while(true) {
    		icc = new ICCInstance();
	        try {
	            icc.setInstance("chessclub.com", 5000, "", "", this, 4096);
	        } catch (Exception e) {
	            // We have handled the "are we accepting generic packets" issue
	            e.printStackTrace();
	        }
	        icc.start();
	        try {
	            icc.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
    	}
    }

    @Override
    public void initHandler() {

    }

    @Override
    public void iccException(ICCException ex) {

    }

    @Override
    public void setInstance(ICCInstance icc) {
        setInstance(icc, this);
        this.icc =icc;
    }

    @Override
    public void whoAmI(WhoAmI p) {
    	Thread.currentThread().setName("Tactics Collector");
        whoami = p.userid();
        setNoAutologout(true);
    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void badCommand1(BadCommand p) {

    }

    @Override
    public void badCommand2(L1ErrorOnly p) {

    }

    @Override
    public void gameResult(GameResultPacket p) {

        GameStarted game = gamelist.get(p.gamenumber());

        if (game == null)
            return;
        
        gamelist.remove(p.gamenumber());

        if (analyzedgameid != null)
            return;
        
        if (((double)Math.min(game.blackrating(), game.whiterating()) / WEIGHT) * Math.random() < 0.5)
            return;
        
        analyzedgameid = game.gameid();
        
        sfen("#" + analyzedgameid);
    }

    @Override
    public void myGameResult(GameResultPacket p) {
    	firstFen = null;
    	firstMove = null;
    }

    @Override
    public void gameStarted(GameStarted p) {
        //
        // Skip the various variations we don't want to process
        //
        if (p.wildnumber() != 0)
            return;
        if (p.irregularLegality())
            return;
        if (p.irregularSemantics())
            return;
        if (p.usesPlunkers())
            return;
        if (p.examined())
            return;

        gamelist.put(p.gamenumber(), p);
    }

    @Override
    public void myGameStarted(GameStarted p) {
        whiteRating = p.whiterating();
        blackRating = p.blackrating();
    	firstFen = null;
    	firstMove = null;
        analyzeGame();
    }

    @Override
    public void sendMoves(SendMoves p) {
        ICCMove move;
        try {
        	firstFen = gameboard.getFEN();
            move = new ICCMove(p);
        	firstMove = move;
            move(move);
        } catch (Exception e) {
            e.printStackTrace();
            nomoremoves();
            return;
        }
    }

    @Override
    public void inSfen(Sfen p) {
        NormalChessBoard b;
		try {
			b = new NormalChessBoard(p.fen());
		} catch (Exception e) {
			e.printStackTrace();
            log.error("Exception caught loading FEN: " + e.getMessage());
            analyzedgameid = null;
            return;
		}

        currentHalfMove = b.moveNumber();

        if (currentHalfMove < 10) {
            log.info("Not enough moves have been made in this game. Ending");
            analyzedgameid = null;
            return;
        }

        examine("#" + analyzedgameid);
    }

    @Override
    public void examinedGameIsGone(GameNumber p) {

    }

    @Override
    public void myGameEnded(GameNumber p) {
        analyzedgameid = null;
    }

    @Override
    public void shuttingDown() {

    }

    // -------------- End of UCIRequest interface
    public boolean atend() {
        log.debug("atend movenumber=" + (gameboard == null ? "null" : gameboard.moveNumber()) + "/" + currentHalfMove);
        return (gameboard == null || gameboard.moveNumber() >= currentHalfMove); // +
                                                          // (currentMoveColor==Color.WHITE?1:0));
    }

    @Override
    public void msec(Msec p) {
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }

	@Override
	public void getnextmove() {
		if(atend())
			nomoremoves();
		else
			forward(1);
	}

	@Override
	public void endofgame() {
		unexamine();
	}

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
	public void goodtactic(Fen fen, String moves, boolean winning) {
		Color winner = fen.toMove();
        this.kibitz("A complete tactic was found: " + fen + " : " + moves + " winning side is " + winner);
        log.debug("GameChecker returned: " + fen + " " + moves);
        if(fen.toMove() == firstFen.toMove()) {
        	log.error("Why isn't our FEN's tomove the opposite of the completed tactic???");
        	log.error("firstFen=" + firstFen + " " + firstMove + "=" + firstMove);
        	log.error("Saving original tactic");
        } else {
	        try {
				String newmoves = firstMove.getShortSmithMove() + " " + moves;
				moves = newmoves;
				fen = firstFen;
			} catch (Exception e) {
				log.error("Unable to add losers first move to move list: " + e.getMessage());
			}
        }
        log.debug("Saving: " + fen + " " + moves + " winning side is " + winner);
        int tacticid = databaseService.saveTactic(fen, moves, winner, winning);

        //- For the learning center
		log.trace("goodtactic");
		if(tacticid == -1) return; // Tactic was already in the database
		NormalChessGame ncg = null;
		Fen _fen = new Fen(fen);
		MoveList moveList = new MoveList();
		
		for(String m : moves.split(" "))
			try {
				moveList.add(new Move(m, false));
				ncg = new NormalChessGame(fen);
				moveIterator i = moveList.iterator();
				while(i.hasNext()) ncg.makeMove(i.next());
			} catch (Exception e2) {
				e2.printStackTrace();
				return;
			}
		
		try {
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		String description;
		try {
			int nMove = moveList.halfMoveCount();
			if(ncg.colorToMove() == _fen.toMove())
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
			else if(linevalue[0] != null && linevalue[0].charAt(0) == 'M')
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
			
			filename += "l_";
			filename += winner.toString();
			
			PGNGame game = new PGNGame();
			game.addTag("FEN", fen.getFEN());
			game.addTag("Event", "?");
			game.addTag("Site",  "?");
			game.addTag("Date",  DateFormat.getDateInstance().format(new Date()));
			game.addTag("Round",  "?");
			if(winner == Color.WHITE)
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
			game.addTag("PlyCount",  "" + _fen.moveNumber());
			game.addTag("TTID",  "" + tacticid);
			moveIterator moveIterator = ncg.getMoveList().iterator();
			while(moveIterator.hasNext())
				game.addmove(null, moveIterator.next().getAlgebraicMove(), null, null, null);
			game.addInitialComment("\"intro\":1,\"rating\":1600");
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
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        //- For the learning center
	}

	@Override
	public void takeBackOccurred(TakeBackOccurred p) {
	}

	@Override
	public void newOffers(OffersInMyGame p) {
	}

	@Override
	public void badtactic(Fen fen, String moves) {
	}

	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
	}
}
