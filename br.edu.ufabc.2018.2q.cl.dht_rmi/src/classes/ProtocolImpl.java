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
	private HashMap<BigInteger, String> view;

	public ProtocolImpl(Node node) {
		this.node = node;
		this.myName = "";
		this.myStub = null;
		this.predecessor = null;
		this.sucessor = null;
		this.view = new HashMap<>();
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

	public void setMyStub(Protocol stub) {
		this.myStub = stub;
	}

	public Node getNode() {
		return this.node;
	}

	public void setNode(Node node) {
		this.node = node;
	}
	
	public HashMap<BigInteger, String> getView() {
		return view;
	}

	public void setView(HashMap<BigInteger, String> view) {
		this.view = view;
	}

	public boolean join(Protocol newStub, BigInteger newId) throws RemoteException {
		// Trata corner cases primeiro

		HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
		HashMap<BigInteger, String> manter = new HashMap<BigInteger, String>();
		HashMap<BigInteger, String> transferir = new HashMap<BigInteger, String>();

		if (this.myStub == newStub && this.getNode().getMyId().compareTo(newId) == 0) {

			// corner case: primeiro no do DHT
			System.out.println("Voce e o primeiro node na rede - Criada uma nova DHT!");
			this.sucessor = this.myStub;
			this.getNode().setNextId(this.getNode().getMyId());
			//newStub.join_ok(this.getMyStub(), this.getMyStub(), this.getNode().getMyId(), this.getNode().getMyId());

		} else if (this.getNode().getMyId().compareTo(this.getNode().getPrevId()) <= 0 && this.getNode().getPrevId().compareTo(newId) < 0) {

			// corner case: node atual eh o menor do DHT, predecessor eh o maior do DHT
			// corner case: newStub eh maior que o maior do DHT
			// como o node atual eh o menor do DHT e eh quem envia o transfer, deve conter as entradas maiores que o maior do DHT
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

	public boolean join_ok(Protocol predecessor, Protocol sucessor, BigInteger prevId, BigInteger nextId) throws RemoteException {
		// atualiza o node atual, que eh novo, com o predecessor e sucessor
		this.predecessor = predecessor;
		this.sucessor = sucessor;
		this.getNode().setPrevId(prevId);
		this.getNode().setNextId(nextId);
		System.out.print("Sua entrada foi aceita e seu lugar encontrado...");
		return this.predecessor.new_node(this.myStub, this.getNode().getMyId());
	}

	@Override
	public boolean new_node(Protocol newStub, BigInteger newId) throws RemoteException {
		// atualiza o node atual, que ja esta na DHT, com o novo sucessor
		this.sucessor = newStub;
		this.getNode().setNextId(newId);
		System.out.println("Informado seu Antecessor sobre sua presenca!");
		return true;
	}
	
	public void begin_to_leave() throws RemoteException {
		this.sucessor.leave(this.predecessor, this.getNode().getPrevId());
		this.predecessor.node_gone(this.sucessor, this.getNode().getNextId());

		HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
		for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
			BigInteger chave = entry.getKey();
			String valor = entry.getValue();
			this.sucessor.transfer(chave, valor);
		}
	}

	@Override
	public boolean leave(Protocol predecessor, BigInteger prevId) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo predecessor
		System.out.print("Informado seu Sucessor sobre sua saida...");
		this.predecessor = predecessor;
		this.getNode().setPrevId(predecessor.getNode().getMyId());
		return true;
	}

	@Override
	public boolean node_gone(Protocol sucessor, BigInteger nextId) throws RemoteException {
		// atualiza o no atual, que ja esta na DHT, com o novo sucessor
		System.out.print("Informado seu Antecessor sobre sua saida...");
		this.sucessor = sucessor;
		this.getNode().setNextId(sucessor.getNode().getMyId());
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
	public void delete(BigInteger key) throws RemoteException{

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
	
	public boolean view(HashMap<BigInteger, String> view) throws RemoteException {
		if(this.predecessor.equals(this.myStub) & this.sucessor.equals(this.myStub)) {
			System.out.println("Somente ha voce na DHT.");
			view.put(this.getNode().getMyId(), this.getMyName());
			this.setView(view);
		}
		else if(view.containsKey(this.getNode().getMyId())) {
			System.out.println("View Finalizada!");
			view.put(this.getNode().getMyId(), this.getMyName());
			this.setView(view);
		}
		else {
		System.out.print("Adicionando Node na View...");
		view.put(this.getNode().getMyId(), this.getMyName());
		}
		return true;
	}
}
