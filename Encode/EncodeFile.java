package Encode;

/*
 * The file takes the following input : sourceFile destinationFile password
 * 
 * 
 * Where	sourceFile 	:- The file to be encoded.
 * 			password 	:- The password used for encryption. This is to be shared privately by encrypter and decrypter
* 
 * Outputs 	destiantionFile 	:- The resulting encoded file from the given source file.
 * 			iv.enc 				:- The initialization vector used to encrypt the file.Needs to be shared privately.
 * 			salt.enc			:- Salting used to enhance the password. Needs to be shared privately.
 * 
 * 
 * @authors : Anirudh, Suraj, Vishal, Paras
 * 
 * Encryption is done using AES[128 bit sized key] block cipher changing technique which used padding to equate the last block size to 128 bits
*/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AlgorithmParameters;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxUsers.FullAccount;

public class EncodeFile {

	public static void enc(String sourceFile, String destinationFile, String password, String ACCESS_TOKEN,
			String hashFile) throws Exception {

		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		// Getting user info
		FullAccount account = client.users.getCurrentAccount();

		// password, iv and salt should be transferred to the other end
		// in a secure manner

		// salt is used for encoding
		// salt should be transferred to the recipient securely for decryption
		byte[] salt = genSecSalt();

		SecretKey secret = genSecKey(password, salt);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		AlgorithmParameters params = cipher.getParameters();

		// iv adds randomness to the text and just makes the mechanism more
		// secure
		// used while initializing the cipher
		// file to store the iv
		genRandIV(params);

		// file to be encrypted
		FileInputStream inputFile = new FileInputStream(sourceFile);

		// encrypted file
		FileOutputStream encodedFile = new FileOutputStream(destinationFile);

		// file encryption
		encryptFile(inputFile, encodedFile, cipher);

		InputStream in = new FileInputStream(destinationFile);
		client.files.uploadBuilder("/" + destinationFile).run(in);

		// Generating a hash file for checking Integrity
		FileOutputStream hashFileOs = new FileOutputStream(hashFile);
		hashFile(in, hashFileOs);

		System.out.println("File Encrypted.");

	}

	private static void hashFile(InputStream in, FileOutputStream hashFileOs) {
		byte[] digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(in, md);
			digest = md.digest();
			hashFileOs.write(digest);
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void encryptFile(FileInputStream inFile, FileOutputStream outFile, Cipher cipher) {
		byte[] input = new byte[128];
		int bytesRead;

		try {
			while ((bytesRead = inFile.read(input)) != -1) {
				byte[] output = cipher.update(input, 0, bytesRead);
				if (output != null)
					outFile.write(output);
			}

			byte[] output = cipher.doFinal();
			if (output != null)
				outFile.write(output);

			inFile.close();
			outFile.flush();
			outFile.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static SecretKey genSecKey(String password, byte[] salt) {
		SecretKeyFactory keyFactory;
		SecretKey secret = null;
		try {
			keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return secret;
	}

	private static void genRandIV(AlgorithmParameters params) {
		try {
			FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			ivOutFile.write(iv);
			ivOutFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidParameterSpecException e) {
			e.printStackTrace();
		}
	}

	private static byte[] genSecSalt() {
		byte[] salt = new byte[8];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		try {
			FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
			saltOutFile.write(salt);
			saltOutFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return salt;
	}

}