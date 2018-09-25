/*=============================================================================
 |   Assignment:  Program #1A:  Creating and Interpolation--Searching a Binary File
 |       Author:  Alex Swindle (aswindle@email.arizona.edu)
 |       Grader:  Terrence Lim
 |
 |       Course:  CSC 460
 |   Instructor:  L. McCann
 |     Due Date:  8/30/18
 |
 |  Description:  Opens a csv file (with the file path passed as a command-line argument) that contains
 |                  lines formatted to contain 19 fields of flight data and writes it to a binary file,
 |                  sorted by the flight number.
 |                  The binary file maintains the same filename as the input, ending in .bin.
 |
 |     Language:  Java 8
 | Ex. Packages:  None
 |
 | Deficiencies:  None; however, I'm turning it in on 8/24 (out of town the following week), so I suspect I'll be
 |                  missing a few clarification items that may crop up on Piazza
 *===========================================================================*/

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Methods other than main():
 * private static FlightRecord parseLine(String line)
 * private static String parseFieldString(String field)
 * private static double parseFieldDouble(String field)
 * private static int parseFieldInt(String field)
 */
public class Prog1A {
    // Store the longest length of all of the String fields
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

    /**
     * Main:
     *
     * Reads CSV file
     * Creates a FlightRecord object from each line by parsing it, adds it to ArrayList of FlightRecords
     * Sorts the list (by flight number)
     * Creates a binary file with the same filename but a .bin extension
     * Creates space for 13 integers at the beginning of the file
     * Writes each FlightRecord to the file
     * Checks that the correct number of records were written
     * Writes the final number and size of records and size of each of the 11 String fields to the beginning of the file
     * Closes the file
     *
     * Pre-conditions: csv file path must be passed as first element of args, csv file must exist
     *
     * @param args: args[0] must be path to the file to be parsed
     */
    public static void main(String[] args) {
        // Path to the file is given as the first command-line argument
        File input = new File(args[0]);

        // Get the actual filename, minus the extension
        String fileName = input.getName().substring(0, input.getName().indexOf('.'));

        // Attempt to open the file
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(input));
        }
        catch (IOException e) {
            System.out.println("Couldn't open file. Exiting.");
            e.printStackTrace();
            System.exit(-1);
        }

        // Create list of flight records, counter of how many were processed
        ArrayList<FlightRecord> records = new ArrayList<FlightRecord>();
        int numberOfFlights = 0;

        // Convert each line into a FlightRecord, add it to the list
        try {
            // Skip over the header line
            br.readLine();

            // Read each line and create a FlightRecord from it
            // maximum size of String fields will be updated as needed when lines are parsed
            String line = br.readLine();
            while (line != null) {
                FlightRecord curFlight = parseLine(line);
                records.add(curFlight);
                numberOfFlights++;

                // Read the next line
                line = br.readLine();
            }
        }
        catch (IOException e) {
            System.out.println("Error parsing a line. Exiting.");
            System.exit(-1);
        }

        // For each FlightRecord, all Strings shorter than that field's max size will be
        // right-padded with spaces
        for (FlightRecord cur : records) {
            if (cur.getFlDate().length() < maxFlDate) {
                cur.setFlDate(cur.pad(cur.getFlDate(), maxFlDate));
            }
            if (cur.getUniqueCarrier().length() < maxCarrier) {
                cur.setUniqueCarrier(cur.pad(cur.getUniqueCarrier(), maxCarrier));
            }
            if (cur.getFlNum().length() < maxFlNum) {
                cur.setFlNum(cur.pad(cur.getFlNum(), maxFlNum));
            }
            if (cur.getTailNum().length() < maxTailNum) {
                cur.setTailNum(cur.pad(cur.getTailNum(), maxTailNum));
            }
            if (cur.getOrigin().length() < maxOrigin) {
                cur.setOrigin(cur.pad(cur.getOrigin(), maxOrigin));
            }
            if (cur.getDest().length() < maxDest) {
                cur.setDest(cur.pad(cur.getDest(), maxDest));
            }
            if (cur.getDepTime().length() < maxDepTime) {
                cur.setDepTime(cur.pad(cur.getDepTime(), maxDepTime));
            }
            if (cur.getWheelsOff().length() < maxWheelsOff) {
                cur.setWheelsOff(cur.pad(cur.getWheelsOff(), maxWheelsOff));
            }
            if (cur.getWheelsOn().length() < maxWheelsOn) {
                cur.setWheelsOn(cur.pad(cur.getWheelsOn(), maxWheelsOn));
            }
            if (cur.getArrTime().length() < maxArrTime) {
                cur.setArrTime(cur.pad(cur.getArrTime(), maxArrTime));
            }
            if (cur.getCancellationCode().length() < maxCancellationCode) {
                cur.setCancellationCode(cur.pad(cur.getCancellationCode(), maxCancellationCode));
            }
        }

        // Sort the records by the flight number. See the compareTo method in FlightRecord
        Collections.sort(records);

        // Create the output binary file
        File output = new File(fileName + ".bin");
        RandomAccessFile dataWriter = null;
        try {
            dataWriter = new RandomAccessFile(output, "rw");

            // Write 52 bytes (13 integers) to the start of the file:
            //  number of records
            //  size of each record
            //  the 11 String sizes
            // Will be overwritten at the end, but allows for correct number of bytes
            dataWriter.seek(0);
            dataWriter.write(new byte[52]);
        }
        catch (IOException e) {
            System.out.println("Error creating file. Exiting.");
            System.exit(-1);
        }

        // Write each FlightRecord to the file
        for (FlightRecord record : records) {
            record.writeObject(dataWriter);
        }

        // Check that records processed == records written

        // Size of the records:
        // 1 int
        // 7 doubles
        // 11 Strings, each with its own size variable
        int recordSize = 1 * 4 + 7 * 8 + maxFlDate + maxCarrier + maxFlNum + maxTailNum + maxOrigin + maxDest +
                maxDepTime + maxWheelsOff + maxWheelsOn + maxArrTime + maxCancellationCode;

        try {
            double writtenRecords = dataWriter.length() / recordSize;
            System.out.println("Records written: " + (int) writtenRecords);
            if ((int) writtenRecords != numberOfFlights) {
                System.out.println("Error: number of lines parsed didn't match number of lines written.");
            }
        }
        catch (IOException e) {
            System.out.println("Couldn't read size of file.");
        }

        // Overwrite the beginning of the file to be the correct integers
        // Written in the same order as they're declared (which is the same order as the fields in the file)
        try {
            // Go to the beginning of the file
            dataWriter.seek(0);

            // Write the number of records and the size of each record
            dataWriter.writeInt(numberOfFlights);
            dataWriter.writeInt(recordSize);

            // Write all 11 ints that store String sizes
            dataWriter.writeInt(maxFlDate);
            dataWriter.writeInt(maxCarrier);
            dataWriter.writeInt(maxFlNum);
            dataWriter.writeInt(maxTailNum);
            dataWriter.writeInt(maxOrigin);
            dataWriter.writeInt(maxDest);
            dataWriter.writeInt(maxDepTime);
            dataWriter.writeInt(maxWheelsOff);
            dataWriter.writeInt(maxWheelsOn);
            dataWriter.writeInt(maxArrTime);
            dataWriter.writeInt(maxCancellationCode);
        }
        catch (IOException e) {
            System.out.println("Error writing the max String sizes.");
        }

        // Close the file
        try {
            dataWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error closing the file. Exiting.");
            System.exit(-1);
        }
    }

    /**
     * Parse a comma-separated String into a 19-field FlightRecord object
     * Strings have quotes removed and null strings become ""
     * Null doubles are stored as -1
     * Null ints are stored as -1
     *
     * The maximum size of each String field is also updated if this record's String field is longer than the current
     * max
     *
     * @param line: comma-separated string from a flight record csv file
     * @return a FlightRecord object with all 19 fields populated
     */
    private static FlightRecord parseLine(String line) {
        FlightRecord flight = new FlightRecord();
        try {
            // Break the line into an array of strings
            String[] fields = line.split(",");

            // Set each of the 19 fields of the FlightRecord using the array

            // Parse the ints and doubles, remove quotes around the strings
            // Check to see if any of the Strings are longer than the current max length, update max if so
            flight.setFlDate(parseFieldString(fields[0]));
            if (flight.getFlDate().length() > maxFlDate) {
                maxFlDate = flight.getFlDate().length();
            }
            flight.setUniqueCarrier(parseFieldString(fields[1]));
            if (flight.getUniqueCarrier().length() > maxCarrier) {
                maxCarrier = flight.getUniqueCarrier().length();
            }
            flight.setAirlineId(parseFieldInt(fields[2]));
            flight.setTailNum(parseFieldString(fields[3]));
            if (flight.getTailNum().length() > maxTailNum) {
                maxTailNum = flight.getTailNum().length();
            }
            flight.setFlNum(parseFieldString(fields[4]));
            if (flight.getFlNum().length() > maxFlNum) {
                maxFlNum = flight.getFlNum().length();
            }
            flight.setOrigin(parseFieldString(fields[5]));
            if (flight.getOrigin().length() > maxOrigin) {
                maxOrigin = flight.getOrigin().length();
            }
            flight.setDest(parseFieldString(fields[6]));
            if (flight.getDest().length() > maxDest) {
                maxDest = flight.getDest().length();
            }
            flight.setDepTime(parseFieldString(fields[7]));
            if (flight.getDepTime().length() > maxDepTime) {
                maxDepTime = flight.getDepTime().length();
            }
            flight.setDepDelay(parseFieldDouble(fields[8]));
            flight.setTaxiOut(parseFieldDouble(fields[9]));
            flight.setWheelsOff(parseFieldString(fields[10]));
            if (flight.getWheelsOff().length() > maxWheelsOff) {
                maxWheelsOff = flight.getWheelsOff().length();
            }
            flight.setWheelsOn(parseFieldString(fields[11]));
            if (flight.getWheelsOn().length() > maxWheelsOn) {
                maxWheelsOn = flight.getWheelsOn().length();
            }
            flight.setTaxiIn(parseFieldDouble(fields[12]));
            flight.setArrTime(parseFieldString(fields[13]));
            if (flight.getArrTime().length() > maxArrTime) {
                maxArrTime = flight.getArrTime().length();
            }
            flight.setArrDelay(parseFieldDouble(fields[14]));
            flight.setCancelled(parseFieldDouble(fields[15]));
            flight.setCancellationCode(parseFieldString(fields[16]));
            if (flight.getCancellationCode().length() > maxCancellationCode) {
                maxCancellationCode = flight.getCancellationCode().length();
            }
            flight.setAirTime(parseFieldDouble(fields[17]));
            flight.setDistance(parseFieldDouble(fields[18]));
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid numerical value.");
            System.out.println(flight);
        }
        return flight;
    }

    /**
     * Handle parsing a String field for the FlightRecord object.
     * Null Strings become empty strings to allow for padding
     * Quotes are removed
     *
     * @param field: string to handle
     * @return: properly formatted String
     */
    private static String parseFieldString(String field) {
        if (field == null) {
            return "";
        }
        else {
            return field.replaceAll("\"", "");
        }
    }

    /**
     * Parses a String into an appropriate double for a FlightRecord field.
     * Any String that can't be parsed returns -1
     *
     * @param field: String to parse
     * @return: the double version of that string or -1 if it throws an exception
     */
    private static double parseFieldDouble(String field) {
        try {
            return Double.parseDouble(field);
        }
        catch (NumberFormatException e) {
            return -1.0;
        }
    }

    /**
     * Parses a String into an appropriate int for a FlightRecord field.
     * Any String that can't be parsed returns -1
     *
     * @param field: String to parse
     * @return: the int version of that String or -1 if it throws an exception
     */
    private static int parseFieldInt(String field) {
        try {
            return Integer.parseInt(field);
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }
}