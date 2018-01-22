package chessclub.com.icc.jb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="gamelog_to_variable")
public class GameLogToVariable implements Serializable {

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    public void setGamelog(GameLog gamelog) {
		this.gamelog = gamelog;
	}

	public void setVariable(GameLogVariable variable) {
		this.variable = variable;
	}

	@Id
	@ManyToOne
	@JoinColumn(name="fk_gamelog_id")
	private GameLog gamelog;
	
	@Id
	@ManyToOne
	@JoinColumn(name="fk_gamelog_variable")
	private GameLogVariable variable;
}
