import java.io.*;
import java.util.*;

/**
 * L3-037 夺宝大赛
 * 思路：从大本营(值为2)出发对可通行格(1和2)做 BFS 求最短距离，
 *       每个队伍的距离即为其起点到大本营的最短路（不可达视为 INF 不参与）。
 *       将可达队伍按距离分组，距离最小且组大小为1的队伍获胜；
 *       若不存在这样的组则输出 No winner.
 * 时间复杂度: O(m*n + k log k)，m*n ≤ 1e4
 * 空间复杂度: O(m*n)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        // 快速读取所有整数 token
        List<Integer> tokens = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                tokens.add(Integer.parseInt(st.nextToken()));
            }
        }
        if (tokens.isEmpty()) return;
        int idx = 0;
        int m = tokens.get(idx++); // 行数
        int n = tokens.get(idx++); // 列数
        int[][] grid = new int[m][n];
        int tr = -1, tc = -1;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (idx >= tokens.size()) break;
                grid[i][j] = tokens.get(idx++);
                if (grid[i][j] == 2) {
                    tr = i; tc = j;
                }
            }
        }
        if (tr == -1) { // 题目保证有1个，不会发生
            System.out.println("No winner.");
            return;
        }
        // BFS
        int[][] dist = new int[m][n];
        for (int[] row : dist) Arrays.fill(row, -1);
        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{tr, tc});
        dist[tr][tc] = 0;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
                if (grid[nr][nc] == 0) continue; // 障碍
                if (dist[nr][nc] != -1) continue;
                dist[nr][nc] = dist[r][c] + 1;
                q.add(new int[]{nr, nc});
            }
        }

        if (idx >= tokens.size()) {
            System.out.println("No winner.");
            return;
        }
        int k = tokens.get(idx++);
        // 队伍从1编号
        int[] teamDist = new int[k + 1];
        Arrays.fill(teamDist, -1); // -1 表示不可达
        List<int[]> reachable = new ArrayList<>(); // {dist, id}
        for (int i = 1; i <= k; i++) {
            if (idx + 1 >= tokens.size()) break;
            int x = tokens.get(idx++); // 列 1..n
            int y = tokens.get(idx++); // 行 1..m
            int c = x - 1, r = y - 1;
            if (r < 0 || r >= m || c < 0 || c >= n) {
                teamDist[i] = -1;
                continue;
            }
            int d = dist[r][c];
            teamDist[i] = d;
            if (d != -1) {
                reachable.add(new int[]{d, i});
            }
        }

        if (reachable.isEmpty()) {
            System.out.println("No winner.");
            return;
        }
        reachable.sort((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });
        int winnerId = -1, winnerDist = -1;
        int p = 0;
        while (p < reachable.size()) {
            int qptr = p;
            while (qptr < reachable.size() && reachable.get(qptr)[0] == reachable.get(p)[0]) qptr++;
            int cnt = qptr - p;
            if (cnt == 1) {
                winnerId = reachable.get(p)[1];
                winnerDist = reachable.get(p)[0];
                break;
            }
            p = qptr;
        }
        if (winnerId == -1) {
            System.out.println("No winner.");
        } else {
            System.out.println(winnerId + " " + winnerDist);
        }
    }
}
