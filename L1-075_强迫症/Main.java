import java.util.Scanner;

/**
 * L1-075 强迫症
 * 实现原理：六位输入已是 yyyyMM；四位输入按 yyMM 拆分，yy 小于 22 补 20，
 * 否则补 19，最后统一插入连字符输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String date = new Scanner(System.in).next();
        if (date.length() == 4) {
            int yy = Integer.parseInt(date.substring(0, 2));
            date = (yy < 22 ? "20" : "19") + date;
        }
        System.out.println(date.substring(0, 4) + "-" + date.substring(4));
    }
}
