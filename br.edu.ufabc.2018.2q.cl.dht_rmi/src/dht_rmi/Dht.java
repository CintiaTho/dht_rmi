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
		System.out.println("Digite: 'commands' para visualisar a lista de poss�veis comandos permitidos ao usu�rio.");
		System.out.println("Caso j� os conhe�a, digite a primeira a��o que deseja realizar e aperte enter...");
		try {	
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {
				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				if(protocol == null) System.out.println("N�o faz parte de uma DHT.");
				else {
					System.out.println("N�: " + protocol.getFalsoID());
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
						System.out.println("quit - Terminar esta sess�o;");
					}
					else {
						System.out.println("Gostaria de: ");
						System.out.println("leave - Sair da DHT;");
						System.out.println("store - Guardar um item na DHT;");
						System.out.println("retrieve - buscar um item na DHT;");
						System.out.println("delete - apagar um item na DHT;");
						System.out.println("viewDHT - visualizar a DHT;");
						System.out.println("quit - Terminar esta sess�o.");
					}
					break;
					//-------------------------------------------------
				case "join":
					//Usando um Arquivo txt local apenas para nossos testes, em uma situa��o "real" este arquivo seria disponibilizado para quem quer entrar na rede;
					//Quando um n� entrar na rede, ele pode gravar a si pr�prio, e quando sair da rede, apagar a si pr�prio do arquivo (na situa��o "real", ele gravaria o antecessor e o sucessor para tentar manter a lista)  
					if(protocol==null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							protocol = criarNodeDHT(protocol, algoritmoHash);
							stubList = leituraStubTxt(nodesFile);
							//Unico erro a ser tratado ser� de tentar entrar na rede (um n� da lista) e descobrir que eles est�o desativados...at� ser necess�rio criar uma nova rede;
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
									System.err.println("N� removido: " + node.toString());
								}
							}
							
							if(stubList.isEmpty()) {
								System.out.println("Voc� � o primeiro n� na rede - Criada uma nova DHT!");	
							}
							stubList.add(protocol.getStub());
							gravarStubTxt(stubList, nodesFile);
							//-------		
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� j� pertence � uma DHT!");
					break;
					//-------------------------------------------------
				case "leave":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							protocol.leave();
							//-------	
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� n�o pertence � uma DHT!");
					break;
					//-------------------------------------------------
				case "store":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------	
							//protocol.store(key, value, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� n�o pertence � uma DHT!");
					break;	
					//-------------------------------------------------
				case "retrieve":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//String value = protocol.retrieve(key, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� n�o pertence � uma DHT!");
					break;	
					//-------------------------------------------------
				case "delete":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//protocol.delete(key, hash);
							//-------
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� n�o pertence � uma DHT!");
					break;	
					//-------------------------------------------------
				case "viewDHT":
					if(protocol!=null) {
						System.out.print("Quer realmente realizar esta a��o? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							//-------
							//ArrayList view = protocol.view();
							//-------
						} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
						else System.out.println("Comando inv�lido, opera��o cancelada!");
					} else System.out.println("Comando inv�lido, voc� n�o pertence � uma DHT!");
					break;	
					//-------------------------------------------------
				case "quit":
					System.out.println("Seus dados ser�o perdidos e sua posi��o atual na DHT!"); 
					System.out.print("Deseja realmente terminar sua sess�o?(s/n) ");
					text = entrada.nextLine();
					if(text.equals("s")){
						//-------
						if(protocol != null) protocol.leave();
						System.out.println("Sess�o Finalizada!");
						System.exit(0);
						//-------
					} else if(text.equals("n")) System.out.println("Opera��o cancelada!");
					else System.out.println("Comando inv�lido, opera��o cancelada!");
					break;	
					//-------------------------------------------------
				default:
					System.out.println("Este n�o � um comando v�lido!");
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
			System.out.println("N�o foi poss�vel gravar");
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
