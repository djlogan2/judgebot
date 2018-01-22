package chessclub.com.icc.l1;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * The instance created when a userid is first registered.
 * @author David Logan
 *
 */
public class Register extends Level1Packet {
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(Register.class);
    private boolean parsed = false;
    private boolean iphone = false;
    private String userid;
    
    public String getUserID() {
        if(!parsed) parse();
        return userid;
    }
    public boolean isIphoneClient() {
        if(!parsed) parse();
        return iphone;
    }
    
    // >Note to channel 0: guest3249 just created "Agentsunshine10"
    // >This registration was for an iphone client.
    private void parse() {
        userid = getParm(1).split("\"")[1];
        iphone = this.numberParms() == 2 && ( getParm(2).indexOf("iphone") != -1 );
    }
}
