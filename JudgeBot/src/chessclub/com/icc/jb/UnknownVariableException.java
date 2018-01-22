package chessclub.com.icc.jb;

import java.util.Locale;

import org.springframework.context.MessageSource;

public class UnknownVariableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String variable;
	private String localMessage;
	
	UnknownVariableException(MessageSource messageSource, Locale locale, String variable)
	{
		localMessage = messageSource.getMessage("unknown.variable", new Object[]{variable}, locale);
	}
	public String getVariable() { return variable; }
	public String getLocalizedMessage() { return localMessage; }
	public String getMessage() { return localMessage; }
}
