/**
 * Assignment:  Program #3: JDBC
 * Author:  Alex Swindle (aswindle@email.arizona.edu)
 * Grader:  Terrence Lim
 *
 * Course: CSC 460
 * Instructor: L. McCann
 * Due Date: 11/1/18
 *
 * Description: Takes CSV files from FL assessment data that have already been partially processed and further scrubs them.
 * Escapes single-quote characters, replaces '*' with NULL, replaces '$!' with ',' in commas and deletes it from ints
 * Adds single quotes around all String fields
 * Also tracks the size of the largest String fields to determine how large the columns should be in the SQL table
 *
 * Language: Java 8
 * External Packages: None
 *
 * Deficiencies: None
 *
 * Constants:
 * none
 *
 * Constructors:
 * none
 *
 * Public Methods:
 * none
 */

import java.io.*;

public class ScrubCSV {

    /**
     * Do preliminary scrubbing of a line:
     * -Replace the * characters in a line with NULL
     * -Also fix any lines with a student population > 3 characters (e.g. 1,234)
     * Those show up in CSV file as "1,234", so remove the comma between the quotes and the quotes themselves
     * @param line: line to scrub
     * @return the line with the fixes applied
     */
    private static String scrubLine(String line){
        //Replace * with NULL
        String retVal = line;
        retVal = retVal.replaceAll("\\*", "NULL");
        int firstQuote = line.indexOf("\"");
        if(firstQuote != -1){
            int comma = line.indexOf(",", firstQuote);
            retVal = line.substring(0, comma) + line.substring(comma + 1);
            retVal = retVal.replaceAll("\\\"", "");
        }
        return retVal;
    }

    /**
     * Escape single quotes, replace stripped out commas, and wrap a String with single quotes
     * @param s: String to process
     * @return: cleaned up version of the string
     */
    private static String scrubString(String s) {
        String retVal = s;
        retVal = retVal.replaceAll("'", "''");
        retVal = retVal.replaceAll("\\$!", ",");
        return "'" + retVal + "'";
    }

    /**
     * Remove any commas from an integer that had already been replaced with $!
     * @param s: String to process
     * @return s with $! removed
     */
    private static String scrubInt(String s){
        return s.replaceAll("\\$!", "");
    }

    /**
     * Create a scrubbed version of a year's CSV file and track the largest size of the two string fields
     * @param year: 2015-2018
     * @param br: BufferedReader object that's been initialized to use that year's CSV file
     * @param writer: PrintWriter object that's been initialized to use that year's output CSV file
     */
    private static void processFile(String year, BufferedReader br, PrintWriter writer) {
        String curLine;
        try {
            //Write the first header line
            curLine = br.readLine();
            writer.println(curLine);
            // track largest string fields
            int maxDistrictName = 0;
            int maxSchoolName = 0;
            int linesProcessed = 0;
            // Loop through the rest of the lines, padding the text fields with " at start and end
            while ((curLine = br.readLine()) != null) {
                linesProcessed++;
                // Replace the * with NULL using scrubLine
                String[] fields = scrubLine(curLine).split(",");
                // Escape the single quotes and replace $! with commas in the string fields
                fields[1] = scrubString(fields[1]);
                fields[3] = scrubString(fields[3]);
                fields[4] = scrubInt(fields[4]);
                if (fields[1].length() > maxDistrictName) {
                    maxDistrictName = fields[1].length();
                }
                if (fields[3].length() > maxSchoolName) {
                    maxSchoolName = fields[3].length();
                }
                for (int i = 0; i < fields.length - 1; i++) {
                    writer.print(fields[i] + ",");
                }
                writer.print(fields[fields.length - 1] + "\n");
            }
            // Print results, close streams
            System.out.printf("Processed %d for %s\n", linesProcessed, year);
            System.out.printf("%s\nMax school district: %d\nMax school name: %d\n", year, maxDistrictName,
                    maxSchoolName);
            br.close();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Error reading lines.");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        //Inputs
        File data2015 = new File("2015.csv");
        File data2016 = new File("2016.csv");
        File data2017 = new File("2017.csv");
        File data2018 = new File("2018.csv");
        //Outputs
        File output15 = new File("2015scrubbed.csv");
        File output16 = new File("2016scrubbed.csv");
        File output17 = new File("2017scrubbed.csv");
        File output18 = new File("2018scrubbed.csv");

        BufferedReader br = null;
        PrintWriter writer = null;
        try {
            // Process all 4 years
            br = new BufferedReader(new FileReader(data2015));
            writer = new PrintWriter(output15);
            processFile("2015", br, writer);

            br = new BufferedReader(new FileReader(data2016));
            writer = new PrintWriter(output16);
            processFile("2016", br, writer);

            br = new BufferedReader(new FileReader(data2017));
            writer = new PrintWriter(output17);
            processFile("2017", br, writer);

            br = new BufferedReader(new FileReader(data2018));
            writer = new PrintWriter(output18);
            processFile("2018", br, writer);
        }
        catch (IOException e) {
            System.out.println("Couldn't open file. Exiting.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
