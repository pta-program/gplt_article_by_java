import java.util.Scanner;

/**
 * L1-029 是不是太胖了
 * 实现原理：标准体重（市斤）=(身高-100)*0.9*2，按一位小数输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        int height = new Scanner(System.in).nextInt();
        System.out.printf("%.1f", (height - 100) * 1.8);
    }
}
