/*
 * JDBC.java -- A simple example of how to use Java and JDBC to
 * connect to an Oracle user's schema.
 *
 * At the time of this writing, the version of Oracle is 11.2g, and
 * the Oracle JDBC driver can be found at
 *   /opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar
 * on the lectura system in the UofA CS dept.
 * (Yes, 10.2, not 11.2.  It's the correct jar file but in a strange location.)
 *
 * To compile and execute this program on lectura:
 *
 *   Add the Oracle JDBC driver to your CLASSPATH environment variable:
 *
 *         export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 *
 *     (or whatever shell variable set-up you need to perform to add the
 *     JAR file to your Java CLASSPATH)
 *
 *   Compile this file:
 *
 *         javac JDBC.java
 *
 *   Finally, run the program:
 *
 *         java JDBC <oracle username> <oracle password>
 *
 * Author:  L. McCann (2008/11/19; updated 2015/10/28)
 */

import java.io.*;
import java.sql.*;                 // For access to the SQL interaction methods

public class JDBC
{
    public static void main (String [] args)
    {

        final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

        final String query =       // our test query
                        "SELECT sno, status FROM mccann.s";

        String username = null,    // Oracle DBMS username
               password = null;    // Oracle DBMS password


        if (args.length == 2) {    // get username/password from cmd line args
            username = args[0];
            password = args[1];
        } else {
            System.out.println("\nUsage:  java JDBC <username> <password>\n"
                             + "    where <username> is your Oracle DBMS"
                             + " username,\n    and <password> is your Oracle"
                             + " password (not your system password).\n");
            System.exit(-1);
        }


            // load the (Oracle) JDBC driver by initializing its base
            // class, 'oracle.jdbc.OracleDriver'.

        try {

                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }


            // make and return a database connection to the user's
            // Oracle database

        Connection dbconn = null;

        try {
                dbconn = DriverManager.getConnection
                               (oracleURL,username,password);

        } catch (SQLException e) {

                System.err.println("*** SQLException:  "
                    + "Could not open JDBC connection.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);

        }


            // Send the query to the DBMS, and get and display the results

        Statement stmt = null;
        ResultSet answer = null;

        try {

            stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);

            if (answer != null) {

                System.out.println("\nThe results of the query [" + query 
                                 + "] are:\n");

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();

                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                    System.out.print(answermetadata.getColumnName(i) + "\t");
                }
                System.out.println();

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values

                while (answer.next()) {
                    System.out.println(answer.getString("sno") + "\t"
                        + answer.getInt("status"));
                }
            }
            System.out.println();

                // Shut down the connection to the DBMS.

            stmt.close();  
            dbconn.close();

        } catch (SQLException e) {

                System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);

        }

    }
}
