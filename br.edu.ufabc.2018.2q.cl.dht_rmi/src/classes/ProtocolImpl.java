package classes;

import java.rmi.RemoteException;

public class ProtocolImpl implements Protocol {
	private int falsoID;
	private Node node;
	private Protocol stub;
	private Protocol next_stub;
	private Protocol ant_stub;
	
	public ProtocolImpl(Node node) {
		this.node = node;
		falsoID = 0;
		stub = null;
		next_stub = null;
		ant_stub = null;
	}
	
	public int getFalsoID() {
		return falsoID;
	}

	public void setFalsoID(int falsoID) {
		this.falsoID = falsoID;
	}

	public Protocol getStub() {
		return stub;
	}

	public void setStub(Protocol stub) {
		this.stub = stub;
	}

	public Protocol getNext_stub() {
		return next_stub;
	}

	public void setNext_stub(Protocol next_stub) {
		this.next_stub = next_stub;
	}

	public Protocol getAnt_stub() {
		return ant_stub;
	}

	public void setAnt_stub(Protocol ant_stub) {
		this.ant_stub = ant_stub;
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
