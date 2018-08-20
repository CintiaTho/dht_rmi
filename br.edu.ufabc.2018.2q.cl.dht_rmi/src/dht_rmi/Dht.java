package dht_rmi;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	static int falsoID = -1;
	static String texto = "";
	static byte[] id = null;
	static Node node = null;
	static Protocol protocol = null;
	static Protocol stub = null;
	static Protocol next_stub = null;
	static Protocol ant_stub = null;
	static List<Protocol> stubList = null;

	public static void main(String[] args) {
		Scanner entrada = new Scanner(System.in);
		String comando = "";
		String text;

		//texto inicial explicativo sobre o funcionamento da interface
		System.out.println("Digite: 'commands' para visualisar a lista de possíveis comandos permitidos ao usuário.");
		System.out.println("Caso já os conheça, digite a primeira ação que deseja realizar e aperte enter...");

		//loop principal para receber os comandos do usuario e mostrar informacoes
		while (comando != "quit") {
			try {	
				//Sempre mantendo atualizado o tamanho da DHT
				leituraStubTxt();

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
					if(falsoID>=0) {
						System.out.print("Quer realmente realizar esta ação? (s/n) ");
						text = entrada.nextLine();
						if(text.equals("s")){
							criarNodeDHT();
							falsoID = stubList.size()+1;
							Protocol primNode = stubList.get(0);
						} else if(text.equals("n")) System.out.println("Operação cancelada!");
						else System.out.println("Comando inválido, operação cancelada!");
					} else System.out.println("Comando inválido, você já pertence à uma DHT!");
					break;
				//-------------------------------------------------
				case "leave":

					break;
				//-------------------------------------------------
				case "store":

					break;	
				//-------------------------------------------------
				case "retrieve":

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
			} catch (QuitException e) {
				System.out.println("Encerrada sua sessão com sucesso!");
			} catch (ConnectException e) {
				System.err.println("Problemas com o Registry: " + e.toString());
			} catch (Exception e) {
				System.err.println("Ocorreu um erro no servidor: " + e.toString());
			}
		}
	}

	public static void criarNodeDHT() {
		texto = gerarStringRandom();
		id = gerarHash(texto);
		node = new NodeImpl(id);
		protocol = new ProtocolImpl(node);
		stub = gerarStub(protocol);
		gravarStubTxt(stub);
	}

	public static Protocol gerarStub(Protocol protocol) {
		Protocol stub = null;
		try {
			stub = (Protocol)UnicastRemoteObject.exportObject(protocol, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stub;
	}

	public static void gravarStubTxt(Protocol stub) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("nodes_list.txt", true));
			out.writeObject(stub);
			out.flush();
			out.close();
		} catch (IOException ex) {
			System.out.println("Não foi possível gravar");
		}
	}

	public static void leituraStubTxt() throws IOException {
		ObjectInputStream in = null;
		boolean oef = false;
		Protocol umObjeto = null;
		in = new ObjectInputStream(new FileInputStream("nodes_list.txt"));
		while (oef == false) {
			try {
				umObjeto = (Protocol) in.readObject();
				stubList.add(umObjeto);           
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
		String algoritmoHash = "MD5";
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
