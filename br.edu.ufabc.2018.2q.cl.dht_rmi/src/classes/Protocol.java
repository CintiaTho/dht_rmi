package classes;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Protocol extends Remote {
	
	public Node getNode() throws RemoteException;

	public void setNode(Node node) throws RemoteException;
	
	public boolean join() throws RemoteException;
	
	public boolean join_ok() throws RemoteException;
	
	public boolean new_node() throws RemoteException;
	
	public boolean leave() throws RemoteException;
	
	public boolean node_gone() throws RemoteException;
	
	public boolean store() throws RemoteException;
	
	public boolean retrieve() throws RemoteException;
	
	public boolean ok() throws RemoteException;
	
	public boolean not_found() throws RemoteException;
	
	public boolean transfer() throws RemoteException;
}
