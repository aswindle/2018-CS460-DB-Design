/*=============================================================================
 |   Assignment:  Program #1B:  Creating and Interpolation--Searching a Binary File
 |       Author:  Alex Swindle (aswindle@email.arizona.edu)
 |       Grader:  Terrence Lim
 |
 |       Course:  CSC 460
 |   Instructor:  L. McCann
 |     Due Date:  9/6/18
 |
 |  Description:  Retrieves data from a binary file of flight record data. Prints the first 5, middle 3 (or 4), and last
 |      5 records, followed by the total number of records printed. Then asks the user to input a flight number to
 |      search for, and uses interpolation search to find the number of records matching it. Continues until the user
 |      enters 0.
 |
 |     Language:  Java 8
 | Ex. Packages:  None
 |
 | Deficiencies:  Certain search values will cause a StackOverflow exception. Interpolation search is clearly off by 1
        on some index change, as it gets stuck in an infinite loop searching on both sides of a repeated flight number
        Known crashes on the large file: 12, 15, 2817
 *===========================================================================*/


import java.io.*;
import java.util.Scanner;

/**
 * Methods other than main:
 * public static int interpSearch(RandomAccessFile input, int target, int low, int high)
 * public static int getFlNum(RandomAccessFile input, int i)
 * public static void printRecord(RandomAccessFile input, int i)
 */
public class Prog1B {
    // Will store constants that are read from the binary file
    private static int numberOfRecords;
    private static int sizeOfRecord;
    private static int maxFlDate;
    private static int maxCarrier;
    private static int maxFlNum;
    private static int maxTailNum;
    private static int maxOrigin;
    private static int maxDest;
    private static int maxDepTime;
    private static int maxWheelsOff;
    private static int maxWheelsOn;
    private static int maxArrTime;
    private static int maxCancellationCode;
    private static int index0 = 52;
    private static final String separator = "-----------------------------";

    /**
     * Print first 5 records
     * Print middle 3 records (if total is odd) or middle 4 records (if total is even)
     * Print last 5 records
     * Print total # of records
     *
     * Ask user to search for flight numbers until 0 is entered
     *
     * @param args
     */
    public static void main(String[] args) {
        File binaryFile;
        RandomAccessFile input = null;

        // File path is given as first command-line argument
        try {
            binaryFile = new File(args[0]);
            input = new RandomAccessFile(binaryFile, "r");
        }
        catch (IOException e) {
            System.out.println("Error opening file. Exiting.");
            System.exit(-1);
        }

        // Read the sizes from the binary file
        try {
            // Start at the beginning of the file
            input.seek(0);

            // Read the 13 ints stored there: number of records, size of each record, size of all 11 String fields
            numberOfRecords = input.readInt();
            sizeOfRecord = input.readInt();
            maxFlDate = input.readInt();
            maxCarrier = input.readInt();
            maxFlNum = input.readInt();
            maxTailNum = input.readInt();
            maxOrigin = input.readInt();
            maxDest = input.readInt();
            maxDepTime = input.readInt();
            maxWheelsOff = input.readInt();
            maxWheelsOn = input.readInt();
            maxArrTime = input.readInt();
            maxCancellationCode = input.readInt();
        }
        catch (IOException e) {
            System.out.println("Error reading the first line. Exiting.");
            System.exit(-1);
        }

        // Will modify how many records are printed if there are < 5
        int max = 5;
        if (numberOfRecords < max) {
            max = numberOfRecords;
        }

        // Print first 5 records
        System.out.println("FIRST FIVE RECORDS:");
        for (int i = 0; i < max; i++) {
            printRecord(input, i);
        }
        System.out.println(separator);

        // Print middle records

        // If < 3 records, then just print all again
        if (numberOfRecords < 3) {
            System.out.println("MIDDLE RECORDS:");
            for (int i = 0; i < numberOfRecords; i++) {
                printRecord(input, i);
            }
            System.out.println(separator);
        }
        // Middle 3 if odd
        else if (numberOfRecords % 2 == 1) {
            System.out.println("MIDDLE 3 RECORDS:");
            printRecord(input, numberOfRecords / 2);
            printRecord(input, numberOfRecords / 2 + 1);
            printRecord(input, numberOfRecords / 2 + 2);
        }
        // Middle 4 if even
        else {
            System.out.println("MIDDLE 4 RECORDS:");
            printRecord(input, numberOfRecords / 2 - 1);
            printRecord(input, numberOfRecords / 2);
            printRecord(input, numberOfRecords / 2 + 1);
            printRecord(input, numberOfRecords / 2 + 2);

        }
        System.out.println(separator);

        // Print last 5 records, or fewer if not enough
        System.out.println("LAST FIVE RECORDS:");
        for (int i = max; i > 0; i--) {
            printRecord(input, numberOfRecords - i);
        }
        System.out.println(separator);

        // Print total # of records
        System.out.printf("There are %d record(s) in the file.\n", numberOfRecords);

        // Ask user to enter search queries
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter a flight number (FL_NUM) that you'd like to search i.e. 2817. Enter zero(0) to " +
                "end your search.");
        String line = kb.next();
        // Keep going until '0' is entered
        while (!line.equals("0")) {
            try {
                // Parse the entry, search for it
                int flight = Integer.parseInt(line);
                int numResults = interpSearch(input, flight, 0, numberOfRecords - 1);
                if (numResults == 0) {
                    System.out.println("No result(s) found.");
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Only enter integers.");
            }

            // Prep next iteration of loop
            System.out.println("Enter a flight number (FL_NUM) that you'd like to search i.e. 2817. Enter zero(0) to " +
                    "end your search.");
            line = kb.next();
        }

        // End of program
        System.out.println("End of search.");
        System.out.println("End of program.");
    }

