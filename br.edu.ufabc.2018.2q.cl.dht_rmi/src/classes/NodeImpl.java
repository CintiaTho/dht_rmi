package classes;

import java.math.BigInteger;
import java.util.HashMap;

@SuppressWarnings("serial")
public class NodeImpl implements Node {

	private BigInteger myid;
	private HashMap <byte[], String> texts;
	
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
	public HashMap<byte[], String> getTexts() {
		return texts;
	}
	public void setTexts(HashMap<byte[], String> texts) {
		this.texts = texts;
	}
	
}
