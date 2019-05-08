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
 * This program is the driver class for the Final Project. Main is a Main Menu that throws the user's choice to one of the methods. Before the user can reach the Main Menu, there is a login process.
 * The user can convert a list of plaintext strings to MD5 hashes for user in determining a list of unknown hashes. Users can also check if their password appears in any passwords used in lists
 * in the program, thus telling the user they have a weak password (WORK IN PROGRESS). Finally, users can interact with the password database that allowed them to login, by adding new users or displaying the database
 * file to display differences between salting and not salting passwords
 * 
 */

public class PasswordProtector
{
	public static Scanner input = new Scanner ( System.in );
	public static String[ ][ ] mD5PasswordArray = new String[10000][2]; // array made from the password file; plaintext in column 0, md5Hash column 1
	public static String[ ][ ] userDatabase = new String[10][5]; 		// Array made up of user login info: column 0 is username, column 1 is md5hash, column 2 is salt
	public static int databaseCounter = 0; 								// Counts how many entries are in the user database

	public static void main( String[ ] args )
	{
		char userChoice; 		// Option user chooses for what they would like to do
		char successfulLogin; 	// Holds if a login was successful
		readUserDatabaseToArray ( );
		readFileToPasswordArray ( "Plaintext.txt" );
		/*
		 * do { successfulLogin = userLogin(); } while (successfulLogin != 'y');
		 */
		do
		{
			makeSolidLine ( 50 );
			System.out.printf ( "\nWhat would you like to do?\n" );
			makeSolidLine ( 50 );
			System.out.printf ( "\n1) Determine plaintext of list of unknown hashes\n" );
			System.out.printf ( "2) Check if your password is weak\n" );
			System.out.printf ( "3) Add new user\n" );
			System.out.printf ( "4) Try logging in\n" );
			System.out.printf ( "5) Show password database\n" );
			System.out.printf ( "6) Display hash types\n" );
			System.out.printf ( "Q) Quit\n" );
			makeSolidLine ( 50 );
			userChoice = input.next ( ).toLowerCase ( ).charAt ( 0 );

			while ( userChoice < '1' || userChoice > '6' && userChoice != 'q' )
			{
				System.out.printf ( "Incorrect Choice\nPlease enter a number between 1 and 6, or press q to Quit\n" );
				userChoice = input.next ( ).charAt ( 0 );
			}
			if ( userChoice == '1' )
			{
				determinePlaintextMenu ( mD5PasswordArray );
			}
			else if ( userChoice == '2' )
			{
				// Console console = System.console();
				System.out.println ( "Enter the password you want to check." );
				String s = input.next ( );
				if ( checkStrongPassword ( s ) == true )
				{

					System.out.println ( "The password entered is safe." );
				}
				else
				{
					System.out.printf ( "Your password is too weak. Please meet password requirements\n"
							+ "( You're password must have between 8 and 16 characters while consisting of:\n1 lowercase letter, 1 uppercase letter\n 1 digit, 1 symbol" );
					s = null;
					continue;
				}
			}
			else if ( userChoice == '3' )
			{
				addNewUser ( );
			}
			else if ( userChoice == '4' )
			{
				do
				{
					successfulLogin = userLogin ( );
				} while ( successfulLogin != 'y' );
			}
			else if ( userChoice == '5' )
			{
				showPasswordDatabase ( );
			}
			else if ( userChoice == '6' )
			{
				showHashTypes();
			}
			else
			{
				System.out.printf ( "Goodbye\n" );
			}

		} while ( userChoice >= '1' && userChoice <= '5' );
	}

