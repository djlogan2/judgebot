package chessclub.com.icc.tt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.ICCInstance;
import chessclub.com.enums.Rating;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.handler.interfaces.IAcceptGenericPackets;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.handler.interfaces.IMyGame;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.l2.GameNumber;
import chessclub.com.icc.l2.GameResult;
import chessclub.com.icc.l2.GameResultPacket;
import chessclub.com.icc.l2.GameStarted;
import chessclub.com.icc.l2.Msec;
import chessclub.com.icc.l2.OffersInMyGame;
import chessclub.com.icc.l2.SendMoves;
import chessclub.com.icc.l2.TakeBackOccurred;
import chessclub.com.icc.tt.entity.CompletedTactic;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.entity.TacticsUser;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.GameScore;
import david.logan.chess.support.MoveList;
import david.logan.custom.collections.AgingList;

public class TacticalTrainerGame extends DefaultICCCommands
        implements IAcceptGenericPackets, IGetpx, IMyGame {
        private static final Logger log = LogManager.getLogger(TacticalTrainerGame.class);
        private String whoami;
        
        private MessageSource messageSource;
        private IDatabaseService databaseService;

        private static ArrayList<Integer> activeIds = new ArrayList<Integer>();
        private static HashMap<Integer, TacticalTrainerGame> gameMap = new HashMap<Integer, TacticalTrainerGame>();
        private Date starttime;
        private Date firstmove;
        private Date endtime;
        private Integer firstwrongmove;
        private int movesmade = 0;
        private TacticsUser user;
        private int uniqueid;
        private String opponent;
        private Color opponentColor;
        private GameStarted gs;
        private Fen fen;
        private MoveList moves;
        private int atmove = 0;
        private int tacticid;
        private int tacticrating;
        private int tacticgames;
        private Locale locale;
        private CompletedTactic ct;
        private int ourMoveEvenOdd;
        private boolean needs_censor = true;
        private static AgingList<String> abusers = new AgingList<String>(60);
        
        static {
            activeIds.add(-1); // Just to make sure we have one
        }

        public void removeAbuser(String user) {
        	abusers.remove(user);
        }
        
        @SuppressWarnings("unused")
        private TacticalTrainerGame() {
        }
        
        public TacticalTrainerGame(MessageSource messageSource, IDatabaseService databaseService, String whoami, String opponent) {
            this.whoami = whoami;
            this.opponent = opponent;
            this.messageSource = messageSource;
            this.databaseService = databaseService;
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
            this.getpx(opponent, "tactic");
        }

        @Override
        public void connectionClosed() {

        }

        @Override
        public void badCommand1(BadCommand p) {

        }

        @Override
        public void badCommand2(L1ErrorOnly p) {
//            if(gs != null)
//                simulresign(gs.gamenumber());
//            else
                log.error("Why are we in badCommand2? : " + p);
        }

        @Override
        public void getpx(Getpx p) {
            if(p.getHasFailed() || !p.isValidUser()) {
                log.warn("Unable to get getpx information for " + p.getUserid());
                _xtell("nousername");
                return;
            }
            
            accept(opponent);

            log.debug("We have a unique id for "+p.getUserid()+": "+p.getUniqueId());
            uniqueid = p.getUniqueId();
            user = databaseService.getUser(p.getUserid(), uniqueid, p.getRating(Rating.STANDARD));
            locale = new Locale(p.getLanguage());
        }

        @Override
        public void myGameResult(GameResultPacket p) {
            if (gs == null || p.gamenumber() != gs.gamenumber())
                return;
            
            synchronized (gameMap) {
                gameMap.remove(gs.gamenumber());
            }
            synchronized (activeIds) {
                activeIds.remove((Object) (new Integer(tacticid))); // cast it to death to make sure we get remove(Object) and not remove(index)
            }

            getInstance().removeHandler(this);
            
            if(ct == null) {
//            	getInstance().removeHandler(this);
            	return;
            }

            if(needs_censor && !p.description().contains("disconnected")) {
            	log.debug("We may have to censor " + opponent);
            	if(abusers.add(opponent) == 5) {
                	censor(opponent);
            		log.error("I censored " + opponent + " for abusing me");
                	message("djlogan", "I have censored " + opponent);
  //              	getInstance().removeHandler(this);
                	return;
            	}
            }
            
            endtime = new Date();
    
            GameResult expectedResult = (opponentColor == Color.WHITE ? GameResult.WHITE_WINS : GameResult.BLACK_WINS);
            GameScore expectedScore = (opponentColor == Color.WHITE ? GameScore.WHITE_WINS : GameScore.BLACK_WINS);
            
            boolean won = false;
            if (firstwrongmove == null &&
                    (((p.gameresult() == GameResult.CHECKMATE || p.gameresult() == GameResult.RESIGNED) && p.gamescore() == expectedScore) ||
                     p.gameresult() == expectedResult))
                won = true;
    
            int newUsersRating = ratingChange(user.getRating(), tacticrating, user.getGamecount(), tacticgames, won);
            int newTacticRating = ratingChange(tacticrating, user.getRating(), tacticgames, user.getGamecount(), !won);
            Integer firstmovetime = ( firstmove == null ? null : new Integer((int) (firstmove.getTime() - starttime.getTime()) / 1000));
            ct.finishTactic(newUsersRating, newTacticRating, firstmovetime, (int) (endtime.getTime() - starttime.getTime()) / 1000, firstwrongmove);
            databaseService.solvedTactic(ct);
    
            if (won) {
                _xtell("youwin");
            }
    
            _xtell("newrating", Integer.toString(newUsersRating), Integer.toString(newTacticRating));
    
            log.trace("Game " + gs.gamenumber() + " with " + opponent + " has ended");
        }

        @Override
        public void myGameStarted(GameStarted p) {
            gs = p;
            
            if(p.whitename().equals(whoami)) {
                opponentColor = Color.BLACK;
            } else {
                opponentColor = Color.WHITE;
            }
            
            synchronized(gameMap) {
                gameMap.put(gs.gamenumber(),  this);
            }
            
            TacticsTable tt = null;
            synchronized(activeIds) {
                tt = databaseService.getRandomTactic(activeIds, user, opponentColor, user.getRating());
            
                if(tt == null) {
                    _xtell("notacticsleft");
                    needs_censor = false;
                    simulresign(gs.gamenumber());
                } else {
                    activeIds.add(tt.getId());
                    try {
                    	fen = tt.getFen();
                        moves = tt.getMoves();
                        tacticid = tt.getId();
                        tacticrating = tt.getRating();
                        tacticgames = tt.getNumberOfGames();
                        simulloadfen(gs.gamenumber(), fen);
                        ct = new CompletedTactic(uniqueid, tacticid, user.getRating(), tacticrating);
                        if(tt.getFen().toMove() != tt.getTomove()) {
                        	simulmove(gs.gamenumber(), tt.getMoves().getMove(0));
                        	ourMoveEvenOdd = 0;
                        } else
                        	ourMoveEvenOdd = 1;
                        starttime = new Date();
                    } catch(Exception e) {
                    	log.error("Had to resign the game due to an exception, probably in the FEN string for tactic " + tt.getId() + ": " + e.getMessage());
                        needs_censor = false;
                    	simulresign(gs.gamenumber());
                    }
                }
                
            }
            
            String rating = Integer.toString(user.getRating());
            @SuppressWarnings("unused")
            String trating = Integer.toString(tacticrating);
            
            if(tacticgames < 20)
                trating += "/P" + tacticgames;
            
            if(user.getGamecount() < 20)
                rating += "/P" + Integer.toString(user.getGamecount());
            
            _kibitz("problemnumber", Integer.toString(tacticid));
            _kibitz("yourrating", rating);
            
            if(user.getGamecount() >= 20)
                _kibitz("solved1", user.getGamecount(), databaseService.getRanking(user));
            else
                _kibitz("solved2", user.getGamecount());
            String oppColor = messageSource.getMessage(opponentColor.toString(), null, locale);
            String myColor  = messageSource.getMessage(opponentColor.other().toString(), null, locale);
            _kibitz("tacticcolor", oppColor, whoami, myColor);

            log.debug("I have started a game with " +opponent);
            
            try {
                simulize();
                seek(20, 20, false, 20, Color.NONE, false, 0, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void sendMoves(SendMoves p) {
            if(gs == null || p.gamenumber() != gs.gamenumber() || moves == null)
                return;
            
            if(atmove % 2 == ourMoveEvenOdd)  { // If TT just made a move
    			if(++atmove == moves.moveCount()) { // If we are out of moves
    				simulresign(gs.gamenumber());
    			}
                return;
            }
            
            if(firstmove == null)
                firstmove = new Date();
            needs_censor = false;
            
            ICCMove playedMove;
            
            try {
                playedMove = new ICCMove(p);
            } catch (Exception e1) {
                e1.printStackTrace();
                return;
            }
            
            movesmade++;
            if(moves == null || atmove < 0 || playedMove == null) {
            	System.out.println("OHMYGOD: moves: " + moves + ", atmove: " + atmove + ", playedMove: " + playedMove);
            	log.debug("OHMYGOD: moves: " + moves + ", atmove: " + atmove + ", playedMove: " + playedMove);
            	return;
            }
            
            if(atmove >= moves.moveCount()) {
            	log.info("User tried to make a move while we are trying to resign");
            	return;
            }
            
            if(!moves.getMove(atmove).equals(playedMove)) {
                if(firstwrongmove == null)
                    firstwrongmove = new Integer(movesmade);
                simultakeback(gs.gamenumber(), 1);
                try {
                    _kibitz("imsorry", moves.getMove(atmove).getShortSmithMove().toString());
                } catch (Exception e) {
                    log.error("Error issuing move: " + e);
                    _kibitz("imsorry", moves.getMove(atmove).toString());
                }
                return;
            }
            
			if(++atmove == moves.moveCount()) { // If we are out of moves
				simulresign(gs.gamenumber());
				return;
			}

            try {
                simulmove(gs.gamenumber(), moves.getMove(atmove));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void examinedGameIsGone(GameNumber p) {

        }

        @Override
        public void myGameEnded(GameNumber p) {
        }

        @Override
        public void shuttingDown() {

        }

        public int ratingChange(int yourRating, int opponentRating, int yourNumberOfGames, int opponentNumberOfGames, boolean win) {
            double Kopp = (opponentNumberOfGames > 20 ? 1 : 1 + opponentNumberOfGames);
            double KYou = (yourNumberOfGames > 20 ? 1 : 21);
            double KYourDiv =  (yourNumberOfGames > 20 ? 1 : 1 + yourNumberOfGames);
            double KOppdiv = (opponentNumberOfGames > 20 ? 1 : 21);
            double result = (double)yourRating + 32 * Kopp * KYou * ((win ? 1 : 0) - 1/(1 + Math.pow(10,((double)opponentRating-(double)yourRating)/400.0))) / KYourDiv / KOppdiv;
            log.trace("ratingChange("+yourRating+","+opponentRating+","+yourNumberOfGames+","+opponentNumberOfGames+","+win+")="+(int)Math.round(result));
            return (int)Math.round(result);
        }

        protected void _xtell(String code, Object ... parameters) {
            String msg = messageSource.getMessage(code, parameters, locale);
            super.xtell(opponent,  msg);
        }
        
        protected void _kibitz(String code, Object ... parameters) {
            String msg = messageSource.getMessage(code, parameters, locale);
            super.kibitz(gs.gamenumber(),  msg);
        }
        
        @Override
        public void kibitz(int gamenumber, String what) {
            super.kibitz(gamenumber, what);
            xtell("djlogan", "Fix this kibitz: '" + what + "'");
        }
        @Override
        public void xtell(String who, String what) {
            super.xtell(who,  what);
            xtell("djlogan", "Fix this xtell: '" + what + "'");
        }

        @Override
        public void msec(Msec p) {
        }

        @Override
        public void Error(chessclub.com.icc.l2.Error error) {
        	log.error(error.text() + " / " + error.errornumber());
//        	if(gs == null) return;
//			simulresign(gs.gamenumber());
        }

		@Override
		public void takeBackOccurred(TakeBackOccurred p) {
		}

		@Override
		public void newOffers(OffersInMyGame p) {
			if(gs == null || p.gameNumber() != gs.gamenumber())
				return;
			log.info(String.format("%s offered %s %s %s %s %s %s %s %s in game %d",
					opponent,
					p.whiteDraw()    ? "White Draw" : "",
					p.blackDraw()    ? "Black Draw" : "",
					p.whiteAdjourn() ? "White Adjourn" : "",
					p.blackAdjourn() ? "Black Adjourn" : "",
					p.whiteAbort()   ? "White Abort" : "",
					p.blackAbort()   ? "Black Abort" : "",
					p.whiteTakeback() != 0 ? String.format("White takeback %d", p.whiteTakeback()) : "",
					p.blackTakeback() != 0 ? String.format("Black takeback %d", p.blackTakeback()) : "",
					p.gameNumber()
					));
		}
}
