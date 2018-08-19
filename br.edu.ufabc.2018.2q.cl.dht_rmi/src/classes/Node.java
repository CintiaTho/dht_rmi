package classes;

import java.rmi.RemoteException;
import java.util.HashMap;

public interface Node extends java.io.Serializable {
	
	public byte[] getMyid() throws RemoteException;
	
	public void setMyid(byte[] myid) throws RemoteException;
	
	public byte[] getProxid() throws RemoteException;
	
	public void setProxid(byte[] proxid) throws RemoteException;
	
	public byte[] getAntid() throws RemoteException;
	
	public void setAntid(byte[] antid) throws RemoteException;
	
	public HashMap<byte[], String> getTexts() throws RemoteException;
	
	public void setTexts(HashMap<byte[], String> texts) throws RemoteException;
}
