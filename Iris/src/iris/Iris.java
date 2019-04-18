package iris;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

// Author: Tom Bylander
// Class: CSC 119/160
//
// Read iris data into a 2D array and do stuff with it

public class Iris {

	public static void main(String[] args) {
		double[][] irisData = array2dFromFile("iris.txt");
		System.out.printf("irisData has %s rows and %s columns\n",
				irisData.length, irisData[0].length);
		
		// Here is a double loop that assigns each value in the array
		// to irisDatum in turn.  Add more code to calculate the average
		// value in irisData.  Hints: Initialize variables before the loop
		// for the sum of the values and the number of values.  Modify these
		// variables inside the inner loop as appropriate.  Use these
		// variables after the loop to calculate and print the average.
		// The average should be between 2.9 and 3.0
		
		int numberOfNumbers = 750;
		double totalOfNumbers = 0;

		for (int r = 0; r < irisData.length; r++) {
			for (int c = 0; c < irisData[0].length; c++) {
				double irisDatum = irisData[r][c];
				totalOfNumbers += irisData[r][c];
			}
		}
		double numberAverage = totalOfNumbers/numberOfNumbers;
		System.out.printf("The average of the %s numbers is %5.5s\n", numberOfNumbers, numberAverage);
		
		// Code a double loop to print how many values are above,
		// below, and equal to the average value. Hint: initialize counter
		// variables before the double loop.
		// There should be more than 400 values below the average.
		// There should be less than 350 values above the average.
		
		int aboveAverage = 0;
		int belowAverage = 0;
		int equalsAverage = 0;
		
		for (int r = 0; r < irisData.length; r++)
		{
			for (int c = 0; c < irisData[0].length; c++)
			{
				if (irisData[r][c] > numberAverage)
				{
					aboveAverage++;
				}
				else if (irisData[r][c] < numberAverage)
				{
					belowAverage++;
				}
				else
				{
					equalsAverage++;
				}
			}
		}
		
		System.out.printf("The number above average is %s, the number below average is %s, and the number equalling the average is %s\n", aboveAverage, belowAverage, equalsAverage);
		
		double[][] irisAverages = determineAverages(irisData);
		
		int[] flowerGuess = compareToAverages(irisAverages, irisData);
		
		String accuracy = determineAccurary(flowerGuess, irisData);
		System.out.println(accuracy);
	}

	// Don't change this.
	// Read numbers from a file. The first two numbers in the file should be
	// the dimensions of the 2D array. See the iris.txt file located in this
	// project.
	public static double[][] array2dFromFile(String filename) {
		try {
			Scanner scanFile = new Scanner(new File(filename));
			int numberRows = scanFile.nextInt();
			int numberCols = scanFile.nextInt();
			double[][] values = new double[numberRows][numberCols];
			for (int r = 0; r < values.length; r++) {
				for (int c = 0; c < values[0].length; c++) {
					values[r][c] = scanFile.nextDouble();
				}
			}
			scanFile.close();
			return values;
		} catch (Exception e) {
			// Use a RuntimeException to avoid messy programming elsewhere.
			throw new RuntimeException("" + e);
		}
	}
	
