import java.util.Scanner;

/**
 * L1-043 阅览室
 * 实现原理：数组记录每本书尚未结束的借出分钟数。读到 E 时只有存在对应 S 才累计
 * 一次有效借阅及其时长；每天书号为 0 的记录仅作结束标志。
 * 时间复杂度 O(总记录数)，空间复杂度 O(1000)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int days = scanner.nextInt();
        while (days-- > 0) {
            int[] startTime = new int[1001];
            int count = 0, totalMinutes = 0;
            while (true) {
                int id = scanner.nextInt();
                String action = scanner.next();
                String[] time = scanner.next().split(":");
                int minute = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
                if (id == 0) break;
                if (action.equals("S")) {
                    startTime[id] = minute + 1; // 0 表示没有有效的借出记录。
                } else if (startTime[id] != 0) {
                    totalMinutes += minute - (startTime[id] - 1);
                    count++;
                    startTime[id] = 0;
                }
            }
            int average = count == 0 ? 0 : (int) Math.round(totalMinutes * 1.0 / count);
            System.out.println(count + " " + average);
        }
    }
}
