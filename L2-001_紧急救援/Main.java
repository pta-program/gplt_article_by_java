import java.util.*;
import java.io.*;

// L2-001 紧急救援: Dijkstra 统计最短路条数、最大救援队数量并还原路径
// 时间复杂度 O(N^2) N<=500, 亦可用堆优化 O(M log N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取第一行 N M S D
        StringTokenizer st = null;
        // 跳过空行
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        st = new StringTokenizer(line);
        // 可能分多行读取, 确保4个整数
        List<Integer> first = new ArrayList<>();
        while (st.hasMoreTokens()) first.add(Integer.parseInt(st.nextToken()));
        while (first.size() < 4) {
            line = br.readLine();
            if (line == null) break;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens()) first.add(Integer.parseInt(st.nextToken()));
        }
        int N = first.get(0), M = first.get(1), S = first.get(2), D = first.get(3);
        int[] team = new int[N];
        // 读取救援队数量 N 个, 可能跨行
        int idx = 0;
        while (idx < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens() && idx < N) {
                team[idx++] = Integer.parseInt(st.nextToken());
            }
        }
        // 邻接矩阵/表
        List<int[]>[] adj = new ArrayList[N];
        for (int i = 0; i < N; i++) adj[i] = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            st = new StringTokenizer(line);
            // 可能一行不完整, 但题目保证完整
            while (st.countTokens() < 3) {
                String extra = br.readLine();
                if (extra != null) line += " " + extra;
                st = new StringTokenizer(line);
            }
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            adj[u].add(new int[]{v, w});
            adj[v].add(new int[]{u, w});
        }

        final int INF = Integer.MAX_VALUE / 4;
        int[] dist = new int[N];
        Arrays.fill(dist, INF);
        int[] cnt = new int[N];
        int[] maxTeam = new int[N];
        int[] pre = new int[N];
        boolean[] vis = new boolean[N];
        Arrays.fill(pre, -1);
        dist[S] = 0;
        cnt[S] = 1;
        maxTeam[S] = team[S];

        for (int iter = 0; iter < N; iter++) {
            int u = -1;
            int best = INF;
            for (int i = 0; i < N; i++) if (!vis[i] && dist[i] < best) { best = dist[i]; u = i; }
            if (u == -1) break;
            vis[u] = true;
            for (int[] e : adj[u]) {
                int v = e[0], w = e[1];
                if (vis[v]) continue;
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    cnt[v] = cnt[u];
                    maxTeam[v] = maxTeam[u] + team[v];
                    pre[v] = u;
                } else if (dist[u] + w == dist[v]) {
                    cnt[v] += cnt[u];
                    if (maxTeam[u] + team[v] > maxTeam[v]) {
                        maxTeam[v] = maxTeam[u] + team[v];
                        pre[v] = u;
                    }
                }
            }
        }

        System.out.println(cnt[D] + " " + maxTeam[D]);
        // 还原路径
        List<Integer> path = new ArrayList<>();
        int cur = D;
        while (cur != -1) {
            path.add(cur);
            cur = pre[cur];
        }
        Collections.reverse(path);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(path.get(i));
        }
        System.out.println(sb.toString());
    }
}
