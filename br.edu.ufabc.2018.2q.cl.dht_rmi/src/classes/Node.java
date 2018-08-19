package classes;

import java.rmi.RemoteException;
import java.util.HashMap;

public interface Node extends java.io.Serializable {
	
	public byte[] getId_my() throws RemoteException;
	
	public void setId_my(byte[] id_my) throws RemoteException;
	
	public byte[] getId_prox() throws RemoteException;
	
	public void setId_prox(byte[] id_prox) throws RemoteException;
	
	public byte[] getId_ant() throws RemoteException;
	
	public void setId_ant(byte[] id_ant) throws RemoteException;
	
	public HashMap<byte[], String> getTexts() throws RemoteException;
	
	public void setTexts(HashMap<byte[], String> texts) throws RemoteException;
}
