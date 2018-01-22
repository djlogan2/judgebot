package chessclub.com.icc.jb.icc;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import chessclub.com.icc.jb.ifac.IDatabaseService;
import chessclub.com.icc.l2.PersonalTellEcho;

public class LocaleXtell extends BasicUserICCHandler {

	private String user;
	private String key;
	private Object[] args;
	private Object[] data;
	private int tellsout = 0;
	public LocaleXtell(IDatabaseService databaseService, MessageSource messageSource, String user, String key, String[] args)
	{
		super(databaseService, messageSource, user);
		this.messageSource = messageSource;
		this.user = user;
		this.key = key;
		this.args = args;
	}
	public LocaleXtell(IDatabaseService databaseService, MessageSource messageSource, String user, Object[] data)
	{
		super(databaseService, messageSource, user);
		this.user = user;
		this.data = data;
	}
	@Override
	protected void continuehandler() {
	    if(iscomputer)
	    {
            removeHandler(this);
            return;
	    }
		if(key == null)
		{
			multitell();
			return;
		}
		try {
		    xtell(user,messageSource.getMessage(key, args, getLocale()));
		} catch(NoSuchMessageException e) {
		    xtell(user,messageSource.getMessage(key, args, new Locale("en", "US")));
		}
		tellsout = 1;
	}

	private void multitell()
	{
		int which = 0;
		String mkey = null;
		for(Object o : data)
		{
			if(which == 0)
			{
				mkey = (String)o;
				which++;
			}
			else
			{
			    try {
			        xtell(user,messageSource.getMessage(mkey, (Object[])o, getLocale()));
			    } catch(NoSuchMessageException e) {
                    xtell(user,messageSource.getMessage(mkey, (Object[])o, new Locale("en", "US")));
			    }
				tellsout++;
				which--;
			}
		}
	}
	public void PersonalTellEcho(PersonalTellEcho p)
	{
		if(--tellsout == 0)
			removeHandler(this);
	}
    @Override
    public void Error(chessclub.com.icc.l2.Error error) {
    }
}
