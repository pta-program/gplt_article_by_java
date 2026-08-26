import java.util.Scanner;

/**
 * L1-108 零头就抹了吧
 * 原理：找到不超过 N 的最大 2 的幂，即 2^floor(log2 N)
 * 从 base=1 开始不断翻倍，直到 2*base > N 为止
 * 时间复杂度 O(log N)，空间 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long N = sc.nextLong();
        sc.close();
        long base = 1;
        while (base * 2 <= N) {
            base *= 2;
        }
        System.out.println(base);
    }
}
