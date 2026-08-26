import java.io.*;
import java.util.*;

// L2-059 森林藏宝图
// 树形结构，求根到叶路径上最小边权的最大值，输出最大值及取得该值的叶编号升序
// 时间 O(n)，空间 O(n)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int n = Integer.parseInt(line.trim());
        // n 个顶点：0 入口，1..n-1 其它
        List<Integer>[] children = new List[n];
        for (int i = 0; i < n; i++) children[i] = new ArrayList<>();
        int[] edgeW = new int[n]; // edgeW[i] = 从父到 i 的权重，0根无意义
        boolean[] hasChild = new boolean[n];
        for (int i = 1; i < n; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            int j = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            children[j].add(i);
            edgeW[i] = s;
            hasChild[j] = true;
        }
        // BFS/DFS 求瓶颈
        int[] bottleneck = new int[n];
        Arrays.fill(bottleneck, Integer.MAX_VALUE);
        bottleneck[0] = Integer.MAX_VALUE; // INF
        Deque<Integer> dq = new ArrayDeque<>();
        dq.add(0);
        while (!dq.isEmpty()) {
            int u = dq.poll();
            for (int v : children[u]) {
                int b = Math.min(bottleneck[u], edgeW[v]);
                bottleneck[v] = b;
                dq.add(v);
            }
        }
        // 叶节点：1..n-1 中无孩子者；若 n==1 则根为叶但题目 n>1
        List<Integer> leaves = new ArrayList<>();
        for (int i = 1; i < n; i++) if (!hasChild[i]) leaves.add(i);
        // 特殊情况：若没有叶（n==1）处理
        if (leaves.isEmpty()) {
            // 只有一个根，无藏宝地？按题意不会
            System.out.println(0);
            System.out.println();
            return;
        }
        int best = -1;
        for (int v : leaves) best = Math.max(best, bottleneck[v]);
        List<Integer> ans = new ArrayList<>();
        for (int v : leaves) if (bottleneck[v] == best) ans.add(v);
        Collections.sort(ans); // 递增
        StringBuilder sb = new StringBuilder();
        sb.append(best).append('\n');
        for (int i = 0; i < ans.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(ans.get(i));
        }
        System.out.println(sb.toString());
    }
}