    /**
     * Use interpolation search to print all records in an input file with a flNum field matching a particular int
     *
     * @param input:  file to search
     * @param target: flight number to search for
     * @param low:    low index for interpolation search
     * @param high:   high index for interpolation search
     */
    public static int interpSearch(RandomAccessFile input, int target, int low, int high) {
        int result = 0;
        // If there's nothing left to search, stop searching
        if (low >= high || low < 0 || high >= numberOfRecords) {
            return 0;
        }

        else {
            int lowKey = getFlNum(input, low);
            int highKey = getFlNum(input, high);

            int probe = low;
            if (lowKey != highKey) {
                // Calculate probe index according to the formula
                probe = (int)(low + Math.abs((double)((target - lowKey) * (high - low)) / (highKey - lowKey)));
                System.out.println(probe);
            }

            // Get the flNum at the probe index
            int probeFlight = getFlNum(input, probe);

            // Found the target
            if (target == probeFlight) {
                printRecord(input, probe);
                result++;
            }

            // Search again on either side, adjusting by 1
            // Probe was too big; search left
            if (probeFlight > target) {
                return result + interpSearch(input, target, low, probe - 1);
            }
            // Probe was too small; search right
            else {
                return result + interpSearch(input, target, probe + 1, high);
            }
        }
    }


    /**
     * Get the flight number (as an int) of the Flight Record at index i of a binary file.
     *
     * @param input: file to search
     * @param i:     index of the flight number to look at
     * @return the integer version of FlNum String
     */
    public static int getFlNum(RandomAccessFile input, int i) {
        int flight = 0;
        // Find beginning of the record
        int pointer = index0 + i * sizeOfRecord;
        // flNum, which is at flDate + uniqueCarrier + 4 (airlineID int) + tailNum
        int flNumOffset = maxFlDate + maxCarrier + 4 + maxTailNum;
        try {
            input.seek(pointer + flNumOffset);
            byte[] flNumBytes = new byte[maxFlNum];
            input.read(flNumBytes);
            String flNum = new String(flNumBytes).trim();
            try {
                flight = Integer.parseInt(flNum);
            }
            catch (NumberFormatException e) {
            }
        }
        catch (IOException e) {
            System.out.println("Error reading an FlNum field.");
        }
        return flight;
    }

    /**
     * Prints relevant information about record i in binary file 'input'
     * Prints '[i] <UniqueCarrier>, <FlNum>, <Origin>, <Dest>'
     * Assumes that index i exists in the file
     *
     * @param input: binary file to retrieve data from
     * @param i:     index of record to print
     */
    public static void printRecord(RandomAccessFile input, int i) {
        // Find beginning of the record
        int pointer = index0 + i * sizeOfRecord;
        // Fields we care about:
        // uniqueCarrier, which is at beginning + maxFlDate
        int carrierOffset = maxFlDate;
        // flNum, which is at flDate + uniqueCarrier + 4 (airlineID int) + tailNum
        int flNumOffset = carrierOffset + maxCarrier + 4 + maxTailNum;
        // origin, which is just past flNum
        int originOffset = flNumOffset + maxFlNum;
        // dest, just past origin
        int destOffset = originOffset + maxOrigin;

        // Print the 4 relevant fields
        try {
            input.seek(pointer + carrierOffset);
            byte[] carrierBytes = new byte[maxCarrier];
            input.read(carrierBytes);
            String carrier = new String(carrierBytes);

            input.seek(pointer + flNumOffset);
            byte[] flNumBytes = new byte[maxFlNum];
            input.read(flNumBytes);
            String flNum = new String(flNumBytes);

            input.seek(pointer + originOffset);
            byte[] originBytes = new byte[maxOrigin];
            input.read(originBytes);
            String origin = new String(originBytes);

            input.seek(pointer + destOffset);
            byte[] destBytes = new byte[maxDest];
            input.read(destBytes);
            String dest = new String(destBytes);

            System.out.printf("[%d] %s, %s, %s, %s\n", i, carrier, flNum, origin, dest);

        }
        catch (IOException e) {
            System.out.println("Error reading a flight record's data.");
        }
    }
}