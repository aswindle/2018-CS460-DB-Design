Alex Swindle
aswindle
CSC 460
Program 3 Files

These are all of the files I used to create my Program 3 tables and run queries using them.

General outline:
	createAll.sql file to drop/create 4 tables needed
	XLS files -> replaced with ',' with '$!' and converted to CSV -> fully processed CSV after using ScrubCSV.java
	Scrubbed CSV files -> SQL files to populate DB tables using CreateInserts.java
	Prog3.java allows interaction with DB

Structure:

SQL Files:
	Files used to create, drop, and populate DB tables
	createAll runs everything: runs config, drops all 4 tables, recreates all 4 tables, grants SELECT to public, then runs all 4 insert files
	insert2015 - insert2018 contain all of the data to insert into each table. Created as the output of CreateInserts.java
	config sets autocommit off and sets the line and page sizes appropriately
	clearAll deletes the data in the tables but doesn't drop the tables themselves

Java Files:
	All Java source code
	
	ScrubCSV: takes the lightly-processed .csv files and escapes quotes, replaces '*' with 'NULL', restores commas, fixes ints, and wraps strings in single quotes
		Input: 2015.csv through 2018.csv
		Output: 2015scrubbed.csv through 2018srcubbed.csv
		
	CreateInserts: creates the SQL files used to populate each table
		Input: 2015scrubbed.csv through 2018scrubbed.csv
		Output: insert2015.sql through insert2018.sql
		
	Statements: the String constants used to form queries in Prog3
	
	Prog3: code to connect to DB, prompt user to run queries, and actually run the queries

CSV Files:
	Lightly processed and fully processed CSV files from the original XLS data
	
	Steps done to 2015.csv through 2018.csv:
		Replaced the commas in the XLS files with '$!'
		Converted XLS to CSV
		Deleted junk header rows and trailing empty rows
		
	Those were then fed into ScrubCSV.java to produce the 201Xscrubbed.csv files that further processed them
