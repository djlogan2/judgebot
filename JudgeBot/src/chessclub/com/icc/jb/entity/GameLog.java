package chessclub.com.icc.jb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import chessclub.com.icc.jb.enums.JBAdjudicateAction;
import david.logan.chess.support.Board;
import david.logan.chess.support.Color;
import david.logan.chess.support.NormalChessBoard;

@Entity
public class GameLog implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int getId() {
		return id;
	}

    public JBAdjudicateAction getfinalAction()
    {
    	return JBAdjudicateAction.getAction(finalaction);
    }
    
    public void setfinalAction(JBAdjudicateAction action)
    {
    	finalaction = action.ordinal();
    }
    
	public String getWhite() {
		return white;
	}

	public String getBlack() {
		return black;
	}

	public String getFen() {
	    if(fen == null)
	        return null;
		String rfen = fen.replace("\n","");
		return rfen;
	}

	public Date getAdjdate() {
		return adjdate;
	}

	public boolean isTestonly() {
		return (testonly != null && testonly == 1);
	}

	public boolean isEnginenohit() {
		return (engine_nohit == null || engine_nohit.intValue() == 1);
	}

	public boolean isAdjudicationnohit() {
		return (adjudication_nohit == null || adjudication_nohit.intValue() == 1);
	}

	public Color getWhodisconnected() {
		return whodisconnected;
	}

	public int getWhitetime() {
		return (whitetime == null ? 0 : whitetime);
	}

	public int getBlacktime() {
		return (blacktime == null ? 0 : blacktime);
	}

	public int getScore() {
		return (score == null ? 0 : score);
	}

	public double getScoredouble() {
		return (score == null ? 0.00 : (double)score/100.0);
	}

	public boolean isIsmate() {
		return (ismate != null && ismate == 1);
	}

	public Collection<AdjudicateRule> getAdjudicaterules() {
		return adjudicaterules;
	}

	public Collection<EngineRule> getEnginerules() {
		return enginerules;
	}

	public Collection<GameLogVariable> getVariables() {
		return variables;
	}

	public JBAdjudicateAction getAction() {
		return action;
	}
	
	public String getLastMoves() {
	    return lasttwomoves;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamelog_sequence")
    @SequenceGenerator(name = "gamelog_sequence", sequenceName = "gamelog_sequence")
	private int id;
	private String white;
	private String black;
	private String fen;
	private Date adjdate = new Date();
	private Integer testonly;
	private Integer engine_nohit;
	private Integer adjudication_nohit;
	private Color whodisconnected;
	private Integer whitetime;
	private Integer blacktime;
	private Integer score;
	private Integer ismate;
	private String lasttwomoves;
	private Integer finalaction;
	@Transient
	private JBAdjudicateAction action;
	@Transient
	private boolean adjudication_skipped;
	
	@ManyToMany
	@JoinTable(
			name="gamelog_to_adjrule",
			joinColumns=@JoinColumn(name="fk_gamelog_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="fk_adjudicaterule_id", referencedColumnName="id")
	)
	@OrderBy("ruleorder asc")
	private Collection<AdjudicateRule> adjudicaterules; 

	@ManyToMany
	@JoinTable(
			name="gamelog_to_engrule",
			joinColumns=@JoinColumn(name="fk_gamelog_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="fk_enginerule_id", referencedColumnName="id")
	)
	@OrderBy("ruleorder asc")
	private Collection<EngineRule> enginerules; 

	@ManyToMany
	@JoinTable(
			name="gamelog_to_variable",
			joinColumns=@JoinColumn(name="fk_gamelog_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="fk_gamelog_variable", referencedColumnName="id")
	)
	@OrderBy("variable asc")
	private Collection<GameLogVariable> variables;
	
	@Transient
	private int whitedigits[] = new int[9];
	@Transient
	private int blackdigits[] = new int[9];
	@Transient
	private Board gameboard = new NormalChessBoard();
	
	public int[] getWhitedigits() { return whitedigits; } 
	public int[] getBlackdigits() { return blackdigits; }
	//public Board getBoard() { return gameboard; }
	
	public Color getColortomove() {
	    String tomove = (fen.split("\\s+"))[1];
	    if("w".equals(tomove))
	        return Color.WHITE;
	    else if("b".equals(tomove))
	        return Color.BLACK;
	    else
	        return Color.NONE;
	}
	
	@PostLoad
	private void postload() {
	    if(whitetime != null)
	        setdigits(whitedigits, whitetime);
	    if(blacktime != null)
	        setdigits(blackdigits, blacktime);
		//if(getFen() != null)
		//   gameboard.loadFEN(getFen());
		//
		// We load the final adjudication rule from our adjudication rule list
		// Because the caller may not want to get the complete list of adjudication rules
		// Obviously they are going to get them anyway... :)
		//
		action = JBAdjudicateAction.MANUAL;
		if(!isAdjudicationnohit()) {
			Iterator<AdjudicateRule> ari = adjudicaterules.iterator();
			while(ari.hasNext()) {
                action = ari.next().getAction();
			}
		}
		adjudication_skipped = false;
		if(!isEnginenohit()) {
		    Iterator<EngineRule> eri = enginerules.iterator();
		    while(eri.hasNext()) {
		        adjudication_skipped = eri.next().getSkip();
		    }
		}
	}
	
	private void setdigits(int[] array, int msec) {
		int hours = msec / (60*60*1000);
		int hint  = hours * 60*60*1000;
		int minutes = (msec - hint)/(60*1000);
		int mint = minutes * 60*1000;
		int seconds = (msec - hint - mint)/1000;
		array[0] = hours / 10;
		array[1] = hours % 10;
		array[2] = minutes / 10;
		array[3] = minutes % 10;
		array[4] = seconds / 10;
		array[5] = seconds % 10;
		array[6] = (msec % 1000)  /100;
		array[7] = (msec % 100) / 10;
		array[8] = (msec % 10);
	}

    public void setFen(String fen) {
        this.fen = fen;
    }

    public void setDisconnector(Color disconnector) {
        this.whodisconnected = disconnector;
    }

    public void setTestOnly(boolean testonly) {
        this.testonly = (testonly?1:0);
    }

    public void setWhiteName(String whitename) {
        this.white = whitename;
    }
    public void setBlackName(String blackname) {
        this.black = blackname;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMate(boolean ismate) {
        this.ismate = (ismate?1:0);
    }

    public void setWhiteTime(int whitemsec) {
        this.whitetime = whitemsec;
    }

    public void setBlackTime(int blackmsec) {
        this.blacktime = blackmsec;
    }
    
    public void setLastTwoMoves(String lasttwomoves) {
        this.lasttwomoves = lasttwomoves;
    }
    
    public boolean isAdjudicationSkipped() {
        return adjudication_skipped;
    }

	public void setAdjudicationNoHit(int i) {
		this.adjudication_nohit = i;
	}

	public void setEngineNoHit(int i) {
		this.engine_nohit = i;
	}
}
