public class StringDealer {

    public static void main(String[] args) {
        /*System.out.println(new StringDealer().longestCommonSubsequence("pmjghexybyrgzczy"
                , "hafcdqbgncrcbihkd"));*/
        System.out.println(new StringDealer().convert("LEE", 3).equals("LEE"));
        System.out.println(new StringDealer().convert("LEETCODEISHIRING", 3).equals("LCIRETOESIIGEDHN"));
        System.out.println(new StringDealer().convert("LEETCODEISHIRING", 4).equals("LDREOEIIECIHNTSG"));
    }

    public int longestCommonSubsequence(String text1, String text2) {
        //构建动态规划缓存数组
        int i = text1.length();
        int j = text2.length();
        int[][] dpTables = new int[i + 1][j + 1];
        //初始化基础数据
        for (int i1 = 1; i1 < i + 1; i1++) {
            for (int i2 = 1; i2 < j + 1; i2++) {
                if (text1.charAt(i1 - 1) == text2.charAt(i2 - 1)) {
                    dpTables[i1][i2] = dpTables[i1 - 1][i2 - 1] + 1;
                } else {
                    dpTables[i1][i2] = Math.max(dpTables[i1][i2 - 1], dpTables[i1 - 1][i2]);
                }
            }
        }
        return dpTables[i][j];
    }

    /**
     * 暴力递归的方式
     */
    private int dp(String text1, String text2, int i, int j) {
        if (i == -1 || j == -1) {
            return 0;
        }
        if (text1.charAt(i) == text2.charAt(j)) {
            return 1 + dp(text1, text2, i - 1, j - 1);
        } else {
            return Math.max(dp(text1, text2, i - 1, j), dp(text1, text2, i, j - 1));
        }
    }

    /**
     * z子形转换
     */
    public String convert(String s, int numRows) {
        if (numRows <= 1 || s.length() <= numRows) {
            return s;
        }
        char[] chars = s.toCharArray();
        int length = chars.length;
        int maxLen = 2 * (numRows - 1);
        StringBuilder rev = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            int startIndex = i;
            int currentLen = (numRows - i - 1) * 2;
            while (startIndex < length) {
                rev.append(chars[startIndex]);
                if (currentLen != 0 && currentLen != maxLen && startIndex + currentLen < length) {
                    rev.append(chars[startIndex + currentLen]);
                }
                startIndex += maxLen;
            }
        }
        return rev.toString();
    }
}
