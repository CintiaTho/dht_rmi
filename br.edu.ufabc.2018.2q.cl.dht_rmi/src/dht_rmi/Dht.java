/**
 * Implementacao de um sistema DHT.
 */

/**
 * @author Cintia Lumi Tho - RA 1103514
 * @author Luiz Felipe M. Garcia - RA 11028613
 */

package dht_rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import classes.Node;
import classes.NodeImpl;
import classes.Protocol;
import classes.ProtocolImpl;

public class Dht {

	public static void main(String[] args) {
		//Criacao de variaveis de trabalho dos metodos dessa classe
		Protocol protocol = null;
		boolean joined = false;

		ArrayList<Protocol> stubList = new ArrayList<Protocol>();
		String file = "./src/dht_rmi/nodes_list.txt";
		File nodesFile = new File(file);
		String algoritmoHash = "MD5";

		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text;
		
		//texto inicial explicativo sobre o funcionamento da interface
		System.out.println("Digite: 'commands' para visualisar a lista de possiveis comandos permitidos ao usuario.");
		System.out.println("Caso ja os conheca, digite a primeira acao que deseja realizar e aperte enter...");
		try {
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				if(!joined) System.out.println("Nao faz parte de uma DHT.");
				else {
					System.out.println("Node: " + protocol.getMyName() + " / Id: " + protocol.getNode().getMyId());
				}
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();

				//switch-case dos comandos possiveis
				switch (comando){
				case "commands":
					if(!joined) {
						System.out.println("Gostaria de: ");
						System.out.println("join - Participar de uma DHT;");
						System.out.println("quit - Terminar esta sessao;");
					}
					else {
						System.out.println("Gostaria de: ");
						System.out.println("leave - Sair da DHT (voce mantem seu ID);");
						System.out.println("store - Guardar um item na DHT;");
						System.out.println("retrieve - buscar um item na DHT;");
						System.out.println("delete - apagar um item na DHT;");
						System.out.println("viewItens - visualizar os Itens sob sua responsabilidade;");
						System.out.println("viewDHT - visualizar a topologia da DHT (nodes e conexoes);");
						System.out.println("quit - Terminar esta sessao.");
					}
					break;
					//-------------------------------------------------
				case "join":
					//Usando um Arquivo txt local compartilhado apenas para nossos testes, em uma situacao "real" este arquivo seria disponibilizado "online" para quem quer entrar na rede;
					//Quando um no entrar na rede, ele grava a si proprio, e quando sair da rede nao apaga a si proprio do arquivo (na situacao "real", ele gravaria o antecessor e o sucessor para tentar manter a lista)
					if(!joined) {
						System.out.print("Quer realmente realizar esta acao? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//Cria o no (cria o ID e Stub) se n�o criado
							if(protocol==null) protocol = criarNodeDHT(protocol, algoritmoHash);
							
							//Busca os registros Stub presentes no arquivo
							stubList = leituraStubTxt(nodesFile);
							
							System.out.println("Procurando e tentando entrar em uma DHT...");
							//Unico erro a ser tratado: ao tentar entrar na rede (verificando um por um os nos da lista e caso um esteja desativado - retira da lista
							//Faz isso ate achar um stub disponivel ou ate ser necessario criar uma nova rede;
							if(!stubList.isEmpty()) {
								Boolean teste = false;
								ArrayList<Protocol> remover = new ArrayList<>();

								for(Protocol node: stubList) {
									try{
										//tentativa de entrar na rede comecando pelo node da lista mais antigo para o mais novo
										teste = node.join(protocol.getMyStub(), protocol.getNode().getMyId());
									} catch (ConnectException e) {
										//caso inativo - node indisponivel
										remover.add(node);
									}
									if(teste) break;
								}
								
								if(!remover.isEmpty()) {
									//remove os stubs conhecidamente inativos
									for(Protocol node: remover) {
										stubList.remove(node);
										System.err.println("Node indispon�vel removido: " + node.toString());
									}
								}
							}
							
							//Caso a lista esteja ou fique vazia (nao conseguiu se conectar com otros nodes) iniciar uma nova DHT
							if(stubList.isEmpty()) {
								protocol.join(protocol.getMyStub(), protocol.getNode().getMyId());
							}

							//insere o node novo na lista de stubs e atualiza o arquivo TXT
							joined = true;
							stubList.add(protocol.getMyStub());
							//Gravando as alteracoes no arquivo de Stubs
							if(gravarStubTxt(stubList, nodesFile)) System.out.println("Grava��o/Atualiza��o terminada!");
							else System.out.println("Nao foi possivel gravar");
							//-------
						} else if(text.equals("n")) System.out.println("Operacao cancelada!");
						else System.out.println("Comando invalido, operacao cancelada!");
					} else System.out.println("Comando invalido, voce ja pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "leave":
					if(joined) {
						System.out.print("Quer realmente realizar esta acao? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							protocol.begin_to_leave();
							System.out.print("Tirando seu registro na Lista de Stubs...");
							leituraStubTxt(nodesFile);
							stubList.remove(protocol.getMyStub());
							//Gravando as alteracoes no arquivo de Stubs
							if(gravarStubTxt(stubList, nodesFile)) System.out.println("Grava��o/Atualiza��o terminada!");
							else System.out.println("Nao foi possivel gravar");
							//Atualizando a situa��o do node local -> Fora de uma DHT
							joined = false;
							//-------
						} else if(text.equals("n")) System.out.println("Operacao cancelada!");
						else System.out.println("Comando invalido, operacao cancelada!");
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "store":
					if(joined) {
						System.out.print("Quer realmente realizar esta acao? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							System.out.print("Informe a chave que sera utilizada para recuperar o item posteriormente: ");
							text = entrada.nextLine();
							String keyText = text;
							BigInteger key = gerarID(keyText, algoritmoHash);
							System.out.print("Informe o valor que sera atribuido a chave previamente informada: ");
							text = entrada.nextLine();
							String value = text;
							
							//Comeca a procura
							LinkedHashMap<BigInteger, String> view = new LinkedHashMap<>(); 
							protocol.store(key, value, protocol.getMyStub());
							//Espera o fim da execucao
							while(protocol.getView().isEmpty()) {
							}
							protocol.setView(new LinkedHashMap<>());
							//-------
						} else if(text.equals("n")) System.out.println("Operacao cancelada!");
						else System.out.println("Comando invalido, operacao cancelada!");
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "retrieve":
					if(joined) {
						//-------
						System.out.print("Informe a chave necessaria para recuperar o item: ");
						text = entrada.nextLine();
						String keyText = text;
						BigInteger key = gerarID(keyText, algoritmoHash);
						
						//Comeca a procura
						LinkedHashMap<BigInteger, String> view = new LinkedHashMap<>(); 
						protocol.retrieve(key, protocol.getMyStub());
						//Espera o fim da execucao
						while(protocol.getView().isEmpty()) {
						}
						//Imprime o retorno
						for (HashMap.Entry<BigInteger, String> it : protocol.getView().entrySet()){
							if(!it.getValue().equals("")) System.out.println("Key : "+it.getKey()+" / Value: "+it.getValue());
						}
						protocol.setView(new LinkedHashMap<>());
						//-------
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "delete":
					if(joined) {
						System.out.print("Quer realmente realizar esta acao? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							System.out.print("Informe a chave necessaria para encontrar o item: ");
							text = entrada.nextLine();
							String keyText = text;
							BigInteger key = gerarID(keyText, algoritmoHash);
							
							//Comeca a procura
							LinkedHashMap<BigInteger, String> view = new LinkedHashMap<>(); 
							protocol.delete(key, protocol.getMyStub());
							//Espera o fim da execucao 
							while(protocol.getView().isEmpty()) {
							}
							protocol.setView(new LinkedHashMap<>());
							//-------
						} else if(text.equals("n")) System.out.println("Operacao cancelada!");
						else System.out.println("Comando invalido, operacao cancelada!");
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "viewItens":
					if(joined) {
						//-------
						if(protocol.getNode().getTexts().isEmpty()) System.out.println("N�o h� Itens no seu Node.");
						else {
							int count = 1;
							for (HashMap.Entry<BigInteger, String> it : protocol.getNode().getTexts().entrySet()){  
								System.out.println("Key "+count+": "+it.getKey()+" / Valor: "+it.getValue());
								count++;
							}
							System.out.println();
						}
						//-------
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "viewDHT":
					if(joined) {
						System.out.print("Quer realmente realizar esta acao? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							System.out.print("Estamos obtendo os dados...");
							LinkedHashMap<BigInteger, String> view = new LinkedHashMap<>(); 
							protocol.view(view);
							while(protocol.getView().isEmpty()) {
							}
							System.out.println("Resultado:");
							System.out.println(".----.");
							for (HashMap.Entry<BigInteger, String> it : protocol.getView().entrySet()){
								System.out.println(":    V");
								System.out.println(": NodeNome : "+it.getValue()+" / Id: "+it.getKey());
								System.out.println(":    :");
							}
							System.out.println("`----�");
							protocol.setView(new LinkedHashMap<>());
							//-------
						} else if(text.equals("n")) System.out.println("Operacao cancelada!");
						else System.out.println("Comando invalido, operacao cancelada!");
					} else System.out.println("Comando invalido, voce nao pertence a uma DHT!");
					break;
					//-------------------------------------------------
				case "quit":
					System.out.println("Seus dados serao perdidos e sua posicao atual na DHT!");
					System.out.print("Deseja realmente terminar sua sessao?(s/n) ");
					text = entrada.nextLine();
					if(text.equals("s")){
						//-------
						if(joined) protocol.begin_to_leave();
						System.out.println("Sessao Finalizada!");
						System.exit(0);
						//-------
					} else if(text.equals("n")) System.out.println("Operacao cancelada!");
					else System.out.println("Comando invalido, operacao cancelada!");
					break;
					//-------------------------------------------------
				default:
					System.out.println("Este nao e um comando valido!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}

	//Metodo usado para criar o no atrelado a aplicacao instanciada (rodando localmente)
	public static Protocol criarNodeDHT(Protocol protocol, String algoritmoHash) {
		Node node = new NodeImpl();
		protocol = new ProtocolImpl(node);
		try {
			//criar o Stub
			System.out.print("Criando o Stub...");
			protocol.setMyStub((Protocol)UnicastRemoteObject.exportObject(protocol, 0));
			String myStub = protocol.getMyStub().toString();
			//criar o ID
			BigInteger id = gerarID(myStub,algoritmoHash);
			protocol.getNode().setMyId(id);
			//criar o Nome do Node (apenas demonstrativo)
			System.out.print("Criando o Nome...");
			protocol.setMyName(myStub.substring(77, 96));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println("Node criado!");
		return protocol;
	}
	
	//Metodo para gerar os IDs seguramente criptografados - tanto para os nos quanto para os itens
	public static BigInteger gerarID(String frase, String algoritmoHash) {
		System.out.print("Gerando o ID...");
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmoHash);
			md.update(frase.getBytes());
			return new BigInteger(md.digest());
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	//Metodo que efetua a atualizacao do arquivo TXT
	public static boolean gravarStubTxt(ArrayList<Protocol> stubList, File nodesFile) {
		System.out.print("Gravando/Atualizando o arquivo de Stubs...");
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(nodesFile));
			out.writeObject(stubList);
			out.flush();
			out.close();
			out = null;
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	//Metodo que le e copia todos os itens armazenados no TXT
	@SuppressWarnings("unchecked")
	public static ArrayList<Protocol> leituraStubTxt(File nodesFile) throws IOException {
		System.out.print("Lendo arquivo de Stubs...");
		ObjectInputStream in = null;
		ArrayList<Protocol> objetos = new ArrayList<Protocol>();
		try {
			in = new ObjectInputStream(new FileInputStream(nodesFile));
			objetos = (ArrayList<Protocol>) in.readObject();
			in.close();
			in = null;
		} catch (IOException ex) {
			//ex.printStackTrace();
			System.err.print("<Arquivo de Stubs vazio: "+ex.toString()+">");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(" Leitura finalizada!");
		return objetos;
	}

}
