import java.util.Scanner;

/**
 * L1-018 大笨钟
 * 实现原理：12:00（含）以前不敲钟，输出 "Only hh:mm.  Too early to Dang." 注意保留输入时间的前导零与双空格。
 * 12:00 之后，每到整点敲 hour-12 下，若分钟>0则再多敲1下（敲到下一整点），最后用 Dang 拼接（每敲一下对应一个 Dang）。
 * 时间复杂度 O(敲钟次数) <=12，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String time = scanner.next();
        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));
        if (hour < 12 || (hour == 12 && minute == 0)) {
            System.out.println("Only " + time + ".  Too early to Dang.");
            return;
        }
        int count = hour - 12 + (minute > 0 ? 1 : 0);
        StringBuilder sb = new StringBuilder(count * 4);
        for (int i = 0; i < count; i++) sb.append("Dang");
        System.out.println(sb.toString());
    }
}
