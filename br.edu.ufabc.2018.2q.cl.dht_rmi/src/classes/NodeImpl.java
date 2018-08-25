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

	private BigInteger myId;
	private BigInteger nextId;
	private BigInteger prevId;
	private HashMap <BigInteger, String> texts;

	public NodeImpl() {
		super();
		this.myId = null;
		this.nextId = null;
		this.prevId = null;
		this.texts = new HashMap<BigInteger, String>();
	}

	public BigInteger getMyId() {
		return this.myId;
	}

	public void setMyId(BigInteger myid) {
		this.myId = myid;
	}

	public BigInteger getPrevId() {
		return this.prevId;
	}

	public void setPrevId(BigInteger prevId) {
		this.prevId = prevId;
	}

	public BigInteger getNextId() {
		return this.nextId;
	}

	public void setNextId(BigInteger nextId) {
		this.nextId = nextId;
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
