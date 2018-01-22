package chessclub.com.icc.tt;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.l1l2.ICCMove;
import chessclub.com.icc.uci.BadOption;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.Option;
import chessclub.com.icc.uci.SearchParameters;
import chessclub.com.icc.uci.UCIEngine;
import chessclub.com.icc.uci.UCIInfo;
import chessclub.com.icc.uci.UCIRequest;
import david.logan.chess.support.ChessPieceEnum;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;
import david.logan.chess.support.NormalChessBoard;
import david.logan.chess.support.NormalChessGame;
import david.logan.chess.support.MoveList.moveIterator;
import david.logan.chess.support.PolyglotBook;

public abstract class GameChecker extends DefaultICCCommands implements
		UCIRequest {
	private static final Logger log = LogManager.getLogger(GameChecker.class);
	// *********** THESE ARE THE VARIABLES USED TO WATCH FOR ENDING GAMES FOR
	// TACTICAL POSITIONS **************
	static protected UCIEngine _engine;
	private Fen startingfen = null;
	protected NormalChessGame gameboard;
	protected MoveList gamemoves;
	private SearchParameters _searchParms;
	private SearchParameters intacticParms;
	private OnlineTablebase otb = new OnlineTablebase();
	private moveIterator skiptacticalmoves = null;
	private MoveList tactic;
	private Fen tacticfen = null;
	private Color tacticcolor;
	// ****************************************** END
	private int iBufferSize = 4096;
	private String uciProgram = null;
	//private String uciBookFile = null;
	private int uciThreads = 1;

	private PolyglotBook book = null;			// The book file if we have one
	
	public abstract void getnextmove();

	public abstract void endofgame();

	public abstract void goodtactic(Fen fen, String moves, boolean winning);
	public abstract void badtactic(Fen fen, String moves);
	
	protected Move getpreferredlosersmove(MoveList tactic, Move moveopt0,
			Move moveopt, Move bestmove) {
		return bestmove;
	}

	public GameChecker(int iBufferSize, String uciProgram,
			String uciBookFile, int uciThreads, int NoTacticTime,
			int InTacticTime) {
		_searchParms = new SearchParameters();
		_searchParms.setMovetime(NoTacticTime * 1000);
		intacticParms = new SearchParameters();
		intacticParms.setMovetime(1000 * InTacticTime);
		this.iBufferSize = iBufferSize;
		this.uciProgram = uciProgram;
		if(uciBookFile != null) {
			book = new PolyglotBook(uciBookFile);
		}
		this.uciThreads = uciThreads;
	}

	public void analyzeGame() {
		log.trace("analyzeGame");
		try {
			startingfen = tacticfen = null;
			tactic = null;
			gamemoves = new MoveList();
			gameboard = new NormalChessGame();
			
			if (_engine == null) {
				_engine = new UCIEngine(this.iBufferSize, this.uciProgram, this, false);
				_engine.start();
			} else {
				_engine.NewGame();
				getnextmove();
			}
			;
		} catch (Exception e) {
			// This catches the invalid FEN exception which of course there is
			// none
			e.printStackTrace();
		}
	}

	public void analyzeGame(String fen, String firstMove) throws Exception
	{
		log.trace("analyzeGame("+fen+","+firstMove+")");
		gamemoves = new MoveList();
		gameboard = new NormalChessGame(new Fen(fen));
		if(firstMove != null && firstMove.length() != 0)
			gameboard.makeMove(new Move(firstMove, true));
		
		startingfen = tacticfen = new Fen(gameboard);
		tactic = new MoveList();
		tacticcolor = tacticfen.toMove();
		
		if (_engine == null) {
			_engine = new UCIEngine(this.iBufferSize, this.uciProgram, this, false);
			_engine.start();
		} else {
			_engine.NewGame();
			doCalculate("analyzeGame", true, false);
		};
	}
	
	public void move(ICCMove move) {
		log.trace("Move("+move+")");
		try {
			Fen bookFen = null;
			if(book != null)
				bookFen = new Fen(gameboard.getFEN());
			else
				log.trace("Book file not being used");
			gameboard.makeMove(move);
			gamemoves.add(move);
			if (skiptacticalmoves != null) {
				if (move.equals(skiptacticalmoves.next())) {
					log.debug("Skipping move due to being in completed tactic: " + move);
					getnextmove();
					return;
				} else {
					skiptacticalmoves = null;
				}
			}
			if(bookFen != null) {
				ArrayList<PolyglotBook.entry> bookmoves = book.getBookMoves(bookFen);
				if(bookmoves != null) {
					Iterator<PolyglotBook.entry> i = bookmoves.iterator();
					while(i.hasNext()) {
						if(i.next().getMove().equals(move)) {
							log.debug("Skipping book move");
							getnextmove();
							return;
						}
					}
				}
			}
			doCalculate("move", false, false);
		} catch (Exception e) {
			e.printStackTrace();
			nomoremoves();
			return;
		}
	}

	public void nomoremoves() {
		log.trace("nomoremoves");
		gameboard = null;
		gamemoves = null;
		tacticfen = null;
		tactic    = null;
		skiptacticalmoves = null;
		endofgame();
	}

	// -------------- Start of UCIRequest interface
	@Override
	public void engineStarted(UCIEngine engine) {
		try {
			engine.setOption("MultiPV", "3");
			engine.setOption("Threads", Integer.toString(uciThreads));
			if(tacticfen != null)
			{
				_engine.NewGame();
				doCalculate("engineStarted 1", true, true);
			}
			else if (startingfen != null) {
				_engine.NewGame();
				// _engine.newPosition(startingfen.getFEN(), gamemoves);
				// _engine.calculate(_searchParms);
				doCalculate("engineStarted 2", false, false);
			} else
				getnextmove(); // Because the engine was started as a function
								// of entering a game
		} catch (Exception e) {
			e.printStackTrace();
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
	public void unknownuci(String line) {
	}

	private String[] moveoption = new String[2];
	private int[] score = new int[2];

	@Override
	public void haveInfo(UCIInfo info) {
		Integer mate = null;
		if (info.haveMate() && info.getMate() != 0) {
			mate = info.getMate();
		}
		if (!info.haveCp() && mate == null)
			return;
		if (!info.haveMultipv())
			return;
		if (!info.havePv())
			return;
		if (info.getMultipv() == 3)
			return; // We only have MultiPV = 3 to improve tactical accuracy
		String[] moves = info.getPv().split("\\s+");
		moveoption[info.getMultipv() - 1] = moves[0];
		if (mate != null) {
			score[info.getMultipv() - 1] = 999999 - Math.abs(mate);
			if (mate < 0)
				score[info.getMultipv() - 1] *= -1;
		} else {
			score[info.getMultipv() - 1] = info.getCp();
		}
	}

	private Integer matein(int score) {
		if(score < 999000) return null;
		return 1000000 - score;
	}

	protected boolean getwinning() {
		if(moveoption[0] != null && !"bom".equals(moveoption[0])) {
			if(moveoption[1] != null && !"bom".equals(moveoption[1])) {
				Integer mate = matein(score[0]);
				if(mate != null && mate <= 5) { // Mate in 5 or less is automatically winning
					return true;
				} else if(mate != null && mate > 9) { // Anything more than a mate in 9 is not winning
					return false;
				} else if(mate != null) { // Mate in 6-9 is winning only if the engine can't find a 2nd best mate
					if(matein(score[1]) == null) return true;
				}else if(score[1] <250) {
					// Yes, I know, this isn't winning just because the 2nd best move isn't > 250
					// But keep in mind that it won't ever go in as a tactic unless score[0] is awesome
					// and there is material gain. All this really does is mark whether or not there
					// was a viable second move.
					return true;
				}
			} else if(score[0] > 0) { // Can't be the losers move, although I doubt this will ever make a difference
				return true; // If there is only one move
			}
		}
		
		return false;
	}
	private String getfen() {
		try {
			return gameboard.getFEN().getFEN();
		} catch (Exception e) {
			e.printStackTrace();
			return "** INVALID FEN **";
		}
	}
	@Override
	public void bestmove(String smithmove, String ponder_smithmove) {
		log.trace("bestmove("+smithmove+","+ponder_smithmove+")");
		// int s1 = score[0] - score[1];
		// int s2 = score[1] - score[0];
		Move move = null;
		Move[] moveopt = null;
		boolean tacticstarting = false;
		boolean winning = getwinning();
		try {
			if (smithmove != null && smithmove.length() != 0 && !"(none)".equals(smithmove))
				move = new winningmove(smithmove, true, winning);
			moveopt = new Move[2];
			if (moveoption[0] != null && !"bom".equals(moveoption[0]))
				moveopt[0] = new winningmove(moveoption[0], true, winning);
			if (moveoption[1] != null && !"bom".equals(moveoption[1]))
				moveopt[1] = new winningmove(moveoption[1], true, false);
		} catch (Exception e) {
			e.printStackTrace();
			nomoremoves();
			return;
		}
		// Character promote = (smithmove.length() > 4 ? smithmove.charAt(4) :
		// null);
		log.debug("bestmove score=[" + score[0] + "," + score[1] + "]"
				+ ",smithmove=" + smithmove + ",ponder=" + ponder_smithmove);
		
		if(score[0] > 0 && !winning) {
			if (tacticfen != null) {
				log.debug("Ending tactic due to not a winning move");
				completetactic();
			}
		} else if (move == null) {
			if (tacticfen != null) {
				completetactic();
			} else
				log.error("Why do we have a bestmove of (none) when not in a tactic??");
		} else if (tacticfen != null && tacticcolor != tacticfen.toMove()) {
			Move preferredMove = getpreferredlosersmove(tactic, moveopt[0],
					moveopt[1], move);
			tactic.add(preferredMove);
			try {
				log.debug("Adding losers best move to tactic: "
						+ preferredMove.getShortSmithMove());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (moveopt[0] == null && moveopt[1] == null) {
			// This should mean we are in book moves, as we have a "best move",
			// but no possibles
			if (tacticfen != null) {
				log.error("How are we in a tactic and yet have no possible moves??");
				completetactic();
			}
		} else if (moveopt[1] == null) {
			log.debug("Forced (1)");
			// One of the moves is forced. This is not a tactic, and if we are
			// in a tactic, since it's a forced move, just keep it going.
			if (tacticfen != null) {
				tactic.add(moveopt[0]);
			}
		} else if (moveopt[0] == null) {
			log.debug("Forced (2)");
			// One of the moves is forced. This is not a tactic, and if we are
			// in a tactic, since it's a forced move, just keep it going.
			if (tacticfen != null) {
				tactic.add(moveopt[1]);
			}
		} else if (score[0] > 900000 && score[0] == score[1]) { // Duplicate moves for checkmate doesn't work!
			log.debug("Duplicate checkmate moves");
			if (tacticfen != null) {
				log.debug("Seems like a we have completed a good tactic as both moves are even, but game score is winning: "
						+ score[0]
						+ ","
						+ score[1]
						+ " : "
						+ getfen());
				completetactic();
			}
		} else if (score[0] > 900000 || score[1] > 900000) {
			log.debug("Nonduplicate checkmate moves");
			if (tacticfen == null) {
				try {
					tacticfen = new Fen(gameboard);
					tactic = new MoveList();
					tacticcolor = tacticfen.toMove();
					tacticstarting = true;
				} catch (Exception e) {
					e.printStackTrace();
					tacticfen = null;
					tactic = null;
					tacticcolor = null;
					tacticstarting = false;
				}
			} else {
				tactic.add(move);
				log.debug("Adding checkmating move to tactic: " + smithmove);
			}
			;
		} else if (score[0] <= 250 && score[1] <= 250) {
			log.debug("Even or bad position");
			if (tacticfen != null) {
				log.debug("Skipping position due to seemingly even or bad position: "
						+ score[0]
						+ ","
						+ score[1]
						+ " : "
						+ getfen());
				badtactic(tacticfen,  null);
				tacticfen = null;
				tactic = null;
			}
		} else if (Math.abs(score[0] - score[1]) < 250) {
			log.debug("Game score is winning on both lines");
			if (tacticfen != null) {
				log.debug("Seems like a we have completed a good tactic as both moves are even, but game score is winning: "
						+ score[0]
						+ ","
						+ score[1]
						+ " : "
						+ getfen());
				completetactic();
			}
		} else if (tacticfen != null) {
			tactic.add((score[0] > score[1] ? moveopt[0] : moveopt[1]));
			log.debug("Adding move to tactic: " + score[0] + "," + score[1] + " : " + moveoption[0] + "," + moveoption[1]);
		} else if (score[0] >= 250 && score[1] >= 250
				&& (Math.abs(score[0] - score[1]) < 900)) {
			log.debug("Skipping tactic because we are already winning and new spread is less than a queen: "
					+ score[0]
					+ ","
					+ score[1]
					+ " : "
					+ moveoption[0]
					+ ","
					+ moveoption[1]);
		} else {
			log.debug("This seems to be a possible tactic: " + score[0] + ","
					+ score[1] + " : " + getfen() + " : "
					+ moveoption[0] + "," + moveoption[1] + ",winning=" + winning);
			try {
				tacticfen = new Fen(gameboard);
				tactic = new MoveList();
				tacticcolor = tacticfen.toMove();
				tacticstarting = true;
			} catch (Exception e) {
				e.printStackTrace();
				tacticfen = null;
				tactic = null;
				tacticcolor = null;
				tacticstarting = false;
			}
		}
		
		log.debug("End of if statement");
		score[0] = -1;
		score[1] = -1;
		moveoption[0] = "bom";
		moveoption[1] = "bom";

		if(tacticstarting && otb.getCount() != 0) {
			log.debug("Adding first OTB move to tactic: " + move);
			tactic.add(move);
			tacticstarting = false;
		}
		if (tacticfen != null) {
			if (!tacticstarting)
				tacticcolor = tacticcolor.other();
			try {
				log.trace("bestmove calculating next move");
				doCalculate("bestmove", true, tacticstarting);
			} catch (Exception e) {
				e.printStackTrace();
				log.trace("bestmove calling nomoremoves");
				nomoremoves();
				return;
			}
		} else {
			log.trace("bestmove calling getnextmove");
			getnextmove();
		}
	}

	private void completetactic() {
		log.trace("completetactic");
		NormalChessGame tempgame = new NormalChessGame();
		tempgame.loadFEN(tacticfen);
		log.debug("setting tempgame to " + tacticfen.getFEN());

		int loserinitialMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove().other());
		int winnerinitialMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove());

		boolean ischeckmate = false;
		for (moveIterator i = tactic.iterator(); i.hasNext();) {
			try {
				log.debug("Trying to make a move: " + i.peeknext());
				tempgame.makeMove(i.next());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int loserfinalMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove().other());
		int winnerfinalMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove());
		ischeckmate = tempgame.isCheckmate();
		log.debug("checkmate=" + tempgame.isCheckmate() + ", im="
				+ loserinitialMaterialCount + ",fm=" + loserfinalMaterialCount + ",iw="
				+ winnerinitialMaterialCount + ",fw="
				+ winnerfinalMaterialCount);
		
		if(!ischeckmate && loserfinalMaterialCount == winnerfinalMaterialCount) {
			log.debug("Abandoning tactic because long tactical string has resulted in nothing more than a retake in material");
			badtactic(tacticfen,  null);
			tacticfen = null;
			tactic = null;
			return;
		}
		
		String xxx = "";
		String space = "";
		tempgame = new NormalChessGame();
		tempgame.loadFEN(tacticfen);
		boolean winning = true;
		for (moveIterator i = tactic.iterator(); i.hasNext();) {
			try {
				// numberofmoves++;
				xxx += space + i.peeknext().getShortSmithMove();
				if(tempgame.colorToMove() == tacticfen.toMove())
					winning = winning && ((winningmove)i.peeknext()).winning;
				tempgame.makeMove(i.next());
				space = " ";
				if (!ischeckmate
						&& tempgame.colorToMove() != tacticfen.toMove()
						&& tempgame.nopawnmaterialcount(tacticfen.toMove().other()) == loserfinalMaterialCount
						&& tempgame.nopawnmaterialcount(tacticfen.toMove()) == winnerfinalMaterialCount)
					break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Redo these values after shaving moves off of the end of the tactic
		loserfinalMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove().other());
		winnerfinalMaterialCount = tempgame.nopawnmaterialcount(tacticfen.toMove());
		log.debug("(2) checkmate=" + tempgame.isCheckmate() + ", stalemate= "
				+ tempgame.isStalemate() + ", im=" + loserinitialMaterialCount
				+ ",fm=" + loserfinalMaterialCount + ",iw="
				+ winnerinitialMaterialCount + ",fw="
				+ winnerfinalMaterialCount + ", winning=" + winning);
		boolean goodTactic = false;
		if (tempgame.isCheckmate()) {
			log.debug("Tactic seems good because it's a checkmate");
			goodTactic = true;
		} else if (loserfinalMaterialCount == winnerfinalMaterialCount) {
				log.debug("Tactic is not good because this seems to be nothing more than a retake of material");
		} else if (winnerinitialMaterialCount - loserinitialMaterialCount < 3
				&& winnerfinalMaterialCount - loserfinalMaterialCount >= 3) {
			log.debug("Tactic seems good because we have gone from an even/losing game to a winning game");
			goodTactic = true;
		}
		// (im-fm)-(iw-fw) = im-fm-iw+fw (22-22)-(22-19) 22fm - 22im - 19fw + 22iw
		else if (loserinitialMaterialCount - loserfinalMaterialCount
				- winnerinitialMaterialCount + winnerfinalMaterialCount >= 3) {
			log.debug("Tactic seems good because we have gone from an even/losing game to a winning game (2)");
			goodTactic = true;
		} else if (loserinitialMaterialCount == loserfinalMaterialCount
				&& winnerfinalMaterialCount - winnerinitialMaterialCount >= 9) {
			log.debug("Tactic seems good because we earned at least a queen");
			goodTactic = true;
		}

		if (!goodTactic) {
			log.debug("Abandoning complete tactic");
			badtactic(tacticfen, xxx);
			tacticfen = null;
			tactic = null;
			return;
		}
		
		goodtactic(tacticfen, xxx, winning);
		skiptacticalmoves = tactic.iterator();
		tacticfen = null;
		tactic = null;
	}

	private void doCalculate(String debugText, boolean intactic, boolean startingtactic)
			throws Exception {
		try {
			int moveCount;
			if (intactic) {
				NormalChessBoard ncb;
				ncb = new NormalChessBoard(tacticfen);
				for (moveIterator mi = tactic.iterator(); mi.hasNext();)
					ncb.makeMove(mi.next());
				moveCount = otb.GetTablebaseMoves(ncb.getFEN());
			} else {
				moveCount = otb.GetTablebaseMoves(gameboard.getFEN());
			}
			int m1 = 0;
			int m2 = 1;
			if (moveCount != 0) {
				if (moveCount < 2 || otb.getScore(0) != otb.getScore(1)) {
					m1 = 0;
					m2 = 1;
				} else if (otb.getMove(0).getQueeningPiece() == ChessPieceEnum.QUEEN
						&& otb.getMove(1).getQueeningPiece() == ChessPieceEnum.ROOK) {
					m1 = 0;
					m2 = 2;
				} else if (otb.getMove(1).getQueeningPiece() == ChessPieceEnum.QUEEN
						&& otb.getMove(0).getQueeningPiece() == ChessPieceEnum.ROOK) {
					m1 = 1;
					m2 = 2;
				} else {
					m1 = 0;
					m2 = 1;
				}
				moveoption[0] = otb.getMove(m1).getShortSmithMove();
				if (otb.getScore(m1) == 0)
					score[0] = 0;
				else {
					score[0] = 999999 - Math.abs(otb.getScore(m1));
					if (otb.getScore(m1) < 0)
						score[0] *= -1;
				}
				;
				if (otb.getCount() > 1) {
					moveoption[1] = otb.getMove(m2).getShortSmithMove();
					if (otb.getScore(m2) == 0)
						score[1] = 0;
					else {
						score[1] = 999999 - Math.abs(otb.getScore(1));
						if (otb.getScore(m2) < 0)
							score[1] *= -1;
					}
					;
				}
				this.haveOnlineTablebase(otb);
				this.bestmove(otb.getMove(m1).getShortSmithMove(), null);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (startingtactic) {
			if (startingfen != null) {
				_engine.newPosition(startingfen, gamemoves);
			} else {
				_engine.newPosition(gamemoves);
			}
			_engine.calculate("GameChecker 1:" + debugText, intacticParms);
		} else if (intactic) {
			_engine.newPosition(tacticfen, tactic);
			_engine.calculate("GameChecker 2:" + debugText, intacticParms);
		} else {
			if (startingfen != null) {
				_engine.newPosition(startingfen, gamemoves);
			} else {
				_engine.newPosition(gamemoves);
			}
			_engine.calculate("GameChecker 3:" + debugText, _searchParms);
		}
	}

	public void shutdown() {
		_engine.stopengine();
	}
	
	private class winningmove extends Move {
		public boolean winning;
		public winningmove(String smithmove, boolean shortsmith, boolean winning) throws Exception {
			super(smithmove, shortsmith);
			this.winning = winning;
		}
	}
}