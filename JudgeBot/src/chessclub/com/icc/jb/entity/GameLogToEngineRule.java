package chessclub.com.icc.jb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="gamelog_to_engrule")
public class GameLogToEngineRule implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void setRuleorder(int ruleorder) {
		this.ruleorder = ruleorder;
	}

	public void setGamelog(GameLog gamelog) {
		this.gamelog = gamelog;
	}

	public void setEnginerule(EngineRule enginerule) {
		this.enginerule = enginerule;
	}

	@SuppressWarnings("unused")
    private int ruleorder;

	@Id
	@ManyToOne
	@JoinColumn(name = "fk_gamelog_id")
	private GameLog gamelog;

	@Id
	@ManyToOne
	@JoinColumn(name = "fk_enginerule_id")
	private EngineRule enginerule;
}