		// Author: Marco
		// This method allows the addition of a new user. They are prompted to enter first and last name, a username is
		// assigned, or the user can choose their own username
	public static void addNewUser( )
	{
		SaltedMD5 securePass = null; 	// Salted password object for storing hash of user password in database
		String username = " "; 			// Username string; starts with space so firstName can be added to it as default username
		boolean allowedPassword; 		// Stores if password is allowed (ie not duplicate or considered weak)
		boolean duplicateUsername; 		// Stores if chosen usename is a duplicate with a existing user
		char userChoice; 				// Holds user choice in different menus or yes/no prompts

		System.out.println ( );
		System.out.printf ( "What is the user's First Name?\n" );
		String firstName = input.next ( );

		System.out.printf ( "What is the user's Last Name?\n" );
		String lastName = input.next ( );

		System.out.printf ( "Is %s okay to use as the username, or would you like to choose a username(y/c)?\n",
				firstName );
		userChoice = input.next ( ).charAt ( 0 );
		while ( userChoice != 'y' && userChoice != 'c' )
		{
			System.out.printf ( "Enter 'y' for using %s as username, or 'c' for choosing your own\n", firstName );
			userChoice = input.next ( ).charAt ( 0 );
		}
		if ( userChoice == 'y' )
		{
			username = username.concat ( firstName ).trim ( );
		}
		else
		{
			System.out.printf ( "What would you like the username to be for the user\n" );
			username = input.next ( );
		}

		duplicateUsername = false; // Declare username to be unique until duplicate is found
		for ( int i = 0; i < databaseCounter; i++ )
		{
			if ( userDatabase[i][0].equals ( username ) )
			{
				duplicateUsername = true;
			}
		}
		while ( duplicateUsername )
		{
			System.out.printf ( "%s is not available as a username. Please choose a different username\n", username );
			username = input.next ( );
			duplicateUsername = false; // Reset for checking of new username
			for ( int i = 0; i < databaseCounter; i++ )
			{
				if ( userDatabase[i][0].equals ( username ) )
				{
					duplicateUsername = true;
				}
			}
		}

		System.out.printf ( "First Name:\t%s\nLast Name:\t%s\nUsername:\t%s\n", firstName, lastName, username );
		System.out.printf ( "Is your information correct?\nY/N?\n", username );
		userChoice = input.next ( ).toLowerCase ( ).charAt ( 0 );
		while ( userChoice != 'y' && userChoice != 'n' )
		{
			System.out.printf ( "First Name:\t%s\nLast Name:\t%s\nUsername:\t%s\n", firstName, lastName, username );
			System.out.printf ( "Please enter 'Y' if your information is correct, or 'N' to make changes\n" );
		}
		while ( userChoice == 'n' )
		{
			System.out.printf ( "Enter number of what you would like to change:\n1) First Name:\t%s\n2) Last Name:\t%s\n",
					firstName, lastName );
			userChoice = input.next ( ).charAt ( 0 );
			while ( userChoice < '1' && userChoice > '2' )
			{
				System.out.printf (
						"Enter number of what you would like to change:\n1) First Name:\t%s\n2) Last Name:\t%s\n", firstName,
						lastName );
				userChoice = input.next ( ).charAt ( 0 );
			}

			if ( userChoice == '1' )
			{
				System.out.printf ( "First name was: %s\nEnter correction:  ", firstName );
				firstName = input.next ( );
			}
			else
			{
				System.out.printf ( "First name was: %s\nEnter correction:  ", lastName );
				lastName = input.next ( );
			}

			System.out.printf ( "First Name:\t%s\nLast Name:\t%s\nUsername:\t%s\n", firstName, lastName, username );
			System.out.printf ( "Is your information correct?\nY/N?\n", username );
			userChoice = input.next ( ).toLowerCase ( ).charAt ( 0 );
			while ( userChoice != 'y' && userChoice != 'n' )
			{
				System.out.printf ( "First Name:\t%s\nLast Name:\t%s\nUsername:\t%s\n", firstName, lastName, username );
				System.out.printf ( "Please enter 'Y' if your information is correct, or 'N' to make changes\n" );
			}
		}

		allowedPassword = false; // Assume weak password until strong one is provided
		do
		{
			System.out.printf ( "Please enter a password for %s:\n", username );
			String password1 = input.next ( );
			System.out.printf ( "Please re-enter the password:\n" );
			String password2 = input.next ( );

			if ( password1.equals ( password2 ) )
			{
				if ( checkStrongPassword ( password2 ) )
				{
					allowedPassword = true;
					securePass = new SaltedMD5 ( password2 );
					String salt = securePass.getSalt ( );
					String hash = securePass.getHash ( );
					userDatabase[databaseCounter][0] = username; 	// Username
					userDatabase[databaseCounter][1] = hash; 		// Salted hash
					userDatabase[databaseCounter][2] = salt; 		// Salt string
					userDatabase[databaseCounter][3] = firstName; 	// First name
					userDatabase[databaseCounter][4] = lastName; 	// Last name

				}
				else
				{
					System.out.printf ( "Your password is too weak. Please meet password requirements\n"
							+ "( You're password must have between 8 and 16 characters while consisting of:\n1 lowercase letter, 1 uppercase letter\n 1 digit, 1 symbol" );
				}
			}
			else
			{
				System.out.println ( "The passwords don't match." );
			}
		} while ( !allowedPassword );

		databaseCounter++;
		if ( databaseCounter >= userDatabase.length )
		{
			userDatabase = increaseArraySize ( userDatabase );
		}
		writeUserDatabaseToFile ( );
	}

