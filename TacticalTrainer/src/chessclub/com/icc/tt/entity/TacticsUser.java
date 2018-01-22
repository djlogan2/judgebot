package chessclub.com.icc.tt.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="tacticsuser")
public class TacticsUser {
    @Id
    private int iccuid;
    private int rating;
    private boolean onlywinning;
    private String username;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "completedtactic",
		joinColumns        = {@JoinColumn(name = "uid", nullable = false, updatable = false) }, 
		inverseJoinColumns = {@JoinColumn(name = "tactic", nullable = false, updatable = false) })
    private Set<TacticsTable> completedtactics;
    public int getGamecount() {
        return (completedtactics == null ? 0 : completedtactics.size());
    }
    
    public void setOnlywinning(boolean winning) {
        this.onlywinning = winning;
    }
    
    public boolean getOnlywinning() {
        return this.onlywinning;
    }
    public void setUid(int uid) {
        this.iccuid = uid;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public int getRating() {
        return this.rating;
    }

    public void newGame(int newRating) {
        this.rating = newRating;
    }

    public int getUid() {
        return this.iccuid;
    }
    
    public void setUsername(String username) {
        if(!username.equals(this.username))
            this.username = username;
    }
    public String getUsername() {
        return this.username;
    }
}
