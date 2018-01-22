package chessclub.com.icc.jb.icc;

import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import chessclub.com.icc.DefaultICCAdminCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IGetpx;
import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.Getpx;
import chessclub.com.icc.l1.L1ErrorOnly;

public abstract class BasicUserICCHandler extends DefaultICCAdminCommands implements IGetpx {

    private static final Logger log        = LogManager.getLogger(WinExempt.class);
    protected IDatabaseService databaseService;
    protected MessageSource messageSource;
    
    protected boolean iscomputer = false;
	private String handle;
	private Locale locale = null;
	private Getpx  getpx = null;
	protected Getpx getpx() { return this.getpx; }
	protected String handle() { return handle; }
	public BasicUserICCHandler(IDatabaseService databaseService, MessageSource messageSource, String handle)
	{
		if(log.isDebugEnabled())
			log.debug(this.hashCode()+"BasicUserICCHandler created for handle "+handle);
	    this.databaseService = databaseService;
	    this.messageSource = messageSource;
		this.handle = handle;
	}
	@Override
	public void initHandler()
	{
		this.getpx(handle, "buhandler");
	}
	@Override
	public void getpx(Getpx p)
	{
		this.getpx = p;
		locale = new Locale(getpx.getLanguage());
		iscomputer = p.isComputer();
		continuehandler();
	}
	protected abstract void continuehandler();
	
	@Override
	public void setInstance(ICCInstance icc) {
	    super.setInstance(icc, this);
	}

	public Locale getLocale()
	{
		return locale;
	}
	
    @Override
    public void iccException(ICCException ex) {
    }
    @Override
    public void connectionClosed() {
    }
    @Override
    public void badCommand1(BadCommand p) {
    }
    @Override
    public void badCommand2(L1ErrorOnly p) {
    }
    @Override
    public void shuttingDown() {
    }
}
