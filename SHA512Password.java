package Version1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Author:		Patrick
 * Date:		5/13/2019
 * Class:	  	CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This program will create a SHA-512 hash of a plaintext string
 * 
 */

public class SHA512Password {
	private static String plaintext;

	public SHA512Password(String plaintext1) {
		plaintext = plaintext1;
	}

	// Author: Patrick
	// This method will determine the SHA-512 hash of a plaintext string, returning the
	// value of the hash
	public String getSHA512Hash() {
		MessageDigest stringDigest;
		BigInteger hashInteger = null;

		try {
			stringDigest = MessageDigest.getInstance("SHA-512");
			byte[] messageDigest = stringDigest.digest(plaintext.getBytes());
			hashInteger = new BigInteger(1, messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashInteger.toString(16);
	}

	// Author: Patrick
	// This method will print the plaintext of this password/hash pair
	public String getPlaintext() {
		return plaintext;
	}

	// Author: Patrick
	// This method will allow a user to set the plaintext for a pair. If plaintext
	// is set, new hash is determined
	public void setPlaintext(String plaintext1) {
		plaintext = plaintext1;
	}
}