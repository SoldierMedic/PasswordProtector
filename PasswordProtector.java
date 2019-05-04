package Version1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
	public static int databaseCounter = 0;// Counts how many entries are in the user database
	
	public static void main( String[ ] args )
	{
		int userChoice;										// Option user chooses for what they would like to do
		char successfulLogin;								// Holds if a login was successful
		readUserDatabaseToArray();
		/*do
		{
			successfulLogin = userLogin();
		} while (successfulLogin != 'y');
		*/
		do
		{
			System.out.printf("\nWhat would you like to do?\n\n");
			System.out.printf("1) Convert file of plaintext strings to MD5 hashes\n");
			System.out.printf("2) Determine plaintext of list of unknown hashes\n");
			System.out.printf("3) Check if your password is weak\n");
			System.out.printf("4) Add new user\n");
			System.out.printf("5) Show password database\n");
			System.out.printf("6) Try logging in\n");
			
			userChoice = input.nextInt();
			
			while (userChoice < 1 || userChoice > 6)
			{
				System.out.printf("Incorrect Choice\nPlease enter a number between 1 and 6\n");
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
			else if (userChoice == 5)
				do
				{
					successfulLogin = userLogin();
				} while (successfulLogin != 'y');
			else
			
				{
					showPasswordDatabase();
					}
			
		} while (userChoice >= 1 && userChoice <= 6);
	}
	
		// Author: Marco
		// This method allows the addition of a new user. They are prompted to enter first and last name, a username is assigned, or the user can choose their own username
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
		
		System.out.printf("Is %s okay to use as the username, or would you like to choose a username(y/c)?\n", firstName);
		char userChoice = input.next().charAt(0);
		while (userChoice != 'y' && userChoice != 'c')
		{
			System.out.printf("Enter 'y' for using %s as username, or 'c' for choosing your own\n", firstName);
			userChoice = input.next().charAt(0);
		}
		if (userChoice == 'y')
		{
			username = username.concat(firstName).trim();
		}
		else
		{
			char changes = 'y';
			do {
			System.out.printf("What would you like the username to be for the user\n");
			username = input.next();
			System.out.printf("You chose %s.\nWould you like to make any changes?\nY/N?\n", username);
			changes = input.next().toLowerCase().charAt(0);
			}while(changes == 'y');
		}
		
		do
		{
			System.out.printf("Please enter a password for %s:\n", username);
			String password1 = input.next();
			System.out.println("Please re-enter the password.");
			String password2 = input.next();
			if(password1.equals(password2))
			{
				x=1;
				securePass = new SaltedMD5(password2);
				String salt = securePass.getSalt();
				System.out.printf("line 125 salt is %s", salt);
				String hash = securePass.getHash();				System.out.println("line 136 hash is "+hash);
				userDatabase[databaseCounter][0] = username;			// Username
				userDatabase[databaseCounter][1] = hash;				// Salted hash
				userDatabase[databaseCounter][2] = salt;				// Salt string
				userDatabase[databaseCounter][3] = firstName;			// First name
				userDatabase[databaseCounter][4] = lastName;			// Last name
				password1 = null;
				password2 = null;
			}
			else
			{
				System.out.println("The passwords don't match.");
			}
		}while(x==0);
		databaseCounter++;
		if (databaseCounter >= userDatabase.length)
		{
			userDatabase = increaseArraySize(userDatabase);
		}
		writeUserDatabaseToFile();
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
					unknownPasswords = increaseArraySize(unknownPasswords);
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
		// This method will take a 2-dimensions String array, and return a copy of the array that is twice the length
	public static String[][] increaseArraySize(String[][] origionalArray)
	{
		String[][] cloneArray = new String[origionalArray.length * 2][origionalArray[0].length];
		for (int i = 0; i < origionalArray.length; i++)
		{
			for (int j = 0; j < origionalArray[0].length; j++)
			{
				cloneArray[i][j] = origionalArray[i][j];
			}
		}
		return cloneArray;
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
				if (passwordCounter >= mD5PasswordArray.length)
				{
					mD5PasswordArray = increaseArraySize(mD5PasswordArray);
				}
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
	
		// Author:
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
				if (databaseCounter >= userDatabase.length)
				{
					userDatabase = increaseArraySize(userDatabase);
				}
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
	
		// Author:
		// This method will print the user database to the screen
	public static void showPasswordDatabase()
	{
		System.out.printf("\n%10s%25s%15s%10s%10s%15s", "Username", "Hash", " ", "First Name", "Last Name", "Salt String\n");
		for (int i = 0; i < userDatabase.length; i++)
		{
			System.out.printf("%10s%40s%15s%10s%10s\n", userDatabase[i][0], userDatabase[i][1], userDatabase[i][3], userDatabase[i][4], userDatabase[i][2]);
		}
	}
	
		// Author:
		// This method allows a user to login. The username is checked against the password database and a hash of the user's password is compared against the one
		// in the database. It is checked whether the user had a salted password or an unsalted password and checks the hash accordingly
	public static char userLogin()
	{
		SaltedMD5 checkLogin = null;						// Create SaltedMD5 object for use in checking correct login
		PasswordMD5 checkLoginPlain = null;					// Create PasswordMD5 object for use in checking correct login without salted hash
		String expectedHash = null;							// Store user hash from userDatabase
		String userSalt = null;								// Store user salt string from userDatabase
		char successfulLogin = 'n';							// Variable to pass back if a user login was successfully confirmed. No be default unless login is successful
		System.out.printf("Enter username:\t");
		String username = input.next();
		System.out.printf("\nEnter password:\t");
		String password = input.next();
		checkLogin = new SaltedMD5(password);
		checkLoginPlain = new PasswordMD5(password);
		
		for (int i = 0; i < userDatabase.length; i++)
		{
			if (username.equals(userDatabase[i][0]))
			{
			
				userSalt = userDatabase[i][2];
				expectedHash = userDatabase[i][1];
				if (checkLogin.checkLogin(password, userSalt).equals(expectedHash))
				{
					System.out.printf("\nWelcome %s!\n", username);
					successfulLogin = 'y';
				}
				else if (checkLoginPlain.getMD5Hash().equals(expectedHash))
				{
					System.out.printf("\nWelcome %s!\n", username);
					successfulLogin = 'y';
				}
				else
				{
					System.out.printf("\nIncorrect password for %s\n", username);
				}
			}
		}
		if (expectedHash == null)
		{
			System.out.printf("\nUser not found\n");
		}
		return successfulLogin;
	}
	
		// Author:
		// This method will write the contents of the userDatabase array back to the UserDatabase.txt file that it was read in from
	public static void writeUserDatabaseToFile()
	{
		PrintWriter writeFile = null;
		
		try
		{
			writeFile = new PrintWriter(new File("UserDatabase.txt"));
			
			for (int i = 0; i < databaseCounter; i++)
			{
				writeFile.printf("%s;%s;%s;%s;%s\n", userDatabase[i][0], userDatabase[i][1], userDatabase[i][2], userDatabase[i][3], userDatabase[i][4]);
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (writeFile != null)
			{
				writeFile.close();
			}
		}
	}
	
	/*
	 * 	Problems
	 * 
	 * 1)	Just the normal debugging issues as coding. Used wrong variable here, < instead of > there
	 * 
	 */

}