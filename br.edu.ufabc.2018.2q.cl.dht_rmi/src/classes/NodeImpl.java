/**
 * Implementacao de um No de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.math.BigInteger;
import java.util.HashMap;

@SuppressWarnings("serial")
public class NodeImpl implements Node {

	private BigInteger myid;
	private BigInteger nextID;
	private BigInteger prevID;
	private HashMap <BigInteger, String> texts;

	public NodeImpl() {
		super();
		this.myid = null;
		this.nextID = null;
		this.prevID = null;
		this.texts = new HashMap<BigInteger, String>();
	}

	public BigInteger getMyID() {
		return this.myid;
	}

	public void setMyID(BigInteger myid) {
		this.myid = myid;
	}

	public BigInteger getPrevID() {
		return this.prevID;
	}

	public void setPrevID(BigInteger prevID) {
		this.prevID = prevID;
	}

	public BigInteger getNextID() {
		return this.nextID;
	}

	public void setNextID(BigInteger nextID) {
		this.nextID = nextID;
	}

	public HashMap<BigInteger, String> getTexts() {
		return this.texts;
	}

	public void setTexts(HashMap<BigInteger, String> texts) {
		this.texts = texts;
	}

	public void insertText(BigInteger chave, String texto) {
		this.texts.put(chave, texto);
	}
}
