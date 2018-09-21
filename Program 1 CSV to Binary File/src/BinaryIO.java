/*
 * BinaryIO.java -- A simple demonstration of binary data input and output
 * using the RandomAccessFile (RAF) class in java.io.  Unlike most of the
 * Java file classes, RAF includes both input and output methods.
 *
 * Using RAF, this program creates a binary data file named 'output.bin'
 * that contains the data fields of the DataRecord class, and then reads
 * what it just wrote.
 *
 * Java includes a nice pair of classes for doing I/O of objects
 * (ObjectInputStream and ObjectOutputStream).  Why didn't I use them?
 * They have to store more than just the content of the fields
 * to completely store an object.  In a database, we generally prefer
 * files of binary data that can be 'randomly' accessed; the excess object
 * overhead complicates access and wastes space.
 *
 * Speaking of random (aka 'direct') access files:  In order for the
 * records of data in such files to be randomly accessed, we need to ensure
 * that each record in a file is of a single predetermined size.  With this
 * knowledge, we can compute the first byte of any record we need.
 * Primitive types (int, double, etc.) are all fixed-size, but Strings are
 * not.  Before writing String values, we have to pad them to a suitable
 * maximum length.  This program also demonstrates one way to do that.
 *
 * Author:  L. McCann (2001/09/07)
 */

import java.io.*;

class DataRecord {
    public static final int RECORD_LENGTH = 30; // 2 ints + str = 2(4)+22 bytes
    private final int COUNTY_NAME_LENGTH = 22;  // the maximum length allowed


    // The data fields that comprise a record of our file

    private int stateCode;    // the FIPS code for states
    private int placeCode;    // the FIPS code for places
    private String countyName;   // of a county the place occupies


    // 'Getters' for the data field values

    public int getStateCode() {
        return (stateCode);
    }

    public int getPlaceCode() {
        return (placeCode);
    }

    public String getCountyName() {
        return (countyName);
    }


    // 'Setters' for the data field values

    public void setStateCode(int newCode) {
        stateCode = newCode;
    }

    public void setPlaceCode(int newCode) {
        placeCode = newCode;
    }

    public void setCountyName(String newName) {
        countyName = newName;
    }


    /* dumpObject(stream) -- write the content of the object's fields
     * to the file represented by the given RandomAccessFile object
     * reference.  Primitive types (e.g., int) are written directly.
     * Non-fixed-size values (e.g., strings) are converted to the
     * maximum allowed size before being written.  The result is a
     * file of uniformly-sized records.  Also note that text is
     * written with just one byte per character, meaning that we are
     * not supporting Unicode text.
     */

    public void dumpObject(RandomAccessFile stream) {
        StringBuffer name = new StringBuffer(countyName);  // paddable county name

        try {
            stream.writeInt(stateCode);
            stream.writeInt(placeCode);
            name.setLength(COUNTY_NAME_LENGTH);
            stream.writeBytes(name.toString());
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't write to the file;\n\t"
                    + "perhaps the file system is full?");
            System.exit(-1);
        }
    }

    /* fetchObject(stream) -- read the content of the object's fields
     * from the file represented by the given RandomAccessFile object
     * reference, starting at the current file position.  Primitive
     * types (e.g., int) are read directly.  To create Strings containing
     * the text, because the file records have text stored with one byte
     * per character, we can read a text field into an array of bytes and
     * use that array as a parameter to a String constructor.
     */

    public void fetchObject(RandomAccessFile stream) {
        byte[] ctyName = new byte[COUNTY_NAME_LENGTH];  // file -> byte[] -> String

        try {
            stateCode = stream.readInt();
            placeCode = stream.readInt();
            stream.readFully(ctyName);
            countyName = new String(ctyName);
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't read from the file;\n\t"
                    + "is the file accessible?");
            System.exit(-1);
        }
    }
}

public class BinaryIO {
    public static void main(String[] args) {
        File fileRef;             // used to create the file
        RandomAccessFile dataStream = null;   // specializes the file I/O
        DataRecord rec1, rec2;          // the objects to write/read
        long numberOfRecords = 0; // loop counter for reading file


        // Create and populate the records to be written

        rec1 = new DataRecord();
        rec1.setStateCode(55);
        rec1.setPlaceCode(87352);
        rec1.setCountyName("A_Name");

        rec2 = new DataRecord();
        rec2.setStateCode(55);
        rec2.setPlaceCode(99999);
        rec2.setCountyName("Another_Name");


        /* Create a File object to represent the file and a
         * RandomAccessFile (RAF) object to supply appropriate
         * file access methods.  Note that there is a constructor
         * available for creating RAFs directly (w/o needing a
         * File object first), but having access to File object
         * methods is often handy.
         */

        fileRef = new File("objects.bin");

        try {
            dataStream = new RandomAccessFile(fileRef, "rw");
        } catch (IOException e) {
            System.out.println("I/O ERROR: Something went wrong with the "
                    + "creation of the RandomAccessFile object.");
            System.exit(-1);
        }


        // Tell the DataRecord objects to write themselves

        rec1.dumpObject(dataStream);
        rec2.dumpObject(dataStream);


        /* Move the file pointer (which marks the byte with which
         * the next access will begin) to the front of the
         * file (that is, to byte 0).
         */

        try {
            dataStream.seek(0);
        } catch (IOException e) {
            System.out.println("I/O ERROR: Seems we can't reset the file "
                    + "pointer to the start of the file.");
            System.exit(-1);
        }

        /* Read the records and display their content
         * to the screen.
         */

        try {
            numberOfRecords = dataStream.length() / DataRecord.RECORD_LENGTH;
        } catch (IOException e) {
            System.out.println("I/O ERROR: Couldn't get the file's length.");
            System.exit(-1);
        }

        System.out.println("\nThere are " + numberOfRecords
                + " records in the file.\n");

        while (numberOfRecords > 0) {
            rec1.fetchObject(dataStream);
            System.out.println("Read state code of " + rec1.getStateCode());
            System.out.println("Read place code of " + rec1.getPlaceCode());
            System.out.println("Read county name of " + rec1.getCountyName());
            System.out.println();
            numberOfRecords--;
        }


        // Clean-up by closing the file

        try {
            dataStream.close();
        } catch (IOException e) {
            System.out.println("VERY STRANGE I/O ERROR: Couldn't close "
                    + "the file!");
        }
    }
}
