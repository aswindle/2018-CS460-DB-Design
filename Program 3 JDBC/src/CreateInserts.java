/**
 * Assignment:  Program #3: JDBC
 * Author:  Alex Swindle (aswindle@email.arizona.edu)
 * Grader:  Terrence Lim
 *
 * Course: CSC 460
 * Instructor: L. McCann
 * Due Date: 11/1/18
 *
 * Description: Takes scrubbed CSV files of FL education data and creates a SQL file that will insert each row into the
 * proper table.
 *
 *
 * Language: Java 8
 * External Packages: None
 *
 * Deficiencies: None
 *
 * String Constants:
 * insert2015
 * insert2016
 * insert2017
 * insert2018
 * valuesStart
 * valuesEnd
 *
 * Public Methods:
 * none
 */

import java.io.*;

public class CreateInserts {
    private final static String insert2015 = "INSERT INTO data2015 VALUES (";
    private final static String insert2016 = "INSERT INTO data2016 VALUES (";
    private final static String insert2017 = "INSERT INTO data2017 VALUES (";
    private final static String insert2018 = "INSERT INTO data2018 VALUES (";
    private final static String valuesEnd = ");\n";

    /**
     * Create an 'insert201X.sql' file filled with the data in a year's CSV file
     * @param insertYear: name of table to insert into. 'data2015' through 'data2018'
     * @param br: BufferedReader object initialized to read that year's CSV file
     * @param writer: PrintWriter object initialized to write to that year's .sql file
     */
    private static void createYear(String insertYear, BufferedReader br, PrintWriter writer){
        String curLine;
        try{
            //Skip over the first header line with column names and the state total lines
            br.readLine();
            br.readLine();
            //Write the insert statement, followed by 'VALUES (', followed by a line of data, followed by closing ');'
            while((curLine = br.readLine()) != null){
                writer.print(insertYear);
                writer.print(curLine);
                writer.print(valuesEnd);
            }
            br.close();
            writer.close();
        }
        catch(IOException e){
            System.out.println("Error reading lines.");
            System.exit(-1);
        }
    }
    public static void main(String[] args){
        File data2015 = new File("2015scrubbed.csv");
        File output2015 = new File("insert2015.sql");

        File data2016 = new File("2016scrubbed.csv");
        File output2016 = new File("insert2016.sql");

        File data2017 = new File("2017scrubbed.csv");
        File output2017 = new File("insert2017.sql");

        File data2018 = new File("2018scrubbed.csv");
        File output2018 = new File("insert2018.sql");

        BufferedReader br = null;
        PrintWriter writer = null;
        try {
            br = new BufferedReader(new FileReader(data2015));
            writer = new PrintWriter(output2015);
            createYear(insert2015, br, writer);

            br = new BufferedReader(new FileReader(data2016));
            writer = new PrintWriter(output2016);
            createYear(insert2016, br, writer);

            br = new BufferedReader(new FileReader(data2017));
            writer = new PrintWriter(output2017);
            createYear(insert2017, br, writer);

            br = new BufferedReader(new FileReader(data2018));
            writer = new PrintWriter(output2018);
            createYear(insert2018, br, writer);

            br.close();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Couldn't open file. Exiting.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
