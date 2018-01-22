package chessclub.com.icc.jb.icc;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IPersonalTellEcho;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.l2.PersonalTellEcho;

public class WinExempt extends BasicUserICCHandler implements IPersonalTellEcho {

    private static final Logger log = LogManager.getLogger(WinExempt.class);
    private boolean putOn = false;

    public WinExempt(ICCInstance icc, IDatabaseService databaseService, MessageSource messageSource, String handle, boolean on) {
        super(databaseService, messageSource, handle);
        putOn = on;
        if (log.isDebugEnabled()) {
            log.debug(this.hashCode() + "WinExempt created for handle " + handle);
        }
    }

    @Override
    protected void continuehandler() {
        if (putOn) {
            add();
        } else {
            remove();
        }
    }

    private void add() {
        switch (databaseService.addWinExempt(getpx().getUniqueId(), handle(), handle())) {
            case OK:
                xtell(handle(), messageSource.getMessage("added.to.nowin.list", null, getLocale()));
                break;
            case DUPLICATE:
                xtell(handle(), messageSource.getMessage("nowin.already.on", null, getLocale()));
                break;
            default:
                xtell(handle(), messageSource.getMessage("nowin.list.error", null, getLocale()));
                break;
        }
    }

    private void remove() {
        switch (databaseService.removeWinExempt(getpx().getUniqueId())) {
            case OK:
                xtell(handle(), messageSource.getMessage("removed.from.nowin.list", null, getLocale()));
                break;
            default:
                xtell(handle(), messageSource.getMessage("win.list.error", null, getLocale()));
                break;
        }
    }

    @Override
    public void personalTellEcho(PersonalTellEcho p) {
        if (log.isDebugEnabled()) {
            log.debug("Removing myself " + hashCode());
        }
        removeHandler(this);
    }

    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }
}