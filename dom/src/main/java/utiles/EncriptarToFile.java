package utiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.LinkedList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class EncriptarToFile {

	private static Cipher encryptCipher;
	private static Cipher decryptCipher;
	private static final byte[] iv = { 11, 22, 33, 44, 99, 88, 77, 66 };

	public EncriptarToFile() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
		
		SecretKey key = KeyGenerator.getInstance("DES").generateKey();
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
		encryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		encryptCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

		// get Cipher instance and initiate in decrypt mode
		decryptCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		decryptCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	}
	

	public static void encryptFile(List<String> moduleList, File fileOut) {
		try {

	
			OutputStream out = new FileOutputStream(fileOut);
			out = new CipherOutputStream(out, encryptCipher);
			StringBuilder moduleSet = new StringBuilder();
			for (String module : moduleList) {
				moduleSet.append(module+"\n");
			}
			out.write(moduleSet.toString().getBytes(Charset.forName("UTF-8")));
			System.out.println("ENCRIPTADO "+moduleSet.toString().getBytes(Charset.forName("UTF-8")));
			out.flush();
			out.close();
					
		} catch (IOException ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
	}

	public static List<String> decryptFile(String file) {
		
		final List<String> list = new LinkedList<String>();
		try {
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(new CipherInputStream(
							new FileInputStream(file), decryptCipher)));
			String line;
			while ((line = reader.readLine()) != null) {
				list.add(line);
			}
		 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
		 
	}
	
}
