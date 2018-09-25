/**
 * Class: Node
 *
 * Author: Alex Swindle (aswindle@email.arizona.edu)
 *
 * Purpose: Node of the index tree that initially points to Buckets. When those Buckets are full, it will point to a
 * new Node that will point to new Buckets
 *
 * Inherits From: DynamicHashingObject
 *
 * Implements: None
 *
 * Constants: No public constants
 *
 * Constructors:
 * public Node(Node parent, String idPiece, RandomAccessFile bucketFile)
 *
 * Methods:
 * getters and setters
 *
 * public boolean insert(BucketEntry entry)
 * public LinkedList<BucketEntry> search(String arrTime)
 * public void split(Bucket bucket)
 * public String toString()
 */

import java.io.RandomAccessFile;
import java.util.LinkedList;

public class Node extends DynamicHashingObject {
    private final int CAPACITY = 10;
    private DynamicHashingObject[] pointers;
    private RandomAccessFile bucketFile;

    public Node(Node parent, String idPiece, RandomAccessFile bucketFile) {
        this.setType("NODE");
        this.setParent(parent);
        this.bucketFile = bucketFile;
        if (parent == null) {
            this.setLevel(0);
            this.setIdentifier("" + idPiece);
        }
        else {
            this.setLevel(this.getParent().getLevel() + 1);
            this.setIdentifier("" + idPiece + parent.getIdentifier());
        }

        pointers = new DynamicHashingObject[CAPACITY];
        // Each node will begin pointing to buckets.
        for (int i = 0; i < pointers.length; i++) {
            char newChar = (char) ('0' + i);
            pointers[i] = new Bucket(this, "" + newChar, this.bucketFile);
        }
    }

    /**
     * Insert a new entry into one of this node's children by matching identifiers.
     *
     * If it's a node, recursively insert it there.
     * If it's a bucket:
     * If the bucket has space, insert it.
     * If it doesn't, create a new node and split the bucket's contents
     *
     * @param entry: BucketEntry item to be inserted
     */
    @Override
    public boolean insert(BucketEntry entry) {
        // Find the node or bucket whose identifier matches the end of the string to be inserted
        for (DynamicHashingObject dho : pointers) {
            if (dho.matchIdentifier(entry.arrTime)) {
                // Insert into node
                if (dho.getType().equals("NODE")) {
                    return dho.insert(entry);
                }
                else {
                    // Insert into bucket with space
                    if (((Bucket) dho).getCount() < ((Bucket) dho).getCAPACITY()) {
                        return dho.insert(entry);
                    }
                    // Insert into full bucket
                    else {
                        // Reached the maximum depth allowed. Terminate insertions.
                        if (dho.getLevel() >= 4) {
                            System.out.println("Index capacity reached. Couldn't insert binary file index " + entry.i
                                    + ".");
                            return false;
                        }
                        else {
                            split((Bucket) dho);
                            return insert(entry);
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Recursively search this Node's children for all Nodes or Buckets that match a search string
     *
     * @param arrTime: 1-4 digit String of an arrTime to look for
     * @return: a LinkedList of all BucketEntries that match the search string across all of this node's children
     */
    @Override
    public LinkedList<BucketEntry> search(String arrTime) {
        LinkedList<BucketEntry> results = new LinkedList<>();
        for (DynamicHashingObject obj : pointers) {
            if (obj != null) {
                if (obj.matchIdentifier(arrTime)) {
                    results.addAll(obj.search(arrTime));
                }
            }
        }
        return results;
    }

    /**
     * Split up a Bucket by creating a new Node that will have new Buckets. Retrieve all the previous entries and
     * insert them into the new Node
     *
     * @param bucket: Bucket that needs to split
     */
    public void split(Bucket bucket) {
        // Update pointers. Find the correct item in the array first
        for (int i = 0; i < pointers.length; i++) {
            if (pointers[i] == bucket) {
                char newChar = (char) ('0' + i);
                Node newNode = new Node(this, "" + newChar, bucketFile);
                pointers[i] = newNode;
                // Insert all of the old entries into the new node
                for (int j = 0; j < bucket.getCAPACITY(); j++) {
                    BucketEntry entry = bucket.getEntry(j);
                    newNode.insert(entry);
                }
            }
        }
    }

    @Override
    public String toString() {
        String indent = "";
        for (int i = 0; i < this.getLevel(); i++) {
            indent += "    ";
        }
        String retVal;
        if (this.getIdentifier().equals("")) {
            retVal = "Node Root, level " + this.getLevel() + ":\n";
        }
        else {
            retVal = indent + "Node " + this.getIdentifier() + ", level " + this.getLevel() + ":\n";
        }
        for (DynamicHashingObject b : pointers) {
            if (b != null) {
                retVal += String.format(indent + "    %s\n", b.toString());
            }
        }
        return retVal;
    }
}
