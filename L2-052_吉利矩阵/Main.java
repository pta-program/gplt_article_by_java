import java.io.*;
import java.util.*;

/**
 * L2-052 吉利矩阵
 * 计数 N x N 非负整数矩阵行列和均为 L
 * 采用 DP：逐行枚举行向量（组合数 C(L+N-1,N-1) <=220），DP 状态为列和向量
 * 状态编码为 base=L+1 的 N 位数，大小 (L+1)^N <= 10000
 * 时间复杂度 O(N * (L+1)^N * C(L+N-1,N-1)) 最坏约 1e4*220*4≈9e6
 * 空间 O((L+1)^N)
 */
public class Main {
    static List<int[]> genRowVecs(int L, int N) {
        List<int[]> res = new ArrayList<>();
        int[] cur = new int[N];
        genRec(0, L, cur, res);
        return res;
    }
    static void genRec(int idx, int remain, int[] cur, List<int[]> res) {
        if (idx == cur.length - 1) {
            cur[idx] = remain;
            res.add(Arrays.copyOf(cur, cur.length));
            return;
        }
        for (int v = 0; v <= remain; v++) {
            cur[idx] = v;
            genRec(idx + 1, remain - v, cur, res);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int L = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());

        int base = L + 1;
        int totalStates = 1;
        for (int i = 0; i < N; i++) totalStates *= base;
        long[] cur = new long[totalStates];
        long[] nxt = new long[totalStates];
        cur[0] = 1;

        List<int[]> rows = genRowVecs(L, N);
        int[] col = new int[N];
        int[] ncol = new int[N];

        for (int r = 0; r < N; r++) {
            Arrays.fill(nxt, 0);
            for (int s = 0; s < totalStates; s++) {
                long ways = cur[s];
                if (ways == 0) continue;
                // decode s -> col
                int tmp = s;
                for (int j = N - 1; j >= 0; j--) {
                    col[j] = tmp % base;
                    tmp /= base;
                }
                // 快速剪枝：已处理 r 行，列和之和应为 r*L
                // 可跳过不符的 state，但 DP 已保证
                for (int[] row : rows) {
                    boolean ok = true;
                    for (int j = 0; j < N; j++) {
                        int ns = col[j] + row[j];
                        if (ns > L) { ok = false; break; }
                        ncol[j] = ns;
                    }
                    if (!ok) continue;
                    int nsEnc = 0;
                    for (int j = 0; j < N; j++) nsEnc = nsEnc * base + ncol[j];
                    nxt[nsEnc] += ways;
                }
            }
            long[] t = cur; cur = nxt; nxt = t;
        }
        int target = 0;
        for (int j = 0; j < N; j++) target = target * base + L;
        System.out.println(cur[target]);
    }
}
