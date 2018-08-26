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
import java.util.LinkedHashMap;

public class ProtocolImpl implements Protocol {
	private String myName;
	private Node node;
	private Protocol myStub;
	private Protocol sucessor;
	private Protocol predecessor;
	private LinkedHashMap<BigInteger, String> view;

	public ProtocolImpl(Node node) {
		this.node = node;
		this.myName = "";
		this.myStub = null;
		this.predecessor = null;
		this.sucessor = null;
		this.view = new LinkedHashMap<>();
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

	public LinkedHashMap<BigInteger,String> getView() {
		return view;
	}

	public void setView(LinkedHashMap<BigInteger, String> view) {
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
			this.sucessor = newStub;
			this.predecessor = newStub;
			this.getNode().setPrevId(newId);
			this.getNode().setNextId(newId);
			//newStub.join_ok(this.getMyStub(), this.getMyStub(), this.getNode().getMyId(), this.getNode().getMyId());
			return true;
		} 

		else if(this.getNode().getMyId().compareTo(this.getNode().getPrevId()) <= 0 && this.getNode().getPrevId().compareTo(newId) < 0) {
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

		} 

		else if (this.getNode().getPrevId().compareTo(newId) < 0 && newId.compareTo(this.getNode().getMyId()) < 0) {
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

		//Avisa o node ingressante sobre sua entrada
		newStub.join_ok(this.predecessor, this.myStub, this.getNode().getPrevId(), this.getNode().getMyId());

		// atualiza o predecessor para o novo node
		this.predecessor = newStub;
		this.getNode().setPrevId(newId);

		// efetua transferencia para o novo node
		for (HashMap.Entry<BigInteger, String> entry: transferir.entrySet()) {
			BigInteger chave = entry.getKey();
			String valor = entry.getValue();
			newStub.transfer(chave, valor);
		}

		// ajusta entradas a manter
		this.getNode().setTexts(manter);

		return true;
	}
	
	//Atualiza o node atual, que eh novo, com o predecessor e sucessor
	public boolean join_ok(Protocol predecessor, Protocol sucessor, BigInteger prevId, BigInteger nextId) throws RemoteException {
		this.predecessor = predecessor;
		this.sucessor = sucessor;
		this.getNode().setPrevId(prevId);
		this.getNode().setNextId(nextId);
		System.out.print("Sua entrada foi aceita e seu lugar na DHT encontrado...");
		if(this.predecessor.new_node(this.myStub, this.getNode().getMyId())) System.out.println("Informado seu Antecessor sobre sua presenca!");
		return true;
	}

	//Atualiza o node atual, que ja esta na DHT, com o novo sucessor que esta entrando na DHT
	public boolean new_node(Protocol newStub, BigInteger newId) throws RemoteException {
		this.sucessor = newStub;
		this.getNode().setNextId(newId);
		return true;
	}
	
	//Informando seus Vizinhos sobre sua saida e transferindo os Itens sob seu Node para seu Sucessor
	public void begin_to_leave() throws RemoteException {
		if(this.predecessor.equals(this.myStub) & this.sucessor.equals(this.myStub)) {
			System.out.println("Somente ha voce na DHT e todos os itens serão perdidos.");
		}
		else {
			if(this.sucessor.leave(this.predecessor, this.getNode().getPrevId())) System.out.print("Informado seu Sucessor sobre sua saida...");
			if(this.predecessor.node_gone(this.sucessor, this.getNode().getNextId())) System.out.print("Informado seu Antecessor sobre sua saida...");

			System.out.print("Transferindo os Itens para o seu Sucessor...");
			HashMap<BigInteger, String> entradasAntigas = this.getNode().getTexts();
			for (HashMap.Entry<BigInteger, String> entry: entradasAntigas.entrySet()) {
				BigInteger chave = entry.getKey();
				String valor = entry.getValue();
				if(this.sucessor.transfer(chave, valor)) System.out.print("Item...");
			}
		}
		System.out.println("Saida efetuada!");
	}

	//Atualiza o node atual, que ja esta na DHT, com um novo predecessor que era do antigo node que esta saindo
	public boolean leave(Protocol predecessor, BigInteger prevId) throws RemoteException {
		this.predecessor = predecessor;
		this.getNode().setPrevId(predecessor.getNode().getMyId());
		return true;
	}

	//Atualiza o node atual, que ja esta na DHT, com o novo sucessor que era do antigo node que esta saindo
	public boolean node_gone(Protocol sucessor, BigInteger nextId) throws RemoteException {
		this.sucessor = sucessor;
		this.getNode().setNextId(sucessor.getNode().getMyId());
		return true;
	}

	//Recebendo um item que era do antigo node predecessor que esta saindo
	public boolean transfer(BigInteger key, String value) throws RemoteException {
		this.node.insertText(key, value);
		return true;
	}

	//Buscando e gravando um item novo na DHT
	public boolean store(BigInteger key, String value) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	//Buscando e devolvendo a quem solicitou um item na DHT
	public boolean retrieve(BigInteger key) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	//Recebendo a confirmacao do Item achado na DHT
	public boolean ok() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	//Recebendo a informacao da inexistencia do Item procurado na DHT
	public boolean not_found() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	//Buscando e deletando um item na DHT
	public void delete(BigInteger key) throws RemoteException{

	}

	//Recebendo a confirmacao do Item achado e deletado na DHT
	public boolean okDel() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	//Buscando a organização dos nodes na DHT
	public boolean view(LinkedHashMap<BigInteger, String> view) throws RemoteException {
		//Caso em que ha apenas voce na DHT
		if(this.predecessor.equals(this.myStub) && this.sucessor.equals(this.myStub)) {
			System.out.print("Somente ha voce na DHT...");
			view.put(this.getNode().getMyId(), this.getMyName());
			this.setView(view);
		}
		//Quando deu-se uma volta completa na DHT capturando as infos de todos os nodes e volta pra voce
		else if(view.containsKey(this.getNode().getMyId())) {
			System.out.print("View Finalizada...");
			this.setView(view);
		}
		//Quando nao deu-se uma volta completa na DHT ainda, adiciona suas infos
		else {
			view.put(this.getNode().getMyId(), this.getMyName());
			this.sucessor.view(view);
		}
		return true;
	}
}
