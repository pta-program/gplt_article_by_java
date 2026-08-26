import java.util.Scanner;

/**
 * L1-060 心理阴影面积
 * 实现原理：红线拐点与蓝线围成的区域是底为 100、高为 x-y 的三角形，
 * 面积为 100*(x-y)/2。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt(), y = scanner.nextInt();
        System.out.println(50 * (x - y));
    }
}
