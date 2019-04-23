package Version1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Author:	Patrick and Marco
 * Date:		5/14/2019
 * Class:	CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This program ......
 */

public class PasswordMD5
{
	private String plaintext;
	private String md5Hash;
	
	public PasswordMD5(String plaintext1)
	{
		plaintext = plaintext1;
		md5Hash = determineMD5Hash(plaintext);		// determine the MD5 hash of the passed string
	}
	
		//	This method will print the plaintext of this password/hash pair
	public String getPlaintext()
	{
		return plaintext;
	}
	
		// This method will print the MD5 hash of this password/hash pair
	public String getMD5Hash()
	{
		return md5Hash;
	}
	
		// This method will allow a user to set the plaintext for a pair. If plaintext is set, new hash is determined
	public void setPlaintext(String plaintext1)
	{
		plaintext = plaintext1;
		md5Hash = determineMD5Hash(plaintext);
	}
	
		// This method is fairly useless, but will allow a user to set the MD5 hash for the pair, if they know it
	public void setMD5Hash(String md5Hash1)
	{
		md5Hash = md5Hash1;	//	will set hash value from value determined in driver method
	}
	
		// This method will determine the MD5 hash of a plaintext string, returning the value of the hash
	public static String determineMD5Hash(String plaintext1)
	{
		MessageDigest stringDigest = MessageDigest.getInstance("md5");
		byte[] messageDigest = stringDigest.digest(plaintext1.getBytes());
		BigInteger hashInteger = new BigInteger(1, messageDigest);	
		return hashInteger.toString(16);
		
	}
}
