import java.io.*;
import java.util.*;

// L3-022 地铁一日游
// 算法：Floyd 求全源最短计费距离 -> 按车费分组求后继 -> BFS 求可达闭包
// 时间复杂度 O(N^3 + N^2 + Q*(N+E_succ))，N<=200
public class Main {
    static final long INF = (long)4e12;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        long K = Long.parseLong(st.nextToken());

        long[][] dist = new long[N+1][N+1];
        for (int i = 1; i <= N; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }
        boolean[] isTerminal = new boolean[N+1];
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            // 收集当前行的 tokens，若该行可能被拆行，继续读取直到获得奇数且至少1个站？
            // 简单：按行解析，若 tokens 数为 0 则重试；若 tokens 为奇数则认为完整，否则继续读下一行拼接
            // 为简化，采用循环累积直到能解析出完整线路（至少一个站且站数与距离交替）
            // 但题目每条线路占一行，正常无需跨行
            List<Long> tokens = new ArrayList<>();
            String curLine = line;
            // 如果行内 tokens 数为偶数，可能线路被截断，继续读
            while (true) {
                StringTokenizer st2 = new StringTokenizer(curLine);
                while (st2.hasMoreTokens()) tokens.add(Long.parseLong(st2.nextToken()));
                // tokens 数量应为奇数（站+距交替，站数=距数+1）
                if (tokens.size() % 2 == 1 && tokens.size() >= 1) break;
                // 偶数说明不完整，读下一行
                String nxt = br.readLine();
                if (nxt == null) break;
                if (nxt.trim().isEmpty()) continue;
                curLine = nxt;
                // 循环继续累积
                // 为避免死循环，若已累积很多仍偶数，继续
            }
            int cnt = tokens.size();
            int stationCnt = (cnt + 1) / 2;
            long[] stations = new long[stationCnt];
            long[] d = new long[Math.max(0, stationCnt - 1)];
            for (int k = 0; k < cnt; k++) {
                if (k % 2 == 0) stations[k/2] = tokens.get(k);
                else d[k/2] = tokens.get(k);
            }
            if (stationCnt > 0) {
                isTerminal[(int)stations[0]] = true;
                isTerminal[(int)stations[stationCnt - 1]] = true;
            }
            for (int k = 0; k + 1 < stationCnt; k++) {
                int u = (int)stations[k];
                int v = (int)stations[k+1];
                long w = d[k];
                if (w < dist[u][v]) {
                    dist[u][v] = w;
                    dist[v][u] = w;
                }
            }
        }
        // Floyd
        for (int k = 1; k <= N; k++) {
            for (int i = 1; i <= N; i++) if (dist[i][k] < INF) {
                for (int j = 1; j <= N; j++) if (dist[k][j] < INF) {
                    long nd = dist[i][k] + dist[k][j];
                    if (nd < dist[i][j]) dist[i][j] = nd;
                }
            }
        }
        // 计算后继图 succ[i] = list
        List<Integer>[] succ = new List[N+1];
        for (int i = 1; i <= N; i++) succ[i] = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            // 按车费分组
            Map<Long, List<Integer>> groups = new HashMap<>();
            Map<Long, Long> maxDistMap = new HashMap<>();
            for (int j = 1; j <= N; j++) if (j != i && dist[i][j] < INF) {
                long fare = 2 + dist[i][j] / K;
                groups.computeIfAbsent(fare, k -> new ArrayList<>()).add(j);
                long curMax = maxDistMap.getOrDefault(fare, -1L);
                if (dist[i][j] > curMax) maxDistMap.put(fare, dist[i][j]);
            }
            Set<Integer> set = new HashSet<>();
            for (Map.Entry<Long, List<Integer>> e : groups.entrySet()) {
                long fare = e.getKey();
                long maxD = maxDistMap.get(fare);
                for (int j : e.getValue()) {
                    if (dist[i][j] == maxD || isTerminal[j]) {
                        set.add(j);
                    }
                }
            }
            succ[i].addAll(set);
        }
        // 读取 Q
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int Q = Integer.parseInt(line.trim());
        StringBuilder out = new StringBuilder();
        for (int qi = 0; qi < Q; qi++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            int start = Integer.parseInt(line.trim());
            boolean[] vis = new boolean[N+1];
            Deque<Integer> dq = new ArrayDeque<>();
            dq.add(start);
            vis[start] = true;
            while (!dq.isEmpty()) {
                int u = dq.poll();
                for (int v : succ[u]) {
                    if (!vis[v]) {
                        vis[v] = true;
                        dq.add(v);
                    }
                }
            }
            boolean first = true;
            for (int v = 1; v <= N; v++) if (vis[v]) {
                if (!first) out.append(' ');
                out.append(v);
                first = false;
            }
            out.append('\n');
        }
        System.out.print(out.toString());
    }
}
