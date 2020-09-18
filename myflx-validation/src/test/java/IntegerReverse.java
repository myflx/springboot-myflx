import java.util.Arrays;

public class IntegerReverse {

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(654321000));
        System.out.println(Integer.toBinaryString(123456));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
        new IntegerReverse().reverse(2147483647);
    }

    public int reverse(int x) {
        byte[] binaryArr = new byte[32];
        if (x < 0) {
            x = Math.abs(x);
            binaryArr[0] = 1;
        }
        StringBuilder str = new StringBuilder(String.valueOf(x));
        boolean allSame = true;
        for (int i = 1; i < str.length(); i++) {
            if (!str.substring(i, i + 1).equals(str.substring(0, 1))) {
                allSame = false;
                break;
            }
        }
        if (allSame) {
            return binaryArr[0] == 1 ? 0 - x : x;
        }
        str.reverse();
        while (str.substring(0, 1).equals("0")) {
            str.deleteCharAt(0);
        }
        if (str.length() == 10 && (int) str.charAt(0) > 2) {
            return 0;
        }

        int k = 0;

        //处理翻转
        return binaryArr[0] == 1 ? 0 - x : x;
    }
}
