public class Game {
    /**
     * NIM游戏
     * 最优解意味着游戏
     *
     * @param n
     * @return
     */
    public boolean canWinNim(int n) {
        return n < 4 || (n % 4 != 0);
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        System.out.println(new Game().canWinNim(1348820612));
    }
}
