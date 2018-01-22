package chessclub.com.icc.tt;

import java.util.ArrayList;

import org.springframework.context.MessageSource;

import chessclub.com.icc.DefaultICCCommands;
import chessclub.com.icc.ICCException;
import chessclub.com.icc.ICCInstance;
import chessclub.com.icc.handler.interfaces.IAbstractICCHandler;
import chessclub.com.icc.l1.BadCommand;
import chessclub.com.icc.l1.L1ErrorOnly;
import chessclub.com.icc.tt.ifac.IDatabaseService;

public class MassMessage extends DefaultICCCommands implements IAbstractICCHandler {
    private ICCInstance icc;
    
    @SuppressWarnings("unused")
	private MessageSource messageSource;
    private IDatabaseService databaseService;

    private String message;
    private Thread massMessageThread = null;
    ArrayList<String> userlist;
    private IAbstractICCHandler thisHandler;
    
	public MassMessage(MessageSource _messageSource, IDatabaseService _databaseService, ICCInstance _icc, String msg) {
	    this.messageSource = _messageSource;
	    this.databaseService = _databaseService;
	    this.message = msg;
	    this.icc = _icc;
	    this.setInstance(icc,  this);
	    thisHandler = this;
	    userlist = new ArrayList<String>(databaseService.getAllUsers());
    	massMessageThread = new Thread() {
			@Override
			public void run() {
				for(int i = 0 ; i < userlist.size() ; i++) {
					message(userlist.get(i).trim(), message);
					//send("message djlogan " + userlist.get(i) + " " + message);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				};
				icc.removeHandler(thisHandler);
			}
    	};
    	massMessageThread.start();
	}

	@Override
	public void initHandler() {
	}

	@Override
	public void iccException(ICCException ex) {
	}

	@Override
	public void setInstance(ICCInstance icc) {
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
	public void Error(chessclub.com.icc.l2.Error error) {
	}

	@Override
	public void shuttingDown() {
	}
}
