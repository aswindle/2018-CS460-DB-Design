/**
 * Assignment:  Program #3: JDBC
 * Author:  Alex Swindle (aswindle@email.arizona.edu)
 * Grader:  Terrence Lim
 *
 * Course: CSC 460
 * Instructor: L. McCann
 * Due Date: 11/1/18
 *
 * Description: Creates a connection to the Oracle database of FL data and allows the user to select queries to perform
 * from a text-based menu. Prompts for needed information (year and district name) when necessary within a query.
 * Keeps going until the user chooses the exit option from the menu.
 *
 * Language: Java 8
 * External Packages: None
 *
 * Deficiencies: None
 *
 * Constants: No public constants
 *
 * Constructors:
 * none
 *
 * Public Methods:
 * none
 */

import java.sql.*;
import java.util.Scanner;

public class Prog3 {

    // Text-based menu
    private final static String menu =
            "Enter 1-5 to make a query selection:\n" +
                    "1) Names and count of schools with different names in 2015 and 2018\n" +
                    "2) Info about schools with decreasing achievement percentages\n" +
                    "3) Top combined Level 4 and 5 percentages for a district in a year\n" +
                    "4) Most improved schools in a district\n" +
                    "5) Exit\n";

    /**
     * Loop until the user enters an integer from 1 to 5; return that number
     *
     * @param kb: Scanner initialized to System.in
     * @return 1, 2, 3, 4, or 5
     */
    private static int getMenuChoice(Scanner kb) {
        while (true) {
            System.out.print(menu);
            String result = kb.next();
            switch (result) {
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                    //Consume the \n still on the input stream
                    kb.nextLine();
                    return Integer.parseInt(result);
            }
        }
    }

    /**
     * Loop until the user inputs a year between 2015 and 2018; return a table name once one of those 4 is entered
     *
     * @param kb: Scanner initialized to System.in
     * @return "data2015", "data2016", "data2017", or "data2018"
     */
    private static String getYear(Scanner kb) {
        while (true) {
            System.out.print("Enter a year (2015-2018): ");
            String attempt = kb.next();
            switch (attempt) {
                case "2015":
                case "2016":
                case "2017":
                case "2018":
                    //Consume the \n still on the input stream
                    kb.nextLine();
                    return "data" + attempt;
            }
        }
    }

    public static void main(String[] args) {
        /*
         * CONNECTION OVERHEAD SECTION
         */

        final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        String username = null,    // Oracle DBMS username
                password = null;    // Oracle DBMS password

        if (args.length == 2) {    // get username/password from cmd line args
            username = args[0];
            password = args[1];
        }
        else {
            System.out.println("\nUsage:  java JDBC <username> <password>\n"
                    + "    where <username> is your Oracle DBMS"
                    + " username,\n    and <password> is your Oracle"
                    + " password (not your system password).\n");
            System.exit(-1);
        }

        // load the (Oracle) JDBC driver by initializing its base class, 'oracle.jdbc.OracleDriver'.

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
            System.exit(-1);
        }

