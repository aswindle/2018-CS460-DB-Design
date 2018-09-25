/**
 * Assignment:  Program #2: Dynamic Hashing
 * Author:  Alex Swindle (aswindle@email.arizona.edu)
 * Grader:  Terrence Lim
 *
 * Course: CSC 460
 * Instructor: L. McCann
 * Due Date: 9/20/18
 *
 * Description: Indexes a binary file of flight data using dynamic hashing. Opens the file (passed as a command line
 * argument) and then creates a tree structure of nodes and buckets, along with a binary file called 'bucketFile.bin'
 * that will store the data. Inserts as much data as possible until a bucket
 * can't be split anymore, then switches to asking the user to search for arrival times. Any found records are
 * printed along with a count of how many were found. Exits when the user enters '0000'.
 *
 *
 * Language: Java 8
 * External Packages: None
 *
 * Deficiencies: None
 */

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Methods other than main:
 *
 * public static String getArrTime(RandomAccessFile input, int i)
 * public static void printRecord(RandomAccessFile input, int i)
 */
public class Prog2 {
    // Will store constants that are read from the binary file; same as Program 1B
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

    /**
     * Open the flight data binary file
     * Read sizes of fields from the file
     * Create binary file for buckets
     * Create root node for the index
     * Process the flight data, insert index data into the bucket file
     * Allow the user to search for arrTimes until they enter 0000
     *
     * @param args
     */
    public static void main(String[] args) {
        // OPEN THE FILE
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

        // CREATE BUCKET BINARY FILE
        RandomAccessFile bucketFile = null;
        try {
            bucketFile = new RandomAccessFile(new File("bucketFile.bin"), "rw");
        }
        catch (IOException e) {
            System.out.println("Error creating bucket binary file.");
            System.exit(-1);
        }

        // CREATE STRUCTURE FOR THE INDEX
        Node root = new Node(null, "", bucketFile);

        // PROCESS THE FILE
        try {
            int accepted = 0;
            int rejected = 0;
            int blank = 0;
            input.seek(index0);
            int curIndex = 0;
            boolean notFull = true;
            while (notFull) {
                // Get the arrTime field. If it's valid, make a new BucketEntry and insert it.
                String curArrTime = getArrTime(input, curIndex);
                if (!curArrTime.equals("BAD ARRTIME") && !curArrTime.equals("BLANK")) {
                    BucketEntry curBucketEntry = new BucketEntry(curArrTime, curIndex);
                    notFull = root.insert(curBucketEntry);
                    if(notFull){
                        accepted++;
                    }
                }
                else {
                    if (curArrTime.equals("BLANK")) {
                        blank++;
                    }
                    else {
                        rejected++;
                    }
                }
                curIndex++;
            }
            System.out.printf("Accepted %d, rejected %d malformed records, skipped %d blank.\n", accepted, rejected,
                    blank);
        }
        catch (IOException e) {
            System.out.println("Error reading flight records.");
            System.exit(-1);
        }


        // USER SEARCH QUERIES
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter an ARR_TIME or ARR_TIME fragment that you'd like to search. Must be 1-4 digits. " +
                "Enter 0000 to exit.");
        String line = kb.next();
        // Keep going until '0000' is entered
        while (!line.equals("0000")) {
            // Check for incorrect lengths
            if (line.length() > 4 || line.length() < 1) {
                System.out.println("Entry error: must have between 1 and 4 digits.");
            }
            else {
                // Check for invalid characters
                boolean valid = true;
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (!Character.isDigit(c)) {
                        System.out.println("ERROR: entry contained a non-digit character.");
                        valid = false;
                    }
                }
                // Actually perform the search if the string is good.
                if (valid) {
                    LinkedList<BucketEntry> results = root.search(line);
                    for (BucketEntry be : results) {
                        printRecord(input, be.i);
                    }
                    System.out.printf("%d record(s) found.\n", results.size());
                }
            }

            // Prep next iteration of loop
            System.out.println("Enter an ARR_TIME or ARR_TIME fragment that you'd like to search. Must be 1-4 digits." +
                    " " +
                    "Enter 0000 to exit.");
            line = kb.next();
        }

        // End of program
        System.out.println("End of search.");
        System.out.println("End of program.");
    }

    /**
     * Get the arrTime of the Flight Record at index i of a binary file.
     *
     * @param input: file to search
     * @param i:     index of the flight record to look at
     * @return the arrTime as a 4-digit string front-padded with 0s, "BLANK" if the field was blank, or "BAD ARRTIME"
     * if there was any other error
     */
    public static String getArrTime(RandomAccessFile input, int i) {
        // Find beginning of the record
        int pointer = index0 + i * sizeOfRecord;
        // Get offset for arrTime based on the size of the preceding fields, some of which are doubles and ints
        int arrTimeOffset = maxFlDate + maxCarrier + 4 + maxTailNum + maxFlNum + maxOrigin + maxDest + maxDepTime +
                16 + maxWheelsOff + maxWheelsOn + 8;
        try {
            input.seek(pointer + arrTimeOffset);
            byte[] arrTimeBytes = new byte[4];
            input.read(arrTimeBytes);
            String arrTime = new String(arrTimeBytes).trim();
            if (arrTime.equals("")) {
                return "BLANK";
            }
            else {
                // Pad with leading 0s if necessary
                while (arrTime.length() < 4) {
                    arrTime = "0" + arrTime;
                }
                return arrTime;
            }
        }
        catch (IOException e) {
            System.out.println("Error reading the ArrTime field.");
        }
        return "BAD ARRTIME";
    }

    /**
     * Prints relevant information about record i in binary file 'input'
     * Prints '[i] <UniqueCarrier>, <FlNum>, <ArrTime>'
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
        // Get offset for arrTime based on the size of the preceding fields, some of which are doubles and ints
        int arrTimeOffset = maxFlDate + maxCarrier + 4 + maxTailNum + maxFlNum + maxOrigin + maxDest + maxDepTime +
                16 + maxWheelsOff + maxWheelsOn + 8;

        // Print the 3 relevant fields
        try {
            input.seek(pointer + carrierOffset);
            byte[] carrierBytes = new byte[maxCarrier];
            input.read(carrierBytes);
            String carrier = new String(carrierBytes);

            input.seek(pointer + flNumOffset);
            byte[] flNumBytes = new byte[maxFlNum];
            input.read(flNumBytes);
            String flNum = new String(flNumBytes);

            input.seek(pointer + arrTimeOffset);
            byte[] arrTimeBytes = new byte[4];
            input.read(arrTimeBytes);
            String arrTime = new String(arrTimeBytes);

            System.out.printf("[%d] %s, %s, %s\n", i, carrier, flNum, arrTime);
        }
        catch (IOException e) {
            System.out.println("Error reading a flight record's data.");
        }
    }
}
