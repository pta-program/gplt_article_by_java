import java.util.Scanner;

/**
 * L1-049 天梯赛座位分配
 * 实现原理：
 * 每校队伍按 10 人一队顺序形成队列。座位分配分为两个阶段：
 * 1) 多校共存阶段：仍有 >=2 所学校有未入座队员时，按学校编号轮流各入座 1 人，座位号每次 +1。
 * 2) 单校隔位阶段：仅剩 1 所学校未坐满时，该校剩余队员需隔位就坐（座位号每次 +2）。
 * 阶段切换时若最后一座恰好属于该单校，则需额外跳过一个座位以保证同校队员间距为 2，
 * 否则直接从下一座开始隔位。此模型能保证同校队员互不相邻且隔位阶段间距最小。
 * 时间复杂度 O(总人数 + N*轮数)，空间复杂度 O(总人数)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] total = new int[n];
        int[] assigned = new int[n];
        int[][] seats = new int[n][];
        for (int i = 0; i < n; i++) {
            total[i] = scanner.nextInt() * 10;
            seats[i] = new int[total[i]];
        }

        int seatNumber = 1;
        int lastSchool = -1;

        // 阶段1：多校轮流，座位号连续
        while (true) {
            int active = 0;
            for (int i = 0; i < n; i++) if (assigned[i] < total[i]) active++;
            if (active <= 1) break;
            for (int i = 0; i < n; i++) {
                if (assigned[i] < total[i]) {
                    seats[i][assigned[i]++] = seatNumber++;
                    lastSchool = i;
                }
            }
        }

        // 阶段2：单校隔位
        int remaining = -1;
        for (int i = 0; i < n; i++) if (assigned[i] < total[i]) remaining = i;
        if (remaining != -1) {
            // 若最后一座即属于该单校，需额外空一座以保证间距为2
            if (lastSchool == remaining) seatNumber++;
            while (assigned[remaining] < total[remaining]) {
                seats[remaining][assigned[remaining]++] = seatNumber;
                seatNumber += 2;
            }
        }

        for (int school = 0; school < n; school++) {
            System.out.println("#" + (school + 1));
            for (int player = 0; player < total[school]; player += 10) {
                for (int j = 0; j < 10; j++) {
                    if (j > 0) System.out.print(' ');
                    System.out.print(seats[school][player + j]);
                }
                System.out.println();
            }
        }
    }
}
