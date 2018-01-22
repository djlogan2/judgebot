package david.logan.chess.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MoveList {

    protected class move {
        public Move move = null;
        public ArrayList<ArrayList<move>> variations = null;
        @Override
        public move clone() {
        	move nm = new move();
        	nm.move = move.clone();
        	if(variations != null) {
        		nm.variations = new ArrayList<ArrayList<move>>();
        		for(ArrayList<move> a : variations) {
        			ArrayList<move> na = new ArrayList<move>();
        			for(move m : a) {
        				na.add(m.clone());
        			}
        			nm.variations.add(na);
        		}
        	}
        	return nm;
        }
    }
    
    private ArrayList<move> moveList = new ArrayList<move>();
    private LinkedList<ArrayList<move>> variationStack = null;
    
    @Override
    public MoveList clone() {
    	MoveList newOne = new MoveList();
    	for(move m : this.moveList) {
    		move nm = m.clone();
    		newOne.moveList.add(nm);
    	}
    	if(variationStack != null) {
    		newOne.variationStack = new LinkedList<ArrayList<move>>();
	    	for(ArrayList<move> v : variationStack) {
	    		ArrayList<move> nv = new ArrayList<move>();
	    		for(move m : v) {
	    			move nm = m.clone();
	    			nv.add(nm);
	    		}
	    		newOne.variationStack.push(v);
	    	}
    	}
    	return newOne;
    }

    public void add(Move move) {
        move m = new move();
        m.move = move;
        moveList.add(m);
    }
    
    public Move removeLastMove() {
    	if(moveList.size() == 0)
    		return null;
    	move m = moveList.get(moveList.size()-1);
    	moveList.remove(moveList.size()-1);
    	return m.move;
    }
    
    protected void add(move move) {
        moveList.add(move);
    }
    
    public Move getMove(int whichmove) {
        if(whichmove < 0 || whichmove >= moveList.size())
            return null;
        return moveList.get(whichmove).move;
    }
    
    public int moveCount() { return moveList.size(); }
    
    public void startVariation() throws Exception {
        if(moveList.size() == 0)
            throw new Exception("Cannot add a variation to an empty game");
        
        if(variationStack == null) {
            variationStack = new LinkedList<ArrayList<move>>();
        }
        
        if(moveList.get(moveList.size()-1).variations == null) {
            moveList.get(moveList.size()-1).variations = new ArrayList<ArrayList<move>>();
        }
        
        ArrayList<move> pushedList = moveList;
        variationStack.push(moveList);

        moveList = new ArrayList<move>();
        pushedList.get(pushedList.size()-1).variations.add(moveList);
    }
    
    public void endVariation() throws Exception {
        if(variationStack == null) throw new Exception("No variation to end");
        moveList = variationStack.pop();
    }
    
    public class variationIterator implements Iterator<moveIterator> {

        private ArrayList<ArrayList<move>> variations;
        private int atvar = 0;
        
        public variationIterator(ArrayList<ArrayList<move>> variations) {
            this.variations = variations;
        }
        
        @Override
        public boolean hasNext() {
            return (variations != null && atvar >= 0 && atvar < variations.size());
        }

        @Override
        public moveIterator next() {
            if((atvar) < variations.size())
                return new moveIterator(variations.get(atvar++));
            else
                return null;
        }

        @Override
        public void remove() {
            if(atvar >= 0 && atvar < variations.size())
                variations.remove(atvar);
        }
    }
    
    public class moveIterator implements Iterator<Move> {

        public moveIterator(ArrayList<move> moveList) {
            this.moveList = moveList;
        }
        private ArrayList<move> moveList;
        private int atmove = -1;
        @Override
        public boolean hasNext() {
            return ((atmove+1) < moveList.size());
        }
        public boolean hasPrev() {
            return (atmove > 0);
        }
        
        public Move forward() {
            return next();
        }
        public Move backward() {
            if(atmove > 0) return moveList.get(--atmove).move;
            return null;
        }
        
        public Move peekprior() {
            if(atmove == 0 || atmove > moveList.size())
                return null;
            return moveList.get(atmove - 1).move;
        }
        public Move peeknext() {
            if(atmove < -1 || atmove >= moveList.size())
                return null;
            return moveList.get(atmove + 1).move;
        }
        @Override
        public Move next() {
            if((atmove+1) < moveList.size())
                return moveList.get(++atmove).move;
            else
                return null;
        }

        @Override
        public void remove() {
            if(atmove >= 0 && atmove < moveList.size())
                moveList.remove(atmove);
        }
        
        public variationIterator variationIterator() {
            if(atmove < 0 || atmove >= moveList.size())
                return new variationIterator(null);
            if(moveList.get(atmove).variations == null)
                return new variationIterator(null);
            return new variationIterator(moveList.get(atmove).variations);
        }
        
        public Move lastmove() {
            atmove = moveList.size() - 1;
            if(atmove >= 0 && atmove < moveList.size())
                return moveList.get(atmove).move;
            else
                return null;
        }
    }
    
    public moveIterator iterator() { // Always return the main move list
//        if(variationStack != null) {
//            return new moveIterator(variationStack.getLast());
//        }
        return new moveIterator(moveList);
    }

    public int halfMoveCount() {
        return moveList.size();
    }

    public Move getLastMove() {
        if(moveList.size() == 0)
            return null;
        return moveList.get(moveList.size()-1).move;
    }
}
