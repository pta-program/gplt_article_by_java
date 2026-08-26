import java.io.*;

/**
 * L1-101 别再来这么多猫娘了！
 * 原理：
 * 1) 题目要求“违禁词按输入顺序依次处理”，即外层按违禁词顺序，内层在当前文本上从左到右、非重叠地查找该词。
 *    找到则计数+1 并替换为 "<censored>"，查找结束后从词末尾继续（不重叠）。因此不能把所有词混在同一位置做优先级比较，
 *    必须对每个词先后在“已替换过的文本”上独立扫描。
 * 2) 样例 5 是关键：违禁词 [BB, AB]，文本 AAABBB。
 *    若按“同位置优先”扫描会先命中 AB（位置2），得到 AA<censored><censored>；而按题意先处理 BB 会先在 3-4 处命中 BB，
 *    得到 AAA<censored>B，后续再处理 AB 就找不到了，符合官方期望。
 * 3) 阈值判断：累计命中次数 cnt，若 cnt < k 输出替换后文本，否则输出 cnt 换行 He Xie Ni Quan Jia!
 *
 * 复杂度：设文本长度 L 初始≤5000，违禁词数 N≤100，词长≤10，替换后文本长度最多 L*10/1 ≈50000。
 *         每轮扫描 O(L)，共 N 轮 → O(N*L) ≤5e5，空间 O(L)。
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null) return;
        line = line.trim();
        if (line.isEmpty()) return;
        int n = Integer.parseInt(line);
        String[] words = new String[n];
        for (int i = 0; i < n; i++) {
            String w = br.readLine();
            // 违禁词不含空格，整行即为词本身；若为 null 视为空串
            words[i] = (w == null ? "" : w);
        }
        String kLine = br.readLine();
        int k = 0;
        if (kLine != null) k = Integer.parseInt(kLine.trim());
        String s = br.readLine();
        if (s == null) s = "";

        int cnt = 0;
        String cur = s;
        // 按输入顺序依次处理每个违禁词
        for (String w : words) {
            if (w == null || w.isEmpty()) continue;
            StringBuilder sb = new StringBuilder(cur.length() + 1024);
            int p = 0;
            while (p < cur.length()) {
                if (cur.startsWith(w, p)) {
                    cnt++;
                    sb.append("<censored>");
                    p += w.length(); // 非重叠，跳过已匹配区间
                } else {
                    sb.append(cur.charAt(p));
                    p++;
                }
            }
            cur = sb.toString();
        }

        if (cnt < k) {
            System.out.println(cur);
        } else {
            System.out.println(cnt);
            System.out.println("He Xie Ni Quan Jia!");
        }
    }
}
