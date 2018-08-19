package classes;

import java.util.HashMap;

@SuppressWarnings("serial")
public class NodeImpl implements Node {

	private byte[] myid;
	private byte[] proxid;
	private byte[] antid;
	private HashMap <byte[], String> texts;
	
	@Override
	public byte[] getId_my() {
		return myid;
	}
	
	@Override
	public void setId_my(byte[] id_my) {
		this.myid = id_my;
	}
	
	@Override
	public byte[] getId_prox() {
		return proxid;
	}
	
	@Override
	public void setId_prox(byte[] id_prox) {
		this.proxid = id_prox;
	}
	
	@Override
	public byte[] getId_ant() {
		return antid;
	}
	
	@Override
	public void setId_ant(byte[] id_ant) {
		this.antid = id_ant;
	}
	
	@Override
	public HashMap<byte[], String> getTexts() {
		return texts;
	}
	
	@Override
	public void setTexts(HashMap<byte[], String> texts) {
		this.texts = texts;
	}
}
