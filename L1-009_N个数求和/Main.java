import java.math.BigInteger;
import java.util.Scanner;

/**
 * L1-009 N个数求和
 * 实现原理：累计分数 sum = numerator/denominator，读入每个 a/b 后执行 sum = sum + a/b = (numerator*b + a*denominator)/(denominator*b)，
 * 并用 gcd 约分以控制数值增长，使用 BigInteger 避免溢出。最后按题目要求的带分数格式输出：
 *  - 若分子为0输出0；
 *  - 否则分离整数部分 integer = numerator/denominator 与余数 remainder = |numerator| % denominator；
 *  - 若整数部分 !=0，输出整数，余数不为0时再输出 " 余数/分母"（余数恒为正）；
 *  - 若整数为0则只输出分数，需保留负号在分子上。
 * 时间复杂度 O(N * logM)，空间复杂度 O(大整数位数)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        BigInteger numerator = BigInteger.ZERO;
        BigInteger denominator = BigInteger.ONE;

        for (int i = 0; i < n; i++) {
            String[] fraction = scanner.next().split("/");
            BigInteger a = new BigInteger(fraction[0]);
            BigInteger b = new BigInteger(fraction[1]);
            numerator = numerator.multiply(b).add(a.multiply(denominator));
            denominator = denominator.multiply(b);
            BigInteger gcd = numerator.gcd(denominator);
            // BigInteger.gcd 恒返回非负值，numerator 为0时 gcd=denominator
            if (!gcd.equals(BigInteger.ZERO)) {
                numerator = numerator.divide(gcd);
                denominator = denominator.divide(gcd);
            }
        }

        if (numerator.equals(BigInteger.ZERO)) {
            System.out.println(0);
            return;
        }
        BigInteger integer = numerator.divide(denominator);
        BigInteger remainder = numerator.abs().remainder(denominator);
        StringBuilder out = new StringBuilder();
        if (!integer.equals(BigInteger.ZERO)) {
            out.append(integer);
            if (!remainder.equals(BigInteger.ZERO)) {
                out.append(" ").append(remainder).append("/").append(denominator);
            }
        } else {
            if (numerator.signum() < 0) remainder = remainder.negate();
            out.append(remainder).append("/").append(denominator);
        }
        System.out.println(out.toString());
    }
}
