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
	private HashMap <byte[], String> texts;
	
	public NodeImpl() {
		super();
		myid = null;
		nextID = null;
		prevID = null;
		texts = null;
	}
	public BigInteger getMyid() {
		return myid;
	}
	public void setMyid(BigInteger myid) {
		this.myid = myid;
	}
	public BigInteger getPrevID() {
		return prevID;
	}
	public void setPrevID(BigInteger prevID) {
		this.prevID = prevID;
	}
	public BigInteger getNextID() {
		return nextID;
	}
	public void setNextID(BigInteger nextID) {
		this.nextID = nextID;
	}
	public HashMap<byte[], String> getTexts() {
		return texts;
	}
	public void setTexts(HashMap<byte[], String> texts) {
		this.texts = texts;
	}
	
}
