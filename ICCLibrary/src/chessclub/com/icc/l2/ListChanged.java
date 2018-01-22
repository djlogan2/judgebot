package chessclub.com.icc.l2;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chessclub.com.enums.CN;
import chessclub.com.enums.ICCList;

public class ListChanged extends Level2Packet {
    public enum _error {
        NONE,
        EMPTY,
        MUSTBEREGISTERED
    };
    private _error error = _error.NONE;
    @SuppressWarnings("unused")
	private boolean parsed = false;
    private ICCList list = null;
    private CN command = null;
    @SuppressWarnings("unused")
	private String listEntry; // Person or alias or IP or whatever
    
    public ICCList getList() {
        return list;
    }
    public CN getCommand() {
        return command;
    }
    public String getEntry() {
        return null;
    }
    public _error getError() {
        return error;
    }
    
    //
    // For figuring out how to get ALL of the data back to the caller
    // regarding changes to lists
    //
    // This isn't actually a level 2 command. We use the DG_COMMAND packet and fake it as if it
    // were another packet. What we really need is info from this command so that we know what
    // fucking LIST we are working with, PLUS the data from the level 1 packet to figure out
    // what HAPPENED. I hate ICC server packets.
    //
    
    @Override
    protected void initialize(final String ppacket, final String pl1key, final ArrayList<String> level1Packets, final int ours) {
        super.initialize(ppacket,  pl1key, level1Packets, ours);
        Pattern p1b = Pattern.compile("(['-'|'+'])\\s*(\\S+)\\s+(.*)");
        Matcher m = p1b.matcher("");
        if(!m.matches())
        if("-".equals(getParm(1)) || getParm(1).toLowerCase().startsWith("mi")) {
            command = CN.MINUS;
        } else if("+".equals(getParm(1)) || getParm(1).toLowerCase().startsWith("pl")) {
            command = CN.PLUS;
        }
        String listname = getParm(1).split("\\s+")[0];
        for(ICCList list : ICCList.values()) {
            if(listname.toString().equalsIgnoreCase(listname)) {
                this.list = list;
            }
        }
    }
}
