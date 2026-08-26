import java.io.*;
import java.util.*;

/**
 * L2-048 寻宝图
 * 统计连通分量：格子中字符 != '0' 为陆地（含宝藏），4邻接，外围视为水
 * BFS/DFS 计数总岛屿数与含宝藏岛屿数
 * 时间复杂度 O(N*M)，空间 O(N*M)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        char[][] g = new char[N][M];
        for (int i = 0; i < N; i++) {
            String s = br.readLine();
            // 可能存在空行或长度不足，需处理
            while (s != null && s.length() < M) {
                // 如果行中含空格则去掉空格
                s = s.replace(" ", "");
                if (s.length() < M) {
                    String extra = br.readLine();
                    if (extra == null) break;
                    s += extra.trim();
                } else break;
            }
            if (s == null) s = "";
            s = s.trim();
            // 若长度仍不足，可能是以空格分隔的数字？题面为连续位
            // 确保长度M
            for (int j = 0; j < M; j++) {
                g[i][j] = j < s.length() ? s.charAt(j) : '0';
            }
        }

        boolean[][] vis = new boolean[N][M];
        int total = 0, withTreasure = 0;
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        ArrayDeque<int[]> q = new ArrayDeque<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (g[i][j] != '0' && !vis[i][j]) {
                    total++;
                    boolean has = false;
                    q.clear();
                    q.add(new int[]{i, j});
                    vis[i][j] = true;
                    if (g[i][j] >= '2' && g[i][j] <= '9') has = true;
                    while (!q.isEmpty()) {
                        int[] cur = q.poll();
                        int x = cur[0], y = cur[1];
                        for (int d = 0; d < 4; d++) {
                            int nx = x + dx[d], ny = y + dy[d];
                            if (nx < 0 || nx >= N || ny < 0 || ny >= M) continue;
                            if (vis[nx][ny]) continue;
                            if (g[nx][ny] == '0') continue;
                            vis[nx][ny] = true;
                            if (g[nx][ny] >= '2' && g[nx][ny] <= '9') has = true;
                            q.add(new int[]{nx, ny});
                        }
                    }
                    if (has) withTreasure++;
                }
            }
        }
        System.out.println(total + " " + withTreasure);
    }
}
