package Version1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
 * Author:		Marco
 * Date:		5/13/2019
 * Class:		CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This object allows the MD5 hashing of strings with the addition of a randomly generated salt string. Each time the object is created, a new string is generated,
 * so there are methods allows the use of a predetermined salt string to check for matching hashes when a user logs in
 * 
 */

public class SaltedMD5 {

	private static String plaintext;
	private static String hashWithSalt;
	private static String saltString;

	public SaltedMD5(String plaintext1)
	{
		plaintext = plaintext1;
		hashWithSalt = getSaltedPassword(plaintext, generateSalt());
	}
	
		// Author: Marco
		// This method will check the hash  a string and predetermined salt string
	public String checkLogin(String plaintext1, String saltString)
	{
		byte[] salt = saltString.getBytes();
		String generatedHash = null;
		try
		{
			MessageDigest digest1 = MessageDigest.getInstance("MD5");	// Create MessageDigest instance for MD5
			digest1.update(salt);										// Add password bytes to digest
			byte[] bytes = digest1.digest(plaintext1.getBytes());		// Get the plain password bytes
			StringBuilder builder1 = new StringBuilder();				// This bytes[] has bytes in decimal format; Convert it to hexadecimal format
			for(int i=0; i< bytes.length ;i++)
			{
				builder1.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedHash = builder1.toString();						// Get complete hashed password in hex format
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return generatedHash;
	}
	
		// Author: Marco
		// This method gets a random salt byte and returns it to be added to the password hash for further security
	public static byte[] generateSalt()
	{
		byte [] salt = null;
		SecureRandom random1;
		try
		{
			random1 = SecureRandom.getInstance("SHA1PRNG");
			salt = new byte[16];
			random1.nextBytes(salt);
			saltString = new String(salt);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return salt;
	}

		// Author: Marco
		// This method will determine the MD5 hash of a plaintext string, returning the value of the hash
	public static String getSaltedPassword(String plaintext1, byte[] salt)
	{
		String saltedPasswordHash = null;
		try
		{
			MessageDigest digest1 = MessageDigest.getInstance("MD5");	// Create MessageDigest instance for MD5
			digest1.update(salt);										// Add password bytes to digest
			byte[] bytes = digest1.digest(plaintext1.getBytes());		// Get the hash's bytes
			StringBuilder builder1 = new StringBuilder();				// This bytes[] has bytes in decimal format, convert it to hexadecimal format
			for(int i=0; i< bytes.length ;i++)
			{
				builder1.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			saltedPasswordHash = builder1.toString();					// Get complete hashed password in hex format
		}
		catch (NoSuchAlgorithmException e)
		{
        	e.printStackTrace();
        }
        return saltedPasswordHash;
    }
	
		// Author: Marco
		// This method will return the salted hash
	public String getHash()
	{
		return hashWithSalt;
	}
	
		// Author: Marco
		// This method
	public String getSalt()
	{
		return saltString;
	}


		// Author: Marco
		// This method will print the plaintext of this password/hash pair
	public String getPlaintext()
	{
		return plaintext;
	}

		// Author: Marco
		// This method will allow a user to set the plaintext for a pair. If plaintext
		// is set, new hash is determined
	public void setPlaintext(String plaintext1)
	{
		plaintext = plaintext1;
	}
}