import java.io.*;
import java.util.*;

// L2-060 大语言模型的推理
// 有向图，按最高概率贪心且已访问节点不再访问，K 条路径独立模拟
// 时间 O(K*(n + m)) 最坏 100*1e4，实际每条路径 O(n+deg)
// 空间 O(n+m)
public class Main {
    static class Edge {
        int to; int p;
        Edge(int to,int p){this.to=to;this.p=p;}
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        // 读取所有整数，兼容换行不规则
        List<Integer> all = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                all.add(Integer.parseInt(st.nextToken()));
            }
        }
        if (all.size() < 2) return;
        int p = 0;
        int n = all.get(p++);
        int m = all.get(p++);
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int i = 0; i < m; i++) {
            if (p + 2 >= all.size()) break;
            int id1 = all.get(p++), id2 = all.get(p++), prob = all.get(p++);
            if (id1 >= 1 && id1 <= n && id2 >= 1 && id2 <= n) {
                adj.get(id1).add(new Edge(id2, prob));
            }
        }
        if (p >= all.size()) return;
        int K = all.get(p++);
        List<Integer> queries = new ArrayList<>();
        while (p < all.size() && queries.size() < K) {
            queries.add(all.get(p++));
        }
        // 若 K 行数据不足，截断
        StringBuilder out = new StringBuilder();
        for (int qi = 0; qi < queries.size(); qi++) {
            int start = queries.get(qi);
            if (start < 1 || start > n) {
                out.append(start);
                if (qi + 1 < queries.size()) out.append('\n');
                continue;
            }
            boolean[] vis = new boolean[n + 1];
            List<Integer> path = new ArrayList<>();
            int cur = start;
            vis[cur] = true;
            path.add(cur);
            while (true) {
                int bestP = -1;
                int bestTo = -1;
                for (Edge e : adj.get(cur)) {
                    if (vis[e.to]) continue;
                    if (e.p > bestP || (e.p == bestP && e.to < bestTo)) {
                        bestP = e.p;
                        bestTo = e.to;
                    }
                }
                if (bestTo == -1) break;
                cur = bestTo;
                vis[cur] = true;
                path.add(cur);
            }
            for (int i = 0; i < path.size(); i++) {
                if (i > 0) out.append("->");
                out.append(path.get(i));
            }
            if (qi + 1 < queries.size()) out.append('\n');
        }
        System.out.println(out.toString());
    }
}
