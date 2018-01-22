package chessclub.com.icc.l1l2;

import java.lang.reflect.Field;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IMyVariable;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.l2.MyVariable;

/**
 * This class maintains all of the ICC user variables.
 * @author David Logan
 *
 */
public class UserVariables extends DefaultICCCommands implements IMyVariable {
    private static final Logger LOG = LogManager.getLogger(ICCInstance.class);
    
    @SuppressWarnings("unused")
    private UserVariables() { }
    
    private String setType = "set";
    
    public UserVariables(final ICCInstance pIcc) {
        super.setInstance(pIcc, this);
        pIcc.addHandler(this);
    }

    /**
     * True if seeks are rated. false if seeks are unrated.
     * @return  True if seeks are rated. false if seeks are unrated.
     */
    public boolean isRated() {
        return rated;
    }
    /**
     * True if seeks are rated. false if seeks are unrated.
     * @param  pRated   True if seeks are rated. false if seeks are unrated.
     */
    public void setRated(final boolean pRated) {
        send(setType + " rated " + (pRated ? "1" : "0"));
    }
    /**
     * The wild type when a seek is issued.
     * @return  The wild type when a seek is issued.
     */
    public int getWild() {
        return wild;
    }
    /**
     * The wild type when a seek is issued.
     * @param   pWild   The wild type when a seek is issued.
     */
    public void setWild(final int pWild) {
        send(setType + " wild " + pWild);
    }
    /**
     * The initial time for a game when a seek is issued.
     * @return  The initial time for a game when a seek is issued.
     */
    public int getTime() {
        return time;
    }
    /**
     * The initial time for a game when a seek is issued.
     * @param   pTime   The initial time for a game when a seek is issued.
     */
    public void setTime(final int pTime) {
        send(setType + " time " + pTime);
    }
    /**
     * The increment for a game when a seek is issued.
     * @return  The increment for a game when a seek is issued.
     */
    public int getIncrement() {
        return increment;
    }
    /**
     * The increment for a game when a seek is issued.
     * @param   pIncrement  The increment for a game when a seek is issued.
     */
    public void setIncrement(final int pIncrement) {
        send(setType + " inc " + pIncrement);
    }
    /**
     * Whether or not a game should be adjourned or automatically resigned if you disconnect.
     * @return  Whether or not a game should be adjourned or automatically resigned if you disconnect.
     */
    public boolean isNoescape() {
        return noescape;
    }
    /**
     * Whether or not a game should be adjourned or automatically resigned if you disconnect.
     * @param   pNoescape  Whether or not a game should be adjourned or automatically resigned if you disconnect.
     */
    public void setNoescape(final boolean pNoescape) {
        send(setType + " noescape " + (pNoescape ? "1" : "0"));
    }
    /**
     * Whether or not a user allows their opponents to request a takeback.
     * @return  true if takebacks are allowed.
     */
    public boolean isNotakeback() {
        return notakeback;
    }
    /**
     * Whether or not a user allows their opponents to request a takeback.
     * @param   pNotakeback true if takebacks are allowed.
     */
    public void setNotakeback(final boolean pNotakeback) {
        send(setType + " notakeback " + (pNotakeback ? "1" : "0"));
    }
    /**
     * The minimum rating allowed to accept a seek.
     * @return  The minimum rating allowed to accept a seek.
     */
    public int getMinseek() {
        return minseek;
    }
    /**
     * The minimum rating allowed to accept a seek.
     * @param   pMinseek    The minimum rating allowed to accept a seek.
     */
    public void setMinseek(final int pMinseek) {
        send(setType + " minseek " + pMinseek);
    }
    /**
     * The maximum rating allowed to accept a seek.
     * @return  The maximum rating allowed to accept a seek.
     */
    public int getMaxseek() {
        return maxseek;
    }
    /**
     * The maximum rating allowed to accept a seek.
     * @param   pMaxseek    The maximum rating allowed to accept a seek.
     */
    public void setMaxseek(final int pMaxseek) {
        send(setType + " maxseek " + pMaxseek);
    }
    /**
     * True if the user must manually accept a game request. False if a game automatically begins.
     * @return  True if the user must manually accept a game request. False if a game automatically begins.
     */
    public boolean isManualaccept() {
        return manualaccept;
    }
    /**
     * True if the user must manually accept a game request. False if a game automatically begins.
     * @param   pManualaccept   True if the user must manually accept a game request. False if a game automatically begins.
     */
    public void setManualaccept(final boolean pManualaccept) {
        send(setType + " manualaccept " + (manualaccept ? "1" : "0"));
    }
    /**
     * True if a seek must meet the users formula.
     * @return  True if a seek must meet the users formula.
     */
    public boolean isUseformula() {
        return useformula;
    }
    /**
     * True if a seek must meet the users formula.
     * @param   pUseformula True if a seek must meet the users formula.
     */
    public void setUseformula(final boolean pUseformula) {
        send(setType + " useformula " + (pUseformula ? "1" : "0"));
    }
    /**
     * True if the user is available for match requests.
     * @return  True if the user is available for match requests.
     */
    public boolean isOpen() {
        return open;
    }
    /**
     * True if the user is available for match requests.
     * @param   pOpen   True if the user is available for match requests.
     */
    public void setOpen(final boolean pOpen) {
        send(setType + " open " + (pOpen ? "1" : "0"));
    }
    /**
     * True if the user is available for a match request different than the rated variable.
     * @return  True if the user is available for a match request different than the rated variable
     */
    public boolean isRopen() {
        return ropen;
    }
    /**
     * True if the user is available for a match request different than the rated variable.
     * @param   rOpen   True if the user is available for a match request different than the rated variable
     */
    public void setRopen(final boolean rOpen) {
        send(setType + " open " + (rOpen ? "1" : "0"));
    }
    /**
     * True if the user is available for a match request different than the wild variable.
     * @return  True if the user is available for a match request different than the wild variable
     */
    public boolean isWopen() {
        return wopen;
    }
    /**
     * True if the user is available for a match request different than the wild variable.
     * @param   wOpen   True if the user is available for a match request different than the wild variable
     */
    public void setWopen(final boolean wOpen) {
        send(setType + " open " + (wOpen ? "1" : "0"));
    }
    /**
     * True if the user is available for a correspondence chess requests.
     * @return  True if the user is available for a correspondence chess requests.
     */
    public boolean isCcopen() {
        return ccopen;
    }
    /**
     * True if the user is available for a correspondence chess requests.
     * @param   cCopen  True if the user is available for a correspondence chess requests.
     */
    public void setCcopen(final boolean cCopen) {
        send(setType + " open " + (cCopen ? "1" : "0"));
    }
    public int getMood() {
        return mood;
    }
    public void setMood(final int mood) {
        send(setType + " mood " + mood);
    }
    public boolean isSeek() {
        return seek;
    }
    public void setSeek(final boolean seek) {
        send(setType + " seek " + (seek ? "1" : "0"));
    }
    public String getSfilter() {
        return sfilter;
    }
    public void setSfilter(final String sfilter) {
        send(setType + " sfilter " + sfilter);
    }
    public boolean isShout() {
        return shout;
    }
    public void setShout(final boolean shout) {
        send(setType + " shout " + (shout ? "1" : "0"));
    }
    public boolean isSshout() {
        return sshout;
    }
    public void setSshout(final boolean sshout) {
        send(setType + " sshout " + (sshout ? "1" : "0"));
    }
    public int getKib() {
        return kib;
    }
    public void setKib(final int kib) {
        send(setType + " kib " + kib);
    }
    public boolean isTell() {
        return tell;
    }
    public void setTell(final boolean tell) {
        send(setType + " tell " + (tell ? "1" : "0"));
    }
    public boolean isCtell() {
        return ctell;
    }
    public void setCtell(final boolean ctell) {
        send(setType + " ctell " + (ctell ? "1" : "0"));
    }
    public boolean isOtell() {
        return otell;
    }
    public void setOtell(final boolean otell) {
        send(setType + " otell " + (otell ? "1" : "0"));
    }
    public boolean isPin() {
        return pin;
    }
    public void setPin(final boolean pin) {
        send(setType + " pin " + (pin ? "1" : "0"));
    }
    public boolean isGin() {
        return gin;
    }
    public void setGin(final boolean gin) {
        send(setType + " gin " + (gin ? "1" : "0"));
    }
    public int getQuietplay() {
        return quietplay;
    }
    public void setQuietplay(final int quietplay) {
        send(setType + " quietplay " + quietplay);
    }
    public int getTolerance() {
        return tolerance;
    }
    public void setTolerance(final int tolerance) {
        send(setType + " tolerance " + tolerance);
    }
    public int getBusy() {
        return busy;
    }
    public void setBusy(final int busy) {
        send(setType + " busy " + busy);
    }
    public int getStyle() {
        return style;
    }
    public void setStyle(final int style) {
        send(setType + " style " + style);
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(final int width) {
        send(setType + " width " + width);
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(final int height) {
        send(setType + " height " + height);
    }
    public boolean isWrap() {
        return wrap;
    }
    public void setWrap(final boolean wrap) {
        send(setType + " wrap " + wrap);
    }
    public boolean isPrompt() {
        return prompt;
    }
    public void setPrompt(final boolean prompt) {
        send(setType + " prompt " + (prompt ? "1" : "0"));
    }
    public int getHighlight() {
        return highlight;
    }
    public void setHighlight(final int highlight) {
        send(setType + " highlight " + highlight);
    }
    public int getBell() {
        return bell;
    }
    public void setBell(final int bell) {
        send(setType + " bell " + bell);
    }
    public boolean isOldmatch() {
        return oldmatch;
    }
    public void setOldmatch(final boolean oldmatch) {
        send(setType + " oldmatch " + (oldmatch ? "1" : "0"));
    }
    public boolean isExamine() {
        return examine;
    }
    public void setExamine(final boolean examine) {
        send(setType + " examine " + (examine ? "1" : "0"));
    }
    public int getUnobserve() {
        return unobserve;
    }
    public void setUnobserve(final int unobserve) {
        send(setType + " gin " + unobserve);
    }
    public boolean isAutoflag() {
        return autoflag;
    }
    public void setAutoflag(final boolean autoflag) {
        send(setType + " autoflag " + (autoflag ? "1" : "0"));
    }
    public String getWho() {
        return who;
    }
    public void setWho(final String who) {
        send(setType + " who " + who);
    }
    public String getPlayers() {
        return players;
    }
    public void setPlayers(final String players) {
        send(setType + " players " + players);
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(final String language) {
        send(setType + " language " + language);
    }
    public boolean isWebhelp() {
        return webhelp;
    }
    public void setWebhelp(final boolean webhelp) {
        send(setType + " webhelp " + (webhelp ? "1" : "0"));
    }
    public int getAllowkib() {
        return allowkib;
    }
    public void setAllowkib(final int allowkib) {
        send(setType + " allowkib " + allowkib);
    }
    public boolean isPstat() {
        return pstat;
    }
    public void setPstat(final boolean pstat) {
        send(setType + " pstat " + (pstat ? "1" : "0"));
    }
    public boolean isMessmail() {
        return messmail;
    }
    public void setMessmail(final boolean messmail) {
        send(setType + " messmail " + (messmail ? "1" : "0"));
    }
    public boolean isAutomail() {
        return automail;
    }
    public void setAutomail(final boolean automail) {
        send(setType + " automail " + (automail ? "1" : "0"));
    }
    public int getMailformat() {
        return mailformat;
    }
    public void setMailformat(final int mailformat) {
        send(setType + " mailformat " + mailformat);
    }
    public boolean isAddresspublic() {
        return addresspublic;
    }
    public void setAddresspublic(final boolean addresspublic) {
        send(setType + " addresspublic " + (addresspublic ? "1" : "0"));
    }
    public boolean isNamepublic() {
        return namepublic;
    }
    public void setNamepublic(final boolean namepublic) {
        send(setType + " namepublic " + (namepublic ? "1" : "0"));
    }
    public boolean isSubscribe() {
        return subscribe;
    }
    public void setSubscribe(final boolean subscribe) {
        send(setType + " subscribe " + (subscribe ? "1" : "0"));
    }
    public String getIface() {
        return iface;
    }
    public void setIface(final String iface) {
        send(setType + " interface " + iface);
    }
    private boolean rated = false;
    private int wild;
    private int time;
    private int increment;
    private boolean noescape;
    private boolean notakeback;
    private int minseek;
    private int maxseek;
    private boolean manualaccept;
    private boolean useformula;
    private boolean open;
    private boolean ropen;
    private boolean wopen;
    private boolean ccopen;
    private int mood;
    private boolean seek;
    private String sfilter = null;
    private boolean shout;
    private boolean sshout;
    private int kib;
    private boolean tell;
    private boolean ctell;
    private boolean otell;
    private boolean pin;
    private boolean gin;
    private int quietplay;
    private int tolerance;
    private int busy;
    private int style;
    private int width;
    private int height;
    private boolean wrap;
    private boolean prompt;
    private int highlight;
    private int bell;
    private boolean oldmatch;
    private boolean examine;
    private int unobserve;
    private boolean autoflag;
    private String who;
    private String players;
    private String language;
    private boolean webhelp;
    private int allowkib;
    private boolean pstat;
    private boolean messmail;
    private boolean automail;
    private int mailformat;
    private boolean addresspublic;
    private boolean namepublic;
    private boolean subscribe;
    private String iface;

    public void initHandler() {
    }

    public void iccException(ICCException ex) {
    }

    public void setInstance(ICCInstance icc) {
    }

    public void myVariable(MyVariable p) throws Exception {
        for(Field f : this.getClass().getDeclaredFields()) {
            if(f.getName().equals(p.variableName())) {
                LOG.debug("myVariable setting "+p.variableName()+"="+p.stringValue());
                if(f.getGenericType() == int.class) {
                    f.set(this, p.intValue());
                    return;
                } else if(f.getGenericType() == boolean.class) {
                    f.set(this, (p.intValue() != 0));
                    return;
                } else if(f.getGenericType() == String.class) {
                    f.set(this, p.stringValue());
                    return;
                } else {
                    throw new Exception("Unknown type: "+f.getGenericType());
                }
            }
        }
    }

    public void connectionClosed() {
    }

    public void shuttingDown() {
    }

    public void Error(chessclub.com.icc.l2.Error error) {
    }

    public void badCommand1(BadCommand p) {
    }

    public void badCommand2(L1ErrorOnly p) {
    }
}
