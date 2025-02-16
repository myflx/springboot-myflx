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
        System.out.println(new StringDealer().strStr("mississippi", "issip"));
        System.out.println(new StringDealer().findSubstring("barfoothefoobarman",
                new String[]{"foo", "bar"}));
        System.out.println(new StringDealer().countAndSay(8));
        List<String> success = Arrays.asList(" ", "0", " 0.1 ", ".1", "2e10", " -90e3   ", " 6e-1", "53.5e93", "-.1", "1.", " 005047e+6", "46.e3", ".2e81");
        for (String s : success) {
            if (!new StringDealer().isNumber(s)) {
                System.out.println(s);
            }
        }
        System.out.println("===========================================");
        List<String> fail = Arrays.asList(" .", ". ", "abc", "1 a", " 1e", "e3", " 99e2.5 ", " --6 ", "95a54e53", "-+3", ".", "+e", "+3. e04116", ". 1", ".e1", "4e+", " -.");
        for (String s : fail) {
            if (new StringDealer().isNumber(s)) {
                System.out.println(s);
            }
        }
        System.out.println((int) '0');
        System.out.println((int) '9');
        System.out.println(new StringDealer().restoreIpAddresses("101023"));*/

        System.out.println(new StringDealer().isPalindrome("9P"));

    }

    public boolean isPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        while (j - i >= 1) {
            char c1 = s.charAt(i);
            while (j - i >= 1 && !Character.isLetter(c1) && !Character.isDigit(c1)) {
                c1 = s.charAt(++i);
            }
            char c2 = s.charAt(j);
            while (j - i >= 1 && !Character.isLetter(c2) && !Character.isDigit(c2)) {
                c2 = s.charAt(--j);
            }
            if (Character.toUpperCase(c1) != Character.toUpperCase(c2)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    public int numDistinct1(String s, String t) {
        if (t.length() == 0) {
            return 1;
        }
        if (s.length() == 0 || s.length() < t.length() || (s.length() == t.length() && !s.equals(t))) {
            return 0;
        }
        int[][] DP = new int[t.length() + 1][s.length() + 1];
        for (int i = 0; i < s.length() + 1; i++) {
            DP[0][i] = 1;
        }
        for (int i = 0; i < t.length(); i++) {
            for (int j = 0; j < s.length(); j++) {
                DP[i + 1][j + 1] = (s.charAt(j) == t.charAt(i)) ? DP[i + 1][j] + DP[i][j] : DP[i + 1][j];
            }
        }
        return DP[t.length()][s.length()];
    }

    final HashMap<String, Integer> cacheMap1 = new HashMap<>();

    public int numDistinct(String s, String t) {
        String key = s + "-" + t;
        Integer integer1 = cacheMap1.get(key);
        if (integer1 != null) {
            return integer1;
        }
        if (t.length() == 0) {
            cacheMap1.put(key, 1);
            return 1;
        }
        if (s.length() == 0 || s.length() < t.length() || (s.length() == t.length() && !s.equals(t))) {
            cacheMap1.put(key, 0);
            return 0;
        }
        char c = t.charAt(0);
        char[] chars = s.toCharArray();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                list.add(i);
            }
        }
        if (t.length() == 1) {
            cacheMap1.put(key, list.size());
            return list.size();
        }
        int count = 0;
        for (Integer integer : list) {
            count += numDistinct(s.substring(integer + 1), t.substring(1));
        }
        cacheMap1.put(key, count);
        return count;
    }

    final HashMap<String, Boolean> cacheMap = new HashMap<>();

    public boolean isInterleave(String s1, String s2, String s3) {
        String cacheKey = s1 + "-" + s2 + "-" + s3;
        Boolean aBoolean = cacheMap.get(cacheKey);
        if (aBoolean != null) {
            return aBoolean;
        }
        if ((s1.length() + s2.length() - s3.length()) != 0) {
            cacheMap.put(cacheKey, false);
            return false;
        }
        if ("".equals(s1) || "".equals(s2)) {
            boolean result = (s1 + s2).equals(s3);
            cacheMap.put(cacheKey, result);
            return result;
        }
        if ((s1 + s2).equals(s3) || (s2 + s1).equals(s3)) {
            cacheMap.put(cacheKey, true);
            return true;
        }
        char c1 = s1.charAt(0);
        char c2 = s2.charAt(0);
        char c3 = s3.charAt(0);
        if (c1 != c3 && c2 != c3) {
            cacheMap.put(cacheKey, false);
            return false;
        }
        String s3sub = s3.substring(1);
        if (c1 == c3 && c2 == c3) {
            boolean result = isInterleave(s1.substring(1), s2, s3sub) || isInterleave(s1, s2.substring(1), s3sub);
            cacheMap.put(cacheKey, result);
            return result;
        }
        boolean result = (c1 == c3) ? isInterleave(s1.substring(1), s2, s3sub) : isInterleave(s1, s2.substring(1), s3sub);
        cacheMap.put(cacheKey, result);
        return result;
    }


    public List<String> restoreIpAddresses(String s) {
        return doRestoreIpAddresses(s, 4);
    }

    public List<String> doRestoreIpAddresses(String s, int num) {
        List<String> ret = new ArrayList<>();
        if (num == 0 || s.length() == 0) {
            return ret;
        }
        if (num == 1 && (s.length() > 3 || (s.length() > 1 && s.charAt(0) == '0'))) {
            return ret;
        }
        List<String> current = new ArrayList<>();
        char[] chars = s.toCharArray();
        if (num == 1) {
            for (char aChar : chars) {
                if ((int) aChar < 48 || (int) aChar > 57) {
                    return ret;
                }
            }
            if (Integer.parseInt(s) <= 255) {
                ret.add(s);
            }
            return ret;
        }
        int i = 0;
        while (i < chars.length && i < 3) {
            if ((int) chars[i] < 48 || (int) chars[i] > 57) {
                return ret;
            }
            char[] chars1 = new char[i + 1];
            System.arraycopy(chars, 0, chars1, 0, i + 1);
            if (i == 0 && chars[i] == '0') {
                current.add(new String(chars1));
                break;
            }
            String s1 = new String(chars1);
            if (Integer.parseInt(s1) > 255) {
                break;
            }
            current.add(s1);
            i++;
        }
        for (String s1 : current) {
            List<String> strings = doRestoreIpAddresses(s.substring(s1.length()), num - 1);
            for (String string : strings) {
                ret.add(s1 + "." + string);
            }
        }
        return ret;
    }


    public int numDecodings(String s) {
        if (s == null || s.length() == 0 || (s.charAt(0) == '0')) {
            return 0;
        }
        char[] chars = s.toCharArray();
        char[] temp = new char[2];
        int[] dp = new int[chars.length + 1];
        dp[0] = 1;
        dp[1] = 1;
        char pre = chars[0];
        for (int i = 1; i < chars.length; i++) {
            temp[0] = pre;
            temp[1] = chars[i];
            int number = Integer.parseInt(new String(temp));
            if (chars[i] == '0') {
                if (number < 1 || number > 26) {
                    break;
                }
                dp[i + 1] = dp[i - 1];
            } else {
                dp[i + 1] = (pre == '0') ? dp[i] : ((number >= 1 && number <= 26) ? dp[i] + dp[i - 1] : dp[i]);
            }
            pre = chars[i];
        }
        return dp[chars.length];
    }

    /**
     * 判断s1是否是s2的扰乱字符串
     *
     * @param s1 s1
     * @param s2 s2
     * @return Boolean
     */
    public boolean isScramble(String s1, String s2) {
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int m = chars1.length;
        int n = chars2.length;
        if (m != n) {
            return false;
        }
        boolean[][][] dp = new boolean[n][n][n + 1];
        //动态规划解题
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                for (int j = 0; j <= n - len; j++) {
                    if (len == 1) {
                        dp[i][j][1] = chars1[i] == chars2[j];
                        continue;
                    }
                    for (int k = 1; k <= len - 1; k++) {
                        if (dp[i][j][k] && dp[i + k][j + k][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                        if (dp[i][j + len - k][k] && dp[i + k][j][len - k]) {
                            dp[i][j][len] = true;
                            break;
                        }
                    }
                }
            }
        }
        return dp[0][0][n];
    }


    public boolean isScramble2(String s1, String s2) {
        HashMap<String, Integer> memoization = new HashMap<>();
        return isScrambleRecursion(s1, s2, memoization);
    }

    public boolean isScrambleRecursion(String s1, String s2, HashMap<String, Integer> memoization) {
        //判断之前是否已经有了结果
        int ret = memoization.getOrDefault(s1 + "#" + s2, -1);
        if (ret == 1) {
            return true;
        } else if (ret == 0) {
            return false;
        }
        if (s1.length() != s2.length()) {
            memoization.put(s1 + "#" + s2, 0);
            return false;
        }
        if (s1.equals(s2)) {
            memoization.put(s1 + "#" + s2, 1);
            return true;
        }

        int[] letters = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            letters[s1.charAt(i) - 'a']++;
            letters[s2.charAt(i) - 'a']--;
        }
        for (int i = 0; i < 26; i++)
            if (letters[i] != 0) {
                memoization.put(s1 + "#" + s2, 0);
                return false;
            }

        for (int i = 1; i < s1.length(); i++) {
            if (isScramble(s1.substring(0, i), s2.substring(0, i)) && isScramble(s1.substring(i), s2.substring(i))) {
                memoization.put(s1 + "#" + s2, 1);
                return true;
            }
            if (isScramble(s1.substring(0, i), s2.substring(s2.length() - i))
                    && isScramble(s1.substring(i), s2.substring(0, s2.length() - i))) {
                memoization.put(s1 + "#" + s2, 1);
                return true;
            }
        }
        memoization.put(s1 + "#" + s2, 0);
        return false;
    }


    public static String minWindow(String s, String t) {
        if (s == null || s.length() == 0 || t == null || t.length() == 0 || s.length() < t.length()) {
            return "";
        }
        //当前滑动窗口
        int[] window = new int[128];
        //当前实现的目标
        int[] target = new int[128];
        int count = 0;
        //目标的前后索引
        int sIndex = 0, eIndex = -1;
        //窗口的前后索引
        int left = 0, right = 0;
        int minLength = s.length() + 1;
        //初始化目标
        for (char c : t.toCharArray()) {
            target[c]++;
        }
        while (right < s.length()) {
            char ch = s.charAt(right);
            window[ch]++;
            if (target[ch] > 0 && target[ch] >= window[ch]) {
                count++;
            }
            //满足了所有的子序列
            while (count == t.length()) {
                ch = s.charAt(left);
                if (target[ch] > 0 && target[ch] >= window[ch]) {
                    count--;
                }
                if (minLength > right - left + 1) {
                    sIndex = left;
                    eIndex = right;
                    minLength = right - left + 1;
                }
                window[ch]--;
                left++;
            }
            right++;
        }
        return s.substring(sIndex, eIndex + 1);
    }


    public static String minWindow2(String s, String t) {
        if (s == null || s == "" || t == null || t == "" || s.length() < t.length()) {
            return "";
        }
        //用来统计t中每个字符出现次数
        int[] needs = new int[128];
        //用来统计滑动窗口中每个字符出现次数
        int[] window = new int[128];

        for (int i = 0; i < t.length(); i++) {
            needs[t.charAt(i)]++;
        }

        int left = 0;
        int right = 0;

        String res = "";

        //目前有多少个字符
        int count = 0;

        //用来记录最短需要多少个字符。
        int minLength = s.length() + 1;

        while (right < s.length()) {
            char ch = s.charAt(right);
            window[ch]++;
            if (needs[ch] > 0 && needs[ch] >= window[ch]) {
                count++;
            }

            //移动到不满足条件为止
            while (count == t.length()) {
                ch = s.charAt(left);
                if (needs[ch] > 0 && needs[ch] >= window[ch]) {
                    count--;
                }
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    res = s.substring(left, right + 1);

                }
                window[ch]--;
                left++;

            }
            right++;

        }
        return res;
    }


    public String minWindow1(String s, String t) {
        if (s == null || s.length() == 0 || t == null || t.length() == 0 || s.length() < t.length()) {
            return "";
        }
        //计数池
        Map<Character, Integer> countMap = new HashMap<>();
        final char[] chars = t.toCharArray();
        for (char c : chars) {
            countMap.put(c, countMap.getOrDefault(c, 0) + 1);
        }
        int len = s.length() + 1;
        int left = 0, right = 0;
        int sIndex = 0, eIndex = -1;
        while (right < s.length()) {
            if (countMap.containsKey(s.charAt(right))) {
                countMap.put(s.charAt(right), countMap.getOrDefault(s.charAt(right), 0) - 1);
            }
            if (isCoverAll(countMap)) {
                //左窗边右移直到覆盖最小边界
                while (!countMap.containsKey(s.charAt(left)) || countMap.get(s.charAt(left)) < 0) {
                    if (countMap.containsKey(s.charAt(left))) {
                        countMap.put(s.charAt(left), countMap.get(s.charAt(left)) + 1);
                    }
                    left++;
                }
                if (len > right - left + 1) {
                    sIndex = left;
                    eIndex = right;
                    len = right - left + 1;
                }
            }
            right++;
        }
        return s.substring(sIndex, eIndex + 1);
    }


    private boolean isCoverAll(Map<Character, Integer> countMap) {
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 0) {
                return false;
            }
        }
        return true;
    }


    public int minDistance(String word1, String word2) {
        if (word1.length() == 0) {
            return word2.length();
        }
        if (word2.length() == 0) {
            return word1.length();
        }
        //动态规划
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];
        //基础数据填充
        for (int i = 0; i < dp.length; i++) {
            int[] arr = dp[i];
            for (int j = 0; j < arr.length; j++) {
                if (i == 0) {
                    dp[i][j] = j;
                    continue;
                }
                if (j == 0) {
                    dp[i][j] = i;
                    continue;
                }
                int down = dp[i][j - 1] + 1;
                int left = dp[i - 1][j] + 1;
                int left_down = dp[i - 1][j - 1];
                if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                    left_down += 1;
                }
                dp[i][j] = Math.min(left, Math.min(down, left_down));
            }
        }
        return dp[word1.length()][word2.length()];
    }

    /**
     * true ：
     * false：".", "+e" ,"+3. e04116"
     * 数字
     * 指数
     * 正负号
     * 小数点
     * <a href='https://leetcode-cn.com/problems/valid-number/'/>
     */
    public boolean isNumber(String s) {
        if (s == null || s.length() == 0) {
            return false;
        }
        final char[] chars = s.toCharArray();
        int start = 0, endIndex = chars.length;
        while (start < chars.length && (int) chars[start] == 32) {
            start++;
        }
        if (start == chars.length) {
            return false;
        }
        while (--endIndex >= 0) {
            if ((int) chars[endIndex] != 32) {
                break;
            }
        }

        boolean hasE = false;
        boolean hasPoint = false;
        boolean end = false;
        for (int i = start; i <= endIndex; i++) {
            if (end) {
                return false;
            }
            if (i == start) {
                if (isNumberChar(chars[i]) || isPrefixChar(chars[i])) {
                    continue;
                }
                if ((int) chars[i] == 46 && endIndex - start != 0) {
                    hasPoint = true;
                    continue;
                }
                //首位但不可打头
                return false;
            }
            if (isNumberChar(chars[i])) {
                continue;
            }
            if (!hasPoint && !hasE) {
                if ((int) chars[i] == 46 && (isNumberChar(chars[i - 1]) || isPrefixChar(chars[i - 1]))) {
                    if (i == endIndex && isPrefixChar(chars[i - 1])) {
                        return false;
                    }
                    hasPoint = true;
                    continue;
                }
            }
            if (!hasE) {
                if ((int) chars[i] == 101 && (isNumberChar(chars[i - 1]) || (int) chars[i - 1] == 46)) {
                    if (i == endIndex) {
                        return false;
                    }
                    hasE = true;
                    if ((int) chars[i - 1] == 46) {
                        if ((i - 2) < 0) {
                            return false;
                        }
                        if (!isNumberChar(chars[i - 2])) {
                            return false;
                        }
                    }
                    continue;
                }
            }
            if ((int) chars[i] == 32) {
                end = true;
                continue;
            }
            if (hasE && isPrefixChar(chars[i]) && (int) chars[i - 1] == 101) {
                //当前是负号前面是E
                if (i == endIndex) {
                    return false;
                }
                continue;
            }
            return false;
        }
        return true;
    }

    public boolean isNumberChar(char c) {
        return (int) c >= 48 && (int) c <= 57;
    }

    public boolean isPrefixChar(char c) {
        return (int) c == 43 || (int) c == 45;
    }

    public int lengthOfLastWord(String s) {
        if (s == null) {
            return 0;
        }
        int len = 0;
        final char[] chars = s.toCharArray();
        int index = chars.length;
        while (--index >= 0) {
            if (chars[index] == ' ') {
                if (len == 0) {
                    continue;
                }
                break;
            }
            len++;
        }
        return len;
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> ret = new ArrayList<>();
        if (strs == null) {
            return ret;
        }
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            String common = strTransfer(str);
            final List<String> orDefault = map.getOrDefault(common, new ArrayList<>());
            orDefault.add(str);
            map.put(common, orDefault);
        }
        for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
            ret.add(stringListEntry.getValue());
        }
        return ret;
    }

    /**
     * 异位字母转换
     */
    private String strTransfer(String str) {
        final char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    static HashMap<Integer, String> map = new HashMap<>();

    static {
        map.put(1, "1");
        map.put(2, "11");
        map.put(3, "21");
        map.put(4, "1211");
        map.put(5, "111221");
    }

    public String countAndSay(int n) {
        String s = map.get(n);
        if (s == null) {
            String preStr = countAndSay(n - 1);
            //对字符进行描述
            map.put(n, s = say(preStr));
        }
        return s;
    }

    private String say(String preStr) {
        StringBuilder builder = new StringBuilder();
        char[] chars = preStr.toCharArray();
        int count = 1;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] != chars[i - 1]) {
                builder.append(count).append(chars[i - 1]);
                count = 1;
                continue;
            }
            count++;
        }
        builder.append(count).append(chars[chars.length - 1]);
        return builder.toString();
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
