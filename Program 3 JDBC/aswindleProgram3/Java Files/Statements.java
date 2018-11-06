/**
 * Assignment:  Program #3: JDBC
 * Author:  Alex Swindle (aswindle@email.arizona.edu)
 * Grader:  Terrence Lim
 *
 * Course: CSC 460
 * Instructor: L. McCann
 * Due Date: 11/1/18
 *
 * Description: Stores the Strings needed for all of the various SQL queries used by Prog3.java
 * Stores them with '%s' for the parameters, as Oracle SQL doesn't appear to allow dynamic table names
 * using PreparedStatements
 *
 * Language: Java 8
 * External Packages: None
 *
 * Deficiencies: None
 *
 * String Constants:
 * diffNames
 * diffNamesCount
 * decreasingStats
 * top45
 * schoolCount
 * mostImproved
 *
 * Constructors:
 * none
 *
 * Methods:
 * none
 */

public class Statements {
    public static final String diffNames =
            "SELECT d15.schoolname AS OldName, d18.schoolname AS NewName " +
            "FROM aswindle.data2015 d15, aswindle.data2018 d18 " +
            "WHERE d15.distno = d18.distno " +
            "AND d15.schoolno = d18.schoolno " +
            "AND d15.schoolname != d18.schoolname";

    public static final String diffNamesCount =
            "SELECT COUNT(*) AS Count " +
            "FROM (SELECT d15.schoolname, d18.schoolname " +
                    "FROM aswindle.data2015 d15, aswindle.data2018 d18 " +
                    "WHERE d15.distno = d18.distno " +
                    "AND d15.schoolno = d18.schoolno " +
                    "AND d15.schoolname != d18.schoolname)";

    public static final String decreasingStats =
            "SELECT distno, schoolno, schoolname " +
            "FROM aswindle.%s " +
            "WHERE one < two " +
            "AND two < three " +
            "AND three < four " +
            "AND four < five";

    public static final String top45 =
            "SELECT * " +
            "FROM (SELECT distname, schoolname, SUM(four + five) AS Total " +
                   "FROM aswindle.%s " +
                   "WHERE distname = '%s' " +
                   "GROUP BY distname, schoolname " +
                   "ORDER BY SUM(four + five) DESC NULLS LAST) " +
            "WHERE ROWNUM <= 5";

    public static final String schoolCount =
            "SELECT distname, COUNT(schoolno) AS Count " +
            "FROM aswindle.%s " +
            "WHERE distname = '%s' " +
            "GROUP BY distname";

    public static final String mostImproved =
            "SELECT * " +
            "FROM (SELECT d18.schoolname, d15.percentpass AS Pass2015, d18.percentpass AS Pass2018, SUM(d18.percentpass - d15.percentpass) AS Change " +
                    "FROM aswindle.data2015 d15, aswindle.data2018 d18 " +
                    "WHERE d15.distno = d18.distno " +
                    "AND d15.schoolno = d18.schoolno " +
                    "AND d15.distname = '%s' " +
                    "GROUP BY d18.distno, d18.schoolname, d18.percentpass, d15.percentpass " +
                    "ORDER BY SUM(d18.percentpass - d15.percentpass) DESC NULLS LAST) " +
            "WHERE ROWNUM <= 10";
}
