import java.util.Scanner;

/**
 * L1-079 天梯赛的善良
 * 实现原理：单次扫描维护当前最小/最大值及其出现次数；遇到新极值时重置对应计数。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, minCount = 0, maxCount = 0;
        while (n-- > 0) {
            int value = scanner.nextInt();
            if (value < min) { min = value; minCount = 1; }
            else if (value == min) minCount++;
            if (value > max) { max = value; maxCount = 1; }
            else if (value == max) maxCount++;
        }
        System.out.println(min + " " + minCount);
        System.out.println(max + " " + maxCount);
    }
}
