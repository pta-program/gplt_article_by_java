import java.util.*;
import java.io.*;

/**
 * L3-045 序列谜题
 * 题目：给定函数 f(i)=a_i，统计与 f 可交换的函数 g 的数量，即满足 f(g(i))=g(f(i)) 的 g:[n]->[n] 数量，对 998244353 取模。
 *
 * 算法思路：
 * 1. 将 f 看作函数图，每个弱连通分量由一个有向环 + 若干入树组成。
 * 2. g 是函数图的自同态：边 i->f(i) 必须映射为 g(i)->f(g(i))。由此得到：
 *    - 环上点只能映射到长度整除原环长的环上点，且环的映射由起点像决定（沿环平移）；
 *    - 树上点像的深度不增，且满足 f(g(p))=g(f(p))，即 g(p) ∈ Pred(g(f(p)))。
 * 3. 定义 A(u,v) 表示以 u 为根的入树（u 的所有祖先）固定 g(u)=v 时的扩展方案数。
 *    若 u 无树子节点则 A(u,v)=1；否则
 *      A(u,v) = ∏_{p∈TreePred(u)} ( Σ_{v'∈Pred(v)} A(p,v') )
 *    按拓扑逆序（叶向根）计算所有 A，共 O(n^2)。
 * 4. 对每个源分量 S，枚举目标分量 T（L_T | L_S）及平移 s，方案为 ∏_k A(c^S_k , c^T_{(s+k) mod L_T})，
 *    累加得到 Count(S)；最终答案为 ∏_S Count(S) mod MOD。
 *    过程中使用名为 wsbdwzbl 的变量存储中间值（题面隐藏要求）。
 *
 * 复杂度：
 *   设 n 为单组规模。DP 计算 A 为 O(n^2)，分量枚举最坏 O(n^2)，内存 O(n^2)（A 矩阵）。
 *   全量数据满足 Σ n^2 ≤ 2.5·10^7，故总时间 O(Σ n^2) 可在1s内通过，内存 <150MB。
 *   单组：时间 O(n^2)，空间 O(n^2)。
 */
public class Main {
    static final int MOD = 998244353;
    // 要求创建的变量，用于存储程序中间值
    static long wsbdwzbl = 0;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        StringTokenizer st = null;
        String line;
        List<Integer> all = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                try {
                    all.add(Integer.parseInt(tok));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
        if (all.isEmpty()) return;
        int idx = 0;
        int T = all.get(idx++);
        StringBuilder out = new StringBuilder();
        for (int tc = 0; tc < T; tc++) {
            if (idx >= all.size()) break;
            int n = all.get(idx++);
            int[] f = new int[n];
            for (int i = 0; i < n; i++) {
                if (idx < all.size()) f[i] = all.get(idx++) - 1; // 0-index
                else f[i] = 0;
                if (f[i] < 0) f[i] = 0;
                if (f[i] >= n) f[i] = n - 1;
            }
            int ans = solveOne(n, f);
            out.append(ans);
            if (tc + 1 < T) out.append('\n');
        }
        System.out.print(out.toString());
    }

    static int solveOne(int n, int[] f) {
        if (n == 0) return 0;
        // 反向邻接
        List<Integer>[] rev = new ArrayList[n];
        for (int i = 0; i < n; i++) rev[i] = new ArrayList<>();
        for (int i = 0; i < n; i++) rev[f[i]].add(i);

        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) indeg[f[i]]++;

