import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * L1-027 出租
 * 实现原理：提取号码中出现过的不同数字并按降序组成 arr；
 * 对号码每一位，在 arr 中查找其位置，依序组成 index。
 * 时间复杂度 O(11*10)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String phone = new Scanner(System.in).next();
        boolean[] used = new boolean[10];
        for (char c : phone.toCharArray()) used[c - '0'] = true;
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) if (used[i]) digits.add(i);
        digits.sort(Collections.reverseOrder());

        StringBuilder arr = new StringBuilder();
        StringBuilder index = new StringBuilder();
        for (int i = 0; i < digits.size(); i++) {
            if (i > 0) arr.append(',');
            arr.append(digits.get(i));
        }
        for (int i = 0; i < phone.length(); i++) {
            if (i > 0) index.append(',');
            index.append(digits.indexOf(phone.charAt(i) - '0'));
        }
        System.out.println("int[] arr = new int[]{" + arr + "};");
        System.out.println("int[] index = new int[]{" + index + "};");
    }
}
