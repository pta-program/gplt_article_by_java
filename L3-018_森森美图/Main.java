import java.io.*;
import java.util.*;

// L3-018 森森美图
// 算法：按直线划分两侧，分别在两侧（除直线点外）做8方向最短路
// 对角移动额外代价 (score_u+score_v)*(sqrt2-1)
// 时间复杂度 O(NM log NM)，NM<=1e4
public class Main {
    static int N, M;
    static int[][] score;
    static int sx, sy, ex, ey;
    static double SQRT2 = Math.sqrt(2.0);

    static class Node implements Comparable<Node> {
        int x, y;
        double d;
        Node(int x, int y, double d) { this.x = x; this.y = y; this.d = d; }
        public int compareTo(Node o) { return Double.compare(d, o.d); }
    }

    static double dijkstra(boolean positiveSide) {
        // blocked: 点在直线上（除起点终点）
        // allowed: cross>0 为正侧，cross<0 为负侧
        double INF = 1e100;
        double[][] dist = new double[N][M];
        for (int i = 0; i < N; i++) Arrays.fill(dist[i], INF);
        boolean[][] blocked = new boolean[N][M];
        // 预计算 blocked 与 side
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                if ((x == sx && y == sy) || (x == ex && y == ey)) {
                    blocked[y][x] = false;
                    continue;
                }
                long cross = (long)(ex - sx) * (y - sy) - (long)(ey - sy) * (x - sx);
                if (cross == 0) blocked[y][x] = true;
            }
        }
        // Dijkstra
        PriorityQueue<Node> pq = new PriorityQueue<>();
        if (blocked[sy][sx] || blocked[ey][ex]) return INF;
        dist[sy][sx] = score[sy][sx];
        pq.offer(new Node(sx, sy, dist[sy][sx]));
        int[] dx = {-1,0,1,-1,1,-1,0,1};
        int[] dy = {-1,-1,-1,0,0,1,1,1};
        while (!pq.isEmpty()) {
            Node cur = pq.poll();
            if (cur.d > dist[cur.y][cur.x] + 1e-12) continue;
            if (cur.x == ex && cur.y == ey) break;
            for (int dir = 0; dir < 8; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if (nx < 0 || nx >= M || ny < 0 || ny >= N) continue;
                if (blocked[ny][nx]) continue;
                // 侧面限制：起点终点例外，否则检查 cross 符号
                if (!((nx == sx && ny == sy) || (nx == ex && ny == ey))) {
                    long cross = (long)(ex - sx) * (ny - sy) - (long)(ey - sy) * (nx - sx);
                    if (positiveSide) {
                        if (cross <= 0) continue;
                    } else {
                        if (cross >= 0) continue;
                    }
                }
                boolean diagonal = (dx[dir] != 0 && dy[dir] != 0);
                double extra = 0;
                if (diagonal) {
                    extra = (score[cur.y][cur.x] + score[ny][nx]) * (SQRT2 - 1);
                }
                double nd = cur.d + score[ny][nx] + extra;
                if (nd + 1e-12 < dist[ny][nx]) {
                    dist[ny][nx] = nd;
                    pq.offer(new Node(nx, ny, nd));
                }
            }
        }
        return dist[ey][ex];
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        score = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            // 可能一行不够
            int j = 0;
            while (j < M) {
                while (!st.hasMoreTokens()) {
                    st = new StringTokenizer(br.readLine());
                }
                score[i][j++] = Integer.parseInt(st.nextToken());
            }
        }
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        st = new StringTokenizer(line);
        sx = Integer.parseInt(st.nextToken());
        sy = Integer.parseInt(st.nextToken());
        ex = Integer.parseInt(st.nextToken());
        ey = Integer.parseInt(st.nextToken());
        // 输入题目坐标是 (X,Y)，X 对应列，Y 对应行，已按此存储

        double dPos = dijkstra(true);
        double dNeg = dijkstra(false);
        double INF = 1e90;
        double ans;
        if (dPos >= INF/2 || dNeg >= INF/2) {
            // 若一侧不可达，按可达那侧？题目保证两侧均可达
            ans = Math.min(dPos, dNeg);
            // 仍需去重逻辑？若只有一侧，答案即为单侧路径
            // 但题要求两条曲线和，去重后若一侧不可达则不合理
            // 按题面应当两侧均存在
        } else {
            ans = dPos + dNeg - score[sy][sx] - score[ey][ex];
        }
        // 保留两位小数，四舍五入
        System.out.printf(Locale.ROOT, "%.2f%n", ans);
    }
}
