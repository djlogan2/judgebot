/* PGNParser.java */
/* Generated By:JavaCC: Do not edit this line. PGNParser.java */
package david.logan.chess.support.pgn.parser;

import java.util.ArrayList;

import david.logan.chess.support.IllegalMoveException;
import david.logan.chess.support.PGNGame;

@SuppressWarnings("all")
public class PGNParser implements PGNParserConstants {
        ArrayList<PGNGame> gamelist = new ArrayList<PGNGame>();
        PGNGame current = null;

  final public ArrayList<PGNGame> parsefile() throws ParseException, IllegalMoveException, Exception {
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INTEGER:
      case LBRACKET:
      case GAMETERM:
      case MOVE:
      case CASTLE:{
        ;
        break;
        }
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      pgngame();
    }
{if ("" != null) return gamelist;}
    throw new Error("Missing return statement in function");
  }

  final public PGNGame pgngame() throws ParseException, IllegalMoveException, Exception {Token t = null;
                current = new PGNGame();
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case LBRACKET:{
      tagpairsection();
      break;
      }
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGER:
    case MOVE:
    case CASTLE:{
      movetextsection();
      break;
      }
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    t = jj_consume_token(GAMETERM);
current.setResult(t.image);
                        if(current.isValid())
                                gamelist.add(current);
                        current = null;
                        {if ("" != null) return gamelist.get(gamelist.size()-1);}
    throw new Error("Missing return statement in function");
  }

  final public void tagpairsection() throws ParseException {
    label_2:
    while (true) {
      tagpair();
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case LBRACKET:{
        ;
        break;
        }
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
    }
  }

  final public void tagpair() throws ParseException {Token tag;
                Token value;
    jj_consume_token(LBRACKET);
    tag = jj_consume_token(TAGNAME);
    value = jj_consume_token(STRLIT);
    jj_consume_token(RBRACKET);
current.addTag(tag.image, value.image.replaceAll("\u005c"", ""));
  }

  final public void movetextsection() throws ParseException, IllegalMoveException, Exception {
    movelist();
  }

  final public void movelist() throws ParseException, IllegalMoveException, Exception {
    label_3:
    while (true) {
      sanmove();
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case OPAREN:{
          ;
          break;
          }
        default:
          jj_la1[4] = jj_gen;
          break label_4;
        }
        jj_consume_token(OPAREN);
current.startvariation();
        movelist();
        jj_consume_token(CPAREN);
current.endvariation();
      }
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case INTEGER:
      case MOVE:
      case CASTLE:{
        ;
        break;
        }
      default:
        jj_la1[5] = jj_gen;
        break label_3;
      }
    }
  }

  final public void sanmove() throws ParseException, IllegalMoveException, Exception {Token moveno = null;
                Token move = null;
                Token ann = null;
                Token nag = null;
                String com = null;
                ArrayList<String> comments = null;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case INTEGER:{
      moveno = jj_consume_token(INTEGER);
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
        case PERIOD:{
          ;
          break;
          }
        default:
          jj_la1[6] = jj_gen;
          break label_5;
        }
        jj_consume_token(PERIOD);
      }
      break;
      }
    default:
      jj_la1[7] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case MOVE:{
      move = jj_consume_token(MOVE);
      break;
      }
    case CASTLE:{
      move = jj_consume_token(CASTLE);
      break;
      }
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case NAG:
    case ANNOTATION:{
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case ANNOTATION:{
        ann = jj_consume_token(ANNOTATION);
        break;
        }
      case NAG:{
        nag = jj_consume_token(NAG);
        break;
        }
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
      }
    default:
      jj_la1[10] = jj_gen;
      ;
    }
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
      case COMMENT:
      case LINECOMMENT:{
        ;
        break;
        }
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
      com = comment();
if(comments == null)
                                        comments = new ArrayList<String>();
                                comments.add(com);
    }
current.addmove((moveno == null?null:moveno.image),
                                        move.image,
                                        (ann == null ? null : ann.image),
                                        (nag == null ? null : nag.image),
                                        comments);
  }

  final public String comment() throws ParseException {Token t;
    switch ((jj_ntk==-1)?jj_ntk_f():jj_ntk) {
    case COMMENT:{
      t = jj_consume_token(COMMENT);
{if ("" != null) return t.image;}
      break;
      }
    case LINECOMMENT:{
      t = jj_consume_token(LINECOMMENT);
{if ("" != null) return t.image;}
      break;
      }
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public PGNParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x2c5000,0x4000,0x281000,0x4000,0x10000,0x281000,0x2000,0x1000,0x280000,0xc00000,0xc00000,0x440,0x440,};
   }

  /** Constructor with InputStream. */
  public PGNParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public PGNParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PGNParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public PGNParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PGNParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public PGNParser(PGNParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(PGNParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk_f() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[24];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 24; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
