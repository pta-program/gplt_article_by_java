import java.io.*;
import java.util.*;

// L3-011 直捣黄龙
// 多准则最短路：1)距离最短 2)经过城镇数最多 3)歼敌最多
// 同时统计最短距离路径条数（所有距离最短的路径数）
// 算法：Dijkstra求最短距离 + 计数 + DAG上DP求最优路径
// 时间复杂度 O(N^2) 或 O((N+M) log N)
public class Main {
    static class Edge {
        int to, w;
        Edge(int to, int w) { this.to = to; this.w = w; }
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        List<String> tokens = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) tokens.add(st.nextToken());
        }
        if (tokens.isEmpty()) return;
        int idx = 0;
        int N = Integer.parseInt(tokens.get(idx++));
        int K = Integer.parseInt(tokens.get(idx++));
        String startName = tokens.get(idx++);
        String endName = tokens.get(idx++);
        Map<String,Integer> id = new HashMap<>();
        List<String> names = new ArrayList<>();
        // 分配id
        id.put(startName, 0);
        names.add(startName);
        int[] enemy = new int[N];
        // 临时存储城镇信息，N-1行
        for (int i = 1; i < N; i++) {
            String name = tokens.get(idx++);
            int e = Integer.parseInt(tokens.get(idx++));
            id.put(name, i);
            names.add(name);
            enemy[i] = e;
        }
        enemy[0] = 0;
        // 若道路中出现未知名城镇，动态添加（防御）
        List<Edge>[] adj = new ArrayList[N];
        for (int i = 0; i < N; i++) adj[i] = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            if (idx + 2 >= tokens.size()) break;
            String a = tokens.get(idx++);
            String b = tokens.get(idx++);
            int w = Integer.parseInt(tokens.get(idx++));
            Integer ia = id.get(a);
            Integer ib = id.get(b);
            if (ia == null || ib == null) {
                // 动态扩容（理论上不应出现）
                if (ia == null) { ia = names.size(); id.put(a, ia); names.add(a); enemy = Arrays.copyOf(enemy, names.size()); adj = Arrays.copyOf(adj, names.size()); adj[ia] = new ArrayList<>(); }
                if (ib == null) { ib = names.size(); id.put(b, ib); names.add(b); enemy = Arrays.copyOf(enemy, names.size()); adj = Arrays.copyOf(adj, names.size()); adj[ib] = new ArrayList<>(); }
                N = names.size();
            }
            adj[ia].add(new Edge(ib, w));
            adj[ib].add(new Edge(ia, w));
        }
        int s = id.get(startName);
        int t = id.get(endName);
        int n = names.size();
        final int INF = Integer.MAX_VALUE/4;
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        long[] cnt = new long[n]; // 最短路径条数
        dist[s] = 0;
        cnt[s] = 1;
        boolean[] vis = new boolean[n];
        // Dijkstra for distances and counting all shortest paths
        for (int iter = 0; iter < n; iter++) {
            int u = -1, best = INF;
            for (int i = 0; i < n; i++) if (!vis[i] && dist[i] < best) { best = dist[i]; u = i; }
            if (u == -1) break;
            vis[u] = true;
            for (Edge e : adj[u]) {
                int v = e.to;
                int nd = dist[u] + e.w;
                if (nd < dist[v]) {
                    dist[v] = nd;
                    cnt[v] = cnt[u];
                } else if (nd == dist[v]) {
                    cnt[v] += cnt[u];
                }
            }
        }
        // 在最短路DAG上求解放城镇最多、歼敌最多的最优路径
        int[] bestTown = new int[n];
        int[] bestKill = new int[n];
        int[] pre = new int[n];
        Arrays.fill(bestTown, -1);
        Arrays.fill(pre, -1);
        bestTown[s] = 0;
        bestKill[s] = 0;
        // 按dist升序处理节点（拓扑）
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(a -> dist[a]));
        for (int u : order) {
            if (dist[u] == INF) continue;
            if (bestTown[u] < 0) continue;
            for (Edge e : adj[u]) {
                int v = e.to;
                if (dist[u] + e.w == dist[v]) {
                    int candTown = bestTown[u] + 1;
                    int candKill = bestKill[u] + enemy[v];
                    if (candTown > bestTown[v] || (candTown == bestTown[v] && candKill > bestKill[v])) {
                        bestTown[v] = candTown;
                        bestKill[v] = candKill;
                        pre[v] = u;
                    }
                }
            }
        }
        // 重建路径
        List<Integer> path = new ArrayList<>();
        int cur = t;
        while (cur != -1) {
            path.add(cur);
            if (cur == s) break;
            cur = pre[cur];
        }
        Collections.reverse(path);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append("->");
            sb.append(names.get(path.get(i)));
        }
        System.out.println(sb.toString());
        System.out.println(cnt[t] + " " + dist[t] + " " + bestKill[t]);
    }
}
