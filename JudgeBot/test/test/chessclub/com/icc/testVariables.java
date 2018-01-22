package test.chessclub.com.icc;

import java.util.HashMap;

import chessclub.com.icc.jb.parserInterface;

public class testVariables implements parserInterface {

	private HashMap<String,Integer> intVars = new HashMap<String,Integer>();
	private HashMap<String,String>  strVars = new HashMap<String,String>();
	
	public void addVar(String name, int value) throws Exception
	{
		if(strVars.containsKey(name)||intVars.containsKey(name)) throw new Exception("Cannot add duplicate variable name "+name);
		intVars.put(name, value);
	}
	public void addVar(String name, String value) throws Exception
	{
		if(strVars.containsKey(name)||intVars.containsKey(name)) throw new Exception("Cannot add duplicate variable name "+name);
		strVars.put(name, value);
	}
	@Override
	public String getSVariable(String variable) throws Exception {
		if(strVars.containsKey(variable))
			return strVars.get(variable);
		else if(intVars.containsKey(variable))
			return intVars.get(variable).toString();
		throw new Exception("No such variable: "+variable);
	}

	@Override
	public int getIVariable(String variable) throws Exception {
		if(intVars.containsKey(variable))
			return intVars.get(variable);
		throw new Exception("No such variable: "+variable);
	}

	@Override
	public Object getVariable(String variable) throws Exception {
		if(strVars.containsKey(variable))
			return strVars.get(variable);
		else if(intVars.containsKey(variable))
			return intVars.get(variable);
		throw new Exception("No such variable: "+variable);
	}

	@Override
	public boolean isIntVariable(String variable) {
		return intVars.containsKey(variable);
	}

}
