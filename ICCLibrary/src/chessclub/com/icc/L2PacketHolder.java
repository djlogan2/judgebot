package chessclub.com.icc;

/**
 * This is an internal class used by the framework to collect packets as they come in.
 * @author David Logan
 *
 */
public class L2PacketHolder {
    /**
     * This is the index number of the current level 1 packet this level 2 packet is within.
     * ICC packets come in with any number of level 1 packets nested. While level 2 packets will never be nested, they can be within any particular level 1 packet on any nested level.
     * This is important because level 2 packets can be associated with a command (think spoof, for example) and if we don't know what level 1 packet it's associated with, we may have no way of knowing what command caused the output.
     * All level 2 packets are built with the associated level 1 packet chain so that callers can determine why a command was issued.
     * @return  The internal index number (of an array of) level 1 packets over the course of parsing the input stream.
     */
    public int getL1index() {
        return l1index;
    }

    /**
     * The packet type number of the level 1 packet if we have one. 
     * @return  The level 1 packet number or null
     */
    public String getL1key() {
        return l1key;
    }

    /**
     * The packet data.
     * @return The packet data.
     */
    public String getPacket() {
        return packet;
    }

    private int l1index = -1;
    private String l1key = null;
    private String packet = null;

    /**
     * Adds to the internal array of level 2 packets. 
     * @param pl1index  The index number of the level 1 packet this level 2 packet was found in.
     * @param pl1key    The level 1 packets key value if we know it. Otherwise null
     * @param ppacket   The raw level 2 packet data.
     */
    public L2PacketHolder(final int pl1index, final String pl1key, final String ppacket) {
        this.l1key = pl1key;
        this.l1index = pl1index;
        this.packet = ppacket;
    }
    
    @Override
    public String toString() {
    	return "" + l1index + "," + l1key + "," + packet;
    }
}
