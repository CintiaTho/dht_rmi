package classes;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Node extends java.io.Serializable {
	
	public BigInteger getMyid() throws RemoteException;
	
	public void setMyid(BigInteger id) throws RemoteException;
	
	public HashMap<byte[], String> getTexts() throws RemoteException;
	
	public void setTexts(HashMap<byte[], String> texts) throws RemoteException;
}
