package classes;

import java.util.HashMap;

@SuppressWarnings("serial")
public class NodeImpl implements Node {

	private byte[] myid;
	private HashMap <byte[], String> texts;
	
	public NodeImpl() {
		super();
		myid = null;
		texts = null;
	}
	public byte[] getMyid() {
		return myid;
	}
	public void setMyid(byte[] myid) {
		this.myid = myid;
	}
	public HashMap<byte[], String> getTexts() {
		return texts;
	}
	public void setTexts(HashMap<byte[], String> texts) {
		this.texts = texts;
	}
	
}
