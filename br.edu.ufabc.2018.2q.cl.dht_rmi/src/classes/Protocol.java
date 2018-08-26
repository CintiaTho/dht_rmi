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
import java.util.LinkedHashMap;

public interface Protocol extends Remote {
	
	public String getMyName() throws RemoteException;

	public void setMyName(String myName) throws RemoteException;

	public Protocol getMyStub() throws RemoteException;

	public void setMyStub(Protocol stub) throws RemoteException;

	public Node getNode() throws RemoteException;

	public void setNode(Node node) throws RemoteException;
	
	public LinkedHashMap<BigInteger, String> getView() throws RemoteException;

	public void setView(LinkedHashMap<BigInteger, String> view) throws RemoteException;

	public boolean join(Protocol newStub, BigInteger newId) throws RemoteException, RuntimeException;

	public boolean join_ok(Protocol predecessor, Protocol sucessor, BigInteger prevId, BigInteger nextId) throws RemoteException;

	public boolean new_node(Protocol newStub, BigInteger newId) throws RemoteException;

	public void begin_to_leave() throws RemoteException;
	
	public boolean leave(Protocol predecessor, BigInteger prevId) throws RemoteException;

	public boolean node_gone(Protocol newStub, BigInteger nextId) throws RemoteException;

	public boolean transfer(BigInteger key, String value) throws RemoteException;

	public boolean store(BigInteger key, String value) throws RemoteException;

	public boolean retrieve(BigInteger key) throws RemoteException;
	
	public boolean ok() throws RemoteException;

	public boolean not_found() throws RemoteException;
	
	public void delete(BigInteger key) throws RemoteException;
	
	public boolean okDel() throws RemoteException;
	
	public boolean view(LinkedHashMap<BigInteger, String> view) throws RemoteException;

}
