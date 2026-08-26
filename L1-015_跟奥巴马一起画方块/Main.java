import java.util.Scanner;

/**
 * L1-015 跟奥巴马一起画方块
 * 实现原理：列数 = N，行数 = N/2 四舍五入，即 (N+1)/2（整数运算等价于 Math.round(N/2.0)）。
 * 为兼容 Java8，不使用 String.repeat，改为手动构造行字符串后循环输出。
 * 时间复杂度 O(N^2)（输出规模），空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char c = scanner.next().charAt(0);
        int rows = (n + 1) / 2;
        // 手动构造一行，避免 Java11 的 String.repeat
        StringBuilder lineBuilder = new StringBuilder(n);
        for (int i = 0; i < n; i++) lineBuilder.append(c);
        String line = lineBuilder.toString();
        for (int i = 0; i < rows; i++) {
            System.out.println(line);
        }
    }
}
