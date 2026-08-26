import java.util.Scanner;

/**
 * L1-022 奇偶分家
 * 实现原理：逐个读取数字，通过 value%2 判断奇偶并累计对应计数器。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int odd = 0, even = 0;
        for (int i = 0; i < n; i++) {
            if (scanner.nextInt() % 2 == 0) even++;
            else odd++;
        }
        System.out.println(odd + " " + even);
    }
}
