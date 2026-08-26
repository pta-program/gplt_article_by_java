import java.util.Scanner;

/**
 * L1-028 判断素数
 * 实现原理：大于 1 的数只需尝试 2 到 sqrt(n) 的因子；若存在整除因子则非素数。
 * 时间复杂度 O(N*sqrt(M))，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int cases = scanner.nextInt();
        while (cases-- > 0) System.out.println(isPrime(scanner.nextLong()) ? "Yes" : "No");
    }

    private static boolean isPrime(long value) {
        if (value < 2) return false;
        for (long divisor = 2; divisor * divisor <= value; divisor++) {
            if (value % divisor == 0) return false;
        }
        return true;
    }
}