		// Author: Marco
		// This method will check the users potential password against our list of requirements.
		// If the password is strong the method will return true.
		// This method uses regex to determine if there is at least:
		// 1 Uppercase, 1 symbol, 1 digits, at least 1 lowercase letters, and a min of 8 characters.
	private static boolean checkStrongPassword( String password1 )
	{
		boolean passwordCheck=false;
	
			if ( password1.matches ( "^(?=.*([A-Z]){1,})(?=.*[!@#$&*]{1,})(?=.*[0-9]{1,})(?=.*[a-z]{1,}).{8,16}$" ) )
			{
				passwordCheck = true;
				if (passwordCheck)
				{
					for ( int i = 0; i < mD5PasswordArray.length; i++ )
					{
						if ( password1.equals ( mD5PasswordArray[i][1] ) ) // Check  against known passwords
						{
							passwordCheck = false;
						}
					}
				}
			}
			return passwordCheck;
	}

		// Author: Marco
		// This method is a menu that allows a user to choose which file to read into the knownPassword array
	public static void convertToMD5Menu( )
	{
		int userChoice; 	// option user chooses for what they would like to do
		String fileChoice; 	// file user specifies they would like to use for MD5 conversion

		System.out.printf ( "\nWhat file would you like to convert?\n\n" );
		System.out.printf ( "1) Convert default file\n" );
		System.out.printf ( "2) Convert your own file\n" );
		userChoice = input.nextInt ( );

		while ( userChoice < 1 && userChoice > 2 )
		{
			System.out.printf ( "Incorrect Choice\nPlease enter a number between 1 and 2\n" );
			userChoice = input.nextInt ( );
		}

		if ( userChoice == 1 )
		{
			fileChoice = "Plaintext.txt";
			readFileToPasswordArray ( fileChoice );
		}
		else
		{
			System.out.printf ( "What file would you like to open? Specify its path\n" );
			fileChoice = input.next ( );

			readFileToPasswordArray ( fileChoice );
		}

		System.out.printf ( "Your file has been read to an array with hashes\n" );
	}

		// Author: Patrick
		// This method will determine the plaintext identity of unknown hashes. This is accomplishes by comparing the list of
		// unknown hashes to our list of hashes we determined from the list of strings. If a match is found, the plaintext of the hash is stored
	public static void determinePlaintext( String[ ][ ] knownPasswords, String file1 )
	{
		int counter = 0; 				// holds how many hashes have been read to the array
		Scanner unknownHashes = null; 	// scanner object reading in the file of unknown hashes
		String determinedPlaintext; 	// holds value of string of determined plaintext of a hash
		String testHash; 				// holds value of known hash from knownPasswords array
		String unknownHash; 			// holds value of string of unknown hash
		String[ ][ ] unknownPasswords = new String[10][2]; // holds unknown hash in column 0, determined (if possible)
																			// string value in column 1

		try
		{
			unknownHashes = new Scanner ( new File ( file1 ) );
			while ( unknownHashes.hasNext ( ) )
			{
				unknownHash = unknownHashes.next ( );
				unknownPasswords[counter][0] = unknownHash;
				for ( int i = 0; i < knownPasswords.length; i++ )
				{
					testHash = knownPasswords[i][1];
					if ( unknownHash.equals ( testHash ) ) // Check unknown hash against known hashes
					{
						determinedPlaintext = knownPasswords[i][0];
						unknownPasswords[counter][1] = determinedPlaintext;
					}
				}
				System.out.printf ( "%40s%15s\n", unknownPasswords[counter][0], unknownPasswords[counter][1] );

				counter++;
				if ( counter >= unknownPasswords.length ) // Double number of rows in array if counter(our index value)
																		// becomes greater than the array length
				{
					unknownPasswords = increaseArraySize ( unknownPasswords );
				}
			}
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace ( );
		} finally
		{
			if ( unknownHashes != null )
			{
				unknownHashes.close ( );
			}
		}
	}

