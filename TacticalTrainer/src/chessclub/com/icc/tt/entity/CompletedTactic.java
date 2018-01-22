package chessclub.com.icc.tt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="completedtactic")
public class CompletedTactic implements Serializable {
    @Id
    private int uid;
    @Id
    private int tactic;
    private Date datecompleted;
    @SuppressWarnings("unused")
    private int userratingbefore;
    private int userratingafter;
    @SuppressWarnings("unused")
    private int tacticratingbefore;
    private int tacticratingafter;
    @SuppressWarnings("unused")
    private int totalsecondstaken;
    private Integer secondsforfirstmove;
    private Integer firstwrongmove;
    
    public int getuid() { return uid; }
    public int gettactic() { return tactic; }
    public int getnewUserRating() { return userratingafter; }
    public int getnewTacticRating() { return tacticratingafter; }
    public Date getDateCompleted() { return datecompleted; }
    public boolean correct() {
    	if(firstwrongmove != null) return false;
    	if(secondsforfirstmove == null) return false;
    	return true;
    }
    
    public CompletedTactic(int uid, int tactic, int initialUserRating, int initialTacticRating) {
        this.uid = uid;
        this.tactic = tactic;
        this.tacticratingbefore = initialTacticRating;
        this.userratingbefore = initialUserRating;
    }
    public void finishTactic(int finalUserRating, int finalTacticRating, Integer firstMoveSeconds, int totalSeconds, Integer firstwrongmove) {
        userratingafter = finalUserRating;
        tacticratingafter = finalTacticRating;
        secondsforfirstmove = firstMoveSeconds;
        totalsecondstaken = totalSeconds;
        this.firstwrongmove = firstwrongmove;
        datecompleted = new Date();
    }
    protected CompletedTactic() { uid = -1 ; tactic = -1; }
}
