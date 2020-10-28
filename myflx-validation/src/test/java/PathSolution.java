import java.util.Arrays;

public class PathSolution {
    public int uniquePaths2(int m, int n) {
        if (m <= 0 || n <= 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }
        return uniquePaths(m - 1, n) + uniquePaths(m, n - 1);
    }


    public int uniquePaths(int m, int n) {
        if (m <= 0 || n <= 0) {
            return 0;
        }
        if (m == 1 || n == 1) {
            return 1;
        }
        int[][] DP = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            DP[i][1] = 1;
        }
        for (int i = 1; i <= n; i++) {
            DP[1][i] = 1;
        }

        for (int i = 2; i <= m; i++) {
            for (int j = 2; j <= n; j++) {
                DP[i][j] = DP[i - 1][j] + DP[i][j - 1];
            }
        }
        for (int i = 0; i <= m; i++) {
            System.out.println(Arrays.toString(DP[i]));
        }

        return DP[m][n];
    }

    public static void main(String[] args) {
        System.out.println(new PathSolution().uniquePaths(51, 9));
    }
}
