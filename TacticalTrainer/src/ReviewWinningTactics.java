import java.util.Collection;
import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import chessclub.com.icc.tt.GameChecker;
import chessclub.com.icc.tt.ICCBean;
import chessclub.com.icc.tt.entity.TacticsTable;
import chessclub.com.icc.tt.ifac.IDatabaseService;
import chessclub.com.icc.uci.OnlineTablebase;
import chessclub.com.icc.uci.SearchParameters;
import chessclub.com.icc.uci.UCIEngine;
import david.logan.chess.support.ChessPieceEnum;
import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;
import david.logan.chess.support.MoveList.moveIterator;


public class ReviewWinningTactics extends GameChecker {
    public ReviewWinningTactics() {
		super(0, "", "", 0, 0, 0);
	}

	private static ApplicationContext applicationContext;
    private static IDatabaseService serv;
    private static ICCBean bean;
    
    private Iterator<TacticsTable> iii = null;
    private TacticsTable currentTactic = null;
    private moveIterator moveIterator = null;
    private UCIEngine _engine;
	private SearchParameters intacticParms = new SearchParameters();
	private MoveList tacticMoves;
    private MoveList movelist;

	
    public static void main(String[] args) {
        applicationContext = new ClassPathXmlApplicationContext("batch-test-context.xml");
        bean = (ICCBean) applicationContext.getBean("icc");
        serv = (IDatabaseService) applicationContext.getBean("databaseService");
        ReviewWinningTactics trt = new ReviewWinningTactics();
        trt.run();
    }
    
    public void run() {
    	Collection<TacticsTable> tactics = serv.getAllTactics();
    	iii = tactics.iterator();
    	while(iii.hasNext()) {
    		TacticsTable tt = iii.next();
    		try {
				@SuppressWarnings("unused")
				Fen fen = tt.getFen();
			} catch (Exception e) {
				System.out.println("Invalid fen " + tt.getId());
				try {
					FixFen newfen = new FixFen(tt.getFenString());
					serv.updateFen(tt.getId(), newfen);
				} catch (Exception e1) {
					System.out.println("Unable to fix fen for " + tt.getId());
				}
			}
    	}
    	System.exit(0);
        tactics = serv.getNullWinningTactics();
        iii = tactics.iterator();
		intacticParms.setMovetime(45000);
		currentTactic = iii.next();
		while(currentTactic != null) {
			_engine = new UCIEngine(4096, bean.getUciProgram(), this, true);
			_engine.start();
			try {
				_engine.join();
				currentTactic = iii.next();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		};
    }

    private void startTactic() {
    	movelist = currentTactic.getMoves();
    	moveIterator = movelist.iterator();
		tacticMoves = new MoveList();
		try {
			if(currentTactic.getFen().toMove() != currentTactic.getTomove()) {
				tacticMoves.add(moveIterator.next());
				_engine.newPosition(currentTactic.getFen(), tacticMoves);
				_engine.calculate("ReviewWinningTactics 1", intacticParms);
			} else {
				_engine.newPosition(currentTactic.getFen());
				_engine.calculate("ReviewWinningTactics 2", intacticParms);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	@Override
	public void engineStarted(UCIEngine engine) {
		try {
			engine.setOption("MultiPV", "3");
			engine.setOption("Threads", Integer.toString(bean.getUciThreads()));
			startTactic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void bestmove(String smithmove, String ponder_smithmove) {
		
		boolean winning = getwinning();
		
		if(winning) {
			Move m = moveIterator.next();
			if(m != null) {
				tacticMoves.add(m);
				m = moveIterator.next();
				if(m != null) {
					tacticMoves.add(m);
					try {
						_engine.newPosition(currentTactic.getFen(), tacticMoves);
						_engine.calculate("ReviewWinningTactics 3", intacticParms);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		
		currentTactic.setWinning(winning);
		serv.winning(currentTactic);
		if(iii.hasNext()) {
			currentTactic = iii.next();
			startTactic();
		} else {
			_engine.quit();
			System.exit(0);
		}
		return;
	}

	@Override
	public void badtactic(Fen fen, String moves) {}
	
	@Override
	public void goodtactic(Fen fen, String moves, boolean winning) {
	}
	private class FixFen extends Fen {
	    public FixFen(String fen) throws Exception {
			super(fen);
		}

		protected void checkCastling() {
			try {
				super.checkCastling();
				return;
			} catch (Exception e) {
			}
	        if(_board[0][4].piece() != ChessPieceEnum.KING || _board[0][4].color() != Color.WHITE) {
	        	whiteCanCastle[KINGSIDE] = whiteCanCastle[QUEENSIDE] = false;
	        } else {
	        	if(_board[0][0].piece() != ChessPieceEnum.ROOK || _board[0][0].color() != Color.WHITE)
	            	whiteCanCastle[QUEENSIDE] = false;
	        	if(_board[0][7].piece() != ChessPieceEnum.ROOK || _board[0][7].color() != Color.WHITE)
	            	whiteCanCastle[KINGSIDE] = false;
	        }
	        
	        if(_board[7][4].piece() != ChessPieceEnum.KING || _board[7][4].color() != Color.BLACK) {
	        	blackCanCastle[KINGSIDE] = blackCanCastle[QUEENSIDE] = false;
	        } else {
	        	if(_board[7][0].piece() != ChessPieceEnum.ROOK || _board[7][0].color() != Color.BLACK)
	            	blackCanCastle[QUEENSIDE] = false;
	        	if(_board[7][7].piece() != ChessPieceEnum.ROOK || _board[7][7].color() != Color.BLACK)
	            	blackCanCastle[KINGSIDE] = false;
	        }
	        String[] fensplit = fen.split("\\s+");
	        fensplit[2] = "";
	        if (whiteCanCastle[KINGSIDE]) {
	            fensplit[2] += 'K';
	        }
	        if (whiteCanCastle[QUEENSIDE]) {
	            fensplit[2] += 'Q';
	        }
	        if (blackCanCastle[KINGSIDE]) {
	            fensplit[2] += 'k';
	        }
	        if (blackCanCastle[QUEENSIDE]) {
	            fensplit[2] += 'q';
	        }
	        if (fensplit[2].length() == 0) {
	            fensplit[2] = "-";
	        }
	        fen = fensplit[0] + " " + fensplit[1] + " " + fensplit[2] + " " + fensplit[3] + " " + fensplit[4] + " " + fensplit[5];
	        shortfen = fensplit[0] + " " + fensplit[1] + " " + fensplit[2] + " " + fensplit[3];
	    }
	}
	@Override
	public void getnextmove() {
	}

	@Override
	public void endofgame() {
	}

	@Override
	public void haveOnlineTablebase(OnlineTablebase otb) {
	}
}
