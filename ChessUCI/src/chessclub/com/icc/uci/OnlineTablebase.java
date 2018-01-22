package chessclub.com.icc.uci;

import java.util.ArrayList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import david.logan.chess.support.Fen;
import david.logan.chess.support.Move;

//
// Yes, I know that the online tablebase doesn't have anything to do with the chess engine, but where else is it going to go?
// Besides, it may be a good idea to incorporate this into UCIEngine so that it's an automatic thing whenever anyone uses any
// UCI engine...
//
// But at the moment, it's standalone.
//
public class OnlineTablebase {

    private static final Logger log = LogManager.getLogger(OnlineTablebase.class);

    private final static int MAXPIECECOUNT = 5;
	private Fen fen;
	private int piececount = 0;
	private ArrayList<MovePlusScore> moveList = new ArrayList<MovePlusScore>();
	//private ArrayList<Integer> scoreList = new ArrayList<Integer>();

	public Fen getFen() { return fen; }
	
	public int getCount() {
		if(moveList == null) return 0;
		return moveList.size();
	}
	
	public Move getMove(int indexat) {
		if(moveList == null || indexat >= moveList.size()) return null;
		return moveList.get(indexat).move;
	}
	
	public Integer getScore(int indexat) {
		if(moveList == null || indexat >= moveList.size()) return null;
		return moveList.get(indexat).score;
	}
	
	public int GetTablebaseMoves(Fen fen) {
		this.fen = fen;
		piececount = fen.pieceCount();
		log.debug("GetTablebaseMoves, fen='" + fen + "', piececount=" + piececount);
		if (piececount > MAXPIECECOUNT)
			return 0;
		moveList.clear();
		try {
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("mes",
					"http://lokasoft.org/message/");
			SOAPBody soapBody = envelope.getBody();
			SOAPElement e1 = soapBody.addChildElement("GetBestMoves", "mes");
			e1.addChildElement("fen").addTextNode(fen.getFEN());
			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction",
					"http://www.lokasoft.nl/tbweb/tbapi.asp");

			soapMessage.saveChanges();

			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory
					.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory
					.createConnection();

			// Send SOAP Message to SOAP Server
			SOAPMessage soapResponse = soapConnection.call(soapMessage,
					"http://www.lokasoft.nl/tbweb/tbapi.asp");
			soapConnection.close();

			String rawmoves = soapResponse.getSOAPBody()
					.getElementsByTagName("Result").item(0).getTextContent();
			// b5-b4 M11
			// Kf2-f3 -M10
			// Kh6-g5 0
			// d2-d1Q M8
			if(rawmoves.length() == 0)
				return 0;
			String[] moves = rawmoves.split("\\n");
			for (String m : moves) {
				MovePlusScore mps = new MovePlusScore();
				mps.score = 999;
				String sq1, sq2;
				String promote = "";
				String[] move_score = m.split(" ");
				String[] move = move_score[0].split("-|x");
				if (move[0].length() == 3)
					sq1 = move[0].substring(1);
				else
					sq1 = move[0];
				if (move[1].length() == 3) {
					sq2 = move[1].substring(0, 2);
					promote = move[1].substring(2);
				} else
					sq2 = move[1];
				if (move_score[1].startsWith("-M")) {
					mps.score = Integer.parseInt(move_score[1].substring(2)) * -1;
				} else if (move_score[1].equals("0")) {
					mps.score = 0;
				} else if (move_score[1].startsWith("M")) {
					mps.score = Integer.parseInt(move_score[1].substring(1));
				} else {
					log.error("Unknown score in return from tablebase call: " + move_score[1]);
					moveList.clear();
					return 0;
				}
				mps.move = new Move(sq1 + sq2 + promote, true);
				log.debug("Tablebase parse: " + m + " / " + mps.move + " / " + mps.score);
				moveList.add(mps);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception in tablebase processing: " + e.getMessage());
			return 0; // Just for safety. Make sure caller doesn't use some partial data
		}
		log.debug("moveList size: " + moveList.size());
		if(moveList == null)
			return 0;
		return moveList.size();
	}
	
	public class MovePlusScore {
		public Move move;
		public int score;
	};
}
