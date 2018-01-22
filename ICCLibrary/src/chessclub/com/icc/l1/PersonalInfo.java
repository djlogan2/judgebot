package chessclub.com.icc.l1;

import java.net.InetAddress;

import org.joda.time.DateTime;

public class PersonalInfo {
    private String user;
    @SuppressWarnings("unused")
    private InetAddress onFrom;
    @SuppressWarnings("unused")
    private String realName;
    @SuppressWarnings("unused")
    private String email;
    @SuppressWarnings("unused")
    private String postalAddress;
    @SuppressWarnings("unused")
    private String telephone;
    @SuppressWarnings("unused")
    private String countryCode;
    @SuppressWarnings("unused")
    private String country;
    @SuppressWarnings("unused")
    private boolean mailingListOK;
    @SuppressWarnings("unused")
    private String regRatingInfo;
    @SuppressWarnings("unused")
    private boolean registeredAsComputer;
    @SuppressWarnings("unused")
    private String previousUsername;
    @SuppressWarnings("unused")
    private String foundString;
    @SuppressWarnings("unused")
    private DateTime expirationDate;
    @SuppressWarnings("unused")
    private String[] cardInfo;
    @SuppressWarnings("unused")
    private DateTime firstUsed;
    @SuppressWarnings("unused")
    private float accumulatedHours;
    @SuppressWarnings("unused")
    private float percentOfLife;
    private boolean parsed = false;
    public String getUserName() {
        if(!parsed) parse();
        return user;
    }
    private void parse() {
        
    }
}
