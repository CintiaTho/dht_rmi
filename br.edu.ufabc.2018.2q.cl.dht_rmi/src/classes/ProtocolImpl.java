/**
 * Implementacao de um Protocolo de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.math.BigInteger;
import java.rmi.RemoteException;

public class ProtocolImpl implements Protocol {
	private String falsoID;
	private Node node;
	private Protocol myStub;
	private Protocol nextStub;
	private Protocol antStub;
	
	public ProtocolImpl(Node node) {
		this.node = node;
		falsoID = "";
		myStub = null;
		nextStub = null;
		antStub = null;
	}
	
	public String getFalsoID() {
		return falsoID;
	}

	public void setFalsoID(String falsoID) {
		this.falsoID = falsoID;
	}

	public Protocol getMyStub() {
		return myStub;
	}

	public void setMyStub(Protocol stub) {
		this.myStub = stub;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public boolean join(Protocol protocol, BigInteger bigInteger) throws RemoteException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean leave() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean store(BigInteger key, String value) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retrieve(BigInteger key) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean join_ok() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean new_node() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean node_gone() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ok() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean not_found() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean transfer() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void delete(BigInteger key) throws RemoteException{
		
	}

}
