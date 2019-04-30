package Version1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Author:	Patrick and Marco
 * Date:		5/13/2019
 * Class:	CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This program ......
 */

public class PasswordProtector
{
	public static Scanner input = new Scanner(System.in);

	public static void main( String[ ] args )
	{
		String[][] passwordArray = null;		// array made from the password file; plaintext in column 0, md5Hash column 1
		int userChoice;							// option user chooses for what they would like to do
		
		do
		{
			System.out.printf("\nWhat would you like to do?\n\n");
			System.out.printf("1) Convert file of plaintext strings to MD5 hashes\n");
			System.out.printf("2) Determine plaintext of list of unknown hashes\n");
			System.out.printf("3) \n");
			System.out.printf("4) \n");
			System.out.printf("5) \n");
			
			userChoice = input.nextInt();
			while (userChoice < 1 && userChoice > 5)
			{
				System.out.printf("Incorrect Choice\nPlease enter a number between 1 and 5\n");
				userChoice = input.nextInt();
			}
			
			if (userChoice == 1)
			{
				passwordArray = convertToMD5Menu();
			}
			else if (userChoice == 2)
			{
				determinePlaintextMenu(passwordArray);
			}
			else if (userChoice == 3)
			{
				
			}
			else if (userChoice == 4)
			{
				
			}
			else
			{
				
			}
		} while (userChoice >= 1 && userChoice <= 5);
	}
	
		// Author:	
		//	This method
	public static String[][] convertToMD5Menu()
	{
		int userChoice;							// option user chooses for what they would like to do
		String fileChoice;						// file user specifies they would like to use for MD5 conversion
		String[][] passwordArray = null;		// array made from the password file; plaintext in column 0, md5Hash column 1
		
		System.out.printf("\nWhat file would you like to convert?\n\n");
		System.out.printf("1) Convert default file\n");
		System.out.printf("2) Convert your own file\n");
		userChoice = input.nextInt();
		
		while (userChoice < 1 && userChoice > 2)
		{
			System.out.printf("Incorrect Choice\nPlease enter a number between 1 and 2\n");
			userChoice = input.nextInt();
		}
		
		if (userChoice == 1)
		{
			fileChoice = "Plaintext.txt";
			passwordArray = readFileToPasswordArray(fileChoice);
		}
		else
		{
			System.out.printf("What file would you like to open? Specify its path\n");
			fileChoice = input.next();
			
			passwordArray = readFileToPasswordArray(fileChoice);
		}
		
		System.out.printf("Your file has been read to an array with hashes\n");
		return passwordArray;
	}
	
		// Author:
		// This method
	public static void determinePlaintext(String[][] knownPasswords, String file1)
	{
		int counter = 0;								// holds how many hashes have been read to the array
		Scanner unknownHashes = null;				// scanner object reading in the file of unknown hashes
		String determinedPlaintext;				// holds value of string of determined plaintext of a hash
		String testHash;								// holds value of known hash from knownPasswords array
		String unknownHash;							// holds value of string of unknown hash
		String[][] unknownPasswords = new String[10][2];	// holds unknown hash in column 0, determined (if possible) string value in column 1
		
		try
		{
			unknownHashes = new Scanner(new File(file1));
			while (unknownHashes.hasNext())
			{
				unknownHash = unknownHashes.next();
				unknownPasswords[counter][0] = unknownHash;
				for (int i = 0; i < knownPasswords.length; i++)
				{
					testHash = knownPasswords[i][1];
					if (unknownHash.equals(testHash))			// Check unknown hash against known hashes
					{
						determinedPlaintext = knownPasswords[i][0];
						unknownPasswords[counter][1] = determinedPlaintext;
					}
				}
				System.out.printf("%40s%15s\n", unknownPasswords[counter][0], unknownPasswords[counter][1]);
				
				counter++;
				if (counter >= unknownPasswords.length)		// Double number of rows in array if counter(our index value) becomes greater than the array length
				{
					String[][] cloneArray = new String[unknownPasswords.length * 2][2];
					for (int i = 0; i < unknownPasswords.length; i++)
					{
						cloneArray[i][0] = unknownPasswords[i][0];
						cloneArray[i][1] = unknownPasswords[i][1];
					}
					unknownPasswords = cloneArray;
				}
			}
		}
		catch ( FileNotFoundException e )
		{
			System.out.println ( "catching" );
			e.printStackTrace();
		}
		finally
		{
			if (unknownHashes != null)
			{
				System.out.println ( "ending" );
				unknownHashes.close();
			}
		}
	}
	
		// Author:	
		//	This method
	public static void determinePlaintextMenu(String[][] knownPasswords)
	{
		int userChoice;							// option user chooses they would like to do
		
		System.out.printf("\nWhat file would you like the determine the plaintext of the hashes?\n\n");
		System.out.printf("1) Small, MD5 only file\n");
		System.out.printf("2) Large, MD5 only file\n");
		System.out.printf("3) Mixed hashes file\n");
		userChoice = input.nextInt();
		while (userChoice < 1 && userChoice > 3)
		{
			System.out.printf("Incorrect Choice\nPlease enter a number between 1 and 3\n");
			userChoice = input.nextInt();
		}
		
		if (userChoice == 1)
		{
			determinePlaintext(knownPasswords, "ShortMD5.txt");
		}
		else if (userChoice == 2)
		{
			
		}
		else
		{
			
		}
	}
	
		// Author:	
		//	This method will read a file of plaintext strings to an array, then in the second column of the array store the MD5 hash of the string in the first column
	public static String[][] readFileToPasswordArray(String plaintextFile)
	{
		int passwordCounter = 0;					// Counts how many passwords have been read, to correctly assign them into the array
		PasswordMD5 convertedPassword = null;	// Object that will convert plaintext to md5Hash
		String[][] passwordArray = null;			// Array that holds plaintext in column 0, md5Hash in column 1
		Scanner readPasswordFile = null;			// Scanner object reading in password file
		String password;								//	Password from plaintext file
		
		try
		{
			readPasswordFile = new Scanner(new File(plaintextFile));
			passwordArray = new String[10000][2];
			
			while (readPasswordFile.hasNextLine())
			{
				password = readPasswordFile.next();
				passwordArray[passwordCounter][0] = password;
				convertedPassword = new PasswordMD5(password);
				passwordArray[passwordCounter][1] = convertedPassword.getMD5Hash();
				
				System.out.printf("%10s%40s\n", passwordArray[passwordCounter][0], passwordArray[passwordCounter][1]);
				
				passwordCounter++;
			}
		}
		catch ( FileNotFoundException e )
		{
			e.printStackTrace();
		}
		finally
		{
			if (readPasswordFile != null)
			{
				readPasswordFile.close();
			}
		}
		
		return passwordArray;
	}
	
	/*
	 * 	Problems
	 * 
	 * 1)	asdf
	 * 
	 */

}
