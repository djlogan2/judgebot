package chessclub.com.icc.jb.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="gamelog_variable")
public class GameLogVariable implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public void setVariable(String variable) {
		this.variable = variable;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public String getVariable() {
		return variable;
	}
	public String getValue() {
		return value;
	}
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gamelog_variable_sequence")
    @SequenceGenerator(name = "gamelog_variable_sequence", sequenceName = "gamelog_variable_sequence")
	private int id;
	private String variable;
	private String value;
}
