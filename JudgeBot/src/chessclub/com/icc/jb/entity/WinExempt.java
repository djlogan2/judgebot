package chessclub.com.icc.jb.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WinExempt implements Serializable {
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
	public void setCreated(Date created) {
		this.created = created;
	}
	public int getUniquehandle() {
		return uniquehandle;
	}
	public String getIcchandle() {
		return icchandle;
	}
	public Date getCreated() {
		return created;
	}
	@Id
	private int uniquehandle;
	private String icchandle;
	private Date created;
}
