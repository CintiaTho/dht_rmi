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
import java.util.HashMap;

public class ProtocolImpl implements Protocol {
	private String myName;
	private Node node;
	private Protocol myStub;
	private Protocol sucessor;
	private Protocol predecessor;

	public ProtocolImpl(Node node) {
		this.node = node;
		this.myName = "";
		this.myStub = null;
		this.predecessor = null;
		this.sucessor = null;
	}

	public String getMyName() {
		return this.myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}

	public Protocol getMyStub() {
		return this.myStub;
	}

	public Protocol getPredecessor() {
		return this.predecessor;
	}

	public Protocol getSucessor() {
		return this.sucessor;
	}

	public void setMyStub(Protocol stub) {
		this.myStub = stub;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void begin_to_leave() throws RemoteException {
		this.sucessor.leave(this.predecessor);
		this.predecessor.node_gone(this.sucessor);

		HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
		for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
			BigInteger chave = entry.getKey();
			String valor = entry.getValue();
			this.sucessor.transfer(chave, valor);
		}
	}

	public boolean join(Protocol newStub, BigInteger newId) throws RemoteException {
		// Trata corner cases primeiro

		HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
		HashMap<BigInteger, String> manter = new HashMap<BigInteger, String>();
		HashMap<BigInteger, String> transferir = new HashMap<BigInteger, String>();

		if (this.myStub == newStub && this.getNode().getMyId().compareTo(newId) == 0) {

			// corner case: primeiro no do DHT
			this.predecessor = this.myStub;
			this.sucessor = this.myStub;
			this.getNode().setPrevId(this.getNode().getMyId());
			this.getNode().setNextId(this.getNode().getMyId());
			newStub.join_ok(this.getMyStub(), this.getMyStub());

		} else if (this.getNode().getMyId().compareTo(this.getNode().getPrevId()) <= 0 && this.getNode().getPrevId().compareTo(newId) < 0) {

			// corner case: no atual eh o menor do DHT, predecessor eh o maior do DHT
			// corner case: newStub eh maior que o maior do DHT
			// como o no atual eh o menor do DHT e eh quem envia o transfer, deve conter as entradas maior que o maior do DHT
			for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
				BigInteger chave = entry.getKey();
				String valor = entry.getValue();
				if (chave.compareTo(newId) <= 0) {
					// somente chaves menores ou iguais a newId serao transferidas
					transferir.put(chave, valor);
				} else {
					manter.put(chave, valor);
				}
			}

		} else if (this.getNode().getPrevId().compareTo(newId) < 0 && newId.compareTo(this.getNode().getMyId()) < 0) {
			// caso usual: predecessorID < newId < this.getNode().getMyID()
			for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
				BigInteger chave = entry.getKey();
				String valor = entry.getValue();
				if (chave.compareTo(newId) <= 0) {
					// somente chaves menores ou iguais a newId serao transferidas
					transferir.put(chave, valor);
				} else {
					manter.put(chave, valor);
				}
			}

		} else {

			// caso nao seja o destinatario, deve encaminhar para o sucessor (3.2)
			return this.sucessor.join(newStub, newId);

		}

		// atualiza o predecessor para o novo no
		this.predecessor = newStub;
		this.getNode().setPrevId(newId);

		// efetua transferencia para o novo no
		for (HashMap.Entry<BigInteger, String> entry: transferir.entrySet()) {
			BigInteger chave = entry.getKey();
			String valor = entry.getValue();
			newStub.transfer(chave, valor);
		}

		// ajusta entradas a manter
		this.getNode().setTexts(manter);

		return true;
	}

	public boolean join_ok(Protocol predecessor, Protocol sucessor) throws RemoteException {
		// atualiza o no atual, que eh novo, com o predecessor e sucessor
		this.predecessor = predecessor;
		this.sucessor = sucessor;
		this.getNode().setPrevId(predecessor.getNode().getMyId());
		this.getNode().setNextId(sucessor.getNode().getMyId());

		return this.predecessor.new_node(this.myStub);
	}

	@Override
	public boolean new_node(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo sucessor
		this.sucessor = newStub;
		this.getNode().setNextId(newStub.getNode().getMyId());
		return true;
	}

	@Override
	public boolean leave(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo predecessor
		this.predecessor = newStub;
		this.getNode().setPrevId(newStub.getNode().getMyId());
		return true;
	}

	@Override
	public boolean node_gone(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo sucessor
		this.sucessor = newStub;
		this.getNode().setNextId(newStub.getNode().getMyId());
		return true;
	}

	@Override
	public boolean transfer(BigInteger key, String value) throws RemoteException {
		this.node.insertText(key, value);
		return true;
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
