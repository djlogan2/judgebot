package chessclub.com.icc.jb.ifac;

public interface parserInterface {
    public String getSVariable(String variable) throws Exception;		// Get only a string variable
    public int getIVariable(String variable) throws Exception;			// Get only an integer variable
    public Object getVariable(String variable) throws Exception;		// Get any variable
    public boolean isIntVariable(String variable) throws Exception;		// True if it's an integer variable
}
