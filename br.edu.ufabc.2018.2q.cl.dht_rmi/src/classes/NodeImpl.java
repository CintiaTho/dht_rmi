package classes;

import java.util.HashMap;

@SuppressWarnings("serial")
public class NodeImpl implements Node {

	private byte[] myid;
	private byte[] proxid;
	private byte[] antid;
	private HashMap <byte[], String> texts;
	
	public byte[] getMyid() {
		return myid;
	}
	public void setMyid(byte[] myid) {
		this.myid = myid;
	}
	public byte[] getProxid() {
		return proxid;
	}
	public void setProxid(byte[] proxid) {
		this.proxid = proxid;
	}
	public byte[] getAntid() {
		return antid;
	}
	public void setAntid(byte[] antid) {
		this.antid = antid;
	}
	public HashMap<byte[], String> getTexts() {
		return texts;
	}
	public void setTexts(HashMap<byte[], String> texts) {
		this.texts = texts;
	}
	
}
