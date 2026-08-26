import java.io.*;
import java.util.*;

/**
 * L2-047 锦标赛
 * 思路：自底向上计算每个区间节点所需的最小胜者能力值 need[i][j]，然后自顶向下按 need 分配胜者/败者
 * need 定义：使子树可行所需的最小根胜者值
 * 递推：设节点(i,j)败者 L=l[i][j]，左右子树 need 为 a,b，令 small=min(a,b), large=max(a,b)
 *      若 small > L => need=INF（无解）
 *      否则 need = max(L, large)
 * 可行性由 need 自底向上决定，构造时按 small 对应 L、large 对应 W 的贪心分配保证成功（单调性）
 * 时间复杂度 O(n)，n=2^k <=262144
 * 空间复杂度 O(n)
 */
public class Main {
    static final long INF = Long.MAX_VALUE / 4;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读 k
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int k = Integer.parseInt(line.trim());
        int n = 1 << k; // 选手数
        // l[i][j] i=1..k, j=1..2^{k-i}
        long[][] l = new long[k + 1][];
        for (int i = 1; i <= k; i++) {
            int sz = 1 << (k - i);
            l[i] = new long[sz + 1]; // 1-indexed
            // 需要读取足够的整数，可能跨行
            int filled = 0;
            while (filled < sz) {
                line = br.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens() && filled < sz) {
                    l[i][++filled] = Long.parseLong(st.nextToken());
                }
            }
        }
        // 读 w
        long w = 0;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line != null) w = Long.parseLong(line.trim());

        // need[i][j]
        long[][] need = new long[k + 1][];
        for (int i = 1; i <= k; i++) {
            int sz = 1 << (k - i);
            need[i] = new long[sz + 1];
            Arrays.fill(need[i], INF);
        }

        for (int i = 1; i <= k; i++) {
            int sz = 1 << (k - i);
            for (int j = 1; j <= sz; j++) {
                long L = l[i][j];
                if (i == 1) {
                    need[i][j] = L; // 叶子间比赛，胜者至少要 >=败者
                } else {
                    long a = need[i - 1][2 * j - 1];
                    long b = need[i - 1][2 * j];
                    if (a >= INF || b >= INF) {
                        need[i][j] = INF;
                    } else {
                        long small = Math.min(a, b);
                        long large = Math.max(a, b);
                        if (small > L) {
                            need[i][j] = INF;
                        } else {
                            need[i][j] = Math.max(L, large);
                        }
                    }
                }
            }
        }

        if (k >= 1 && need[k][1] >= INF || w < need[k][1]) {
            System.out.println("No Solution");
            return;
        }
        // 特例 k=0 不存在（k>=1）

        long[] ans = new long[n + 1]; // 1-indexed

        // 递归构造
        construct(k, 1, w, l, need, ans, n);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            if (i > 1) sb.append(' ');
            sb.append(ans[i]);
        }
        System.out.println(sb.toString());
    }

    static void construct(int lev, int idx, long W, long[][] l, long[][] need, long[] ans, int n) {
        if (lev == 0) {
            // 叶子节点 idx 对应选手编号
            ans[idx] = W;
            return;
        }
        if (lev == 1) {
            long L = l[1][idx];
            // 两个叶子 2*idx-1, 2*idx
            int leftLeaf = 2 * idx - 1;
            int rightLeaf = 2 * idx;
            // 任意分配 W/L 到左右，因无更深约束；固定 W 在左
            ans[leftLeaf] = W;
            ans[rightLeaf] = L;
            return;
        }
        long L = l[lev][idx];
        int leftIdx = 2 * idx - 1;
        int rightIdx = 2 * idx;
        long leftNeed = need[lev - 1][leftIdx];
        long rightNeed = need[lev - 1][rightIdx];

        // 按 need 小的分配 L，大的分配 W
        // 判断两种分配是否可行，优先按排序选择
        boolean leftSmaller = leftNeed <= rightNeed;
        // 期望分配：small->L, large->W
        long smallNeed = Math.min(leftNeed, rightNeed);
        long largeNeed = Math.max(leftNeed, rightNeed);
        // 检查排序分配是否满足 L>=smallNeed && W>=largeNeed
        // 如果满足，则按排序分配
        if (smallNeed <= L && largeNeed <= W) {
            if (leftSmaller) {
                // left small -> L, right large -> W
                construct(lev - 1, leftIdx, L, l, need, ans, n);
                construct(lev - 1, rightIdx, W, l, need, ans, n);
            } else {
                construct(lev - 1, leftIdx, W, l, need, ans, n);
                construct(lev - 1, rightIdx, L, l, need, ans, n);
            }
        } else {
            // 理论上不应出现，因为 need[lev][idx] 已保证可行且 W>=need
            // 尝试另一种分配作为兜底
            if (leftNeed <= W && rightNeed <= L) {
                construct(lev - 1, leftIdx, W, l, need, ans, n);
                construct(lev - 1, rightIdx, L, l, need, ans, n);
            } else if (rightNeed <= W && leftNeed <= L) {
                construct(lev - 1, leftIdx, L, l, need, ans, n);
                construct(lev - 1, rightIdx, W, l, need, ans, n);
            }
        }
    }
}