	public static double[][] determineAverages(double[][] dataArray)
	{
		double totalColumn0Type0 = 0;
		double totalColumn1Type0 = 0;
		double totalColumn2Type0 = 0;
		double totalColumn3Type0 = 0;
		double totalColumn0Type1 = 0;
		double totalColumn1Type1 = 0;
		double totalColumn2Type1 = 0;
		double totalColumn3Type1 = 0;
		double totalColumn0Type2 = 0;
		double totalColumn1Type2 = 0;
		double totalColumn2Type2 = 0;
		double totalColumn3Type2 = 0;
		int totalType0 = 0;
		int totalType1 = 0;
		int totalType2 = 0;
		
		for (int r = 0; r < dataArray.length; r++)
		{
			if (dataArray[r][4] == 0)
			{
				totalColumn0Type0 += dataArray[r][0];
				totalColumn1Type0 += dataArray[r][1];
				totalColumn2Type0 += dataArray[r][2];
				totalColumn3Type0 += dataArray[r][3];
				totalType0++;
			}
			else if (dataArray[r][4] == 1)
			{
				totalColumn0Type1 += dataArray[r][0];
				totalColumn1Type1 += dataArray[r][1];
				totalColumn2Type1 += dataArray[r][2];
				totalColumn3Type1 += dataArray[r][3];
				totalType1++;
			}
			else
			{
				totalColumn0Type2 += dataArray[r][0];
				totalColumn1Type2 += dataArray[r][1];
				totalColumn2Type2 += dataArray[r][2];
				totalColumn3Type2 += dataArray[r][3];
				totalType2++;
			}
		}
			// Compute average of each type of each column
		double averageColumn0Type0 = totalColumn0Type0 / totalType0;
		double averageColumn1Type0 = totalColumn1Type0 / totalType0;
		double averageColumn2Type0 = totalColumn2Type0 / totalType0;
		double averageColumn3Type0 = totalColumn3Type0 / totalType0;
		double averageColumn0Type1 = totalColumn0Type1 / totalType1;
		double averageColumn1Type1 = totalColumn1Type1 / totalType1;
		double averageColumn2Type1 = totalColumn2Type1 / totalType1;
		double averageColumn3Type1 = totalColumn3Type1 / totalType1;
		double averageColumn0Type2 = totalColumn0Type2 / totalType2;
		double averageColumn1Type2 = totalColumn1Type2 / totalType2;
		double averageColumn2Type2 = totalColumn2Type2 / totalType2;
		double averageColumn3Type2 = totalColumn3Type2 / totalType2;
		
		double[][] averageArray = {{averageColumn0Type0, averageColumn1Type0, averageColumn2Type0, averageColumn3Type0}, {averageColumn0Type1, averageColumn1Type1,
			averageColumn2Type1, averageColumn3Type1}, {averageColumn0Type2, averageColumn1Type2, averageColumn2Type2, averageColumn3Type2}};
		
		String s = Arrays.deepToString(averageArray);
		System.out.println(s);
	
		return averageArray;
	}
	
	public static int[] compareToAverages(double[][] averageArray, double[][] dataArray)
	{
		int[] guessedFlower = new int[dataArray.length];
		double type1Compare = 0;
		double type2Compare = 0;
		double type3Compare = 0;
		for (int r = 0; r <= dataArray.length; r++)
		{
			type1Compare = Math.pow(averageArray[0][0] - dataArray[r][0], 2) + Math.pow(averageArray[0][1] - dataArray[r][1], 2) + Math.pow(averageArray[0][2] - dataArray[r][2], 2) + Math.pow(averageArray[0][3] - dataArray[r][3], 2);
			type2Compare = Math.pow(averageArray[1][0] - dataArray[r][0], 2) + Math.pow(averageArray[1][1] - dataArray[r][1], 2) + Math.pow(averageArray[1][2] - dataArray[r][2], 2) + Math.pow(averageArray[1][3] - dataArray[r][3], 2);
			type3Compare = Math.pow(averageArray[2][0] - dataArray[r][0], 2) + Math.pow(averageArray[2][1] - dataArray[r][1], 2) + Math.pow(averageArray[2][2] - dataArray[r][2], 2) + Math.pow(averageArray[2][3] - dataArray[r][3], 2);
			
			if (type1Compare > type2Compare && type1Compare > type3Compare)
			{
				guessedFlower[r] = 1;
			}
			else if (type2Compare > type1Compare && type2Compare > type3Compare)
			{
				guessedFlower[r] = 2;
			}
			else
			{
				guessedFlower[r] = 3;
			}
		}
		
		
		return guessedFlower;
	}
	
	public static String determineAccurary(int[] guessedFlower, double[][] dataArray)
	{
		int correctCount = 0;
		int totalCount = guessedFlower.length;
		for (int r = 0; r < guessedFlower.length; r++)
		{
			if (guessedFlower[r] == dataArray[r][4])
			{
				correctCount++;
			}
		}
		double percentCorrect = 100 * (correctCount / totalCount);
		return String.format("The accuracy of the flower sample to flower species is %s.", percentCorrect);
	}
}
 