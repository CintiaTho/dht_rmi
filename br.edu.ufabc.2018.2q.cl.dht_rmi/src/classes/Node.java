/**
 * Interface de um No de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Node extends java.io.Serializable {
	
	public BigInteger getMyid() throws RemoteException;
	
	public void setMyid(BigInteger id) throws RemoteException;
	
	public BigInteger getPrevID() throws RemoteException;
	
	public void setPrevID(BigInteger prevID) throws RemoteException;
	
	public BigInteger getNextID() throws RemoteException;
	
	public void setNextID(BigInteger nextID) throws RemoteException;
	
	public HashMap<byte[], String> getTexts() throws RemoteException;
	
	public void setTexts(HashMap<byte[], String> texts) throws RemoteException;
}
