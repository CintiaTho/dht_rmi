package dht_rmi;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

import classes.Node;
import classes.NodeImpl;
import classes.Protocol;
import classes.ProtocolImpl;

public class Dht {
	static String texto;
	static byte[] id;
	static Node node;
	static Protocol protocol;
	static Protocol stub;
	static Protocol next_stub;
	static Protocol ant_stub;
	
	public static void main(String[] args) {
			
	}
	
	public static void criarNodeDHT() {
		texto = gerarStringRandom();
		id = gerarHash(texto);
		node = new NodeImpl(id);
		protocol = new ProtocolImpl(node);
		stub = gerarStub(protocol);
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
	
	public static void leitura() throws IOException {
        ObjectInputStream in = null;
        boolean oef = false;
        Protocol umObjeto = null;
        List<Protocol> umList = null;
        in = new ObjectInputStream(new FileInputStream("Lista.txt"));
         while (oef == false) {
            try {
                umObjeto = (Protocol) in.readObject();
                System.out.println("Leu");
				umList.add(umObjeto);
                System.out.println("Adicionou");            
            }  catch (EOFException  eofException) {
                 System.out.println("Terminou de ler");
                 in.close();
                 break;
            } catch (Exception e) {
                 System.out.println("Não foi possível ler");
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
