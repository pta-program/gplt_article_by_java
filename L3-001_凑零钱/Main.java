import java.util.*;
import java.io.*;

// L3-001 凑零钱: 01背包 + 后缀可达性求字典序最小解
// 思路：硬币升序排序，计算后缀 DP dp[i][j] 表示从 i 到末尾能否凑出 j。
// 若能凑出则贪心从小到大选硬币，保证字典序最小。
// 时间复杂度 O(N*M) , M<=100, N<=1e4 => 约 1e6
// 空间复杂度 O(N*M)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取所有整数，兼容跨行
        List<Integer> all = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                // 去掉可能的冒号等
                tok = tok.replace(":", "");
                if (tok.isEmpty()) continue;
                try {
                    all.add(Integer.parseInt(tok));
                } catch (NumberFormatException e) {
                    // 忽略非数字
                }
            }
        }
        if (all.size() < 2) return;
        int N = all.get(0);
        int M = all.get(1);
        int[] a = new int[N];
        for (int i = 0; i < N && 2 + i < all.size(); i++) {
            a[i] = all.get(2 + i);
        }
        // 若实际读到的硬币数不足 N（输入含换行不全），已按 all 读取；若多于 N 取前 N
        // 若 N 大于 all 剩余，补零(不应发生)
        Arrays.sort(a); // 升序，便于字典序最小
        // dp[i][j] i from 0..N, j 0..M
        boolean[][] dp = new boolean[N + 1][M + 1];
        dp[N][0] = true;
        for (int i = N - 1; i >= 0; i--) {
            for (int j = 0; j <= M; j++) {
                dp[i][j] = dp[i + 1][j];
                if (j >= a[i] && dp[i + 1][j - a[i]]) {
                    dp[i][j] = true;
                }
            }
        }
        if (!dp[0][M]) {
            System.out.println("No Solution");
            return;
        }
        List<Integer> ans = new ArrayList<>();
        int need = M;
        for (int i = 0; i < N && need > 0; i++) {
            if (need >= a[i] && dp[i + 1][need - a[i]]) {
                ans.add(a[i]);
                need -= a[i];
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ans.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(ans.get(i));
        }
        System.out.println(sb.toString());
    }
}
