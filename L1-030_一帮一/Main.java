import java.util.Scanner;

/**
 * L1-030 一帮一
 * 实现原理：按排名保存学生。依次从前向后选取尚未分组的最高排名学生，
 * 从末尾反向寻找第一位异性且未分组的学生，即当前可选的最低排名异性。
 * 时间复杂度 O(N^2)，空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] gender = new int[n];
        String[] name = new String[n];
        boolean[] paired = new boolean[n];
        for (int i = 0; i < n; i++) {
            gender[i] = scanner.nextInt();
            name[i] = scanner.next();
        }
        for (int i = 0; i < n; i++) {
            if (paired[i]) continue;
            for (int j = n - 1; j > i; j--) {
                if (!paired[j] && gender[i] != gender[j]) {
                    paired[i] = paired[j] = true;
                    System.out.println(name[i] + " " + name[j]);
                    break;
                }
            }
        }
    }
}
