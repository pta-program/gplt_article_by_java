import java.util.Scanner;

/**
 * L1-120 智慧文本编辑器
 * 原理：用可变字符串维护文本
 *  操作1：查找 s1 在当前文本 T 中出现的位置（允许重叠），按下标升序输出前 3 次，未找到输出 -1
 *  操作2：在下标 p 处插入字符串 s2（整体插入，p==|T| 时插末尾），输出新文本
 *  操作3：翻转闭区间 [l,r]（下标从 0 开始），输出新文本
 * 时间复杂度 O(N * |T|) N<=50 |T|<=1e3+ ，空间 O(|T|)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        String text = sc.next();
        for (int i = 0; i < N; i++) {
            int op = sc.nextInt();
            if (op == 1) {
                String s1 = sc.next();
                int found = 0;
                StringBuilder out = new StringBuilder();
                int printed = 0;
                for (int pos = 0; pos + s1.length() <= text.length() && printed < 3; pos++) {
                    if (text.regionMatches(pos, s1, 0, s1.length())) {
                        if (found > 0) out.append(' ');
                        out.append(pos);
                        found++;
                        printed++;
                    }
                }
                if (found == 0) System.out.println("-1");
                else System.out.println(out.toString());
            } else if (op == 2) {
                int p = sc.nextInt();
                String s2 = sc.next();
                // 插入整个 s2
                if (p < 0) p = 0;
                if (p > text.length()) p = text.length();
                text = text.substring(0, p) + s2 + text.substring(p);
                System.out.println(text);
            } else { // op==3
                int l = sc.nextInt();
                int r = sc.nextInt();
                char[] arr = text.toCharArray();
                while (l < r) {
                    char tmp = arr[l];
                    arr[l] = arr[r];
                    arr[r] = tmp;
                    l++; r--;
                }
                text = new String(arr);
                System.out.println(text);
            }
        }
        sc.close();
    }
}
