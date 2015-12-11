package Decode;

import java.io.File;
/*
 * The file takes the following inputs : sourceFile destinationFile password iv.enc salt.enc
 * 
 * Where	sourceFile 	:- The file to be decoded.
 * 			password 	:- The password used for encryption. This is to be shared privately by encrypter and decrypter
 * 			iv.enc 		:- The initialization vector used to encrypt the file. Generated by encryption process, also needs to be shared privately.
 * 			salt.enc	:- Salting used to enhance the password. Generated by encryption process, also needs to be shared privately.
 * 
 * Outputs 	destiantionFile 	:- The resulting decoded file from the given source file.
 * 
 * 
 * @authors : Anirudh, Suraj, Vishal, Paras
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.DbxUsers.FullAccount;

public class DecodeFile {

	// Method to decrypt the file using the given cipher settings and output the
	// decoded file.
	private static void decryptFile(FileInputStream encodedFile, FileOutputStream decodedFile, Cipher cipher) {
		try {
			// block size of 128 bytes -- should be equal to the setting of the
			// cipher specified to encrypt.
			byte[] in = new byte[128];
			int read;
			while ((read = encodedFile.read(in)) != -1) {
				byte[] output = cipher.update(in, 0, read);
				if (output != null)
					decodedFile.write(output);
			}

			byte[] output = cipher.doFinal();
			if (output != null)
				decodedFile.write(output);
			encodedFile.close();
			decodedFile.flush();
			decodedFile.close();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void dec(String encodedFilePath, String decodedFilePath, String password, String ivFilePath,
			String saltFilePath, String ACCESS_TOKEN, String hashFile) throws Exception {

		System.out.println(encodedFilePath + " :::" + decodedFilePath + " :::" + password + " :::" + ivFilePath + " :::"
				+ saltFilePath);

		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
		DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

		// Getting user info
		FullAccount account = client.users.getCurrentAccount();

		// reading the salt
		// user should have secure mechanism to transfer the
		// password, iv and salt to the recipient
		String saltFileName = saltFilePath;
		byte[] salt = getSalt(saltFileName);

		// reading the iv
		String ivFileName = ivFilePath;

		// Get the initialization vector used to encrypt the data.
		byte[] iv = getIV(ivFileName);

		// generate the secret key from the provided salt file and password.
		SecretKey secret = genSecKey(password, salt);

		// file decryption
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));

		FileOutputStream tmp = new FileOutputStream("tmp.dat");

		client.files.downloadBuilder("/" + encodedFilePath).run(tmp);

		FileInputStream encodedFile = new FileInputStream("tmp.dat");
		FileOutputStream decodedFile = new FileOutputStream(decodedFilePath);

		boolean check = compareHashvalue(encodedFile, hashFile);

		if (check == true) {
			decryptFile(encodedFile, decodedFile, cipher);
			System.out.println("File Decrypted.");
		} else {
			System.out.println("Encrypted file has been altered. Decryption cannot be done.");
		}

	}

	private static boolean compareHashvalue(FileInputStream encodedFile, String hashFile) {
		byte[] digest = null;
		boolean check = false;
		try {
			FileOutputStream hashFileOs = new FileOutputStream(new File("tmpHash.dat"));
			MessageDigest md = MessageDigest.getInstance("MD5");
			DigestInputStream dis = new DigestInputStream(encodedFile, md);
			digest = md.digest();
			hashFileOs.write(digest);
			hashFileOs.close();
		} catch (NoSuchAlgorithmException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			byte[] givenHashFile = Files.readAllBytes(new File(hashFile).toPath());
			check = Arrays.equals(givenHashFile, digest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return check;
	}

	private static SecretKey genSecKey(String password, byte[] salt) {
		SecretKey secret = null;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
			SecretKey tmp = factory.generateSecret(keySpec);
			secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return secret;
	}

	private static byte[] getIV(String ivFileName) {
		byte[] iv = null;
		try {
			FileInputStream ivFis = new FileInputStream(ivFileName);
			iv = new byte[16];
			ivFis.read(iv);
			ivFis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iv;
	}

	private static byte[] getSalt(String saltFileName) {
		byte[] salt = null;
		try {
			FileInputStream saltFis = new FileInputStream(saltFileName);
			salt = new byte[8];
			saltFis.read(salt);
			saltFis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salt;
	}
}