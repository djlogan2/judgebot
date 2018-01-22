package chessclub.com.icc.jb.icc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.enums.AdjudicateAction;
import chessclub.com.icc.DefaultICCAdminCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IGameList;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.handler.interfaces.IMyGame;
import chessclub.com.icc.handler.interfaces.ISFen;
import chessclub.com.icc.jb.GameLogManager;
import chessclub.com.icc.jb.UCIThreadManager;
import chessclub.com.icc.jb.entity.EngineRule;
import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.jb.ifac.parserInterface;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.GameAdjudicated;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1.Sfen;
import chessclub.com.icc.l1l2.GameList;
import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.l2.GameListItem;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.OffersInMyGame;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.TakeBackOccurred;
import chessclub.com.icc.parser.ParseException;
import chessclub.com.icc.uci.BadOption;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.Option;
import chessclub.com.icc.uci.SearchParameters;
import chessclub.com.icc.uci.UCIEngine;
import chessclub.com.icc.uci.UCIInfo;
import chessclub.com.icc.uci.UCIRequest;
import david.logan.chess.support.Color;
import david.logan.chess.support.MoveList;
import david.logan.chess.support.NormalChessBoard;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.MoveList.moveIterator;

// TODO: Add a new table, with columns Interface and variable, so that said interface will return "isvariable=1"
//

