import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * L1-033 出生年
 * 实现原理：从出生年份起逐年枚举，将年份格式化为四位后以集合统计不同数字数，
 * 首次恰好等于目标值时输出年龄差和该年份。
 * 时间复杂度 O(答案年份-出生年份)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int birth = scanner.nextInt();
        int target = scanner.nextInt();
        for (int year = birth; ; year++) {
            String text = String.format("%04d", year);
            Set<Character> digits = new HashSet<>();
            for (char c : text.toCharArray()) digits.add(c);
            if (digits.size() == target) {
                System.out.printf("%d %s", year - birth, text);
                return;
            }
        }
    }
}
