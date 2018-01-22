package chessclub.com.icc.jb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import chessclub.com.icc.jb.enums.JBAdjudicateAction;

@Entity
@Table(name="adjudicaterules")
public class AdjudicateRule implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public void setRule(String rule) {
        this.rule = rule;
    }
    public void setRuleorder(int ruleorder) {
        this.ruleorder = ruleorder;
    }
    public void setAction(JBAdjudicateAction action) {
        this.action = action;
    }
    public int getId() {
		return id;
	}
	public String getRule() {
		return rule;
	}
	public int getRuleorder() {
		return ruleorder;
	}
	public JBAdjudicateAction getAction() {
		return action;
	}
	public String getCreatedby() {
		return createdby;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public String getDeletedby() {
		return deletedby;
	}
	public Date getDeletedate() {
		return deletedate;
	}
	public int getMissed() {
		return missed;
	}
	public int getHit() {
		return hit;
	}
	public int getDeleted() {
		return deleted;
	}
	public int getRulemissed() {
		return rulemissed;
	}
	public int getRulehit() {
		return rulehit;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "adjudicaterule_sequence")
    @SequenceGenerator(name = "adjudicaterule_sequence", sequenceName = "adjudicaterule_sequence") private int id;
	private String rule;
	private int ruleorder;
	private JBAdjudicateAction action;
	private String createdby;
	private Date createdate = new Date();
	private String deletedby;
	private Date deletedate;
	private int missed;
	private int hit;
	private int deleted;
	private int rulemissed;
	private int rulehit;
    public void setCreatedBy(String createdby) {
        this.createdby = createdby;
    }
    public void hit() {
        hit++;
        rulehit++;
    }
    public void missed() {
        missed++;
        rulemissed++;
    }
}
