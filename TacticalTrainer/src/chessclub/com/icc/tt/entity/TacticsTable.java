package chessclub.com.icc.tt.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import david.logan.chess.support.Color;
import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;
import david.logan.chess.support.MoveList;
import david.logan.chess.support.MoveList.moveIterator;

@Entity
@Table(name="tacticstable")
public class TacticsTable implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tacticstable_sequence")
    @SequenceGenerator(name = "tacticstable_sequence", sequenceName = "tacticstable_sequence")
    private int id;
    private Character good = null;
    private String fen;
    private String moves;
    private int rating;
    private int played;
    @Enumerated(EnumType.STRING)
    private Color tomove;
    private String badreason;
    private Boolean winning;
    @ManyToOne
    @JoinColumn(name="sourcepgn_id")
    private PGNTable pgn;
    
    public int getNumberOfGames() { return played; }
    public int getId() {
        return this.id;
    }
    
    public int getPlayed() {
    	return played;
    }
    
    public Fen getFen() throws Exception {
        return new Fen(fen);
    }
    public String getFenString() {
    	return fen;
    }
    
    public void setFen(Fen fen) {
        this.fen = fen.getFEN();
        this.tomove = fen.toMove();
    }
    
    public MoveList getMoves() {
        MoveList umoves = new MoveList();
        String[] movelist = moves.split("\\s+");
        for(String m : movelist) {
            try {
                umoves.add(new Move(m, true));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return umoves;
    }
    
    public void setMoves(MoveList moves) {
        this.moves = "";
        String space = "";
        moveIterator i = moves.iterator();
        while(i.hasNext()) {
            try {
                this.moves += space + i.next().getShortSmithMove();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            } 
            space = " ";
        }
    }

    public int getRating() {
        return rating;
    }
    
    public void setTomove(Color tomove) {
    	this.tomove = tomove;
    }
    
    public Color getTomove() {
        return this.tomove;
    }
    
    public void setWinning(boolean winning) {
    	this.winning = winning;
    }
    
    public boolean getWinning() {
        return (this.winning == null ? false : this.winning);
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }
    public void newGame(int newRating) {
        this.rating = newRating;
        this.played++;
    }
    public void setgood(boolean b) {
        good = (b ? 'y' : 'n');
    }
    public boolean getgood() {
    	return (good == null || good == 'y' ? true : false);
    }
    
    public String getBadreason() {
    	return badreason;
    }
    public void setBadreason(String reason) {
    	badreason = reason;
    }
}
