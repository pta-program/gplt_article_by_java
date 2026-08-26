import java.util.Scanner;

/**
 * L1-005 考试座位号
 * 实现原理：试机座位号范围为 1..N，可将其作为数组下标建立直接映射，
 * 查询时 O(1) 取出准考证号与考试座位号。
 * 预处理时间 O(N)，查询时间 O(M)，空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] ticket = new String[n + 1];
        int[] examSeat = new int[n + 1];

        for (int i = 0; i < n; i++) {
            String id = scanner.next();
            int testSeat = scanner.nextInt();
            ticket[testSeat] = id;
            examSeat[testSeat] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int testSeat = scanner.nextInt();
            System.out.println(ticket[testSeat] + " " + examSeat[testSeat]);
        }
    }
}
