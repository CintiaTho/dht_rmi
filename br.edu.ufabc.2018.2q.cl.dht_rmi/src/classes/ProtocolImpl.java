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
	private String myName;
	private Node node;
	private Protocol myStub;
	private Protocol nextStub;
	private Protocol prevStub;

	public ProtocolImpl(Node node) {
		this.node = node;
		myName = "";
		myStub = null;
		nextStub = null;
		prevStub = null;
	}

	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
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
		if(prevStub == null & nextStub == null) {
			prevStub = newStub;
			nextStub = newStub;
			getNode().setPrevID(newId);
			getNode().setNextID(newId);
			newStub.join_ok(nextStub, prevStub);
		}
		//Quando o id do node ingresante eh maior que o seu -> envia para o seu sucessor tratar
		else if(getNode().getMyid().compareTo(newId) == -1){
			nextStub.join(newStub, newId);
		}
		//Quando o id do node ingresante eh menor que o seu
		else if(getNode().getMyid().compareTo(newId) == 1){
			//E é maior que o ID do seu antecessor -> confirma a entrada na rede e atualiza os stubs
			if(getNode().getPrevID().compareTo(newId) == -1) {
				
			}
			//Ou eh menor que o antecessor -> envia a solicitacao para ele proprio tratar
			else {
				prevStub.join(newStub, newId);
			}
		}
		return true;
	}
	
	public boolean join_ok(Protocol nextStub, Protocol antStub) throws RemoteException {
		this.prevStub = antStub;
		this.nextStub = nextStub;
		return true;
	}

	@Override
	public boolean new_node(Protocol newStub) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean leave() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean node_gone() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean transfer() throws RemoteException {
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

	public void delete(BigInteger key) throws RemoteException{

	}

}
