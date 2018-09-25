/**
 * Class: Bucket
 *
 * Author: Alex Swindle (aswindle@email.arizona.edu)
 *
 * Purpose: Bucket that will allow access to a binary file; uses BucketEntry objects to pass data around, but doesn't
 * store it in memory, instead writing the data to the file
 *
 * Inherits From: DynamicHashingObject
 *
 * Implements: None
 *
 * Constants: No public constants
 *
 * Constructors:
 * public Bucket(Node parent, String idPiece, RandomAccessFile bucketFile)
 *
 * Methods:
 * getters and setters
 *
 * public boolean insert(BucketEntry entry)
 * public LinkedList<BucketEntry> search(String searchTime)
 * public BucketEntry getEntry(int i)
 * public String toString()
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

public class Bucket extends DynamicHashingObject {
    // Size of a bucket in the file: 100 * (4 characters for arrTime + 1 int (4 bytes) for i)
    private final int bucketSize = 800;
    // Size of a BucketEntry object: 4-char String plus 1 int
    private final int entrySize = 8;
    // Keep track of how many buckets have been created
    private static int bucketCount;
    // Keep track of this bucket's number
    protected int bucketNum;
    // Binary file the bucket will write to
    private RandomAccessFile bucketFile;
    // Capacity of the the bucket
    private final int CAPACITY = 100;
    // Current count of how many entries are in this bucket
    private int count;
    // Location of first entry in the binary file
    private int index0;

    public Bucket(Node parent, String idPiece, RandomAccessFile bucketFile) {
        this.bucketFile = bucketFile;
        this.bucketNum = bucketCount;
        this.setParent(parent);
        this.setType("BUCKET");
        this.setIdentifier("" + idPiece + parent.getIdentifier());
        this.setLevel(parent.getLevel() + 1);
        this.count = 0;
        setIndex0();
        bucketCount++;
    }

    /**
     * Resets the first index of this bucket. Will be used if the bucketNum is ever changed
     */
    public void setIndex0() {
        this.index0 = bucketNum * bucketSize;
    }

    /**
     * Sets the number for this bucket. Also resets index0. Allows reuse of old buckets
     *
     * @param bucketNum
     */
    public void setBucketNum(int bucketNum) {
        this.bucketNum = bucketNum;
        setIndex0();
    }

    public int getCount() {
        return this.count;
    }


    /**
     * Insert a BucketEntry item into the binary index file. 8 bytes will be written: a 4-char arrTime String and an int
     *
     * @param entry: BucketEntry to insert
     * @return: true if the item was inserted (should always work); false if the file couldn't be written to
     */
    @Override
    public boolean insert(BucketEntry entry) {
        try {
            bucketFile.seek(index0 + count * entrySize);
            bucketFile.writeBytes(entry.arrTime);
            bucketFile.writeInt(entry.i);
            count++;
            return true;
        }
        catch (IOException e) {
            System.out.println("ERROR: couldn't seek to correct spot in bucket file.");
            return false;
        }
    }

    /**
     * Return a LinkedList of all BucketEntry items in this bucket that match an arrTime String
     *
     * @param searchTime: String of 1-4 digits to search for in this bucket
     * @return: LinkedList containing all entries that match the search string
     */
    @Override
    public LinkedList<BucketEntry> search(String searchTime) {
        LinkedList<BucketEntry> list = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            BucketEntry obj = getEntry(i);

            if (obj != null) {
                // Perfect match should be returned
                if (obj.arrTime.equals(searchTime)) {
                    list.add(obj);
                }
                // As should matches of shorter substrings being searched for: '0010' should be returned on '10' and '0'
                else if (obj.arrTime.length() > searchTime.length()) {
                    if (obj.arrTime.substring(obj.arrTime.length() - searchTime.length()).equals(searchTime)) {
                        list.add(obj);
                    }
                }
            }
        }
        return list;
    }

    public int getCAPACITY() {
        return CAPACITY;
    }

    /**
     * Create and return a BucketEntry item containing the information at index i in this bucket
     *
     * @param i: index to retrieve from the binary file
     * @return: BucketEntry object with the necessary data
     */
    public BucketEntry getEntry(int i) {
        try {
            // Find the data in the binary file, create new BucketEntry item from it
            byte[] arrTimeBytes = new byte[4];
            String foundArrTime;
            int foundIndex;
            bucketFile.seek(index0 + i * entrySize);
            bucketFile.read(arrTimeBytes);
            foundArrTime = new String(arrTimeBytes);
            foundIndex = bucketFile.readInt();
            return new BucketEntry(foundArrTime, foundIndex);
        }
        catch (IOException e) {
            System.out.println("Error finding entry " + i + " in the binary file.");
            return null;
        }
    }

    @Override
    public String toString() {
        String indent = "";
        for (int i = 0; i < this.getLevel(); i++) {
            indent += "    ";
        }
        String retVal = indent + "Bucket " + bucketNum + ", ID " + this.getIdentifier() + ", level " + this.getLevel
                () + ":\n";
        for (int i = 0; i < count; i++) {
            BucketEntry b = getEntry(i);
            if (b != null) {
                retVal += String.format(indent + "        " + "%s %d\n", b.arrTime, b.i);
            }
        }
        return retVal;
    }

}