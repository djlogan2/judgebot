package chessclub.com.icc.l2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.enums.CN;
import chessclub.com.enums.L2;
import chessclub.com.icc.l1.Level1Packet;

/**
 * The generic Level 2 Packet.
 * @author David Logan
 *
 */
public class Level2Packet {

    private static final Logger LOG = LogManager.getLogger(Level2Packet.class);

    private String packet;
    private ArrayList<String> parms = new ArrayList<String>();
    private L2 l2;
    private String l1key;
    private String[] l1PacketData = null;
    private HashMap<Integer, Level1Packet> l1Map = null;

    private Level1Packet getL1(final int i) {
        if (l1Map != null && l1Map.containsKey(i)) {
            return l1Map.get(i);
        }

        if (l1Map == null) {
            l1Map = new HashMap<Integer, Level1Packet>();
        }

        try {
            Level1Packet l1 = Level1Packet.guaranteePacketInstance(l1PacketData[i]);
            l1Map.put(i, l1);
            return l1;
        } catch (IndexOutOfBoundsException e) {
            LOG.error("(Level2Packet) Index out of bounds, index=" + i + ",packet=" + packet);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns one of the parameters.
     * @param index The zero based index level of the parameters.
     * @return  The string value of the parameter if it exists. <b>null</b> if it doesn't.
     */
    public String getParm(final int index) {
        if (index < parms.size()) {
            return parms.get(index);
        } else {
            return null;
        }
    }
    
    /**
     * The number of parameters in the packet.
     * @return  The number of parameters in the packet.
     */
    public int numberParms() {
        return parms.size();
    }
    
    /**
     * Standard toString() function.
     * @return  Formatted packet data: "<b>[L1/L1/L1/L2KEYVALUE] raw data</b>"
     * <ul>
     * <li><b>L1/L1/L1</b> - Each of the {@link CN} key enumeration values for each nested level 1 value</li>
     * <li><b>L2KEYVALUE</b> - The {@link L2} enumeration</li>
     * <li><b>raw data</b> -The raw packet data</li></ul>
     */
    public String toString() {
        String str = "";
        String comma = "";
        for (int i = 0; i < l1PacketData.length; i++) {
            try {
                str += comma + level1Type(i).name();
            } catch(Exception e) {
                str += comma + e.toString();
            }
            comma = ",";
        }
        return "[" + str + "/" + (l1key == null ? "" : l1key) + "/" + l2.name() + "] " + packet;
    }

    /**
     * The raw packet data.
     * 
     * @return The raw packet data
     */
    public String getRawPacket() {
        return packet;
    }

    /**
     * The constructor.
     */
    public Level2Packet() {
    }

    /**
     * The level 2 packet type.
     * 
     * @return The level 2 packet type
     */
    public L2 type() {
        return l2;
    }

    /**
     * Returns the type of level 1 packet at that nesting level. 0 means it's the outermost packet.
     * 
     * @param i
     *            The nesting level. In most cases this should be zero
     * @return The level 1 packet type. Returns null if unable to return a packet at the requested level
     */
    public CN level1Type(final int i) {
        return Objects.requireNonNull(getL1(i)).getType();
    }

    /**
     * Returns the level 1 key (what is sent using the `key`command ICC command).
     * 
     * @return The level 1 key (what is sent using the `key`command ICC command)
     */
    public String level1Key() {
        return l1key;
    }

    protected void initialize(final String ppacket, final String pl1key, final ArrayList<String> level1Packets, final int ours) {
        this.l1PacketData = new String[ours];
        for (int x = 0; x < ours; x++) {
            l1PacketData[x] = level1Packets.get(x);
        }
        this.l1key = pl1key;
        this.packet = ppacket;

        boolean ctrl = false;
        StringBuilder currentparm = new StringBuilder();
        int state = 0; // 0 = in between, 1 = in simple parm, 2=in {} parms, 3=in ^Y{^Y} parms

        for (byte by : packet.getBytes()) {
            switch (by) {
            case 25: // ^Y
                ctrl = true;
                break;
            case '{':
                if (ctrl && state == 0) {
                    state = 3;
                } else if (state == 0) {
                    state = 2;
                } else if (ctrl) {
                    currentparm.append((char) 25);
                    currentparm.append('{');
                } else {
                    currentparm.append('{');
                }
                ctrl = false;
                break;
            case '}':
                if ((state == 3 && ctrl) || state == 2) {
                    parms.add(currentparm.toString());
                    currentparm = new StringBuilder();
                    state = 0;
                } else if (ctrl) {
                    currentparm.append((char) 25);
                    currentparm.append('}');
                } else {
                    currentparm.append('}');
                }
                ctrl = false;
                break;
            case ' ':
                if (ctrl) {
                    ctrl = false;
                    currentparm.append((char) 25);
                }
                if (state > 1) {
                    currentparm.append(' ');
                } else if (state == 1) {
                    parms.add(currentparm.toString());
                    currentparm = new StringBuilder();
                    state = 0;
                }
                break;
            default:
                if (ctrl) {
                    ctrl = false;
                    currentparm.append((char) 25);
                }
                currentparm.append((char) by);
                if (state == 0) {
                    state = 1;
                }
                break;
            }
        }
        if (currentparm.length() != 0) {
            parms.add(currentparm.toString());
        }
        l2 = L2.getL2(parms.get(0));
    }
    
    public int getLevel1Count() {
        return l1PacketData.length;
    }
    public String[] getLevel1RawPacketData() {
        return l1PacketData;
    }

    /**
     * Gets an instance of a level 2 packet.
     * @param packet    -   The raw packet data.
     * @param l1key     -   The key value from the level 1 packet. In this framework this will be the hash code of the handler that is to get these packets.
     * @param level1Packets -   The complete list of level 1 packets the lower level code has collected so far. Some of these will be at our nesting level and above.
     * @param count -   The number of level 1 packets that are at our nesting level or above.   
     * @return  -   An instance of a <b><i>DERIVED</i></b> class based on the level 2 packet type.
     * @see     L2
     * @throws InstantiationException if this Class represents an abstract class, an interface, an array class, a primitive type, or void; or if the class has no nullary constructor; or if the instantiation fails for some other reason. 
     * @throws IllegalAccessException if the class or its nullary constructor is not accessible.
     * @throws ClassNotFoundException if the class cannot be located
     */
    public static Level2Packet getPacketInstance(final String packet, final String l1key, final ArrayList<String> level1Packets, final int count) throws InstantiationException, IllegalAccessException {
        try {
        Level2Packet l2p = L2.getL2(packet.split("\\s+")[0]).getPacketInstance();
        l2p.initialize(packet, l1key, level1Packets, count);
        return l2p;
        } catch(NullPointerException e) {
            System.out.println("Fuck me!");
            return null;
        }
    }
}
