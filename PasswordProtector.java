package Version1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Author:		Patrick and Marco
 * Date:		5/13/2019
 * Class:		CSC 160 Combo
 * Assignment:	Final Project
 * 
 * This program is the driver class for the Final Project. Main is a Main Menu that throws the user's choice to one of the methods. Before the user can reach the Main Menu, there is a login process (WORK IN PROGRESS)
 * The user can convert a list of plaintext strings to MD5 hashes for user in determining a list of unknown hashes (WORK IN PROGRESS). Users can also check if their password appears in any passwords used in lists
 * in the program, thus telling the user they have a weak password (WORK IN PROGRESS). Finally, users can interact with the password database that allowed them to login, by adding new users or displaying the database
 * file to display differences between salting and not salting passwords (WORK IN PROGRESS)
 * 
 */

public class PasswordProtector
{
	public static Scanner input = new Scanner(System.in);
	public static String[][] mD5PasswordArray = new String[10000][2];	// array made from the password file; plaintext in column 0, md5Hash column 1
	public static String[][] userDatabase = new String[10][5];			// Array made up of user login info: column 0 is username, column 1 is md5hash, column 2 is salt
	public static int databaseCounter = 0;								// Counts how many entries are in the user database
	
	public static void main( String[ ] args )
	{
		int userChoice;										// option user chooses for what they would like to do
		
		readUserDatabaseToArray();
		userLogin();
		
		do
		{
			System.out.printf("\nWhat would you like to do?\n\n");
			System.out.printf("1) Convert file of plaintext strings to MD5 hashes\n");
			System.out.printf("2) Determine plaintext of list of unknown hashes\n");
			System.out.printf("3) Check if your password is weak\n");
			System.out.printf("4) Add new user\n");
			System.out.printf("5) Show password database\n");
			
			userChoice = input.nextInt();
			while (userChoice < 1 && userChoice > 5)
			{
				System.out.printf("Incorrect Choice\nPlease enter a number between 1 and 5\n");
				userChoice = input.nextInt();
			}
			
			if (userChoice == 1)
			{
				convertToMD5Menu();
			}
			else if (userChoice == 2)
			{
				determinePlaintextMenu(mD5PasswordArray);
			}
			else if (userChoice == 3)
			{
				checkWeakPassword();
			}
			else if (userChoice == 4)
			{
				addNewUser();
			}
			else
			{
				showPasswordDatabase();
			}
		} while (userChoice >= 1 && userChoice <= 5);
	}
	
		// Author
		// This method
	public static void addNewUser()
	{
		SaltedMD5 securePass = null;
		String username = " ";
		int x = 0;
		System.out.println();
		System.out.printf("What is the user's First Name?\n");
		String firstName = input.next();
		
		System.out.printf("What is the user's Last Name?\n");
		String lastName = input.next();
		
		System.out.printf("Is %s okay to use as the username, or would you like to choose a username(y/n)?\n", firstName);
		char userChoice = input.next().charAt(0);
		while (userChoice != 'y' && userChoice != 'n')
		{
			System.out.printf("Enter 'y' for using %s as username, or 'n' for choosing your own\n", firstName);
		}
		if (userChoice == 'y')
		{
			username = username.concat(firstName).trim();
		}
		else
		{
			char changes = 'y';
			do {
			System.out.println("What would you like the username to be for the user\n");
			username = input.next();
			System.out.printf("You chose %s.\nWould you like to make any changes?\nY/N?");
			changes = input.next().toLowerCase().charAt(0);
			}while(changes == 'y');
		}
		do {
		System.out.printf("Please enter a password for %s:\n", username);
		String password1 = input.next();
		System.out.println("Please re-enter the password.");
		String password2 = input.next();
		if(password1!=password2)
		{
			System.out.println("The passwords don't match.");
			
		}
		else {
			x=1;
			password1 = null;
			securePass = new SaltedMD5(password2);
			password2=null;
			String salt = securePass.getSalt();
			String hash = securePass.getHash();
			userDatabase[databaseCounter][0] = username;			// Username
			userDatabase[databaseCounter][1] = hash;				// Salted hash
			userDatabase[databaseCounter][2] = salt;				// Salt string
			userDatabase[databaseCounter][3] = firstName;			// First name
			userDatabase[databaseCounter][4] = lastName;			// Last name
		}
		}while(x==0);
		databaseCounter++;
	}
	
		// Author
		// This method
	public static void checkWeakPassword()
	{
		
	}
	
