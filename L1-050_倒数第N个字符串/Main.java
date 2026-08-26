import java.util.Scanner;

/**
 * L1-050 倒数第N个字符串
 * 实现原理：长度 L 的字符串可视为 26 进制数（a 为 0）。倒数第 N 个对应从 0 起的
 * 正序编号 26^L-N，将该编号转换为固定 L 位 26 进制即可。
 * 时间复杂度 O(L)，空间复杂度 O(L)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int length = scanner.nextInt();
        int n = scanner.nextInt();
        int total = 1;
        for (int i = 0; i < length; i++) total *= 26;
        int value = total - n;
        char[] result = new char[length];
        for (int i = length - 1; i >= 0; i--) {
            result[i] = (char) ('a' + value % 26);
            value /= 26;
        }
        System.out.println(result);
    }
}
