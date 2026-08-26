import java.util.Scanner;

/**
 * L1-042 日期格式化
 * 实现原理：按连字符拆分月、日、年，再按年、月、日的顺序重组。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String[] date = new Scanner(System.in).next().split("-");
        System.out.println(date[2] + "-" + date[0] + "-" + date[1]);
    }
}
