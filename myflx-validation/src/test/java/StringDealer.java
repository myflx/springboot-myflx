import java.util.Arrays;
import java.util.List;

public class StringDealer {

    public static void main(String[] args) {

        final List<String> list = Arrays.asList("1", "2", "3");
        System.out.println(Arrays.toString(list.toArray(new String[0])));
        System.out.println(new StringDealer().longestCommonSubsequence("ezupkr", "ubmrapg"));
    }

    public int longestCommonSubsequence(String text1, String text2) {
        char[] longger = text1.toCharArray();
        char[] smaller = text2.toCharArray();
        int maxLen = Math.max(text1.length(), text2.length());
        if (longger.length < maxLen) {
            longger = text2.toCharArray();
            smaller = text1.toCharArray();
        }
        int max = 0;
        int i = 0;
        while (i < smaller.length && smaller.length - i - 1 > max) {
            int k = 0;
            int length = 0;
            for (int j = i; j < smaller.length; j++) {
                char c = smaller[j];
                int m = k;
                while (m < longger.length) {
                    if (c == longger[m++]) {
                        length++;
                        k++;
                        break;
                    }
                }
                if (length > max) {
                    max = length;
                    if (m >= longger.length) {
                        length--;
                    }
                }
            }
            i++;
        }
        return max;
    }
}
