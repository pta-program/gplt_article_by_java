import java.util.Scanner;

/**
 * L1-092 进化论
 * 实现原理：优先判断 C 是否为 A×B，否则判断是否为 A+B，均不匹配时输出吐槽。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            int a = scanner.nextInt(), b = scanner.nextInt(), c = scanner.nextInt();
            if (c == a * b) System.out.println("Lv Yan");
            else if (c == a + b) System.out.println("Tu Dou");
            else System.out.println("zhe du shi sha ya!");
        }
    }
}
