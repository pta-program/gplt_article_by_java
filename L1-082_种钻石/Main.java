import java.util.Scanner;

/**
 * L1-082 种钻石
 * 实现原理：题意要求不到一天不计入，因此直接取需求量除以每日速度的整数商。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(scanner.nextInt() / scanner.nextInt());
    }
}
