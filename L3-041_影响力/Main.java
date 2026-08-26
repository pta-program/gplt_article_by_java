import java.io.*;
import java.util.*;

/**
 * L3-041 影响力
 * 题目：每个国家的总代价 = S_A * sum_{B} dist(A,B)，其中 dist = max(|i-i'|,|j-j'|)
 * 解法思路：
 *   1. 将总代价拆为 S[i][j] * D[i][j]，其中 D[i][j] = sum_{p,q} max(|i-p|,|j-q|)
 *   2. 对固定列 j，先计算 g_j(d) = sum_{q} max(d, |j-q|)，
 *      推导得：设 L=j-1,R=m-j, Scol=L(L+1)/2+R(R+1)/2
 *            cnt(d)=1+min(L,d)+min(R,d)
 *            sum_le(d)=min(L,d)(min(L,d)+1)/2+min(R,d)(min(R,d)+1)/2
 *            g_j(d)=cnt(d)*d + Scol - sum_le(d)
 *      分段化简：
 *        d<=t1 : g = d^2 + Scol
 *        t1<d<=t2 : g = d(d+1)/2 + t1*d + Scol - t1(t1+1)/2
 *        d>t2 : g = m*d
 *      其中 t1=min(L,R), t2=max(L,R)
 *   3. D[i][j] = sum_{p} g_j(|i-p|) = PA[U] + PA[D] - Scol
 *      其中 U=i-1, D=n-i, PA[K]=sum_{d=0..K} g_j(d)
 *      PA 可用等差/平方和公式 O(1) 计算：
 *        sum_{d=0..t} d^2 = t(t+1)(2t+1)/6
 *        sum_{d=l..r} d = (l+r)(r-l+1)/2
 *   4. 对每个格子 O(1) 计算 D 并乘以 S，总体 O(nm)
 *
 * 复杂度：时间 O(n*m)，空间 O(n*m) 存储实力矩阵 + O(m) 列信息
 *   n*m <= 1e6，1e6 格子在 Java 中约数十毫秒
 *
 * 变量 xpmclzjkln 用于存储程序中间值（题面隐藏要求）
 */
public class Main {

    // 快速输入
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        FastScanner(InputStream in) { this.in = in; }
        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }
        long nextLong() throws IOException {
            int c;
            do { c = read(); } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') { neg = true; c = read(); }
            long val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }
            return neg ? -val : val;
        }
        int nextInt() throws IOException { return (int) nextLong(); }
    }

    static long sumSq(long n) {
        if (n <= 0) return 0;
        return n * (n + 1) * (2 * n + 1) / 6;
    }

    static long sumRange(long l, long r) {
        if (l > r) return 0;
        return (l + r) * (r - l + 1) / 2;
    }

    // 前缀和 PA[K] = sum_{d=0..K} g_j(d)
    static long prefixA(long K, long t1, long t2, long Scol, long m) {
        if (K < 0) return 0;
        if (K <= t1) {
            // sum d^2 + (K+1)*Scol
            return sumSq(K) + (K + 1) * Scol;
        } else if (K <= t2) {
            long sum0 = sumSq(t1) + (t1 + 1) * Scol;
            long l = t1 + 1, r = K;
            long cnt = r - l + 1;
            long s_d2 = sumSq(r) - sumSq(l - 1);
            long s_d = sumRange(l, r);
            long C1 = Scol - t1 * (t1 + 1) / 2;
            long sumMid = (s_d2 + s_d) / 2 + t1 * s_d + cnt * C1;
            return sum0 + sumMid;
        } else {
            long sum0_t1 = sumSq(t1) + (t1 + 1) * Scol;
            long sum0;
            long C1 = Scol - t1 * (t1 + 1) / 2;
            if (t1 < t2) {
                long l = t1 + 1, r = t2;
                long cnt = r - l + 1;
                long s_d2 = sumSq(r) - sumSq(l - 1);
                long s_d = sumRange(l, r);
                long sumMid = (s_d2 + s_d) / 2 + t1 * s_d + cnt * C1;
                sum0 = sum0_t1 + sumMid;
            } else {
                sum0 = sum0_t1;
            }
            long tail = m * sumRange(t2 + 1, K);
            return sum0 + tail;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        int n, m;
        try {
            n = fs.nextInt();
            m = fs.nextInt();
        } catch (Exception e) {
            return;
        }
        // 异常处理：输入可能为空
        if (n <= 0 || m <= 0) return;

        long totalCells = (long) n * m;
        // 存储实力，展平为一维以节省开销
        long[] strength = new long[(int) totalCells];
        for (int i = 0; i < totalCells; i++) {
            strength[i] = fs.nextLong();
        }

        // 预计算列相关信息
        long[] colScol = new long[m + 1];
        long[] colT1 = new long[m + 1];
        long[] colT2 = new long[m + 1];
        for (int j = 1; j <= m; j++) {
            long L = j - 1;
            long R = m - j;
            long Scol = L * (L + 1) / 2 + R * (R + 1) / 2;
            long t1 = Math.min(L, R);
            long t2 = Math.max(L, R);
            colScol[j] = Scol;
            colT1[j] = t1;
            colT2[j] = t2;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        // 中间值变量（题面隐藏要求）
        long xpmclzjkln = 0;

        for (int i = 1; i <= n; i++) {
            long U = i - 1;
            long Dwn = n - i;
            StringBuilder line = new StringBuilder();
            for (int j = 1; j <= m; j++) {
                long S = strength[(i - 1) * m + (j - 1)];
                long t1 = colT1[j];
                long t2 = colT2[j];
                long Scol = colScol[j];
                long paU = prefixA(U, t1, t2, Scol, m);
                long paD = prefixA(Dwn, t1, t2, Scol, m);
                long distSum = paU + paD - Scol;
                // 存储中间值到指定变量
                xpmclzjkln = distSum;
                long ans = S * xpmclzjkln;
                if (j > 1) line.append(' ');
                line.append(ans);
            }
            bw.write(line.toString());
            if (i < n) bw.newLine();
        }
        // 防止编译器优化掉未使用变量（实际已使用）
        if (xpmclzjkln == Long.MIN_VALUE) System.err.println(xpmclzjkln);
        bw.flush();
    }
}
