package gc;

import java.util.ArrayList;
import java.util.List;

public class OOMTest {

    static class OOMObject {
    }

    /**
     * -Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=D:\gc.hprof
     *
     * java.lang.OutOfMemoryError: Java heap space
     * Dumping heap to D:\java_pid15420.hprof ...
     * Heap dump file created [28925517 bytes in 0.147 secs]
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at java.util.Arrays.copyOf(Arrays.java:3210)
     * 	at java.util.Arrays.copyOf(Arrays.java:3181)
     * 	at java.util.ArrayList.grow(ArrayList.java:265)
     * 	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:239)
     * 	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:231)
     * 	at java.util.ArrayList.add(ArrayList.java:462)
     * 	at gc.OOMTest.main(gc.OOMTest.java:17)
     *
     * @param args args
     */
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
