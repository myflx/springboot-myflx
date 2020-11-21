package map;

import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    private static int RESIZE_STAMP_BITS = 16;

    public static void main(String[] args) {
        final ConcurrentHashMap<Object, Object> hashMap = new ConcurrentHashMap<>(17);
        hashMap.put("1", "2");
        System.out.println("hello world!");
        System.out.println(Integer.numberOfLeadingZeros(32));
        System.out.println((1 << (RESIZE_STAMP_BITS - 1)));
        System.out.println(Integer.numberOfLeadingZeros(32) | (1 << (RESIZE_STAMP_BITS - 1)));

        System.out.println("2020/10/01".substring(0,7));

    }
}
