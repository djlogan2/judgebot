package chessclub.com.icc.jb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="gamelog_to_adjrule")
public class GameLogToAdjudicateRule implements Serializable {
	public void setRuleorder(int ruleorder) {
        this.ruleorder = ruleorder;
    }

    public void setGamelog(GameLog gamelog) {
        this.gamelog = gamelog;
    }

    public void setAdjudicaterule(AdjudicateRule adjudicaterule) {
        this.adjudicaterule = adjudicaterule;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private int ruleorder;

	@Id
	@ManyToOne
	@JoinColumn(name = "fk_gamelog_id")
	private GameLog gamelog;

	@Id
	@ManyToOne
	@JoinColumn(name = "fk_adjudicaterule_id")
	private AdjudicateRule adjudicaterule;
}
