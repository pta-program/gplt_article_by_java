import java.util.Scanner;

/**
 * L1-111 大幂数
 * 原理：枚举幂次 k 从 31 递减到 1，对每个 k 计算部分和 S(m)=1^k+2^k+...+m^k
 *   若某项 i^k 超过剩余可凑数值或累加和超过 n，则该 k 不可行；
 *   一旦累加和恰等于 n，则找到最大 k，输出表达式 1^k+2^k+...+m^k
 *   使用限量幂函数防止溢出：若中间结果 > limit 则返回 limit+1
 * 时间复杂度 O(31 * m) 其中 m 最大约 2^(31/k)，总体可接受，空间 O(1)
 */
public class Main {
    // 计算 base^exp，若超过 limit 立即返回 limit+1
    static long limitedPower(long base, int exp, long limit) {
        long res = 1;
        while (exp-- > 0) {
            if (res > limit / base) return limit + 1;
            res *= base;
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        sc.close();
        int ansK = 0, ansM = 0;
        for (int k = 31; k >= 1 && ansK == 0; k--) {
            long sum = 0;
            for (int i = 1; ; i++) {
                long term = limitedPower(i, k, n);
                if (term > n - sum) break;
                sum += term;
                if (sum == n) {
                    ansK = k;
                    ansM = i;
                    break;
                }
            }
        }
        if (ansK == 0) {
            System.out.println("Impossible for " + n + ".");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= ansM; i++) {
                if (i > 1) sb.append('+');
                sb.append(i).append('^').append(ansK);
            }
            System.out.println(sb.toString());
        }
    }
}
