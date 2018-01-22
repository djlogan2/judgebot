package chessclub.com.icc.jb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class LoseExempt implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    public void setUniquehandle(int uniquehandle) {
		this.uniquehandle = uniquehandle;
	}
	public void setIcchandle(String icchandle) {
		this.icchandle = icchandle;
	}
	public void setAddedby(String addedby) {
		this.addedby = addedby;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getUniquehandle() {
		return uniquehandle;
	}
	public String getIcchandle() {
		return icchandle;
	}
	public String getAddedby() {
		return addedby;
	}
	public Date getCreated() {
		return created;
	}
	@Id
	private int uniquehandle;
	private String icchandle;
	private String addedby;
	private Date created;
}
