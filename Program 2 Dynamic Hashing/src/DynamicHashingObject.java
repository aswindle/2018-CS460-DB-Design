/**
 * Class: DynamicHashingObject
 *
 * Author: Alex Swindle (aswindle@email.arizona.edu)
 *
 * Purpose: Wrapper class for Bucket and Node objects so nodes can always point to the same kind of object.
 * 'type' will let the nodes know if they're pointing to buckets or other nodes.
 *
 * Inherits From: None
 *
 * Implements: None
 *
 * Constants: No public constants
 *
 * Constructors:
 * none
 *
 * Methods:
 * getters and setters
 *
 * public abstract boolean insert(BucketEntry entry);
 * public abstract LinkedList<BucketEntry> search(String arrTime);
 * public boolean matchIdentifier(String str)
 */

import java.util.LinkedList;

public abstract class DynamicHashingObject {
    // Level of the tree
    private int level;
    // Digits of the string that this part of the tree is storing
    private String identifier;
    private Node parent;
    // Either NODE or BUCKET
    private String type;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public abstract boolean insert(BucketEntry entry);

    public abstract LinkedList<BucketEntry> search(String arrTime);

    /**
     * Return whether the end of a String matches an object's identifier
     *
     * @param str: string to check. Will only use as many characters as DHO's identifier has
     * @return: true if the IDs match, false otherwise
     */
    public boolean matchIdentifier(String str) {
        boolean result;
        // Look for a string at least as long as the identifier
        if(str.length() >= identifier.length()) {
            String strPiece = str.substring(str.length() - identifier.length());
            result = (identifier.equals(strPiece));
        }
        // Look for a string shorter than the identifier
        else{
            String idPiece = identifier.substring(identifier.length() - str.length());
            result = str.equals(idPiece);
        }
        return result;
    }
}
