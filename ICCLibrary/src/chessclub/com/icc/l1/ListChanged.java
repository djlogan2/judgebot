package chessclub.com.icc.l1;

import chessclub.com.enums.ICCList;

public class ListChanged extends Level1Packet {
    public enum listError {
      NONE,
      EMPTY,
    };
    @SuppressWarnings("unused")
	private listError _error = listError.NONE;
    
    public boolean isAdded() { return false; }
    public boolean isRemoved() { return false; }
    public ICCList whichList() { return null; }
    // Usually a person, but like "alias" it could be a command or IP addr for "ipfilter", etc.
    public String parameter() { return null; }
    
    public listError errorEnum() { return null; }
    
    @SuppressWarnings("unused")
	private void parse() {
        if("The list is empty.".equals(getParm(1))) {
            _error = listError.EMPTY;
            return;
        }
    }
}
