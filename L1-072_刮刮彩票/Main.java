import java.util.Scanner;

/**
 * L1-072 刮刮彩票
 * 实现原理：输入中 0 是初始显示位置，其实际数字为 1..9 中未出现的唯一数字。
 * 补全后按三次刮开坐标输出数字，最后根据指定方向计算三数和并查表兑奖。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    private static final int[] PRIZE = {0, 0, 0, 0, 0, 0, 10000, 36, 720, 360, 80, 252, 108, 72, 54, 180, 72, 180, 119, 36, 306, 1080, 144, 1800, 3600};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] board = new int[3][3];
        boolean[] exists = new boolean[10];
        int zeroRow = 0, zeroCol = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = scanner.nextInt();
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                } else exists[board[i][j]] = true;
            }
        }
        for (int value = 1; value <= 9; value++) if (!exists[value]) board[zeroRow][zeroCol] = value;
        for (int i = 0; i < 3; i++) {
            int row = scanner.nextInt() - 1, col = scanner.nextInt() - 1;
            System.out.println(board[row][col]);
        }
        int direction = scanner.nextInt();
        int sum;
        if (direction <= 3) sum = board[direction - 1][0] + board[direction - 1][1] + board[direction - 1][2];
        else if (direction <= 6) sum = board[0][direction - 4] + board[1][direction - 4] + board[2][direction - 4];
        else if (direction == 7) sum = board[0][0] + board[1][1] + board[2][2];
        else sum = board[0][2] + board[1][1] + board[2][0];
        System.out.println(PRIZE[sum]);
    }
}