		// Author:	
		// This method is a menu that allows a user to choose which file to read into the knownPassword array
	public static void convertToMD5Menu()
	{
		int userChoice;										// option user chooses for what they would like to do
		String fileChoice;									// file user specifies they would like to use for MD5 conversion
		
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
			readFileToPasswordArray(fileChoice);
			
			for (int i = 0; i < mD5PasswordArray.length; i++)
			{
				System.out.printf("%10s%40s\n", mD5PasswordArray[i][0], mD5PasswordArray[i][1]);
			}
		}
		else
		{
			System.out.printf("What file would you like to open? Specify its path\n");
			fileChoice = input.next();
			
			readFileToPasswordArray(fileChoice);
		}
		
		System.out.printf("Your file has been read to an array with hashes\n");
	}
	
		// Author:
		// This method will determine the plaintext identity of unknown hashes. This is accomplishes by comparing the list of unknown hashes to our list of hashes
		// we determined from the list of strings. If a match is found, the plaintext of the hash is stored
	public static void determinePlaintext(String[][] knownPasswords, String file1)
	{
		int counter = 0;									// holds how many hashes have been read to the array
		Scanner unknownHashes = null;						// scanner object reading in the file of unknown hashes
		String determinedPlaintext;							// holds value of string of determined plaintext of a hash
		String testHash;									// holds value of known hash from knownPasswords array
		String unknownHash;									// holds value of string of unknown hash
		String[][] unknownPasswords = new String[10][2];	// holds unknown hash in column 0, determined (if possible) string value in column 1
		
		for (int i = 0; i < knownPasswords.length; i++)
		{
			System.out.printf("%10s%40s\n", knownPasswords[i][0], knownPasswords[i][1]);
		}
		
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
					if (unknownHash.equals(testHash))		// Check unknown hash against known hashes
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
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (unknownHashes != null)
			{
				unknownHashes.close();
			}
		}
	}
	
		// Author:	
		// This method creates a menu that allows a user to choose different unknown hash files to try and determine their identities
	public static void determinePlaintextMenu(String[][] knownPasswords)
	{
		int userChoice;										// option user chooses they would like to do
		
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
			determinePlaintext(knownPasswords, "LongMD5.txt");
		}
		else
		{
			determinePlaintext(knownPasswords, "MixedHashes.txt");
		}
	}
	
		// Author:	
		// This method will read a file of plaintext strings to an array, then in the second column of the array store the MD5 hash of the string in the first column
	public static void readFileToPasswordArray(String plaintextFile)
	{
		int passwordCounter = 0;							// Counts how many passwords have been read, to correctly assign them into the array
		PasswordMD5 convertedPassword = null;				// Object that will convert plaintext to md5Hash
		Scanner readPasswordFile = null;					// Scanner object reading in password file
		String password;									// Password from plaintext file
		
		try
		{
			readPasswordFile = new Scanner(new File(plaintextFile));
			
			while (readPasswordFile.hasNextLine())
			{
				password = readPasswordFile.next();
				mD5PasswordArray[passwordCounter][0] = password;
				convertedPassword = new PasswordMD5(password);
				mD5PasswordArray[passwordCounter][1] = convertedPassword.getMD5Hash();
				
				System.out.printf("%10s%40s\n", mD5PasswordArray[passwordCounter][0], mD5PasswordArray[passwordCounter][1]);
				
				passwordCounter++;
			}
		}
		catch (FileNotFoundException e)
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
	}
	
		// Author: Patrick
		// This method will read in the password database file to the userDatabase array for use in the program
	
	
		// Author: Patrick
		// This method will read in the password database file to the userDatabase array for use in the program


	// Author: Patrick
		// This method will read in the password database file to the userDatabase array for use in the program
	public static void readUserDatabaseToArray()
	{
		Scanner readDatabase = null;						// Object to read in the database file
		Scanner scanLine = null;							// Object to assign lines from the database file to the array
		String string1;										// Line from the file, to be read an assigned to array
		
		try
		{
			readDatabase = new Scanner(new File("UserDatabase.txt"));
			
			while (readDatabase.hasNextLine())
			{
				string1 = readDatabase.nextLine();
				scanLine = new Scanner(string1);
				scanLine.useDelimiter(";");
				while (scanLine.hasNext())
				{
					userDatabase[databaseCounter][0] = scanLine.next();		// Username
					userDatabase[databaseCounter][1] = scanLine.next();		// Salted hash
					userDatabase[databaseCounter][2] = scanLine.next();		// Salt string
					userDatabase[databaseCounter][3] = scanLine.next();		// First name
					userDatabase[databaseCounter][4] = scanLine.next();		// Last name
				}
				
				databaseCounter++;
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (readDatabase != null)
			{
				readDatabase.close();
			}
			if (scanLine != null)
			{
				scanLine.close();
			}
		}
	}

	// Author
		// This method
	public static void showPasswordDatabase()
	{
		
	}
	
		// Author
		// This method
	public static void userLogin()
	{
		
	}

	/*
	 * 	Problems
	 * 
	 * 1)	Just the normal debugging issues as coding. Used wrong variable here, < instead of > there
	 * 
	 */

}