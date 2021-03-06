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

	public BigInteger getMyId() throws RemoteException;

	public void setMyId(BigInteger Id) throws RemoteException;
	
	public BigInteger getPrevId() throws RemoteException;
	
	public void setPrevId(BigInteger prevId) throws RemoteException;
	
	public BigInteger getNextId() throws RemoteException;
	
	public void setNextId(BigInteger nextId) throws RemoteException;

	public HashMap<BigInteger, String> getTexts() throws RemoteException;

	public void setTexts(HashMap<BigInteger, String> texts) throws RemoteException;

	public void insertText(BigInteger chave, String texto) throws RemoteException;
}
