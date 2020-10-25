package hash;

import java.util.HashMap;

public class HashMapSolution {
    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(1));
        System.out.println(Integer.toBinaryString(3));
        System.out.println(Integer.toBinaryString(7));
        System.out.println(Integer.toBinaryString(15));
        System.out.println(Integer.toBinaryString(31));
        System.out.println(Integer.toBinaryString(63));
        System.out.println(Integer.toBinaryString(127));
        System.out.println("<==============================>");
        System.out.println(Integer.toBinaryString(1 & 100));
        System.out.println(Integer.toBinaryString(3 & 100));
        System.out.println(Integer.toBinaryString(7 & 100));
        System.out.println(Integer.toBinaryString(15 & 100));
        System.out.println(Integer.toBinaryString(31 & 100));
        System.out.println(Integer.toBinaryString(63 & 100));
        System.out.println(Integer.toBinaryString(127 & 100));
        System.out.println("<==============================>");
        System.out.println(Integer.toBinaryString(2 & 100));
        System.out.println(Integer.toBinaryString(4 & 100));
        System.out.println(Integer.toBinaryString(8 & 100));
        System.out.println(Integer.toBinaryString(16 & 100));
        System.out.println(Integer.toBinaryString(32 & 100));
        System.out.println(Integer.toBinaryString(64 & 100));
        System.out.println(Integer.toBinaryString(128 & 100));
        System.out.println(Integer.toBinaryString(256 & 100));
        System.out.println(Integer.toBinaryString(512 & 100));
    }
}

class User {
    String name;

    public User(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
