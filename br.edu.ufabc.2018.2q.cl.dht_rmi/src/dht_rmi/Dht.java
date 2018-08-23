package dht_rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnknownHostException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import classes.Node;
import classes.NodeImpl;
import classes.Protocol;
import classes.ProtocolImpl;

public class Dht {

	public static void main(String[] args) {
		//Criacao de variaveis de trabalho dos metodos dessa classe
		Protocol protocol = null;
		ArrayList<Protocol> stubList = new ArrayList<>();
		File nodesFile = new File("./src/dht_rmi/nodes_list.txt");
		String algoritmoHash = "MD5";
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text;

		//texto inicial explicativo sobre o funcionamento da interface
		System.out.println("Digite: 'commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
		System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");
		try {	
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				if(protocol == null) System.out.println("Não faz parte de uma DHT.");
				else {
					System.out.println("Nó: " + protocol.getFalsoID());
				}
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();

				//switch-case dos comandos possiveis
				switch (comando){
				case "commands":
					if(protocol == null) {
						System.out.println("Gostaria de: ");
						System.out.println("join - Participar de uma DHT;");
						System.out.println("quit - Terminar esta sessão;");
					}
					else {
						System.out.println("Gostaria de: ");
						System.out.println("leave - Sair da DHT;");
						System.out.println("store - Guardar um item na DHT;");
						System.out.println("retrieve - buscar um item na DHT;");
						System.out.println("delete - apagar um item na DHT;");
						System.out.println("viewDHT - visualizar a DHT;");
						System.out.println("quit - Terminar esta sessão.");
					}
					break;
					//-------------------------------------------------
				case "join":
					//Usando um Arquivo txt local apenas para nossos testes, em uma situação "real" este arquivo seria disponibilizado para quem quer entrar na rede;
					//Quando um nó entrar na rede, ele pode gravar a si próprio, e quando sair da rede, apagar a si próprio do arquivo (na situação "real", ele gravaria o antecessor e o sucessor para tentar manter a lista)  
					if(protocol==null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							protocol = criarNodeDHT(protocol, algoritmoHash);
							stubList = leituraStubTxt(nodesFile);
							//Unico erro a ser tratado será de tentar entrar na rede (um nó da lista) e descobrir que eles estão desativados...até ser necessário criar uma nova rede;
							if(!stubList.isEmpty()) {
								ArrayList<Protocol> remover = new ArrayList<>();
								Boolean teste = false;
								for(Protocol node: stubList) {
									if(!teste) {
										try{
											teste = node.join();
										}catch (ConnectException e) {
											remover.add(node);
										}
									}
								}
								
								for(Protocol node: remover) {
									stubList.remove(node);
									System.err.println("Nó removido: " + node.toString());
								}
							}
							
							if(stubList.isEmpty()) {
								System.out.println("Você é o primeiro nó na rede - Criada uma nova DHT!");	
							}
							stubList.add(protocol.getStub());
							gravarStubTxt(stubList, nodesFile);
							//-------		
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você já pertence à uma DHT!");
					break;
					//-------------------------------------------------
				case "leave":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							protocol.leave();
							//-------	
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;
					//-------------------------------------------------
				case "store":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							//protocol.store(key, value, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;	
					//-------------------------------------------------
				case "retrieve":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//String value = protocol.retrieve(key, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;	
					//-------------------------------------------------
				case "delete":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//protocol.delete(key, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;	
					//-------------------------------------------------
				case "viewDHT":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//ArrayList view = protocol.view();
							//-------
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;	
					//-------------------------------------------------
				case "quit":
					System.out.println("Seus dados serão perdidos e sua posição atual na DHT!"); 
					System.out.print("Deseja realmente terminar sua sessão?(s/n) ");
					text = entrada.nextLine();
					if(text.equals("s")){
						//-------
						if(protocol != null) protocol.leave();
						System.out.println("Sessão Finalizada!");
						System.exit(0);
						//-------
					} else if(text.equals("n")) System.out.println("Operação cancelada!");
					else System.out.println("Comando inválido, operação cancelada!");
					break;	
					//-------------------------------------------------
				default:
					System.out.println("Este não é um comando válido!");
				}
			}	
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}

	public static Protocol criarNodeDHT(Protocol protocol, String algoritmoHash) {
		Node node = new NodeImpl();
		protocol = new ProtocolImpl(node);
		
		try {
			protocol.setStub((Protocol)UnicastRemoteObject.exportObject(protocol, 0));
			String myStub = protocol.getStub().toString();
			BigInteger id = new BigInteger(gerarHash(myStub,algoritmoHash));
			protocol.getNode().setMyid(id);
			protocol.setFalsoID(myStub.substring(77, 96));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return protocol;
	}

	public static void gravarStubTxt(ArrayList<Protocol> stubList, File nodesFile) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(nodesFile));
			out.writeObject(stubList);
			out.flush();
			out.close();
		} catch (IOException ex) {
			System.out.println("Não foi possível gravar");
		}
	}

	public static ArrayList<Protocol> leituraStubTxt(File nodesFile) throws IOException {
		ObjectInputStream in = null;
		ArrayList<Protocol> objetos = new ArrayList<>();
		try {
			in = new ObjectInputStream(new FileInputStream(nodesFile));
			objetos = (ArrayList<Protocol>) in.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//System.out.println("ok");
		in.close();
		return objetos;
	}

	public static byte[] gerarHash(String frase, String algoritmoHash) {
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmoHash);
			md.update(frase.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