        // make and return a database connection to the user's Oracle database
        Connection dbconn = null;
        try {
            dbconn = DriverManager.getConnection
                    (oracleURL, username, password);
        }
        catch (SQLException e) {
            System.err.println("*** SQLException:  "
                    + "Could not open JDBC connection.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);
        }

        /*
         * QUERY SECTION
         */

        // Scanner for inputs
        Scanner kb = new Scanner(System.in);
        String year, district;

        // Pieces of the queries; will be reused by each selection
        Statement stmt;
        String query;
        ResultSet answer;

        try {
            // Prompt the user for which choice they'd like to make
            int menuChoice = getMenuChoice(kb);
            System.out.println();

            while (menuChoice != 5) {
                stmt = dbconn.createStatement();
                switch (menuChoice) {
                    // Execute selected query
                    case 1:
                        // Different names
                        query = Statements.diffNames;
                        answer = stmt.executeQuery(query);
                        if (answer != null && answer.isBeforeFirst()) {
                            System.out.println("Schools with different names in 2015 and 2018:");
                            System.out.printf("%-37s\t%-37s\n", "Old Name", "New Name");
                            int count = 0;
                            while (answer.next()) {
                                System.out.printf("%-37s\t%-37s\n", answer.getString(1), answer.getString(2));
                                count++;
                            }
                            System.out.printf("\nTotal number of schools: %d\n", count);
                        }
                        else {
                            System.out.println("No data was returned for this query.");
                        }
                        System.out.println();
                        break;
                    case 2:
                        // Decreasing achievement: needs a year
                        year = getYear(kb);
                        query = String.format(Statements.decreasingStats, year);
                        answer = stmt.executeQuery(query);
                        if (answer != null && answer.isBeforeFirst()) {
                            System.out.println("Decreasing results for " + year.substring(4) + ":");
                            System.out.println("Dist#\tSchool#\tSchool Name");
                            while (answer.next()) {
                                System.out.printf("%s\t%s\t%s\n", answer.getString(1),
                                        answer.getString(2), answer.getString(3));
                            }
                        }
                        else {
                            System.out.println("No data was returned for this query.");
                        }
                        System.out.println();
                        break;
                    case 3:
                        //Highest 4 and 5 percentages: needs year and district
                        year = getYear(kb);
                        System.out.print("Enter a district name: ");
                        district = kb.nextLine().toUpperCase();

                        //Check for at least 10 schools in the district
                        query = String.format(Statements.schoolCount, year, district);
                        answer = stmt.executeQuery(query);
                        if (answer != null && answer.isBeforeFirst()) {
                            int numschools = 0;
                            while (answer.next()) {
                                // This query sends back the district name in col 1 and the count of schools in col 2
                                numschools = Integer.parseInt(answer.getString(2));
                            }
                            stmt.close();
                            stmt = dbconn.createStatement();

                            // Do full query if there are enough schools
                            if (numschools >= 10) {
                                query = String.format(Statements.top45, year, district);
                                answer = stmt.executeQuery(query);
                                System.out.printf("%-14s\t%-37s\t%s\n", "District Name", "School Name", "Total");
                                while (answer.next()) {
                                    System.out.printf("%-14s\t%-37s\t%s\n", answer.getString(1),
                                            answer.getString(2), answer.getString(3));
                                }
                            }
                            // Print error message if there aren't enough schools
                            else {
                                System.out.printf("I'm sorry, but district %s has just %d schools.\n", district,
                                        numschools);
                            }
                        }
                        else {
                            System.out.println("No data was returned for this query.");
                        }
                        System.out.println();
                        break;
                    case 4:
                        // Most improved by district
                        System.out.print("Enter a district name: ");
                        district = kb.nextLine().toUpperCase();
                        query = String.format(Statements.mostImproved, district);
                        answer = stmt.executeQuery(query);
                        if (answer != null && answer.isBeforeFirst()) {
                            System.out.printf("Most improved schools (by pass rate) in district %s from 2015 to " +
                                            "2018:\n", district);
                            System.out.printf("%-37s\t%s\t%s\t%s\n", "School Name", "2015", "2018", "Change");
                            while (answer.next()) {
                                System.out.printf("%-37s\t%s\t%s\t%s\n", answer.getString(1),
                                        answer.getString(2), answer.getString(3),
                                        answer.getString(4));
                            }
                        }
                        else {
                            System.out.println("No data was returned for this query.");
                        }
                        System.out.println();
                        break;
                }
                stmt.close();
                menuChoice = getMenuChoice(kb);
            }
            System.out.println("Exiting.");

            // Close everything down
            dbconn.close();

        }
        catch (SQLException e) {

            System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
            System.err.println("\tMessage:   " + e.getMessage());
            System.err.println("\tSQLState:  " + e.getSQLState());
            System.err.println("\tErrorCode: " + e.getErrorCode());
            System.exit(-1);

        }

    }
}
