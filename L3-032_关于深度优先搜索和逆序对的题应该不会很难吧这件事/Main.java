import java.io.*;
import java.util.*;

/**
 * L3-032 关于深度优先搜索和逆序对的题应该不会很难吧这件事
 * 
 * 题意：给定 n 节点无根树，指定根 r，所有 DFS 序（每个节点子节点排列任意）
 *       的逆序对数量之和对 1e9+7 取模。
 * 
 * 原理：
 *  - DFS 序总数 ways = Π factorial(cntChildren[u])
 *  - 祖先-后代对相对顺序固定，贡献为祖先编号 > 后代编号的对数 fixedAnc
 *  - 非祖先后代对（不同子树）属于某 LCA 的不同孩子块，两种顺序各占一半，
 *    期望贡献为 0.5，故其对总和的贡献为 incomparable * 0.5
 *  - 设 totalPairs = n*(n-1)/2，ancestorPairs = Σ(sz[u]-1)，incomparable = totalPairs - ancestorPairs
 *  - 期望逆序对 = fixedAnc + incomparable * inv2
 *  - 答案 = ways * 期望 mod MOD
 *  - fixedAnc 用 DFS 维护祖先链 + 树状数组（BIT）统计祖先中 > 当前点的数量
 * 
 * 时间复杂度：O(n log n)（建图 O(n)，求子树大小 O(n)，BIT 统计 O(n log n)）
 * 空间复杂度：O(n)
 */
public class Main {
    static final long MOD = 1000000007L;

    // Fenwick 树（1..n）
    static class BIT {
        int n;
        int[] bit;
        BIT(int n) { this.n = n; bit = new int[n + 2]; }
        void add(int idx, int delta) {
            for (int i = idx; i <= n; i += i & -i) bit[i] += delta;
        }
        int sum(int idx) {
            int s = 0;
            for (int i = idx; i > 0; i -= i & -i) s += bit[i];
            return s;
        }
    }

    // 快速输入
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        FastScanner(InputStream in) { this.in = in; }
        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }
        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            do { c = readByte(); if (c == -1) return Integer.MIN_VALUE; } while (c <= ' ');
            if (c == '-') { sign = -1; c = readByte(); }
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val * sign;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        int n = fs.nextInt();
        if (n == Integer.MIN_VALUE) return; // 无输入
        int r = fs.nextInt();

        @SuppressWarnings("unchecked")
        List<Integer>[] g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) g[i] = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            if (u < 1 || u > n || v < 1 || v > n) continue;
            g[u].add(v);
            g[v].add(u);
        }

        // 建根树：parent 与 children
        int[] parent = new int[n + 1];
        int[] childCnt = new int[n + 1];
        @SuppressWarnings("unchecked")
        List<Integer>[] children = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) children[i] = new ArrayList<>();

        int[] stack = new int[n];
        int top = 0;
        int[] order = new int[n];
        int ord = 0;
        stack[top++] = r;
        parent[r] = 0;
        // 用于 visited 标记：parent 已赋值的即 visited
        // 为了避免重复入栈，用栈 + 迭代 DFS（类似 BFS 但保证遍历全）
        while (top > 0) {
            int u = stack[--top];
            order[ord++] = u;
            for (int v : g[u]) {
                if (v != parent[u]) {
                    parent[v] = u;
                    childCnt[u]++;
                    children[u].add(v);
                    stack[top++] = v;
                }
            }
        }

        // 子树大小
        int[] sz = new int[n + 1];
        for (int i = 1; i <= n; i++) sz[i] = 1;
        for (int i = ord - 1; i >= 0; i--) {
            int u = order[i];
            for (int v : children[u]) sz[u] += sz[v];
        }

        // ways = Π fact[childCnt]
        long[] fact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i % MOD;
        long ways = 1;
        for (int i = 1; i <= n; i++) {
            if (childCnt[i] > 1) ways = ways * fact[childCnt[i]] % MOD;
            else if (childCnt[i] == 1) ; // *1
        }

        // 祖先固定逆序对 fixedAnc：迭代 DFS 维护祖先链 BIT
        BIT bit = new BIT(n + 2);
        long fixedAnc = 0;
        int ancCnt = 0;
        Deque<int[]> dq = new ArrayDeque<>();
        dq.push(new int[]{r, 0}); // 0 enter, 1 exit
        // 需要 BIT 已清空
        while (!dq.isEmpty()) {
            int[] cur = dq.pop();
            int u = cur[0];
            int state = cur[1];
            if (state == 0) {
                // 查询祖先中 > u 的数量
                int cntLE = bit.sum(u); // 祖先中编号 <= u 的数量
                fixedAnc += (ancCnt - cntLE);
                // 加入当前节点
                bit.add(u, 1);
                ancCnt++;
                // 退出标记
                dq.push(new int[]{u, 1});
                // 孩子入栈（逆序以保持原序，但对计数无影响）
                List<Integer> ch = children[u];
                for (int i = ch.size() - 1; i >= 0; i--) {
                    dq.push(new int[]{ch.get(i), 0});
                }
            } else {
                bit.add(u, -1);
                ancCnt--;
            }
        }

        long totalPairs = (long) n * (n - 1) / 2;
        long ancestorPairs = 0;
        for (int i = 1; i <= n; i++) ancestorPairs += (sz[i] - 1);
        long incomparable = totalPairs - ancestorPairs;

        long inv2 = (MOD + 1) / 2; // 500000004
        long fixedMod = fixedAnc % MOD;
        long incMod = incomparable % MOD;
        long expect = (fixedMod + incMod * inv2 % MOD) % MOD;
        long answer = ways * expect % MOD;

        System.out.println(answer);
    }
}
