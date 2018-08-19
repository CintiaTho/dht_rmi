package classes;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProtocolImpl implements Protocol {
	private Node node;

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
