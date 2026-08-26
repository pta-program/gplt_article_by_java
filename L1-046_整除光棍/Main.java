import java.util.Scanner;

/**
 * L1-046 整除光棍
 * 实现原理：模拟光棍数除以 x 的长除法，只维护余数，因此不必保存巨大被除数。
 * 每次补入一个 1，余数为零时得到最短光棍；同时收集各位商即为 s。
 * 时间复杂度 O(n)，空间复杂度 O(n)，n 为答案光棍长度。
 */
public class Main {
    public static void main(String[] args) {
        int divisor = new Scanner(System.in).nextInt();
        int remainder = 0, length = 0;
        boolean started = false;
        StringBuilder quotient = new StringBuilder();
        do {
            remainder = remainder * 10 + 1;
            int digit = remainder / divisor;
            remainder %= divisor;
            if (digit != 0 || started) {
                quotient.append(digit);
                started = true;
            }
            length++;
        } while (remainder != 0);
        System.out.println(quotient + " " + length);
    }
}