		// Author: Patrick
		// This method creates a menu that allows a user to choose different unknown hash files to try and determine their
		// identities
	public static void determinePlaintextMenu( String[ ][ ] knownPasswords )
	{
		int userChoice; // option user chooses they would like to do

		System.out.printf ( "\nWhat file would you like the determine the plaintext of the hashes?\n\n" );
		System.out.printf ( "1) Small, MD5 only file\n" );
		System.out.printf ( "2) Large, MD5 only file\n" );
		System.out.printf ( "3) Mixed hashes file\n" );
		userChoice = input.nextInt ( );
		while ( userChoice < 1 && userChoice > 3 )
		{
			System.out.printf ( "Incorrect Choice\nPlease enter a number between 1 and 3\n" );
			userChoice = input.nextInt ( );
		}

		if ( userChoice == 1 )
		{
			determinePlaintext ( knownPasswords, "ShortMD5.txt" );
		}
		else if ( userChoice == 2 )
		{
			determinePlaintext ( knownPasswords, "LongMD5.txt" );
		}
		else
		{
			determinePlaintext ( knownPasswords, "MixedHashes.txt" );
		}
	}

		// Author: Marco
		// This method will take a 2-dimensions String array, and return a copy of the array that is twice the length
	public static String[ ][ ] increaseArraySize( String[ ][ ] origionalArray )
	{
		String[ ][ ] cloneArray = new String[origionalArray.length * 2][origionalArray[0].length];
		for ( int i = 0; i < origionalArray.length; i++ )
		{
			for ( int j = 0; j < origionalArray[0].length; j++ )
			{
				cloneArray[i][j] = origionalArray[i][j];
			}
		}
		return cloneArray;
	}

		// Author: Marco
		// This method will read a file of plaintext strings to an array, then in the second column of the array store the
		// MD5 hash of the string in the first column
	public static void readFileToPasswordArray( String plaintextFile )
	{
		int passwordCounter = 0; 				// Counts how many passwords have been read, to correctly assign them into the array
		PasswordMD5 convertedPassword = null; 	// Object that will convert plaintext to md5Hash
		Scanner readPasswordFile = null; 		// Scanner object reading in password file
		String password; 						// Password from plaintext file

		try
		{
			readPasswordFile = new Scanner ( new File ( plaintextFile ) );

			while ( readPasswordFile.hasNextLine ( ) )
			{
				password = readPasswordFile.next ( );
				mD5PasswordArray[passwordCounter][0] = password;
				convertedPassword = new PasswordMD5 ( password );
				mD5PasswordArray[passwordCounter][1] = convertedPassword.getMD5Hash ( );

				passwordCounter++;
				if ( passwordCounter >= mD5PasswordArray.length )
				{
					mD5PasswordArray = increaseArraySize ( mD5PasswordArray );
				}
			}
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace ( ); 	
		} finally
		{
			if ( readPasswordFile != null )
			{
				readPasswordFile.close ( );
			}
		}
	}

		// Author: Patrick
		// This method will read in the password database file to the userDatabase array for use in the program
	public static void readUserDatabaseToArray( )
	{
		Scanner readDatabase = null; 	// Object to read in the database file
		Scanner scanLine = null; 		// Object to assign lines from the database file to the array
		String string1; 				// Line from the file, to be read an assigned to array

		try
		{
			readDatabase = new Scanner ( new File ( "UserDatabase.txt" ) );

			while ( readDatabase.hasNextLine ( ) )
			{
				string1 = readDatabase.nextLine ( );
				scanLine = new Scanner ( string1 );
				scanLine.useDelimiter ( ";" );
				while ( scanLine.hasNext ( ) )
				{
					userDatabase[databaseCounter][0] = scanLine.next ( ); // Username
					userDatabase[databaseCounter][1] = scanLine.next ( ); // Salted hash
					userDatabase[databaseCounter][2] = scanLine.next ( ); // Salt string
					userDatabase[databaseCounter][3] = scanLine.next ( ); // First name
					userDatabase[databaseCounter][4] = scanLine.next ( ); // Last name
				}
				databaseCounter++;
				if ( databaseCounter >= userDatabase.length )
				{
					userDatabase = increaseArraySize ( userDatabase );
				}
			}
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace ( );
		} finally
		{
			if ( readDatabase != null )
			{
				readDatabase.close ( );
			}
			if ( scanLine != null )
			{
				scanLine.close ( );
			}
		}
	}
	
