import java.io.*;

// L3-020 至多删三个字符
// 算法：按删除数 DP，h[i][d] 表示前 i 字符删恰好 d 个的不同串数
// 转移 h[i][d]=h[i-1][d]+h[i-1][d-1]- h[prev-1][d-gap]（若重复，gap=i-prev<=d）
// 时间复杂度 O(n*4)，n<=1e6，空间 O(4*4) 滚动
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String s = br.readLine();
        if (s == null) return;
        s = s.trim();
        int n = s.length();
        if (n == 0) { System.out.println(0); return; }
        int K = 3;
        // 环形缓冲，大小 4，存储最近的 h 值
        long[][] hist = new long[4][K + 1];
        hist[0][0] = 1; // h[0][0]=1
        // last 出现位置 1-indexed
        int[] last = new int[26];
        java.util.Arrays.fill(last, -1);
        // 用于当前 i 的新行
        long[] cur = new long[K + 1];
        for (int i = 1; i <= n; i++) {
            int c = s.charAt(i - 1) - 'a';
            int prev = last[c];
            int gap = (prev == -1 ? Integer.MAX_VALUE : i - prev);
            for (int d = 0; d <= K; d++) {
                if (d > i) {
                    cur[d] = 0;
                    continue;
                }
                if (d == 0) {
                    cur[d] = 1;
                    continue;
                }
                long val = 0;
                // h[i-1][d]
                int pi = (i - 1) & 3;
                if (d <= i - 1) val += hist[pi][d];
                // h[i-1][d-1]
                if (d - 1 <= i - 1) val += hist[pi][d - 1];
                // 去重
                if (prev != -1 && gap <= d) {
                    int idx = (prev - 1) & 3;
                    // 当 prev-1 ==0 时 hist[0][d-gap] 已有值，d-gap 可能为0
                    // 需确保 d-gap <= prev-1，或 d-gap==0 恒为1
                    // 但若 d-gap > prev-1 且 d-gap>0，则该值应为0
                    if (d - gap <= prev - 1 || d - gap == 0) {
                        val -= hist[idx][d - gap];
                    }
                }
                cur[d] = val;
            }
            // 写入 hist
            int ci = i & 3;
            System.arraycopy(cur, 0, hist[ci], 0, K + 1);
            last[c] = i;
        }
        long ans = 0;
        int fn = n & 3;
        for (int d = 0; d <= K && d <= n; d++) ans += hist[fn][d];
        System.out.println(ans);
    }
}
