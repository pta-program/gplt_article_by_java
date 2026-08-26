import java.io.*;
import java.util.*;

// L2-056 被n整除的n位数（polydivisible）
// 深度优先生成所有满足前缀整除性的 n 位数，再与区间 [a,b] 取交集
// n<=15，搜索树分支极小
// 时间取决于生成数量（通常 <1e5），空间 O(n)
public class Main {
    static int n;
    static long a, b;
    static List<Long> ans = new ArrayList<>();

    static void dfs(long cur, int len) {
        if (len == n) {
            if (cur >= a && cur <= b) ans.add(cur);
            return;
        }
        int nextLen = len + 1;
        for (int d = 0; d <= 9; d++) {
            long nxt = cur * 10 + d;
            if (nxt % nextLen != 0) continue;
            dfs(nxt, nextLen);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        n = Integer.parseInt(st.nextToken());
        a = Long.parseLong(st.nextToken());
        b = Long.parseLong(st.nextToken());
        // 生成
        for (int d = 1; d <= 9; d++) {
            long cur = d;
            if (cur % 1 != 0) continue;
            if (n == 1) {
                if (cur >= a && cur <= b) ans.add(cur);
            } else {
                dfs(cur, 1);
            }
        }
        Collections.sort(ans);
        if (ans.isEmpty()) {
            System.out.println("No Solution");
        } else {
            StringBuilder sb = new StringBuilder();
            for (long v : ans) sb.append(v).append('\n');
            System.out.print(sb.toString());
        }
    }
}
