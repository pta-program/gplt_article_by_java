import java.util.Scanner;

/**
 * L1-017 到底有多二
 * 实现原理：字符串统计数字 2 的个数与实际数字位数；若为负数乘 1.5，
 * 若末位是偶数再乘 2，最后转换成百分数。
 * 时间复杂度 O(k)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String number = new Scanner(System.in).next();
        boolean negative = number.charAt(0) == '-';
        int start = negative ? 1 : 0;
        int countTwo = 0;
        for (int i = start; i < number.length(); i++) {
            if (number.charAt(i) == '2') countTwo++;
        }
        double degree = countTwo * 100.0 / (number.length() - start);
        if (negative) degree *= 1.5;
        if ((number.charAt(number.length() - 1) - '0') % 2 == 0) degree *= 2;
        System.out.printf("%.2f%%", degree);
    }
}
