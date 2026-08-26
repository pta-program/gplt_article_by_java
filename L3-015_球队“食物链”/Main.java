import java.io.*;
import java.util.*;

// L3-015 球队“食物链” - 哈密顿回路字典序最小
// 算法：按字典序DFS + 记忆化可行性剪枝（bitmask DP）
// N<=20, 用 int mask 表示已访问集合
// 时间复杂度 O(N^2 * 2^N) 最坏，实际剪枝后可接受
public class Main {
    static int N;
    static boolean[][] win;
    static int allMask;
    static int start;
    static byte[] memo; // 2:未知 1:可达 0:不可达
    static int[] perm;
    static boolean found;

    static boolean can(int mask, int cur) {
        if (mask == allMask) {
            return win[cur][start];
        }
        int idx = mask * N + cur;
        byte v = memo[idx];
        if (v != 2) return v == 1;
        for (int nxt = 0; nxt < N; nxt++) {
            if ((mask & (1 << nxt)) != 0) continue;
            if (!win[cur][nxt]) continue;
            if (can(mask | (1 << nxt), nxt)) {
                memo[idx] = 1;
                return true;
            }
        }
        memo[idx] = 0;
        return false;
    }

    static boolean dfs(int pos, int mask, int cur) {
        if (pos == N) {
            return win[cur][start];
        }
        // 按字典序尝试 nxt 从小到大
        for (int nxt = 0; nxt < N; nxt++) {
            if ((mask & (1 << nxt)) != 0) continue;
            if (!win[cur][nxt]) continue;
            int nmask = mask | (1 << nxt);
            if (!can(nmask, nxt)) continue;
            perm[pos] = nxt;
            if (dfs(pos + 1, nmask, nxt)) return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        if (line == null) return;
        line = line.trim();
        while (line.isEmpty()) line = br.readLine();
        N = Integer.parseInt(line.trim());
        win = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            String s = br.readLine();
            while (s != null && s.trim().isEmpty()) s = br.readLine();
            if (s == null) s = "";
            s = s.trim();
            for (int j = 0; j < N && j < s.length(); j++) {
                char c = s.charAt(j);
                win[i][j] = (c == 'W');
            }
        }
        allMask = (1 << N) - 1;
        perm = new int[N];
        // 按字典序枚举起点
        for (int s = 0; s < N; s++) {
            start = s;
            int size = (1 << N) * N;
            memo = new byte[size];
            Arrays.fill(memo, (byte)2);
            int mask0 = 1 << s;
            if (!can(mask0, s)) continue;
            perm[0] = s;
            if (dfs(1, mask0, s)) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < N; i++) {
                    if (i > 0) sb.append(' ');
                    sb.append(perm[i] + 1);
                }
                System.out.println(sb.toString());
                return;
            }
        }
        System.out.println("No Solution");
    }
}
