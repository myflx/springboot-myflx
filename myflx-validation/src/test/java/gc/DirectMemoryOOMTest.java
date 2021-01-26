package gc;

import sun.misc.Unsafe;

public class DirectMemoryOOMTest {

    private static final int _1M = 1024 * 1024;


    /**
     * Exception in thread "main" java.lang.SecurityException: Unsafe
     * at sun.misc.Unsafe.getUnsafe(Unsafe.java:90)
     * at gc.DirectMemoryOOMTest.main(gc.DirectMemoryOOMTest.java:16)
     */
    public static void main(String[] args) {
        Unsafe unsafe = Unsafe.getUnsafe();
        while (true) {
            unsafe.allocateMemory(_1M);
        }
    }
}
