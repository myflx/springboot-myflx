import java.util.Arrays;
import java.util.List;

public class IntegerReverse {

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(654321000));
        System.out.println(Integer.toBinaryString(123456));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);//2147483647
        System.out.println(new IntegerReverse().reverse(-2147447412));
    }

    private static final int UP = Integer.MAX_VALUE / 10;
    private static final int DOWN = Integer.MIN_VALUE / 10;

    public int reverse(int x) {
        int rev = 0;
        while (x / 10 != 0) {
            rev = rev * 10 + x % 10;
            x /= 10;
        }
        if (rev > UP || rev < DOWN) {
            return 0;
        }
        return rev * 10 + x % 10;
    }

    private static final List<Integer> list = Arrays.asList(2, 1, 4, 7, 4, 8, 3, 6, 4, 7);


    public int reverse2(int x) {
        boolean hasPre = false;
        final StringBuilder str = new StringBuilder(String.valueOf(x));
        if (str.charAt(0) == '-') {
            str.deleteCharAt(0);
            hasPre = true;
        }
        str.reverse();
        if (str.toString().equals(String.valueOf(x))) {
            return hasPre ? -x : x;
        }
        while (str.substring(0, 1).equals("0")) {
            str.deleteCharAt(0);
        }
        if (str.length() == 10) {
            int i = 0;
            while (i < str.length()) {
                if (i == 10 && hasPre && Integer.valueOf(str.substring(i, i + 1)) > list.get(i) + 1) {
                    return 0;
                }
                if (Integer.valueOf(str.substring(i, i + 1)) > list.get(i)) {
                    return 0;
                } else if (Integer.valueOf(str.substring(i, i + 1)) < list.get(i)) {
                    break;
                }
                i++;
            }
        }
        //处理符号
        x = Integer.parseInt(str.toString());
        return hasPre ? -x : x;
    }
}