		// Author: Patrick
		// This method will display the difference between MD5 and SHA-512 Hashing, with and without salt
	public static void showHashTypes()
	{
		System.out.printf("What word would you like to hash?\n");
		String word = input.next();
		PasswordMD5 md5Password = new PasswordMD5(word);
		SaltedMD5 md5Salted = new SaltedMD5(word);
		SHA512Password sha512Password = new SHA512Password(word);
		SaltedSHA512 sha512Salted = new SaltedSHA512(word);
		System.out.printf("MD5 unsalted:\t%s\n", md5Password.getMD5Hash());
		System.out.printf("MD5 Salted:\t%s\tSalt:\t%s\n", md5Salted.getHash(), md5Salted.getSalt());
		System.out.printf("SHA-512 unsalted:\t%s\n", sha512Password.getSHA512Hash());
		System.out.printf("SHA-512 Salted:\t\t%s\tSalt:\t%s\n", sha512Salted.getHash(), sha512Salted.getSalt());
	}

		// Author: Patrick
		// This method will print the user database to the screen
	public static void showPasswordDatabase( )
	{
		System.out.printf ( "\n%10s%25s%15s%10s%10s%15s", "Username", "Hash", " ", "First Name", "Last Name",
				"Salt String\n" );
		for ( int i = 0; i < userDatabase.length; i++ )
		{
			System.out.printf ( "%10s%40s%15s%10s%10s\n", userDatabase[i][0], userDatabase[i][1], userDatabase[i][3],
					userDatabase[i][4], userDatabase[i][2] );
		}
	}

		// Author: Patrick
		// This method allows a user to login. The username is checked against the password database and a hash of the user's
		// password is compared against the one
		// in the database. It is checked whether the user had a salted password or an unsalted password and checks the hash
		// accordingly
	public static char userLogin( )
	{
		SaltedMD5 checkLogin = null; 		// Create SaltedMD5 object for use in checking correct login
		PasswordMD5 checkLoginPlain = null; // Create PasswordMD5 object for use in checking correct login without salted hash
		String expectedHash = null; 		// Store user hash from userDatabase
		String userSalt = null; 			// Store user salt string from userDatabase
		char successfulLogin = 'n'; 		// Variable to pass back if a user login was successfully confirmed. No be default
											// unless login is successful
		System.out.printf ( "Enter username:\t" );
		String username = input.next ( );
		System.out.printf ( "\nEnter password:\t" );
		String password = input.next ( );
		checkLogin = new SaltedMD5 ( password );
		checkLoginPlain = new PasswordMD5 ( password );

		for ( int i = 0; i < userDatabase.length; i++ )
		{
			if ( username.equals ( userDatabase[i][0] ) )
			{
				userSalt = userDatabase[i][2];
				expectedHash = userDatabase[i][1];
				if ( checkLogin.checkLogin ( password, userSalt ).equals ( expectedHash ) )
				{
					System.out.printf ( "\nWelcome %s!\n", username );
					successfulLogin = 'y';
				}
				else if ( checkLoginPlain.getMD5Hash ( ).equals ( expectedHash ) )
				{
					System.out.printf ( "\nWelcome %s!\n", username );
					successfulLogin = 'y';
				}
				else
				{
					System.out.printf ( "\nIncorrect password for %s\n", username );
				}
			}
		}
		if ( expectedHash == null )
		{
			System.out.printf ( "\nUser not found\n" );
		}
		return successfulLogin;
	}

		// Author: Patrick
		// This method will write the contents of the userDatabase array back to the UserDatabase.txt file that it was read
		// in from
	public static void writeUserDatabaseToFile( )
	{
		PrintWriter writeFile = null;

		try
		{
			writeFile = new PrintWriter ( new File ( "UserDatabase.txt" ) );

			for ( int i = 0; i < databaseCounter; i++ )
			{
				writeFile.printf ( "%s;%s;%s;%s;%s\n", userDatabase[i][0], userDatabase[i][1], userDatabase[i][2],
						userDatabase[i][3], userDatabase[i][4] );
			}
		} catch ( FileNotFoundException e )
		{
			e.printStackTrace ( );
		} finally
		{
			if ( writeFile != null )
			{
				writeFile.close ( );
			}
		}
	}

		// Author: Marco
		// This method will print a solid line of stars, with a new line following the last star
	public static void makeSolidLine( int stars )
	{
		for ( int num2 = 0; num2 < stars; num2++ )
		{
			System.out.print ( "*" );
		}
		System.out.println();
	}



/*
 * Problems
 * 
 * 1) Just the normal debugging issues as coding. Used wrong variable here, < instead of > there
 * 
 * 2) We had an issue with the userLogin method for a while. There was a problem confirming the String of the hash
 * created at user creation and the one from user login. We knew both were being generated the same way, but couldn't
 * get the right results. Fixed the problem by tracing exactly what the text was along each step of the hashing process
 * to determine where it went wrong.
 * 
 */

}