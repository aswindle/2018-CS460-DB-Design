/**
 * Class: FlightRecord
 *
 * Author: Alex Swindle (aswindle@email.arizona.edu)
 *
 * Purpose: stores the 19 fields of a flight record read from a CSV file, allows for accessing and changing
 * the values. Provides a few utility methods for padding Strings and writing the data to a file
 *
 * Inherits From: None
 *
 * Implements: Comparable to allow a list of the records to be sorted by the flNum field
 *
 * Constants: No public constants
 *
 * Constructors:
 * FlightRecord() : simply initializes all fields to default values
 *
 * Methods:
 * getters and setters for all 19 instance variables
 * public String pad(String start, int size)
 * public int compareTo(FlightRecord o)
 * public String toString()
 * public void writeObject(RandomAccessFile file)
 */

import java.io.IOException;
import java.io.RandomAccessFile;

class FlightRecord implements Comparable<FlightRecord> {
    private String flDate;         //0
    private String uniqueCarrier;  //1
    private int airlineId;         //2
    private String tailNum;        //3
    private String flNum;          //4
    private String origin;         //5
    private String dest;           //6
    private String depTime;        //7
    private double depDelay;       //8
    private double taxiOut;        //9
    private String wheelsOff;      //10
    private String wheelsOn;       //11
    private double taxiIn;         //12
    private String arrTime;        //13
    private double arrDelay;       //14
    private double cancelled;      //15
    private String cancellationCode;//16
    private double airTime;        //17
    private double distance;       //18


    /**
     * Default constructor. All fields will be set through the parseLine method later.
     */
    public FlightRecord() {
    }

    /**
     * Right-Pad a String with spaces until it's a particular length
     *
     * @param start: string to pad
     * @param size:  final desired size of string
     * @return start padded with spaces so it's 'size' characters long
     */
    public String pad(String start, int size) {
        int spacesToAdd = size - start.length();
        for (int i = 0; i < spacesToAdd; i++) {
            start += " ";
        }
        return start;
    }

    /**
     * Getters and Setters
     */

    public String getFlDate() {
        return flDate;
    }

    public void setFlDate(String flDate) {
        this.flDate = flDate;
    }

    public String getUniqueCarrier() {
        return uniqueCarrier;
    }

    public void setUniqueCarrier(String uniqueCarrier) {
        this.uniqueCarrier = uniqueCarrier;
    }

    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    public String getTailNum() {
        return tailNum;
    }

    public void setTailNum(String tailNum) {
        this.tailNum = tailNum;
    }

    public String getFlNum() {
        return flNum;
    }

    public void setFlNum(String flNum) {
        this.flNum = flNum;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public double getDepDelay() {
        return depDelay;
    }

    public void setDepDelay(double depDelay) {
        this.depDelay = depDelay;
    }

    public double getTaxiOut() {
        return taxiOut;
    }

    public void setTaxiOut(double taxiOut) {
        this.taxiOut = taxiOut;
    }

    public String getWheelsOff() {
        return wheelsOff;
    }

    public void setWheelsOff(String wheelsOff) {
        this.wheelsOff = wheelsOff;
    }

    public String getWheelsOn() {
        return wheelsOn;
    }

    public void setWheelsOn(String wheelsOn) {
        this.wheelsOn = wheelsOn;
    }

    public double getTaxiIn() {
        return taxiIn;
    }

    public void setTaxiIn(double taxiIn) {
        this.taxiIn = taxiIn;
    }

    public String getArrTime() {
        return arrTime;
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public double getArrDelay() {
        return arrDelay;
    }

    public void setArrDelay(double arrDelay) {
        this.arrDelay = arrDelay;
    }

    public double getCancelled() {
        return cancelled;
    }

    public void setCancelled(double cancelled) {
        this.cancelled = cancelled;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public void setCancellationCode(String cancellationCode) {
        this.cancellationCode = cancellationCode;
    }

    public double getAirTime() {
        return airTime;
    }

    public void setAirTime(double airTime) {
        this.airTime = airTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Allows 2 FlightRecords to be compared. Sorts based on the flNum field in ascending order
     *
     * Reference used for how to make a class sortable:
     * https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
     *
     * @param o: FlightRecord to be compared to
     * @return: Positive if this flight number is greater than o's, 0 if same, negative otherwise
     */
    @Override
    public int compareTo(FlightRecord o) {
        //Cast them both to ints, then subtract
        try {
            // Trim whitespace to avoid errors
            int thisFlight = Integer.parseInt(this.getFlNum().trim());
            int otherFlight = Integer.parseInt(o.getFlNum().trim());
            return thisFlight - otherFlight;
        }
        catch (NumberFormatException e) {
            System.out.println("Error with one of the flight numbers.");
            System.out.println("This flight num: " + this.getFlNum());
            System.out.println("Other flight num: " + o.getFlNum());
            return 0;
        }

    }

    /**
     * Simple String representation of all 19 fields of the FlightRecord
     *
     * @return
     */
    @Override
    public String toString() {
        return "FlightRecord{" +
                "flDate='" + flDate + '\'' +
                ", uniqueCarrier='" + uniqueCarrier + '\'' +
                ", airlineId=" + airlineId +
                ", tailNum='" + tailNum + '\'' +
                ", flNum=" + flNum +
                ", origin='" + origin + '\'' +
                ", dest='" + dest + '\'' +
                ", depTime='" + depTime + '\'' +
                ", depDelay=" + depDelay +
                ", taxiOut=" + taxiOut +
                ", wheelsOff='" + wheelsOff + '\'' +
                ", wheelsOn='" + wheelsOn + '\'' +
                ", taxiIn=" + taxiIn +
                ", arrTime='" + arrTime + '\'' +
                ", arrDelay=" + arrDelay +
                ", cancelled=" + cancelled +
                ", cancellationCode='" + cancellationCode + '\'' +
                ", airTime=" + airTime +
                ", distance=" + distance +
                '}';
    }

    /**
     * Write all of the data fields of the FlightRecord object to the end of a file
     * All Strings are written as bytes, ints and doubles written as themselves
     *
     * @param file: file to be written to
     */
    public void writeObject(RandomAccessFile file) {
        // Go to the end of the file
        try {
            file.seek(file.length());
        }
        catch (IOException e) {
            System.out.println("Error going to the end of the file. Exiting.");
            System.exit(-1);
        }

        // Write each of the 19 fields to the file
        try {
            file.writeBytes(flDate);
            file.writeBytes(uniqueCarrier);
            file.writeInt(airlineId);
            file.writeBytes(tailNum);
            file.writeBytes(flNum);
            file.writeBytes(origin);
            file.writeBytes(dest);
            file.writeBytes(depTime);
            file.writeDouble(depDelay);
            file.writeDouble(taxiOut);
            file.writeBytes(wheelsOff);
            file.writeBytes(wheelsOn);
            file.writeDouble(taxiIn);
            file.writeBytes(arrTime);
            file.writeDouble(arrDelay);
            file.writeDouble(cancelled);
            file.writeBytes(cancellationCode);
            file.writeDouble(airTime);
            file.writeDouble(distance);
        }
        catch (IOException e) {
            System.out.println("Error writing to file. Exiting.");
            System.exit(-1);
        }
    }
}
