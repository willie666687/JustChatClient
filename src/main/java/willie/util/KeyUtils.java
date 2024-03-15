package willie.util;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils{
	protected static PrivateKey privateKey;
	public static PublicKey publicKey;
	public static void generateKey(){
		KeyPairGenerator keyPairGenerator;
		try{
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}
	public static String encrypt(String message) throws Exception {
		byte[] publicKeyBytes = publicKey.getEncoded();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		PublicKey publicKey =  keyFactory.generatePublic(publicKeySpec);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
	}
	public static String decrypt(String encryptedMessage) throws Exception {
		byte[] privateKeyBytes = privateKey.getEncoded();
		PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		
		PrivateKey privateKey= keyFactory.generatePrivate(privateKeySpec);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
	}
}
