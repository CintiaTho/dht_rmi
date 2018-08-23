/**
 * Implementacao de um Protocolo de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.rmi.RemoteException;

public class ProtocolImpl implements Protocol {
	private String falsoID;
	private Node node;
	private Protocol stub;
	private Protocol next_stub;
	private Protocol ant_stub;
	
	public ProtocolImpl(Node node) {
		this.node = node;
		falsoID = "";
		stub = null;
		next_stub = null;
		ant_stub = null;
	}
	
	public String getFalsoID() {
		return falsoID;
	}

	public void setFalsoID(String falsoID) {
		this.falsoID = falsoID;
	}

	public Protocol getStub() {
		return stub;
	}

	public void setStub(Protocol stub) {
		this.stub = stub;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public boolean join() throws RemoteException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean leave() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean store() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retrieve() throws RemoteException {
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

}
