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
	private String falsoID;
	private Node node;
	private Protocol myStub;
	private Protocol sucessor;
	private Protocol predecessor;
	private BigInteger sucessorID;
	private BigInteger predecessorID;

	public ProtocolImpl(Node node) {
		this.node = node;
		this.falsoID = "";
		this.myStub = null;
		this.predecessor = null;
		this.predecessorID = BigInteger.valueOf(0);
		this.sucessor = null;
		this.sucessorID = BigInteger.valueOf(0);
	}

	public String getFalsoID() {
		return falsoID;
	}

	public void setFalsoID(String falsoID) {
		this.falsoID = falsoID;
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

		BigInteger myID = this.getNode().getMyid();

		HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
		HashMap<BigInteger, String> manter = new HashMap<BigInteger, String>();
		HashMap<BigInteger, String> transferir = new HashMap<BigInteger, String>();

		if (this.myStub == newStub && myID.compareTo(newId) == 0) {

			// corner case: primeiro no do DHT
			this.predecessor = this.myStub;
			this.sucessor = this.myStub;
			this.predecessorID = this.getNode().getMyid();
			this.sucessorID = this.getNode().getMyid();
			newStub.join_ok(this.getMyStub(), this.getMyStub());

		} else if (this.getMyStub() == this.predecessor) {

			// corner case: unico no no DHT
			newStub.join_ok(this.getMyStub(), this.getMyStub());

			// separa as entradas do hash a serem transferidas
			if (myID.compareTo(newId) < 0) {
				// id atual menor que id novo
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
			} else if (myID.compareTo(newId) > 0) {
				// id atual maior que id novo
				for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
					BigInteger chave = entry.getKey();
					String valor = entry.getValue();
					if (chave.compareTo(myID) <= 0) {
						// somente chaves menores ou iguais que myID ficarao no no atual
						manter.put(chave, valor);
					} else {
						transferir.put(chave, valor);
					}
				}
			} else {
				// improvavel caso do id ser igual
				throw new RuntimeException("improvavel caso do id ser exatamente igual a um existente");
			}

		} else if (myID.compareTo(this.predecessorID) < 0) {

			// corner case: no atual eh o menor do DHT, predecessor eh o maior do DHT
			if (this.predecessorID.compareTo(newId) < 0) {
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
			} else {
				// caso nao seja o destinatario, deve encaminhar para o sucessor (3.2)
				return this.sucessor.join(newStub, newId);
			}

		} else if (newId.compareTo(myID) < 0) {

			if (this.predecessorID.compareTo(newId) < 0) {
				// caso usual: predecessorID < newId < myID
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

		} else {

			// caso nao seja o destinatario, deve encaminhar para o sucessor (3.2)
			return this.sucessor.join(newStub, newId);

		}

		// atualiza o predecessor para o novo no
		this.predecessor = newStub;
		this.predecessorID = newId;

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
		this.predecessorID = predecessor.getNode().getMyid();
		this.sucessorID = sucessor.getNode().getMyid();

		return this.predecessor.new_node(this.myStub);
	}

	@Override
	public boolean new_node(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo sucessor
		this.sucessor = newStub;
		this.sucessorID = newStub.getNode().getMyid();
		return true;
	}

	@Override
	public boolean leave(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo predecessor
		this.predecessor = newStub;
		this.predecessorID = newStub.getNode().getMyid();
		return true;
	}

	@Override
	public boolean node_gone(Protocol newStub) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo sucessor
		this.sucessor = newStub;
		this.sucessorID = newStub.getNode().getMyid();
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
