import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class TestBuckets {

    public static void main(String[] args) {
        BucketEntry b1 = new BucketEntry("0000", 3);
        BucketEntry b2 = new BucketEntry("0010", 4);
        BucketEntry b3 = new BucketEntry("0020", 5);
        BucketEntry b4 = new BucketEntry("0100", 6);
        BucketEntry b5 = new BucketEntry("0200", 7);
        BucketEntry b6 = new BucketEntry("0110", 8);
        BucketEntry b7 = new BucketEntry("0060", 9);
        BucketEntry b8 = new BucketEntry("0010", 10);
        BucketEntry b9 = new BucketEntry("0020", 11);
        BucketEntry b10 = new BucketEntry("0030", 12);

        try {
            RandomAccessFile bucketFile = new RandomAccessFile(new File("testBucket.bin"), "rw");
            Bucket bucket1 = new Bucket(null, "0", bucketFile);
            bucket1.insert(b1);
            bucket1.insert(b2);
            bucket1.insert(b3);
            bucket1.insert(b4);
            bucket1.insert(b5);
            bucket1.insert(b6);
            bucket1.insert(b7);
            bucket1.insert(b8);
            bucket1.insert(b9);
            bucket1.insert(b10);
            System.out.println(bucket1);
        }
        catch (FileNotFoundException e) {

        }
    }
}
