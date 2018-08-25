/**
 * Interface de um Protocolo de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Protocol extends Remote {

	public String getFalsoID() throws RemoteException;

	public void setFalsoID(String string) throws RemoteException;

	public Protocol getMyStub() throws RemoteException;

	public Protocol getPredecessor() throws RemoteException;

	public Protocol getSucessor() throws RemoteException;

	public void setMyStub(Protocol stub) throws RemoteException;

	public Node getNode() throws RemoteException;

	public void setNode(Node node) throws RemoteException;

	public void begin_to_leave() throws RemoteException;

	public boolean join(Protocol newStub, BigInteger newId) throws RemoteException, RuntimeException;

	public boolean join_ok(Protocol predecessor, Protocol sucessor) throws RemoteException;

	public boolean new_node(Protocol newStub) throws RemoteException;

	public boolean leave(Protocol newStub) throws RemoteException;

	public boolean node_gone(Protocol newStub) throws RemoteException;

	public boolean transfer(BigInteger key, String value) throws RemoteException;

	public boolean store(BigInteger key, String value) throws RemoteException;

	public boolean retrieve(BigInteger key) throws RemoteException;

	public boolean ok() throws RemoteException;

	public boolean not_found() throws RemoteException;

	public void delete(BigInteger key) throws RemoteException;
}
