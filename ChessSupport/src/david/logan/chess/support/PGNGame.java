package david.logan.chess.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import david.logan.chess.support.MoveList.moveIterator;
import david.logan.chess.support.MoveList.variationIterator;
import david.logan.chess.support.pgn.parser.ParseException;

public class PGNGame {
    private NormalChessBoard currentboard = new NormalChessBoard();
    private NormalChessBoard previousboard = new NormalChessBoard();
    private Stack<NormalChessBoard> boardVariations;
    private HashMap<String, String> tags = new HashMap<String, String>();
    private MoveList moveList = new MoveList();
    //private int halfMove = 1;
    private GameScore score = null;
    private String initialComment = null; // Any comment prior to the first move

    public void addInitialComment(String initialComment)
    {
    	this.initialComment = initialComment;
    }
    
    /* Standard 7 tags
    1) Event (the name of the tournament or match event)
    2) Site (the location of the event)
    3) Date (the starting date of the game)
    4) Round (the playing round ordinal of the game)
    5) White (the player of the white pieces)
    6) Black (the player of the black pieces)
    7) Result (the result of the game)
    */
    
    public void addTag(String key, String value) {
        tags.put(key, value);
        if("FEN".equals(key)) {
            try {
				currentboard.loadFEN(value);
	            previousboard.loadFEN(value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    public HashMap<String, String> getTags() {
        return tags;
    }
    
    public boolean hasTag(String tagName) {
        return tags.containsKey(tagName);
    }
    
    public String tagValue(String tagName) {
        return tags.get(tagName);
    }
    
    public boolean isValid() {
        if(score == null) return false;
        if(!tags.containsKey("Event")) return false;
        if(!tags.containsKey("Site")) return false;
        if(!tags.containsKey("Date")) return false;
        if(!tags.containsKey("Round")) return false;
        if(!tags.containsKey("White")) return false;
        if(!tags.containsKey("Black")) return false;
        if(!tags.containsKey("Result")) return false;
        return true;
    }
    
    public void addmove(String moveno, String mv, String annotation, String nag, ArrayList<String> comments) throws Exception {
        if(moveList.halfMoveCount() != 0)
            previousboard.makeMove(moveList.getLastMove());
        if(moveno != null) {
            if(Integer.parseInt(moveno) != (((currentboard.moveNumber()-1)/2)+1))
                throw new ParseException("Invalid move number");
        }
        //halfMove++;
        PGNMove move = new PGNMove(currentboard.colorToMove(), mv);
        Square to = move.getToSquare();
        ArrayList<Square[]> legalMoves = currentboard.legalMoves();
        Square from = null;
        legalloop:
        for(Square[] sq2 : legalMoves) {
            if(sq2[1].equals(to) && currentboard.piecetypeAt(sq2[0]) == move.getMovingPiece()) {
                if(move.getFromFile() == -1 && move.getFromRank() == -1) {
                    from = sq2[0];
                    break legalloop;
                } else if(move.getFromFile() == -1 && move.getFromRank() != -1) {
                    if(sq2[0].rank == move.getFromRank()) {
                        from = sq2[0];
                        break legalloop;
                    }
                } else if(move.getFromFile() != -1 && move.getFromRank() == -1) {
                    if(sq2[0].file == move.getFromFile()) {
                        from = sq2[0];
                        break legalloop;
                    }
                } else if(sq2[0].file == move.getFromFile() && sq2[0].rank == move.getFromRank()) {
                    from = sq2[0];
                    break legalloop;
                }
            }
        }

        if(from == null)
            throw new IllegalMoveException(currentboard.getFEN().toString(), null, to);
        else {
            move.finishMove(from, currentboard.piecetypeAt(to), from.equals(currentboard.enPassantSquare()));
            currentboard.makeMove(move);
        }

        moveList.add(move);
    }
    
    public void startvariation() throws Exception {
        if(boardVariations == null)
            boardVariations = new Stack<NormalChessBoard>();
        boardVariations.add(currentboard);
        boardVariations.add(previousboard);
        previousboard = previousboard.clone();
        currentboard = previousboard.clone();
        moveList.startVariation();
    }

    public void endvariation() throws Exception {
        moveList.endVariation();
        previousboard = boardVariations.pop();
        currentboard = boardVariations.pop();
    }

    public void setResult(String result) {
        if("1-0".equals(result)) {
            score = GameScore.WHITE_WINS;
        } else if("0-1".equals(result)) {
            score = GameScore.BLACK_WINS;
        } else if("1/2-1/2".equals(result)) {
            score = GameScore.DRAW;
        } else if("*".equals(result)) {
            score = GameScore.ADJOURNED;
        };
    }

    public int numberMoves() {
        return moveList.halfMoveCount();
    }

    public moveIterator iterator() {
        return moveList.iterator();
    }

    public boolean hasFEN() {
        return tags.containsKey("FEN");
    }
    public String getFEN() {
        return tags.get("FEN");
    }

    public String getWhiteName() {
        return tags.get("White");
    }

    public String getBlackName() {
        return tags.get("Black");
    }
    
    private ArrayList<String> pgnStringData;
    
    private void addToPgn(String data) {
        //
        // This doesn't handle comments, but right now, we aren't supporting comments in the code
        // The parser parses them out, so if we are converting TO a string, there aren't any comments.
        // When we do decide to support comments, we will have to be able to split the comment strings
        // to support 80 bytes.
        //
        int i = pgnStringData.size() - 1;
        String s;
        if(i != -1)
            s = pgnStringData.get(i);
        else {
            s = "";
            pgnStringData.add(s);
            i++;
        }
        if(s.length() + data.length() > 80) {
            s = "";
            pgnStringData.add(s);
            i++;
        }
        s += data;
        pgnStringData.set(i, s);
    }
    
    private void getMoves(int movenumber, Color p_tomove, moveIterator mi) {
        boolean variations = false;
        boolean firstthreedots = (p_tomove == Color.BLACK);
        Color tomove = p_tomove;
        
        while(mi.hasNext()) {
            Move m = mi.next();
            try {
                if(tomove == Color.BLACK && (variations || firstthreedots)) {
                    addToPgn(Integer.toString(movenumber) + "...");
                    firstthreedots = false;
                } else if(tomove == Color.WHITE)
                    addToPgn(Integer.toString(movenumber) + ".");
                if(tomove == Color.BLACK)
                    movenumber++;
                tomove = tomove.other();
                variations = false;
                addToPgn(m.getAlgebraicMove());
                addToPgn(" ");
            } catch (Exception e) {
                e.printStackTrace();
                addToPgn(Integer.toString(movenumber) + ".");
                addToPgn("???");
            }
            variationIterator vi = mi.variationIterator();
            while(vi.hasNext()) {
                moveIterator m2 = vi.next();
                addToPgn(" (");
                getMoves(movenumber + 1, tomove.other(), m2);
                variations = true;
                addToPgn(")");
                addToPgn(" ");
            }
        }
    }

    public String pgnToString() {
        pgnStringData = new ArrayList<String>();
        for(String key : tags.keySet()) {
            pgnStringData.add("[" + key + " \"" + tags.get(key) + "\"]");
        }
        pgnStringData.add("");
        int mn = 1;
        Color mc = Color.WHITE;
        if(hasFEN())
        {
        	try {
				Fen fen = new Fen(getFEN());
				mn = fen.moveNumber();
				mc = fen.toMove();
				if(mc == Color.WHITE)
					mn++;
				mn /= 2;
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        if(initialComment != null)
        	addToPgn("{" + initialComment + "}");
        getMoves(mn, mc, moveList.iterator());
        addToPgn(" ");
        if(tags.containsKey("Result"))
            addToPgn(tags.get("Result"));
        else
            addToPgn("*");
        String ret = "";
        for(String s : pgnStringData)
            ret += s + "\r\n";
        return ret;
    }
}
