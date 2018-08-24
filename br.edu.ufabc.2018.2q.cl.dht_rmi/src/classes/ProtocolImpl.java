/**
 * Implementacao de um Protocolo de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package classes;

import java.math.BigInteger;
import java.rmi.RemoteException;

public class ProtocolImpl implements Protocol {
	private String falsoID;
	private Node node;
	private Protocol myStub;
	private Protocol nextStub;
	private Protocol antStub;

	public ProtocolImpl(Node node) {
		this.node = node;
		falsoID = "";
		myStub = null;
		nextStub = null;
		antStub = null;
	}

	public String getFalsoID() {
		return falsoID;
	}

	public void setFalsoID(String falsoID) {
		this.falsoID = falsoID;
	}

	public Protocol getMyStub() {
		return myStub;
	}

	public void setMyStub(Protocol stub) {
		this.myStub = stub;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public boolean join(Protocol newStub, BigInteger newId) throws RemoteException {
		//Caso especial quando só havia um nó na rede
		if(antStub == null & nextStub == null) {
			antStub = newStub;
			nextStub = newStub;
			newStub.join_ok(nextStub, antStub);
		}
		//Quando o id do nó ingresante é maior que o seu -> envia ara o seu sucessor tratar
		else if(getNode().getMyid().compareTo(newId) == -1){
			nextStub.join(newStub, newId);
		}
		//Quando o id do nó ingresante é menor que o seu
		else if(getNode().getMyid().compareTo(newId) == 1){
			//E é maior que o ID do seu antecessor -> confirma a entrada na rede e atualiza os stubs
			if(antStub.getNode().getMyid().compareTo(newId) == -1) {
				
			}
			//Ou é menor que o antecessor -> envia a solicitacao para ele proprio tratar
			else {
				antStub.join(newStub, newId);
			}
		}
		return true;
	}

	@Override
	public boolean leave() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean store(BigInteger key, String value) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retrieve(BigInteger key) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean join_ok(Protocol nextStub, Protocol antStub) throws RemoteException {
		this.antStub = antStub;
		this.nextStub = nextStub;
		return true;
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

	public void delete(BigInteger key) throws RemoteException{

	}

}
