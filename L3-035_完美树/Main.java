import java.util.*;
import java.io.*;

/**
 * L3-035 完美树
 * 题意：N个结点的有根树(根1)，每个结点有初始颜色 C_i(0白1黑) 和翻转代价 P_i。
 *      要求对每个结点 u，以 u 为根的子树内黑白数量差绝对值 <=1，求最小总代价。
 *      子树大小 sz[u]，若 sz 为偶数则黑数必须 = sz/2；若奇数则黑数 = floor/ceil。
 *
 * 算法：树形 DP，自底向上。
 *   设 sz[u] 已知，记 low[u]=sz[u]/2, high[u]=(sz[u]+1)/2（奇数时 low+1=high，偶数相等）。
 *   DP[u][b] = 子树 u 内黑数为 b 时的最小代价（b 仅取 low/high 之一或两者）。
 *   转移：设子结点 v 的 low/high 已知，记 base = sum lowCost(v)，Smin = sum low(v)，
 *         对奇数子结点 delta = highCost-lowCost，排序后前缀和 pref[t] 为选 t 个奇数子取 high 的最小额外代价。
 *         则子结点总和 S = Smin + t，子树 u 的黑数 B = S + col_u (col_u 0/1)。
 *         枚举 col_u 和 t 使 B 达到 low/high 目标，取最小。
 *   正确性：每个奇数子结点在 low/high 间差 1，选 t 个取 high 的最优即选 delta 最小的 t 个。
 *   复杂度：每个结点对子结点 deltas 排序， sum deg log deg = O(N log N)，空间 O(N)。
 *   N=1e5 时递归深度可能溢出，采用迭代栈求后序。
 */
public class Main {
    static final long INF = (long)4e18;
    public static void main(String[] args) throws Exception {
        BufferedInputStream in = new BufferedInputStream(System.in);
        FastScanner fs = new FastScanner(in);
        Integer nObj = fs.nextInt();
        if (nObj == null) return;
        int N = nObj;
        int[] C = new int[N+1];
        int[] P = new int[N+1];
        @SuppressWarnings("unchecked")
        List<Integer>[] ch = new ArrayList[N+1];
        for (int i=1;i<=N;i++) ch[i]=new ArrayList<>();
        for (int i=1;i<=N;i++) {
            int ci = fs.nextInt();
            int pi = fs.nextInt();
            int ki = fs.nextInt();
            C[i]=ci;
            P[i]=pi;
            for (int k=0;k<ki;k++) {
                int child = fs.nextInt();
                ch[i].add(child);
            }
        }

        // 迭代求后序
        int[] sz = new int[N+1];
        List<Integer> order = new ArrayList<>(N);
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);
            for (int v: ch[u]) stack.push(v);
        }
        // postorder = reverse(order)
        long[] dpLow = new long[N+1];
        long[] dpHigh = new long[N+1];
        for (int idx = order.size()-1; idx>=0; idx--) {
            int u = order.get(idx);
            int s = 1;
            for (int v: ch[u]) s += sz[v];
            sz[u]=s;

            // 收集子结点信息
            long base = 0;
            long sMin = 0;
            List<Long> deltas = new ArrayList<>();
            for (int v: ch[u]) {
                int szv = sz[v];
                int low = szv/2;
                sMin += low;
                base += dpLow[v]; // dpLow==dpHigh for even, else low cost
                if ((szv & 1) == 1) {
                    long d = dpHigh[v] - dpLow[v];
                    deltas.add(d);
                }
            }
            Collections.sort(deltas);
            int odd = deltas.size();
            long[] pref = new long[odd+1];
            pref[0]=0;
            for (int i=0;i<odd;i++) pref[i+1]=pref[i]+deltas.get(i);

            long col0 = (C[u]==0?0:P[u]);
            long col1 = (C[u]==1?0:P[u]);
            int szu = sz[u];
            if ((szu & 1)==0) {
                int target = szu/2;
                long best = INF;
                long t0 = (long)target - sMin - 0;
                if (t0>=0 && t0<=odd) {
                    long cand = base + pref[(int)t0] + col0;
                    if (cand < best) best = cand;
                }
                long t1 = (long)target - sMin - 1;
                if (t1>=0 && t1<=odd) {
                    long cand = base + pref[(int)t1] + col1;
                    if (cand < best) best = cand;
                }
                dpLow[u]=best;
                dpHigh[u]=best;
            } else {
                int lowT = szu/2;
                int highT = lowT+1;
                long bestLow = INF, bestHigh = INF;
                long t0 = (long)lowT - sMin - 0;
                if (t0>=0 && t0<=odd) bestLow = Math.min(bestLow, base + pref[(int)t0] + col0);
                long t1 = (long)lowT - sMin - 1;
                if (t1>=0 && t1<=odd) bestLow = Math.min(bestLow, base + pref[(int)t1] + col1);
                long th0 = (long)highT - sMin - 0;
                if (th0>=0 && th0<=odd) bestHigh = Math.min(bestHigh, base + pref[(int)th0] + col0);
                long th1 = (long)highT - sMin - 1;
                if (th1>=0 && th1<=odd) bestHigh = Math.min(bestHigh, base + pref[(int)th1] + col1);
                dpLow[u]=bestLow;
                dpHigh[u]=bestHigh;
            }
        }
        long ans;
        if ((sz[1] & 1)==0) ans = dpLow[1];
        else ans = Math.min(dpLow[1], dpHigh[1]);
        System.out.println(ans);
    }

    // 快速输入
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1<<16];
        private int ptr=0,len=0;
        FastScanner(InputStream in){this.in=in;}
        private int readByte() throws IOException {
            if (ptr>=len) {
                len=in.read(buffer);
                ptr=0;
                if (len<=0) return -1;
            }
            return buffer[ptr++];
        }
        Integer nextInt() throws IOException {
            int c,sign=1,val=0;
            do {
                c=readByte();
                if (c==-1) return null;
            } while (c<=' ');
            if (c=='-'){sign=-1;c=readByte();}
            while (c>' ') {
                val=val*10+(c-'0');
                c=readByte();
            }
            return val*sign;
        }
    }
}
