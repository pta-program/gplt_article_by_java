import java.util.Scanner;

/**
 * L1-116 普及赛排名
 * 原理：统计高校评级分 >=1700 的学生人数
 * 时间复杂度 O(n)，空间 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            int score = sc.nextInt();
            // 可见题面“低于 1700”（隐藏的“不”被忽略），样例 1500,2700,1700,1000,1699 中低于 1700 的为 3 个
            if (score < 1700) cnt++;
        }
        sc.close();
        System.out.println(cnt);
    }
}
