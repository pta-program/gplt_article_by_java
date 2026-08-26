import java.util.Scanner;

/**
 * L1-003 个位数统计
 * 实现原理：1000 位整数不能用普通数值类型保存，因此按字符串逐字符计数。
 * 下标 0 到 9 分别记录对应数字出现次数，随后按升序输出非零计数。
 * 时间复杂度 O(k)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String number = scanner.next();
        int[] count = new int[10];
        for (char digit : number.toCharArray()) {
            count[digit - '0']++;
        }
        for (int digit = 0; digit <= 9; digit++) {
            if (count[digit] > 0) {
                System.out.println(digit + ":" + count[digit]);
            }
        }
    }
}