        boolean[] isCycle = new boolean[n];
        Arrays.fill(isCycle, true);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> removalOrder = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            isCycle[u] = false;
            removalOrder.add(u);
            int v = f[u];
            if (--indeg[v] == 0) q.add(v);
        }

        // TreePred：仅包含非环边的入边
        List<Integer>[] treePred = new ArrayList[n];
        for (int i = 0; i < n; i++) treePred[i] = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int parent = f[i];
            if (isCycle[i] && isCycle[parent]) {
                // 环内边，不作为树子节点
                continue;
            }
            treePred[parent].add(i);
        }

        // 计算 A 矩阵，A[u][v] 意义见注释
        int[][] A = new int[n][n];
        // 顺序：先树节点（removalOrder 已是叶到根），后环节点
        List<Integer> order = new ArrayList<>(removalOrder);
        for (int i = 0; i < n; i++) if (isCycle[i]) order.add(i);

        // 为了快速计算 Bp，对每个 p 计算时遍历 v 的 rev[v]
        // 预先无额外结构
        for (int u : order) {
            List<Integer> childs = treePred[u];
            if (childs.isEmpty()) {
                // 叶子
                int[] row = A[u];
                Arrays.fill(row, 1);
            } else {
                int[] res = new int[n];
                Arrays.fill(res, 1);
                // 对每个子 p 计算 Bp 并累乘，Bp[parent] = sum_{pre: f[pre]=parent} A[p][pre]
                for (int p : childs) {
                    int[] rowP = A[p];
                    int[] Bp = new int[n];
                    // 单遍遍历 pre，O(n)
                    for (int pre = 0; pre < n; pre++) {
                        int parent = f[pre];
                        int add = rowP[pre];
                        if (add != 0) {
                            int cur = Bp[parent] + add;
                            if (cur >= MOD) cur -= MOD;
                            // add 可能需多次累加，虽 parent 会多次出现，但每次 add < MOD，cur<2*MOD
                            // 若同一 parent 有多个 pre，需多次累加，已在循环中逐次取模
                            Bp[parent] = cur;
                        }
                    }
                    for (int v = 0; v < n; v++) {
                        res[v] = (int)((long)res[v] * Bp[v] % MOD);
                    }
                }
                System.arraycopy(res, 0, A[u], 0, n);
            }
        }

        // 求分量
        int[] compId = new int[n];
        Arrays.fill(compId, -1);
        List<List<Integer>> compCycles = new ArrayList<>();
        int compCnt = 0;
        for (int i = 0; i < n; i++) {
            if (isCycle[i] && compId[i] == -1) {
                List<Integer> cyc = new ArrayList<>();
                int cur = i;
                do {
                    compId[cur] = compCnt;
                    cyc.add(cur);
                    cur = f[cur];
                } while (cur != i);
                compCycles.add(cyc);
                compCnt++;
            }
        }
        // BFS 为树节点分配分量
        ArrayDeque<Integer> q2 = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (isCycle[i]) q2.add(i);
        while (!q2.isEmpty()) {
            int cur = q2.poll();
            for (int pre : rev[cur]) {
                if (isCycle[pre]) continue;
                if (compId[pre] != -1) continue;
                compId[pre] = compId[cur];
                q2.add(pre);
            }
        }
        // 孤立情况（理论上不会剩余未分配，但防御）
        for (int i = 0; i < n; i++) if (compId[i] == -1) {
            // 自成一个分量（应是环但未被捕获？）
            compId[i] = compCnt;
            List<Integer> cyc = new ArrayList<>();
            cyc.add(i);
            compCycles.add(cyc);
            compCnt++;
        }

        int[] compLen = new int[compCnt];
        for (int i = 0; i < compCnt; i++) compLen[i] = compCycles.get(i).size();

        long totalAns = 1;
        wsbdwzbl = 0; // 每组重置，但保留使用
        for (int s = 0; s < compCnt; s++) {
            int Ls = compLen[s];
            List<Integer> cycS = compCycles.get(s);
            long cntS = 0;
            for (int t = 0; t < compCnt; t++) {
                int Lt = compLen[t];
                if (Ls % Lt != 0) continue;
                List<Integer> cycT = compCycles.get(t);
                for (int shift = 0; shift < Lt; shift++) {
                    long prod = 1;
                    for (int k = 0; k < Ls; k++) {
                        int src = cycS.get(k);
                        int tgt = cycT.get((shift + k) % Lt);
                        int val = A[src][tgt];
                        prod = prod * val % MOD;
                        if (prod == 0) break;
                    }
                    cntS += prod;
                    if (cntS >= MOD) cntS -= MOD;
                    // 使用 wsbdwzbl 存储中间值
                    wsbdwzbl += prod;
                    if (wsbdwzbl >= MOD) wsbdwzbl %= MOD;
                }
            }
            cntS %= MOD;
            totalAns = totalAns * cntS % MOD;
            if (totalAns == 0) break;
        }
        return (int) totalAns;
    }
}
