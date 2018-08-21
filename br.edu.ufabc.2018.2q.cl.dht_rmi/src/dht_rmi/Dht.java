package dht_rmi;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import classes.Node;
import classes.NodeImpl;
import classes.Protocol;
import classes.ProtocolImpl;
import classes.QuitException;

public class Dht {
	//Criacao de variaveis de trabalho dos metodos
	static int falsoID;
	static String texto = "";
	static File nodesFile = new File("./src/dht_rmi/nodes_list.txt");
	static String algoritmoHash = "MD5";
	static byte[] id = null;
	static Node node = null;
	static Protocol protocol = null;
	static Protocol stub = null;
	static Protocol next_stub = null;
	static Protocol ant_stub = null;
	static ArrayList<Protocol> stubList = null;

	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text;
		falsoID = -1;
		//texto inicial explicativo sobre o funcionamento da interface
		System.out.println("Digite: 'commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
		System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");
		try {	
			//loop principal para receber os comandos do usuario e mostrar informacoes
			while (comando != "quit") {

				//Sempre mantendo atualizado o tamanho da DHT para fins demonstrativos
				//if(falsoID >= 0) 
				try {
					leituraStubTxt();
				}  catch (EOFException  eofException) {
				}

				//informacoes que sempre aparecerao ao usuario
				System.out.println();
				System.out.print("Comando: ");
				comando = entrada.nextLine();
				System.out.println();

				//switch-case dos comandos possiveis
				switch (comando){
				case "commands":
					if(falsoID < 0) {
						System.out.println("Não faz parte de uma DHT.");
						System.out.println("Gostaria de: ");
						System.out.println("join - Participar de uma DHT;");
						System.out.println("quit - Terminar esta sessão;");
					}
					else {
						System.out.println("Nó: " + falsoID);
						System.out.println("Existe(m): "+ stubList.size() + " Nós na DHT na qual você participa.");
						System.out.println("Gostaria de: ");
						System.out.println("leave - Sair da DHT;");
						System.out.println("store - Guardar um nome na DHT;");
						System.out.println("retrieve - buscar um nome na DHT;");
						System.out.println("quit - Terminar esta sessão.");
					}
					break;
					//-------------------------------------------------
				case "join":
					//Vamos fazer tudo usando um aArquivo txt local apenas para nossos testes, em uma situação "real" este arquivo seria disponibilizado para quem quer entrar na rede;
					//Quando um nó entrar na rede, ele pode gravar a si próprio, e quando sair da rede, apagar a si próprio do arquivo (na situação "real", ele gravaria o antecessor e o sucessor para tentar manter a lista)  
					if(falsoID<0) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							criarNodeDHT();
							leituraStubTxt();
							falsoID = stubList.size()+1;
							if(falsoID==1) {
								System.out.println("Você é o primeiro nó na rede - Criada uma nova DHT!");	
							}
							//Unico erro a ser tratado será de tentar entrar na rede (um nó da lista) e descobrir que eles estão desativados...até ser necessário criar uma nova rede;
							else {
								Protocol primNode = stubList.get(0);
							}
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você já pertence à uma DHT!");
					break;
					//-------------------------------------------------
				case "leave":
					if(falsoID>=0) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;
					//-------------------------------------------------
				case "store":
					if(falsoID>=0) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você não pertence à uma DHT!");
					break;	
					//-------------------------------------------------
				case "retrieve":
					if(falsoID>=0) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							
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

						throw new QuitException();
					}
					else if(text.equals("n")) System.out.println("Operação cancelada!");
					else System.out.println("Comando inválido, operação cancelada!");
					//-------------------------------------------------
				default:
					System.out.println("Este não é um comando válido!");
				}
			}	
		} catch (QuitException e) {
			System.out.println("Encerrada sua sessão com sucesso!");
		} catch (Exception e) {
			System.err.println("Ocorreu um erro no servidor: " + e.toString());
		}
	}

	public static void criarNodeDHT() {
		texto = gerarStringRandom();
		id = gerarHash(texto);
		node = new NodeImpl(id);
		protocol = new ProtocolImpl(node);
		stub = gerarStub(protocol);
		stubList.add(stub);
		gravarStubTxt(stubList);
	}

	public static Protocol gerarStub(Protocol protocol) {
		Protocol stub = null;
		try {
			stub = (Protocol)UnicastRemoteObject.exportObject(protocol, 0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return stub;
	}

	public static void gravarStubTxt(List<Protocol> stubList) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nodesFile)));
			out.writeObject(stubList);
			out.flush();
			out.close();
		} catch (IOException ex) {
			System.out.println("Não foi possível gravar");
		}
	}

	public static void leituraStubTxt() throws IOException {
		ObjectInputStream in = null;
		boolean oef = false;
		ArrayList<Protocol> objetos = null;
		in = new ObjectInputStream(new FileInputStream(nodesFile));
		while (oef == false) {
			try {
				objetos = (ArrayList<Protocol>) in.readObject();
				if(!objetos.equals(null)) stubList = objetos;
			}  catch (EOFException  eofException) {
				in.close();
				break;
			} catch (Exception e) {
				System.out.println("Não foi possível ler o Arquivo com os Nós.");
				in.close();
				break;
			} 
		}
		in.close();
	}

	public static String gerarStringRandom() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	//Retirado de<http://codare.aurelio.net/2007/02/02/java-gerando-codigos-hash-md5-sha/>
	public static byte[] gerarHash(String frase) {
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmoHash);
			md.update(frase.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static int byteToInt(byte[] id) {
		return id.hashCode();
	}

	//Retirado de<http://codare.aurelio.net/2007/02/02/java-gerando-codigos-hash-md5-sha/>
	public static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0) s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}

}
