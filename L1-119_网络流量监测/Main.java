import java.util.Scanner;

/**
 * L1-119 网络流量监测
 * 原理：一次遍历求最大值、最小值、总和，进而求向下取整平均值 avg=sum/n
 *   第二行输出所有满足 flow[i] > 2*avg 的时段编号(从1开始)，若无输出 Normal
 * 时间复杂度 O(n)，空间 O(n)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        long sum = 0;
        int mx = Integer.MIN_VALUE, mn = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            sum += a[i];
            if (a[i] > mx) mx = a[i];
            if (a[i] < mn) mn = a[i];
        }
        sc.close();
        long avg = sum / n; // 向下取整
        System.out.println(mx + " " + mn + " " + avg);
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            if ((long) a[i] > 2 * avg) {
                if (cnt > 0) sb.append(' ');
                sb.append(i + 1);
                cnt++;
            }
        }
        if (cnt == 0) System.out.println("Normal");
        else System.out.println(sb.toString());
    }
}
