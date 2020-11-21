package number;


public class BigNumberSolution {
    private static final String ZERO = "0";

    /**
     * @param num1 "1232154651"
     * @param num2 "97579854"
     */
    public String multiply(String num1, String num2) {
        if (ZERO.equals(num1) || ZERO.equals(num2)) {
            return num2;
        }
        StringBuilder max;
        StringBuilder min;
        if (num1.length() < num2.length()) {
            max = new StringBuilder(num2);
            min = new StringBuilder(num1);
        } else {
            max = new StringBuilder(num1);
            min = new StringBuilder(num2);
        }

        StringBuilder preNumber = null;
        int maxLen = max.length();//10
        int minLen = min.length();//8
        int len = minLen;//8
        StringBuilder maxT;
        while (len-- > 0) {//0
            maxT = new StringBuilder(max);
            //高位先乘后拼接0
            String n1 = min.substring(minLen - len - 1, minLen - len);
            int i = 0;
            int pN = 0;
            while (i < maxLen) {
                String n2 = maxT.substring(maxLen - i - 1, maxLen - i);
                String n3 = String.valueOf(Integer.parseInt(n1) * Integer.parseInt(n2) + pN);
                maxT.setCharAt(maxLen - i - 1, n3.charAt(n3.length() - 1));
                pN = n3.length() > 1 ? Integer.parseInt(n3.substring(0, 1)) : 0;
                i++;
            }
            if (pN > 0) {
                maxT.insert(0, pN);
            }
            int append = len;
            while (append-- > 0) {
                maxT.append(ZERO);
            }
            if (preNumber == null) {
                preNumber = new StringBuilder(maxT);
            } else {
                //大数相加
                int j = preNumber.length() - 1;
                int k = maxT.length() - 1;
                int pre = 0;
                while (j >= 0 && k >= 0) {
                    String number = String.valueOf(Integer.parseInt(preNumber.substring(j, j + 1)) + Integer.parseInt(maxT.substring(k, k + 1)) + pre);
                    preNumber.setCharAt(j, number.charAt(number.length() - 1));
                    pre = number.length() > 1 ? Integer.parseInt(number.substring(0, 1)) : 0;
                    j--;
                    k--;
                }
                while (pre > 0 && j >= 0) {
                    String number = String.valueOf(Integer.parseInt(preNumber.substring(j, j + 1)) + pre);
                    preNumber.setCharAt(j, number.charAt(number.length() - 1));
                    pre = number.length() > 1 ? Integer.parseInt(number.substring(0, 1)) : 0;
                    j--;
                }
                if (pre > 0) {
                    preNumber.insert(0, pre);
                }

            }
        }
        return String.valueOf(preNumber);
    }

    public static void main(String[] args) {
        String multiply = new BigNumberSolution().multiply("721", "140");
        System.out.println(multiply);
    }
}

