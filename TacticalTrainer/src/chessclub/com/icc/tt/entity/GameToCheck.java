package chessclub.com.icc.tt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import david.logan.chess.support.Fen;

@SuppressWarnings("serial")
@Entity
@Table(name="gamestocheck")
public class GameToCheck implements Serializable {
    @Id
    private Date dateentered;
    private Date datecompleted;
    //@Convert(converter = GameToCheck.FENConverter.class)
    private String fen;
    private Integer gameid;
    private String whoentered;
    
    public String whoentered() { return whoentered; }
    public int gameid() { return (gameid == null ? -1 : gameid); }
    public Date dateEntered() { return dateentered; }
    public Date dateCompleted() { return datecompleted; }
    public Fen fen() throws Exception { return new Fen(fen); }
    
    public GameToCheck(String whoEntered, int gameid) {
        this.whoentered = whoEntered;
        this.gameid = gameid;
    }
    public GameToCheck(String whoEntered, Fen fen) {
        this.whoentered = whoEntered;
        this.fen = fen.getFEN();
    }
}