public class AdjudicateBase extends DefaultICCAdminCommands
    implements UCIRequest, parserInterface,
                ISFen, IMyGame, IGameList, IGetpx {

	private MessageSource messageSource;
	private IDatabaseService databaseService;
	private ICCBean iccBean;
	
    // initHandler - Starts the process. It calls "stored names[0]" which then calls "stored names[1]" which then haveStoredGames(gameid)
    //               I put this in so that an overriding class can do whatever. By default this class calls "sfen names[0] names[1]"
    //
    // Once an sfen is issued, inSfen() gets control. Calls hasSfen() which by default then tries to examine by doing a "getexamineboard" call
    //
    // That call triggers a "MyGameStarted" packet. It starts the walk through with the first "forward 1" command
    //
    // That triggers a SendMove() and an Msec() callback. Msec() sends another "forward 1" until end of game, where it sends "unexamine"
	// DJL DJL DJL DJL
    //
    // That triggers a MyGameEnded() callback which then starts a chess engine
    //
    // That triggers the engineStarted() callback. That sends the position and starts the engine calculations.
    //
    // The engine calculations trigger various callbacks. The important one is "bestmove". This means the engine has finished calculating.
    //
    // bestmove() gathers the rest of the raw data for later use by the variables and calls the engines quit() method
    //
    // This triggers the engineStopping callback.
    //
    // This calls getpx() a couple of times to get the white and black locale information and unique IDs
    //
    // These unique IDs are checked against the winexempt and loseexempt tables to see if either the winner or loser is exempt from adjudication
    //  If either player is exempt, the handler is unregistered
    //
    // adjudicateThisGame() for the final hoorah, then unregisters this handler
    //
    private static final Logger log = LogManager.getLogger(AdjudicateBase.class);
    // private int depth;                                          // The last calculated depth from the chess engine
    // private int msec;                                           // The # of MS the chess engine took to get to this score
    // private int nodes;                                          // The # of nodes the chess engine searched to get to this score
    // private int nps;                                            // The # of positions the chess engine searched to get to this score
    //private String enginemoveList;                                 // A whitespace separated list of moves from the chess engine the score belongs to
    //private int loser;                                           // Which one of these players is the loser
    //private ArrayList<ICCMove> moveList = new ArrayList<ICCMove>();      // The list of moves we get from ICC as we move forward in the game
    private MoveList moveList = new MoveList();
    private NormalChessGame gameboard = new NormalChessGame();             // A Board instance from the ICC library
    private Color currentMoveColor;                                // The color to move
    private Color disconnectorColor = Color.NONE;                  // The color of the person that disconnected
    private Color losingColor = Color.NONE;                        // The color losing the game
    private EngineRule engineRule = null;                          // The engine rule that will set our engine parameters
    private GameLogManager gamelog = null;                         // The game log class
    private GameStarted gamestarted = null;                        // The game started packet we get when we examine the game
    private ICCMove currentMove = null;                            // The current move
    private Msec currentMsec = null;                               // The current msec if msec comes in first
    private String gameEvent = null;                               // The event name from the stored game. If data, we have a tournament game
    private String gamefen;                                        // The games FEN string
    private String language[] = null;                              // The 2-byte language identifiers, obtained from getpx commands
    private String[] names;                                        // Names passed in with the handler
    private UCIEngine uciengine = null;                            // The chess engine assigned to us
    //private MoveList engineMoves = new MoveList();                 // This is the move list that the engine will want.
    private boolean skipcheck = false;                             // Set to true of a check of the engine rules wants us to skip this game
    private int currentHalfMove;                                   // The current halfmove number
    private int disconnected = -1;                                 // 0 or 1, which player disconnected
    private int gameid = -1;                                       // The game id we are looking for. This should be == gamestarted.gameid()
    private int matein = 0;                                        // Mate in X moves (victim will win) or -X moves (victim will lose) From the chess engine.
    private int score;                                             // The last score from the chess engine. +X if the victim is winning, -X if victim is losing
    private int e_depth = 0;
    private long e_nodes = 0;
    private int e_nps= 0;
    private int e_time = 0;                                        // Evaluation depth, nps and time for putting in the log file
    private int uniqueid[] = null;                                 // The unique ICC ids (obtained from getpx commands)
    private int[] adjourned = new int[] { 0, 0 };                  // The number of adjourned games for each player
    private int[] countchange = new int[] { 0, 0 };                // The piece count change from the other colors move-1 to their last move
    private boolean testonly;

    static private AdjudicateBase examineHandler = null;
    static private LinkedList<AdjudicateBase> examineList = new LinkedList<AdjudicateBase>();
    static private int maxExamineList = 0;

    static private HashMap<String, Method> stringVariables = null; // The static list of variables our rulesengine will have available
    static private HashMap<String, Method> intVariables = null;    // The static list of variables our rules engine will have available

    private static synchronized void getexamineboard(AdjudicateBase handler, final String gameString) {
        log.debug("getexamineboard handler=" + handler + ",gameString=" + gameString + "examineHandler=" + (examineHandler == null ? 0 : examineHandler.hashCode()));

        if (examineHandler == null) {
            examineHandler = handler;
            handler.canExamine();
        } else {
            examineList.add(handler);
            if(maxExamineList < examineList.size())
                maxExamineList = examineList.size();
        }
    }

    public static synchronized int getCurrentExamineQueue() {
        return examineList.size();
    }
    
    public static synchronized int getMaxExamineQueue() {
        return maxExamineList;
    }

    private static synchronized void nextexamine() {
        log.debug("nextexamine examineList size=" + examineList.size() + ", examineHandler=" + (examineHandler == null ? 0 : examineHandler.hashCode()));

        if (examineList.isEmpty()) {
            examineHandler = null;
            return;
        }

        examineHandler = examineList.remove();
        examineHandler.canExamine();
    }

    public AdjudicateBase(ICCInstance icc, MessageSource messageSource, IDatabaseService databaseService, ICCBean iccBean, String[] names, boolean testonly) {
        this.setInstance(icc,  this);
        this.messageSource = messageSource;
        this.databaseService = databaseService;
        this.iccBean = iccBean;
        this.names = names;
        this.testonly = testonly;

        if (AdjudicateBase.stringVariables == null)
            AdjudicateBase.initializeVariables();
    }

    public int disc(GameListItem g) {
        switch (g.whoDisconnected()) {
        case WHITE:
            if (g.whitename().equals(names[0]))
                return 0;
            else if (g.whitename().equals(names[1]))
                return 1;
            else
                return -1;
        case BLACK:
            if (g.blackname().equals(names[0]))
                return 0;
            else if (g.blackname().equals(names[1]))
                return 1;
            else
                return -1;
        default:
            return -1;
        }
    }

    @Override
    public void gameList(GameList p) {
        log.debug("Have gamelist for " + p.parameters()[0]);
        if (names[0].equals(p.parameters()[0])) {
            this.adjourned[0] = p.nhits();
            for (GameListItem g : p.games()) {
                log.debug("Stored game: " + g.whitename() + "/" + g.blackname());
                if ((g.blackname().equals(names[0]) && g.whitename().equals(names[1])) || (g.blackname().equals(names[1]) && g.whitename().equals(names[0]))) {
                    this.gameid = g.id();
                    disconnected = disc(g);
                    gameEvent = g.event();
                    if ("?".equals(gameEvent))
                        gameEvent = null;
                    stored(names[1]);
                    return;
                }
            }
            noStoredGames();
            return;
        } else if (names[1].equals(p.parameters()[0])) {
            this.adjourned[1] = p.nhits();
            for (GameListItem g : p.games()) {
                log.debug("Stored game: " + g.whitename() + "/" + g.blackname());
                if ((g.blackname().equals(names[0]) && g.whitename().equals(names[1])) || (g.blackname().equals(names[1]) && g.whitename().equals(names[0]))) {
                    if (disconnected != disc(g)) {
                        log.error("Discrepancy in the stored games about who disconnected");
                        disconnected = -1;
                    } else {
                        disconnected = disc(g);
                        log.debug("Found disconnector. It was " + (disconnected == -1 ? "unknown" : names[disconnected]));
                        if (disconnected == -1) {
                            removeHandler(this);
                            return;
                        }
                    }
                    if (gameid != g.id()) {
                        log.error("Found the same stored game in " + names[1] + " stored games, but the game id doesn't match! gameid in " + names[0] + " library is " + gameid + " and found " + g.id());
                        removeHandler(this);
                        return;
                    }
                    haveStoredGames();
                    return;
                }
            }
            log.error("Unable to find a stored game between " + names[0] + " and " + names[1] + " in the stored games of " + names[1]);
            removeHandler(this);
            return;
        }
    }

    protected void noStoredGames() {
        log.error("Unable to find a stored game between " + names[0] + " and " + names[1] + " in the stored games of " + names[0]);
        removeHandler(this);
    }

    protected void haveStoredGames() {
        sfen("#" + gameid);
    }

    @Override
    public void inSfen(Sfen p) {
        NormalChessBoard b = null;
        
        try {
			gamefen = p.fen().toString();
	        b = new NormalChessBoard(p.fen());
		} catch (Exception e) {
	        log.error("Sfen failed with an exception: " + e.getMessage());
			e.printStackTrace();
			removeHandler(this);
			return;
		}

        currentMoveColor = b.colorToMove();
        currentHalfMove = b.moveNumber(); //b.moveNumber() * 2 - (currentMoveColor == Color.WHITE ? 1 : 0);

        log.debug("Sfen current move is " + currentHalfMove + " and tomove is " + currentMoveColor);

        if (currentHalfMove == 1) {
            log.info("No moves have been made in this game. Ending");
            removeHandler(this);
            return;
        }

        hasSfen();
    }

    private static boolean messaged = false;
    
    public static void resetMessaged() { messaged = false; }
    
    protected void hasSfen() {
        getexamineboard(this,this.gameEvent);
        if(examineList.size() > 5) {
            if(!messaged) {
                for(String user : ICCHandler.getNotifyList()) {
                    xtell(user, "Check on my examine queue");
                    message(user,  "Check on my examine queue");
                }
                messaged = true;
            }
        } else if(messaged && examineList.size() == 0) {
            messaged = false;
        }
    }

    @Override
    public void badCommand1(BadCommand p) {
        log.error("Abandoning adjudicate request due to bad command: " + p.command());
        //removeHandler(this);
        unexamine();
    }

    @Override
    public void badCommand2(L1ErrorOnly p) {
        if (p.errortext().startsWith("Warning: your graphical interface may not correctly display wild 22 (Chess960) games"))
            return;
        log.error("Abandoning adjudicate request due to error: " + p.errortext());
        //removeHandler(this);
        unexamine();
    }

    @Override
    public void msec(Msec p) {
        //
        // A couple of fixes that allow MSEC to come in before DG_SEND_MOVE
        // *sigh*
        //
        if(p == null) {
            if(currentMsec != null) {
                p = currentMsec;
                currentMsec = null;
            } else {
                log.error("msec called with null packet and no valid currentMsec!");
                return;
            }
        }
        
        log.debug("Msec: " + p + ", currentMove=" + currentMove + ", atend()=" + atend());
        //if (currentMove != null && p.color() == gameboard.colorToMove()) {
            currentMove.set(p);
            moveList.add(currentMove);
            //engineMoves.addMove(currentMove); //currentMove.getFromSquare().toString(), currentMove.getToSquare().toString(), currentMove.getQueeningPiece());
            try {
                gameboard.makeMove(currentMove);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fromend(gameboard.colorToMove()) == 1) {
                if ((names[0].equals(gamestarted.whitename()) && gameboard.colorToMove() == Color.WHITE) || (names[0].equals(gamestarted.blackname()) && gameboard.colorToMove() == Color.BLACK))
                    countchange[0] = gameboard.pieceCount(gameboard.colorToMove());
                else if ((names[1].equals(gamestarted.whitename()) && gameboard.colorToMove() == Color.WHITE) || (names[1].equals(gamestarted.blackname()) && gameboard.colorToMove() == Color.BLACK))
                    countchange[1] = gameboard.pieceCount(gameboard.colorToMove());
            } else if (fromend(gameboard.colorToMove()) == 0) {
                if ((names[0].equals(gamestarted.whitename()) && gameboard.colorToMove() == Color.WHITE) || (names[0].equals(gamestarted.blackname()) && gameboard.colorToMove() == Color.BLACK))
                    countchange[0] -= gameboard.pieceCount(gameboard.colorToMove());
                else if ((names[1].equals(gamestarted.whitename()) && gameboard.colorToMove() == Color.WHITE) || (names[1].equals(gamestarted.blackname()) && gameboard.colorToMove() == Color.BLACK))
                    countchange[1] -= gameboard.pieceCount(gameboard.colorToMove());
            }
            currentMove = null;
            if (!atend())
                forward(1);
            else
                unexamine();
        //} else
        //    currentMsec = p;
    }

    @Override
    public void myGameStarted(GameStarted p) {
        log.debug("MyGameStarted handler=" + this.hashCode());
        gamestarted = p;
        if (p.wildnumber() != 0) {
            log.info("Skipping adjudication check due a non wild 0 game");
            skipcheck = true;
            this.unexamine();
            return;
        }

        if (disconnected != -1) {
            if (names[disconnected].equals(gamestarted.whitename()))
                disconnectorColor = Color.WHITE;
            else
                disconnectorColor = Color.BLACK;

            log.debug("We have determined that " + disconnectorColor + " was the disconnector");

        } else
            disconnectorColor = Color.NONE;

        gamelog = new GameLogManager(databaseService, p, disconnectorColor, gamefen, testonly);

        if (names == null)
            names = new String[] { p.whitename(), p.blackname() };

        log.debug("AdjudicateBase examining game " + p.gameid() + " between " + p.whitename() + " and " + p.blackname());

        forward(1);
    }

    @Override
    public void sendMoves(SendMoves p) {
        log.debug("SendMoves: " + p);
        try {
            currentMove = new ICCMove(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(currentMsec != null) {
            msec(null);
        }
    }

    public static void kill() {
        if(examineHandler != null) {
            examineHandler.skipcheck = true;
            examineHandler.unexamine();
        }
    }
    
    @Override
    public void myGameEnded(GameNumber p) {

        log.debug("MyGameEnded p=" + p + ", gamestarted=" + gamestarted + ", skipcheck=" + skipcheck);

        nextexamine();
        
        if (skipcheck)
            return;

        moveIterator i = moveList.iterator();
        ICCMove m1 = (ICCMove) i.lastmove();
        ICCMove m2 = (ICCMove) i.backward();
        String m1_move = "";
        String m2_move = "";

        if (m1 != null) {
            try {
                m1_move = m1.getAlgebraicMove();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        if (m2 != null) {
            try {
                m2_move = m2.getAlgebraicMove();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        int whitemsec, blackmsec;
        int halfmove = (gameboard.moveNumber() - 1) / 2;

        if (gameboard.colorToMove() == Color.WHITE) {
            blackmsec = (m1 == null ? gamestarted.blackinitial() * 60 * 1000 : m1.getMsec());
            whitemsec = (m2 == null ? gamestarted.whiteinitial() * 60 * 1000 : m2.getMsec());
            if(halfmove == 1) {
                gamelog.setMoves("No moves");
            } else {
                gamelog.setMoves(halfmove + ". " + m2_move + "  " + m1_move);
            }
        } else {
            blackmsec = (m2 == null ? gamestarted.blackinitial() * 60 * 1000 : m2.getMsec());
            whitemsec = (m1 == null ? gamestarted.whiteinitial() * 60 * 1000 : m1.getMsec());
            if(halfmove == 1) {
                gamelog.setMoves("1. " + m1_move);
            } else {
                gamelog.setMoves(halfmove + ". ... " + m2_move + "  " + (halfmove + 1) + ". " + m1_move);
            }
        }

        gamelog.setTimes(whitemsec, blackmsec);

        if (checkEngineRules() == false) {
            log.info("Skipping adjudication check due to engine check rules");
            skipcheck = true;
            return;
        }

        log.debug("Calling thread manager to start chess engine for us");
        if (skipcheck) {
            log.info("AdjudicateBase ending due to a request to skip checking this game");
            removeHandler(this);
            return;
        } else
            UCIThreadManager.getEngine(this);
    }

//    @Override
//    public void moveList(chessclub.com.icc.l2.MoveList p) {
//        log.debug("Movelist: " + p);
//        if ("*".equals(p.initialposition())) {
//            gameboard.setStartingPosition();
//        } else {
//            gameboard.loadFEN(p.initialposition());
//        }
//        forward(1);
//    }
//
    public boolean atend() {
        log.debug("atend movenumber=" + gameboard.moveNumber() + "/" + currentHalfMove + " color=" + gameboard.colorToMove() + "/" + currentMoveColor);
        return gameboard.moveNumber() >= currentHalfMove; // +
                                                          // (currentMoveColor==Color.WHITE?1:0));
    }

    // 58 BLACK was last
    // 59 WHITE is the current unplayed move
    public int fromend(Color c) { // WHITE BLACK
        int halfmove = currentHalfMove; // 59 59
        if (c != currentMoveColor)
            halfmove--; // 59 58

        halfmove -= gameboard.moveNumber(); // 1: 58 57
                                            // 59: 0 -1 0
                                            // 58: 1 0 0 0
                                            // 57: 2 1 1 0
                                            // 56: 3 2 1 1

        log.debug("fromend movenumber=" + gameboard.moveNumber() + "/" + currentHalfMove + " color=" + gameboard.colorToMove() + "/" + currentMoveColor + " returning " + (halfmove / 2));

        return (halfmove / 2);
    }

    @Override
    public void engineStopping() {
        log.debug("engine is stopping");

        UCIThreadManager.endEngine();
        uciengine = null;

        if (this.matein != 0)
            this.gamelog.setScore(true, varmatein());
        else
            this.gamelog.setScore(false, varscore());
        this.getpx(names[0], "names0");
    }

    @Override
    public void getpx(Getpx p) {
        if("names0".equals(p.getKey()))
        {
                uniqueid = new int[2];
                uniqueid[0] = p.getUniqueId();
                language = new String[2];
                language[0] = p.getLanguage();
                getpx(names[1],"names1");
        }
        else if("names1".equals(p.getKey()))
        {
            uniqueid[1] = p.getUniqueId();
            language[1] = p.getLanguage();
            if(disconnectorColor == Color.WHITE)
            {
                if(names[1].equalsIgnoreCase(databaseService.getWinExempt(uniqueid[1])))
                {
                    log.info("Not adjudicating game between "+names[0]+" and "+names[1]+" because "+names[1]+" would win, but is on the nowin list");
                    removeHandler(this);
                    return;
                }
                if(names[0].equalsIgnoreCase(databaseService.getNoLose(uniqueid[0])))
                {
                    log.info("Not adjudicating game between "+names[0]+" and "+names[1]+" because "+names[0]+" would lose, but is on the nolose list");
                    removeHandler(this);
                    return;
                }
            }
            else
            {
                if(names[0].equalsIgnoreCase(databaseService.getWinExempt(uniqueid[0])))
                {
                    log.info("Not adjudicating game between "+names[0]+" and "+names[1]+" because "+names[0]+" would win, but is on the nowin list");
                    removeHandler(this);
                    return;
                }
                if(names[1].equalsIgnoreCase(databaseService.getNoLose(uniqueid[1])))
                {
                    log.info("Not adjudicating game between "+names[0]+" and "+names[1]+" because "+names[1]+" would lose, but is on the nolose list");
                    removeHandler(this);
                    return;
                }
            }
            adjudicateThisGame();
        }
    }
    
    private void adjudicateThisGame() {
        JBAdjudicateAction action = gamelog.getMatchingAdjudicationRule(this);
        switch (action) {
        case WIN:
            log.info("adjudicate-win");
            if (!testonly) {
                switch (disconnectorColor) {
                case WHITE:
                    adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.WHITE);
                    break;
                case BLACK:
                    adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.BLACK);
                    break;
				default:
					log.error("INVALID COLOR FOUND");
					break;
                }
            }
            break;
        case LOSE:
            log.info("adjudicate-lose");
            if (!testonly) {
                switch (disconnectorColor) {
                case WHITE:
                    adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.BLACK);
                    break;
                case BLACK:
                    adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.WHITE);
                    break;
				default:
					log.error("INVALID COLOR FOUND (2)");
					break;
                }
            }
            break;
        case DRAW:
            log.info("adjudicate-draw");
            if (!testonly) {
                adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.DRAW);
            }
            break;
        case ABORT:
            log.info("adjudicate-abort");
            if (!testonly) {
                adjudicate(gamestarted.whitename(), gamestarted.blackname(), AdjudicateAction.ABORT);
            }
            break;
        case MANUAL:
            log.info("adjudicate-manual");
            removeHandler(this);
            break;
        }
        return;
    }
    
    @Override
    public void setUciOption(Option o) {
        log.debug("setUciOption");
    }

    @Override
    public void badUciOption(BadOption o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void engineStarted(UCIEngine engine) {
        log.debug("Notified that engine has been started");
        uciengine = engine;
        
        uciengine.NewGame();
        
        try {
            engine.newPosition(moveList);
        } catch (Exception e1) {
            e1.printStackTrace();
            engine.quit();
            return;
        }
        
        SearchParameters p = new SearchParameters();
        if (engineRule != null)
            engineRule.setSearchParameters(p);
        else
            p.setMovetime(10 * 1000);
        try {
            engine.calculate("AdjudicateBase", p);
        } catch (Exception e) {
            e.printStackTrace();
            engine.quit();
            return;
        }
        //engine.isready();
    }

    @Override
    public void bestmove(String smithmove, String ponder_smithmove) {
        log.debug("bestmove found: " + smithmove + " score=" + score + ",matein=" + matein);
        log.info("Engine statistics: depth="+e_depth+",nps="+e_nps+",nodes="+e_nodes+",time="+e_time+",score="+score+",mate="+matein);

        if (matein != 0) {
            if (matein < 0)
                losingColor = currentMoveColor;
            else
                losingColor = (currentMoveColor == Color.WHITE ? Color.BLACK : Color.WHITE);
        } else if (score > 0)
            losingColor = (currentMoveColor == Color.WHITE ? Color.BLACK : Color.WHITE);
        else if (score < 0)
            losingColor = currentMoveColor;

        log.debug("losingColor has been set to " + losingColor);

        uciengine.quit();
    }

    @Override
    public void unknownuci(String line) {
        log.error("Unknown UCI line:" + line);
    }

    @Override
    public void setUciButton(String name) {
        log.error("I don't know what to do about button properties: " + name);
    }

    private ICCMove getDisconnectorMove() {
        moveIterator i = moveList.iterator();
        ICCMove move = (ICCMove) i.lastmove();
        if (move == null)
            return null;
        if (currentMoveColor == disconnectorColor) {
            return (ICCMove) i.backward();
        }
        return move;
    }

    private ICCMove getVictimMove() {
        moveIterator i = moveList.iterator();
        ICCMove move = (ICCMove) i.lastmove();
        if (move == null)
            return null;
        if (currentMoveColor != disconnectorColor) {
            return (ICCMove) i.backward();
        }
        return move;
    }

    public int varmovesto50() {
        return gameboard.moveTo50MoveRule();
    }

    public String varratingtype() {
        return gamestarted.ratingtype();
    }

    public int varinitial() {
        return gamestarted.whiteinitial();
    }

    public int varcanvictimwinontime() {
        return (gameboard.canWinOnTime(disconnectorColor == Color.BLACK ? Color.WHITE : Color.BLACK) ? 1 : 0);
    }
    
    public int varincrement() {
        return gamestarted.whiteincrement();
    }

    public int varrating() {
        if (disconnectorColor == Color.WHITE)
            return gamestarted.whiterating();
        else if (disconnectorColor == Color.BLACK)
            return gamestarted.blackrating();
        else
            return -1;
    }

    public int varvictimrating() {
        if (disconnectorColor == Color.WHITE)
            return gamestarted.blackrating();
        else if (disconnectorColor == Color.BLACK)
            return gamestarted.whiterating();
        else
            return -1;
    }

    public int varratingdifference() {
        //return Math.abs(gamestarted.whiterating() - gamestarted.blackrating());
        return varvictimrating() - varrating();
    }

    public String varevent() {
        return gameEvent;
    }

    public int varisevent() {
        return (gameEvent == null || gameEvent.isEmpty() ? 0 : 1);
    }

    public int varistitled() {
        if (disconnectorColor == Color.WHITE)
            return (gamestarted.whitetitles().isTitled() ? 1 : 0);
        else if (disconnectorColor == Color.BLACK)
            return (gamestarted.blacktitles().isTitled() ? 1 : 0);
        else
            return 0;

    }

    public int varisvictimtitled() {
        if (disconnectorColor == Color.WHITE)
            return (gamestarted.blacktitles().isTitled() ? 1 : 0);
        else if (disconnectorColor == Color.BLACK)
            return (gamestarted.whitetitles().isTitled() ? 1 : 0);
        else
            return 0;

    }

    public int varhighertitle() {
        if (disconnectorColor == Color.NONE)
            return 0;

        if (disconnectorColor == Color.WHITE)
            return gamestarted.whitetitles().compareFideTitle(gamestarted.blacktitles());
        else
            return gamestarted.blacktitles().compareFideTitle(gamestarted.whitetitles());
    }

    public int varvictimhighertitle() {
        if (disconnectorColor == Color.NONE)
            return 0;

        if (disconnectorColor == Color.WHITE)
            return gamestarted.blacktitles().compareFideTitle(gamestarted.whitetitles());
        else
            return gamestarted.whitetitles().compareFideTitle(gamestarted.blacktitles());
    }

    public int varhalfmoves() {
        return this.gameboard.moveNumber();
    }

    public int varmovenumber() {
        return this.currentHalfMove;
    }

    public int varscore() {
        if (this.disconnectorColor == this.currentMoveColor)
            return score;
        else
            return -score;
    }

    public int vartimeleft() {
        ICCMove m = getDisconnectorMove();
        if (m == null)
        {
            if(disconnectorColor == Color.WHITE) {
                return gamestarted.whiteinitial()*60*1000;
            } else if(disconnectorColor == Color.BLACK) {
                return gamestarted.blackinitial()*60*1000;
            } else {
                return 0;
            }
        } else
            return m.getMsec();
    }

    public int varvictimtimeleft() {
        ICCMove m = getVictimMove();
        if (m == null)
            if(disconnectorColor == Color.WHITE) {
                return gamestarted.blackinitial()*60*1000;
            } else if(disconnectorColor == Color.BLACK) {
                return gamestarted.whiteinitial()*60*1000;
            } else {
                return 0;
            }
        else
        {
            int ms = m.getMsec();
            if(ms<15000) ms += 15000;
            return ms;
        }
    }

    public int varwillbemated() {
        if (disconnectorColor == losingColor)
            return (matein < 0 ? 1 : 0);
        else
            return (matein > 0 ? 1 : 0);
    }

    public int varmatein() {
        if (disconnectorColor == currentMoveColor)
            return matein;
        else
            return -matein;
    }

    public int varisvictimcomputer() {
        if (disconnected == -1 || disconnectorColor == Color.NONE)
            return 0;
        if (disconnectorColor == Color.WHITE) {
            return (gamestarted.blacktitles().isC()?1:0);
        } else
            return (gamestarted.whitetitles().isC()?1:0);
    }

    public int variscomputer() {
        if (disconnected == -1 || disconnectorColor == Color.NONE)
            return 0;
        if (disconnectorColor == Color.WHITE) {
            return (gamestarted.whitetitles().isC() ? 1 : 0);
        } else {
            return (gamestarted.blacktitles().isC() ? 1 : 0);
        }
    }

    public int varadjournedgames() {
        if (disconnected == -1)
            return 0;
        else
            return adjourned[disconnected];
    }

    public int varvictimadjournedgames() {
        if (disconnected == -1)
            return 0;
        else
            return adjourned[(disconnected == 0 ? 1 : 0)];
    }

    public String varwhodisconnected() {
        if (losingColor == Color.NONE || disconnectorColor == Color.NONE)
            return "unknown";
        if (losingColor == disconnectorColor)
            return "loser";
        else
            return "winner";
    }

    public int varhavedisconnector() {
        return (disconnectorColor != Color.NONE ? 1 : 0);
    }

    static public String[] getStringVariables() {
        if (AdjudicateBase.stringVariables == null)
            AdjudicateBase.initializeVariables();
        String[] ret = new String[0];
        return AdjudicateBase.stringVariables.keySet().toArray(ret);
    }

    static public String[] getIntVariables() {
        if (AdjudicateBase.stringVariables == null)
            AdjudicateBase.initializeVariables();
        String[] ret = new String[0];
        return AdjudicateBase.intVariables.keySet().toArray(ret);
    }

    private boolean checkEngineRules() {
        EngineRule er = gamelog.getMatchingEngineRule(this);
        return (er != null && !er.getSkip());
    }

    static private void initializeVariables() {
        AdjudicateBase.stringVariables = new HashMap<String, Method>();
        AdjudicateBase.intVariables = new HashMap<String, Method>();

        for (Method m : AdjudicateBase.class.getDeclaredMethods()) {
            log.debug("variables checking " + m);
            if (m.getName().startsWith("var")) {
                if (m.getReturnType().getName().equals("java.lang.String")) {
                    log.debug("adding string variable " + m.getName().substring(3));
                    AdjudicateBase.stringVariables.put(m.getName().substring(3), m);
                } else if (m.getReturnType().getName().equals("int")) {
                    log.debug("adding integer variable " + m.getName().substring(3));
                    AdjudicateBase.intVariables.put(m.getName().substring(3), m);
                }
            }
        }
    }

    public String getSVariable(String variable) throws ParseException {
        log.debug("getSVariable looking for " + variable);
        Method m = AdjudicateBase.stringVariables.get(variable);
        if (m != null) {
            try {
                String ret = (String) m.invoke(this, new Object[0]);
                if (gamelog != null)
                    gamelog.storeLogVariable(variable, ret);
                return (ret == null ? "" : ret);
            } catch (IllegalAccessException ex) {
            	log.error(ex);
            } catch (IllegalArgumentException ex) {
            	log.error(ex);
            } catch (InvocationTargetException ex) {
            	log.error(ex);
            }
        } else {
            throw new ParseException(variable+" cannot be found");
        }
        return null;
    }

    public int getIVariable(String variable) throws ParseException {
        log.debug("getIVariable looking for " + variable);
        Method m = AdjudicateBase.intVariables.get(variable);
        if (m != null) {
            try {
                int ret = (Integer) m.invoke(this, new Object[0]);
                if (gamelog != null)
                    gamelog.storeLogVariable(variable, Integer.toString(ret));
                return ret;
            } catch (IllegalAccessException ex) {
            	log.error(ex);
            } catch (IllegalArgumentException ex) {
            	log.error(ex);
            } catch (InvocationTargetException ex) {
            	log.error(ex);
            }
        } else {
            throw new ParseException(variable+" cannot be found");
            
        }
        return 0;
    }

    @Override
    public Object getVariable(String variable) throws ParseException {
        if (intVariables.containsKey(variable))
            return getIVariable(variable);
        else
            return getSVariable(variable);
    }

    @Override
    public boolean isIntVariable(String variable) {
        return intVariables.containsKey(variable);
    }

    @Override
    public void initHandler() {
        stored(names[0]);
    }

    public void gameAdjudicated(GameAdjudicated p) {

        log.debug("gameAdjudicated wasAdjudicated="+p.wasAdjudicated()+",testonly="+testonly+",white="+p.white()+",black="+p.black()+",action="+p.action());
        this.removeHandler(this);
        
        if(!p.wasAdjudicated() || testonly)
            return;
        
        if(!p.white().equals(names[0]) || !p.black().equals(names[1]))
        {
            log.error("Unable to send messages or tells because names[x] does not match white/black of game started!");
            return;
        }
        
        switch (p.action()) {
        case WHITE:
            if(iccBean.getMessageWinner())
                message(p.white(), messageSource.getMessage("adjudication.winner.message", null, new Locale(language[0])));
            if(iccBean.getMessageLoser())
                message(p.black(), messageSource.getMessage("adjudication.loser.message", null, new Locale(language[1])));
            if(iccBean.getTellWinner() && !gamestarted.whitetitles().isC())
                xtell(p.white(), messageSource.getMessage("adjudication.winner.message", null, new Locale(language[0])));
            if(iccBean.getTellLoser() && !gamestarted.blacktitles().isC())
            	xtell(p.black(), messageSource.getMessage("adjudication.loser.message", null, new Locale(language[1])));
            break;
        case BLACK:
            if(iccBean.getMessageWinner())
                message(p.black(), messageSource.getMessage("adjudication.winner.message", null, new Locale(language[1])));
            if(iccBean.getMessageLoser())
                message(p.white(), messageSource.getMessage("adjudication.loser.message", null, new Locale(language[0])));
            if(iccBean.getTellWinner() && !gamestarted.blacktitles().isC())
                xtell(p.black(), messageSource.getMessage("adjudication.winner.message", null, new Locale(language[1])));
            if(iccBean.getTellLoser() && !gamestarted.whitetitles().isC())
                xtell(p.white(), messageSource.getMessage("adjudication.loser.message", null, new Locale(language[0])));
            break;
        case ABORT:
            if(iccBean.getMessageAbort())
            {
            	message(p.white(), messageSource.getMessage("adjudication.abort.message", null, new Locale(language[0])));
            	message(p.black(), messageSource.getMessage("adjudication.abort.message", null, new Locale(language[1])));
            }
            if(iccBean.getTellAbort())
            {
                if(!gamestarted.whitetitles().isC())
                	xtell(p.white(), messageSource.getMessage("adjudication.abort.message", null, new Locale(language[0])));
                if(!gamestarted.blacktitles().isC())
                	xtell(p.black(), messageSource.getMessage("adjudication.abort.message", null, new Locale(language[1])));
            }
            break;
        case DRAW:
            if(iccBean.getMessageDraw())
            {
            	message(p.white(), messageSource.getMessage("adjudication.draw.message", null, new Locale(language[0])));
            	message(p.black(), messageSource.getMessage("adjudication.draw.message", null, new Locale(language[1])));
            }
            if(iccBean.getTellDraw())
            {
                if(!gamestarted.whitetitles().isC())
                	xtell(p.white(), messageSource.getMessage("adjudication.draw.message", null, new Locale(language[0])));
                if(!gamestarted.blacktitles().isC())
                	xtell(p.black(), messageSource.getMessage("adjudication.draw.message", null, new Locale(language[1])));
            }
            break;
		default:
			log.error("INVALID ACTION FOUND");
			break;
        }
    }

    @Override
    public void haveInfo(UCIInfo info) {
        if(info.haveNps())
            e_nps = info.getNps();
        if(info.haveNodes())
            e_nodes = info.getNodes();
        if(info.haveTime())
            e_time = info.getTime();
        if(info.haveDepth())
            e_depth = info.getDepth();
        if(info.haveMate()) {
//            if(score > 0) {
//                log.error("Why do we have a non-zero getMate("+info.getMate()+") plus a score("+score+")?");
//            }
            matein = info.getMate();
            log.debug("Mate in " + matein + " moves found!");
        }
        if(info.haveCp()) {
//            if(matein > 0) {
//                log.error("Why do we have a non-zero matein("+matein+") plus a cp("+info.getCp()+")?");
//            }
            log.debug("Current score is " + info.getCp());
            score = info.getCp();
            matein = 0;
        }
    }

    @Override
    public void iccException(ICCException ex) {
    }

    @Override
    public void connectionClosed() {
    }

    @Override
    public void myGameResult(GameResultPacket p) {
    }

    @Override
    public void examinedGameIsGone(GameNumber p) {
    }

    public void canExamine() {
        log.debug("examine #" + gameid + ",handler=" + this.hashCode());
        examine("#" + gameid);
    }

    @Override
    public void shuttingDown() {
    }

    @Override
    public void setInstance(ICCInstance icc) {
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }

	@Override
	public void takeBackOccurred(TakeBackOccurred p) {
	}

	@Override
	public void newOffers(OffersInMyGame p) {
	}

	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
	}

}
