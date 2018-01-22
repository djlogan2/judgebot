package chessclub.com.icc.l1;

import chessclub.com.enums.CN;
import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.ICCInstance;

/**
 * The generic Level1Packet.
 * @author David Logan
 *
 */
public class Level1Packet {
    private String packet = null;
    private String[] parms;
    private CN type;
    private String userid;
    private String prefix = null;

    protected void initialize(final String pPacket) {
        this.packet = pPacket;
        parms = pPacket.split("(\r\n)+");
        String[] x = parms[0].split("\\s+");
        type = CN.getCNValue(x[0]);
        userid = x[1];
        if (x.length == 3) {
            prefix = x[2];
        }
    }

    /**
     * Default constructor.
     */
    protected Level1Packet() {
    }

    /**
     * Default toString().
     * @return The default string.
     */
    public String toString() {
        return packet;
    }

    /**
     * The number of individual parameters in this level 1 packet.
     * @return  The number of individual parameters in this level 1 packet.
     */
    public int numberParms() {
        return parms.length;
    }
    
    /**
     * Returns a particular parameter.
     * @param index The index number (zero based) of the parameter
     * @return  The string value at that parameter index number. null if there is no parameter there.
     */
    public String getParm(final int index) {
        if (index < parms.length) {
            return parms[index];
        } else {
            return null;
        }
    }

    /**
     * Returns the raw packet data.
     * @return  the raw packet data.
     */
    public String getPacket() {
        return packet;
    }

    /**
     * Gets the type of the Level 1 packet.
     * @return  The {@link CN} packet type.
     */
    public CN getType() {
        return type;
    }

    /**
     * The userid associated with this level 1 packet. Usually it is "*" I think. If another user spawns the command,
     * such as a spoof, their username may be there, but rarely will you have a need to use this method.
     * @return  The userid associated with this packet.
     */
    public String getUser() {
        return userid;
    }

    /**
     * The prefix associated with this level 1 packet.
     * <p>There are a few ways to explain this. Within ICC, you issue the command:  `<b>abcd1234</b>`command if you wish to have level 1 packets come back with the
     * prefix of "<b>abcd1234</b>". This way you can tell what level 1 output is related to what input command.
     * <p>Within the ICC framework, it will put the callers hash code into the prefix area. When level 1 packets (and level 2 packets within the nest)
     * come out, a handler lookup is performed. If a handler with that hash code is found, that handler gets the level 1 and level 2 packet data.
     * <p>In order to ensure the packets get back to the correct handler, make SURE the instance that will be handling the result is the same instance sending the command.
     * <p>In general, make sure your class structure looks like:   <b>class MyHandler implements {@link DefaultICCCommands} extends {@link AbstractICCHandler}</b>
     * <p>and calls {@link ICCInstance#addHandler(AbstractICCHandler)} before issuing the command.
     * @return  The prefix value.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets an instance of this class based on the first parameter, which is the level 1 packet type.
     * @param packet    The string packet data.
     * @return  A <b><i>DERIVED</i></b> instance of a Level1Packet. Using the first parameter,
     *              it will perform a table lookup to see if there is a derived class dedicated to this packet type.
     *              If so, it creates an instance of that class and returns that. If not, it throws an error.  
     * @throws Exception 

     */
    public static Level1Packet getPacketInstance(final String packet) throws Exception {
        Level1Packet l1 = null;
        l1 = CN.getInstance(packet);
        if(l1 != null)
            l1.initialize(packet);
        return l1;
    }

    /**
     * Gets an instance of this class based on the first parameter, which is the level 1 packet type.
     * This differs from {@link #getPacketInstance(String)} in the fact that if we cannot find a derived instance to create, it will instead create a
     * generic Level1Packet instance and return that.
     * @param packet    The raw packet data.
     * @return          An instance of a derived packet if possible, a Level1Packet instance otherwise.
     */
    public static Level1Packet guaranteePacketInstance(final String packet) {
        Level1Packet l1 = null;
        try {
            l1 = CN.getInstance(packet);
        } catch (Exception e) {
            e.printStackTrace();
            l1 = new Level1Packet();
        }
        if(l1 == null)
            l1 = new Level1Packet();
        l1.initialize(packet);
        return l1;
    }
}
