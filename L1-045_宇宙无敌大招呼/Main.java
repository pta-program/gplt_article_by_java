import java.util.Scanner;

/**
 * L1-045 宇宙无敌大招呼
 * 实现原理：读取星球名后拼接固定问候前缀输出。
 * 时间复杂度 O(|S|)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello " + new Scanner(System.in).next());
    }
}
