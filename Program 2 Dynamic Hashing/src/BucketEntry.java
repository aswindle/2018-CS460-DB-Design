/**
 * Class: BucketEntry
 *
 * Author: Alex Swindle (aswindle@email.arizona.edu)
 *
 * Purpose: Simple object to hold the data for an entry in the index.
 *
 * Inherits From: none
 *
 * Implements: None
 *
 * Constants: No public constants
 *
 * Constructors:
 * public BucketEntry(String arrTime, int i)
 *
 * Methods:
 * public String toString()
 */

public class BucketEntry {
    // Each slot in the bucket needs to hold an ARR_TIME field (which is a String) and an index for the binary data file
    String arrTime;
    int i;

    public BucketEntry(String arrTime, int i) {
        this.arrTime = arrTime;
        this.i = i;
    }

    public String toString(){
        return String.format("Bucket Entry: %s %d", arrTime, i);
    }
}
