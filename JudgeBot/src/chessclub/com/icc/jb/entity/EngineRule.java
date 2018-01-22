package chessclub.com.icc.jb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import chessclub.com.icc.uci.SearchParameters;

@Entity
@Table(name="enginerules")
public class EngineRule implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public void setSkip(boolean skip) {
        this.skip = (skip?1:0);
    }
    public void setWtime(Integer wtime) {
        this.wtime = wtime;
    }
    public void setBtime(Integer btime) {
        this.btime = btime;
    }
    public void setWinc(Integer winc) {
        this.winc = winc;
    }
    public void setBinc(Integer binc) {
        this.binc = binc;
    }
    public void setMovestogo(Integer movestogo) {
        this.movestogo = movestogo;
    }
    public void setDepth(Integer depth) {
        this.depth = depth;
    }
    public void setNodes(Integer nodes) {
        this.nodes = nodes;
    }
    public void setMate(Integer mate) {
        this.mate = mate;
    }
    public void setMovetime(Integer movetime) {
        this.movetime = movetime;
    }
    public void setRule(String rule) {
        this.rule = rule;
    }
    public void setRuleorder(int ruleorder) {
        this.ruleorder = ruleorder;
    }
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
    public int getId() {
		return id;
	}
	public boolean getSkip() {
		return (skip==1);
	}
	public Integer getWtime() {
		return wtime;
	}
	public Integer getBtime() {
		return btime;
	}
	public Integer getWinc() {
		return winc;
	}
	public Integer getBinc() {
		return binc;
	}
	public Integer getMovestogo() {
		return movestogo;
	}
	public Integer getDepth() {
		return depth;
	}
	public Integer getNodes() {
		return nodes;
	}
	public Integer getMate() {
		return mate;
	}
	public Integer getMovetime() {
		return movetime;
	}
	public String getRule() {
		return rule;
	}
	public int getRuleorder() {
		return ruleorder;
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
	public Integer getRulehit() {
		return rulehit;
	}
	public Integer getRulemissed() {
		return rulemissed;
	}
	public void setSearchParameters(SearchParameters p)
	{
		if(binc != null) p.setBinc(binc);
		if(btime != null) p.setBmsec(btime);
		if(depth != null) p.setDepth(depth);
		if(mate != null) p.setMate(mate);
		if(movestogo != null) p.setMovesToGo(movestogo);
		if(movetime != null) p.setMovetime(movetime);
		if(nodes != null) p.setNodes(nodes);
		if(winc != null) p.setWinc(winc);
		if(wtime != null) p.setWmsec(wtime);
	}
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enginerule_sequence")
    @SequenceGenerator(name = "enginerule_sequence", sequenceName = "enginerule_sequence")
	private int id;
	private Integer skip;
	private Integer wtime;
	private Integer btime;
	private Integer winc;
	private Integer binc;
	private Integer movestogo;
	private Integer depth;
	private Integer nodes;
	private Integer mate;
	private Integer movetime;
	private String rule;
	private int ruleorder;
	private String createdby;
	private Date createdate = new Date();
	private String deletedby;
	private Date deletedate;
	private int missed;
	private int hit;
	private int deleted;
	private Integer rulehit;
	private Integer rulemissed;
    public void hit() {
        rulehit++;
        hit++;
    }
    public void missed() {
        missed++;
        rulemissed++;
    }
}
