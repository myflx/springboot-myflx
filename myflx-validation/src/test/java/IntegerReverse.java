import java.util.Objects;

public class IntegerReverse {

    public static void main(String[] args) {
        int reverse = new IntegerReverse().reverse(123);
        System.out.println(Objects.equals(reverse, 321));
        StringBuilder str = new StringBuilder("123");
        System.out.println(str.reverse());
    }

    public int reverse(int x) {
        int pre = 1;
        int max = Integer.MAX_VALUE;
        if (x<0){
            x = Math.abs(x);
            pre = -1;
        }
        StringBuilder str = new StringBuilder(x + "");
        return 0;
    }
}
