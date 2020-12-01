import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

public class StringDealer {

    public static void main(String[] args) {
        /*System.out.println(new StringDealer().longestCommonSubsequence("pmjghexybyrgzczy"
                , "hafcdqbgncrcbihkd"));
        System.out.println(new StringDealer().convert("LEE", 3).equals("LEE"));
        System.out.println(new StringDealer().convert("LEETCODEISHIRING", 3).equals("LCIRETOESIIGEDHN"));
        System.out.println(new StringDealer().convert("LEETCODEISHIRING", 4).equals("LDREOEIIECIHNTSG"));*/

        /*String s = new StringDealer().reverseWords("Let's take LeetCode contest");
        System.out.println("s'teL ekat edoCteeL tsetnoc".equals(s));*/

        /*//1-空串
        System.out.println(new StringDealer().isValid(""));

        //2-奇数长度
        System.out.println(new StringDealer().isValid("{}]"));

        //3中心对称
        System.out.println(new StringDealer().isValid("([{}])"));

        //4大包小
        System.out.println(new StringDealer().isValid("([]{}())"));

        //5多个同级
        System.out.println(new StringDealer().isValid("()[]{}()"));

        //6括号交叉
        System.out.println(new StringDealer().isValid("([)]{}()"));*/

        /*System.out.println(new StringDealer().longestCommonPrefix(new String[]{"dog", "racecar", "car"}));*/

        System.out.println(new StringDealer().myAtoi("+-12"));

        /*System.out.println(new StringDealer().longestCommonPrefix(new String[]{"dog", "racecar", "car"}));

        System.out.println(new StringDealer().myAtoi(""));
        System.out.println(new StringDealer().generateParenthesis(4));
        System.out.println(new StringDealer().strStr("mississippi", "issip"));*/
        System.out.println(new StringDealer().findSubstring("barfoothefoobarman",
                new String[]{"foo", "bar"}));
    }

    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        int wordLen = words[0].length();//跨度
        int len = words.length;//步长
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(words[i], map.getOrDefault(words[i], 0) + 1);
        }
        int window = wordLen * len;
        Map<String, Integer> used = new HashMap<>();//记录字符使用次数
        for (int i = 0; i < wordLen; i++) {//按照跨度中的单个长度跳跃覆盖所有字符
            for (int j = i; j <= s.length() - window; j += wordLen) {//按跨度跳跃
                if (!used.isEmpty()) {
                    used.clear();
                }
                int startIndex = j + wordLen;//定位到当前第一个跨度索引
                for (int k = j + window; k > j; k -= wordLen) {//因为要记录起始位置采用倒叙遍历
                    int beginIndex = k - wordLen;
                    String subsub = s.substring(beginIndex, k);
                    int use = used.getOrDefault(subsub, 0) + 1;
                    if (use > map.getOrDefault(subsub, 0)) {//没有可用的数据
                        j = beginIndex;//没有可用字符跳出并设置下次缓存的起点
                        break;
                    }
                    if (k == startIndex) {//能跳到这里都是能满足条件的
                        res.add(j);
                    } else {//记录使用次数
                        used.put(subsub, use);
                    }
                }
            }
        }
        return res;
    }


    public List<Integer> findSubstring2(String s, String[] words) {
        List<Integer> ret = new ArrayList<>();
        if (s == null || "".equals(s) || words == null || words.length < 1) {
            return ret;
        }
        final char[] chars = s.toCharArray();
        final int wLen = words[0].length();
        int totalLen = words.length * wLen;
        if (chars.length < totalLen) {
            return ret;
        }
        List<String> rmList = new ArrayList<>();
        List<String> container = new ArrayList<>(Arrays.asList(words));
        char[] tmp = new char[wLen];
        for (int i = 0; i < chars.length; i++) {
            if (chars.length - i < totalLen) {
                break;
            }
            int j = i;
            container.addAll(rmList);
            rmList.clear();
            while (j < chars.length) {
                System.arraycopy(chars, j, tmp, 0, tmp.length);
                final int i1 = container.indexOf(new String(tmp));
                if (i1 >= 0) {
                    rmList.add(container.remove(i1));
                } else {
                    break;
                }
                if (rmList.size() == words.length) {
                    ret.add(i);
                    break;
                }
                j += wLen;
            }
        }
        return ret;
    }

    private boolean computeCharArray(int i, char[] chars, char[] tmp) {
        System.arraycopy(chars, i, tmp, 0, tmp.length);
        return true;
    }

    public int strStr(String haystack, String needle) {
        if (needle == null || needle.equals("")) {
            return 0;
        }
        if (haystack == null || "".equals(haystack) || haystack.length() < needle.length()) {
            return -1;
        }
        if (haystack.length() == needle.length()) {
            return haystack.equals(needle) ? 0 : -1;
        }
        char[] source = haystack.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if ((source.length - i) < needle.length()) {
                return -1;
            }
            if (source[i] == needle.charAt(0) && needle.equals(haystack.substring(i, i + needle.length()))) {
                return i;
            }
        }
        return -1;
    }


    public int strStr2(String haystack, String needle) {
        if (needle == null || needle.equals("")) {
            return 0;
        }
        if (haystack == null || "".equals(haystack) || haystack.length() < needle.length()) {
            return -1;
        }
        if (haystack.length() == needle.length()) {
            return haystack.equals(needle) ? 0 : -1;
        }
        char[] source = haystack.toCharArray();
        char[] target = needle.toCharArray();
        int first = -1, j = 0;
        for (int i = 0; j < target.length && i < source.length; i++) {
            if (source[i] == target[j]) {
                j++;
                first = first >= 0 ? first : i;
            } else {
                j = 0;
                if (first >= 0) {
                    i = first;
                }
                first = -1;
            }
        }
        return j == target.length ? first : -1;
    }

    /**
     * https://leetcode-cn.com/problems/generate-parentheses/
     */
    public List<String> generateParenthesis(int n) {
        Set<String> ret = new HashSet<>();
        char[] base = getStartCharArray(n);
        ret.add(new String(base));
        generateParenthesisHelp(base, n, ret);
        return new ArrayList<>(ret);
    }

    private void generateParenthesisHelp(char[] base, int n, Set<String> ret) {
        if (n == 1) {
            return;
        }
        char[] temp = Arrays.copyOf(base, base.length);
        int index = 2 * n - 1;
        for (int j = n; j < index; j++) {
            char c1 = temp[j - 1];
            char c2 = temp[j];
            if (c1 == c2) {
                break;
            }
            temp[j] = c1;
            temp[j - 1] = c2;
            if (!ret.add(new String(temp))) {
                break;
            }
            generateParenthesisHelp(temp, n - 1, ret);
        }
    }

    public char[] getStartCharArray(int n) {
        int len = 2 * n;
        char[] chars = new char[len];
        for (int j = 0; j < len; j++) {
            chars[j] = j < n ? '(' : ')';
        }
        return chars;
    }

    public String longestCommonPrefix(String[] strs) {
        //处理边界
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        String str = strs[0];
        int count = str.length();
        for (int i = 0; i < count; i++) {
            char c = str.charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || c != strs[j].charAt(i)) {
                    return str.substring(0, i);
                }
            }
        }
        return "";
    }

    /**
     * 最长公共前缀
     */
    public String longestCommonPrefix2(String[] strs) {
        //处理边界
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        StringBuilder ret = new StringBuilder();
        //确定最小长度为循环边界
        int len = Integer.MAX_VALUE;
        for (String str : strs) {
            if (str == null || str.length() == 0) {
                return "";
            }
            len = Math.min(len, str.length());
        }

        //遍历，比较
        l:
        for (int i = 0; i < len; i++) {
            Character character = null;
            for (String str : strs) {
                char c = str.charAt(i);
                if (character == null) {
                    character = c;
                } else if (c != character) {
                    break l;
                }
            }
            ret.append(character);
        }
        return ret.toString();
    }

    /**
     * ()[]{}
     * ()[{}]
     * [(){}]
     * [{()}]
     * 有效的括号
     * https://leetcode-cn.com/problems/valid-parentheses/
     */
    static HashMap<Character, Character> MAP = new HashMap<>();

    static {
        MAP.put('{', '}');
        MAP.put('(', ')');
        MAP.put('[', ']');
    }

    public boolean isValid(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }
        if ((s.length() & 1) == 1) {
            return false;
        }
        char[] chars = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        for (char c : chars) {
            Character character = MAP.get(c);
            if (character != null) {
                stack.push(character);
            } else {
                if (!stack.isEmpty()) {
                    if (stack.peek() == c) {
                        stack.pop();
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return stack.isEmpty();
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

    public String reverseWords2(String s) {
        String[] s1 = s.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s1.length; i++) {
            if (i != 0) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(new StringBuilder(s1[i]).reverse());
        }
        return stringBuilder.toString();
    }

    public String reverseWords3(String s) {
        if (s == null) {
            return s;
        }
        int i = s.indexOf(" ");
        if (i < 0) {
            return new StringBuilder(s).reverse().toString();
        }
        return new StringBuilder(s.substring(0, i)).reverse().append(" ").append(reverseWords(s.substring(i + 1))).toString();
    }

    /**
     * 原地不动的算法，
     *
     * @param s s
     * @return str
     */
    public String reverseWords(String s) {
        if (s == null) {
            return s;
        }
        int preIndex = 0;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                reverse(chars, preIndex, i - 1);
                preIndex = i + 1;
            }
        }
        reverse(chars, preIndex, s.length() - 1);
        return new String(chars);
    }

    private void reverse(char[] chars, int preIndex, int endIndex) {
        while (preIndex < endIndex) {
            char tmp = chars[preIndex];
            chars[preIndex] = chars[endIndex];
            chars[endIndex] = tmp;
            preIndex++;
            endIndex--;
        }
    }

    public void reverseString(char[] s) {
        reverse(s, 0, s.length - 1);
    }


    final Pattern compile2 = Pattern.compile("^[\\+\\-]?\\d+");
    final Pattern compile = Pattern.compile("^[0-9]*$");


    final List<Character> characters = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');

    /**
     * ascii to integer
     */
    public int myAtoi(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        final StringBuilder builder = new StringBuilder(s.trim());
        if (builder.length() == 0) {
            return 0;
        }
        boolean prefix = false;
        int i = 0;
        final char charAt = builder.charAt(i);
        if (charAt == '-') {
            prefix = true;
            builder.deleteCharAt(i);
        } else if (charAt == '+') {
            builder.deleteCharAt(i);
        } else if (!isNumber(charAt)) {
            return 0;
        }
        while (i < builder.length()) {
            final char c = builder.charAt(i);
            if (!isNumber(c)) {
                break;
            }
            i++;
        }
        if (i < builder.length()) {
            builder.delete(i, builder.length());
        }
        if (builder.length() == 0) {
            return 0;
        }
        if (prefix) {
            builder.insert(0, '-');
        }
        try {
            return Integer.valueOf(builder.toString());
        } catch (NumberFormatException e) {
            return prefix ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        }
    }

    public boolean isNumber(char c) {
        try {
            Integer.valueOf(Character.toString(c));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
