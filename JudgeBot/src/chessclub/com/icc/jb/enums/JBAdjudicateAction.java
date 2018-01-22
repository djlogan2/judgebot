package chessclub.com.icc.jb.enums;

public enum JBAdjudicateAction {
	WIN,LOSE,DRAW,ABORT,MANUAL;
	public static JBAdjudicateAction getAction(int i)
	{
		for(JBAdjudicateAction a : JBAdjudicateAction.values())
			if(a.ordinal() == i)
				return a;
		return null;
	}
}
