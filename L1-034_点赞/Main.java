import java.util.Scanner;

/**
 * L1-034 点赞
 * 实现原理：用标签编号作为数组下标累计出现次数；扫描数组时，若次数相同则用较大
 * 编号覆盖答案，即可满足并列时选择最大编号的规则。
 * 时间复杂度 O(总标签数)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] count = new int[1001];
        int n = scanner.nextInt();
        while (n-- > 0) {
            int k = scanner.nextInt();
            while (k-- > 0) count[scanner.nextInt()]++;
        }
        int bestTag = 0;
        for (int tag = 1; tag <= 1000; tag++) {
            if (count[tag] >= count[bestTag]) bestTag = tag;
        }
        System.out.println(bestTag + " " + count[bestTag]);
    }
}
