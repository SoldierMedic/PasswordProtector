package Version1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class SaltedMD5 {

	private static String plaintext;
	private static String saltedWithHash;

	public SaltedMD5(String plaintext1) {
		plaintext = plaintext1;
		try 
		{
			saltedWithHash = getSaltedPassword(plaintext, getSalt());
		}
		catch (NoSuchAlgorithmException | NoSuchProviderException e)
		{
			e.printStackTrace();
		}
	}

	// This method will determine the MD5 hash of a plaintext string, returning the
	// value of the hash
	public String getSaltedPassword(String plaintext, byte[] salt) throws NoSuchAlgorithmException, NoSuchProviderException
	    {
	        String generatedPassword = null;
	        try {
	            // Create MessageDigest instance for MD5
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            //Add password bytes to digest
	            md.update(salt);
	            //Get the hash's bytes
	            byte[] bytes = md.digest(plaintext.getBytes());
	            //This bytes[] has bytes in decimal format;
	            //Convert it to hexadecimal format
	            StringBuilder sb = new StringBuilder();
	            for(int i=0; i< bytes.length ;i++)
	            {
	                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	            }
	            //Get complete hashed password in hex format
	            generatedPassword = sb.toString();
	        }
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
	       // saltedWithHash = generatedPassword;
	        return generatedPassword;
	    }
	  
	  public String getHash()
	  {
		  return saltedWithHash;
	  }


	// This method will print the plaintext of this password/hash pair
	public String getPlaintext() {
		return plaintext;
	}

	// This method will allow a user to set the plaintext for a pair. If plaintext
	// is set, new hash is determined
	public void setPlaintext(String plaintext1) {
		plaintext = plaintext1;
	}
	// this method gets a random salt byte and returns it to be added to the password hash for further security.
	public static byte[] getSalt() throws NoSuchAlgorithmException
	{
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte [] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}
}