package rmi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Dht {
	String algoritmoHash = "MD5";
	
	public byte[] gerarHash(String frase, String algoritmoHash) {
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmoHash);
			md.update(frase.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		String myRandom = uuid.toString();
		System.out.println(myRandom.substring(0,20));	

	}

}
