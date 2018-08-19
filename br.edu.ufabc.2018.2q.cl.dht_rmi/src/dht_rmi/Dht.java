package dht_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import classes.Node;
import classes.NodeImpl;
import classes.Protocol;
import classes.ProtocolImpl;

public class Dht {	
	public static void main(String[] args) {		
		String texto = gerarStringRandom();
		byte[] id = gerarHash(texto);
		Node node = new NodeImpl();
		Protocol protocol = new ProtocolImpl();
		Protocol stub = gerarStub(protocol);
		System.out.println("texto = "+texto);
		System.out.println("id = "+id);
		System.out.println("stringHexa = "+stringHexa(id));
		System.out.println(".hashCode() = "+id.hashCode());
		System.out.println("stub = "+stub);
		System.out.println();	
	}
	
	private static Protocol gerarStub(Protocol protocol) {
		Protocol stub = null;
		try {
			stub = (Protocol)UnicastRemoteObject.exportObject(protocol, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stub;
	}
	
	private static String gerarStringRandom() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
	//Retirado os dois proximos métodos  <http://codare.aurelio.net/2007/02/02/java-gerando-codigos-hash-md5-sha/>
	private static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0) s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}

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

}
