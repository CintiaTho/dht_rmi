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
	private HashMap <BigInteger, String> texts;

	public NodeImpl() {
		super();
		myid = null;
		texts = null;
	}
	public BigInteger getMyid() {
		return myid;
	}
	public void setMyid(BigInteger myid) {
		this.myid = myid;
	}
	public HashMap<BigInteger, String> getTexts() {
		return texts;
	}
	public void setTexts(HashMap<BigInteger, String> texts) {
		this.texts = texts;
	}

	public void insertText(BigInteger chave, String texto) {
		this.texts.put(chave, texto);
	}
}
