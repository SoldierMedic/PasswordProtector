package Version1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Author:	Patrick and Marco
 * Date:		5/13/2019
 * Class:	CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This program ......
 */

public class PasswordMD5
{
	private static String plaintext;
	
	
	public PasswordMD5(String plaintext1)
	{
		plaintext = plaintext1;
	}
	
	// This method will determine the MD5 hash of a plaintext string, returning the value of the hash
public String getMD5Hash()
{
	MessageDigest stringDigest;
	BigInteger hashInteger = null;
	
	try
	{
		stringDigest = MessageDigest.getInstance("md5");
		byte[] messageDigest = stringDigest.digest(plaintext.getBytes());
		hashInteger = new BigInteger(1, messageDigest);	
	} catch (NoSuchAlgorithmException e)
	{
		e.printStackTrace();
	}
	return hashInteger.toString(16);	
}
	
		//	This method will print the plaintext of this password/hash pair
	public String getPlaintext()
	{
		return plaintext;
	}
	
		// This method will allow a user to set the plaintext for a pair. If plaintext is set, new hash is determined
	public void setPlaintext(String plaintext1)
	{
		plaintext = plaintext1;
	}
}