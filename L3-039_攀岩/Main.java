import java.io.*;
import java.util.*;

/**
 * L3-039 攀岩 (30分)
 * 模型：
 *  - 岩点 pi，机械臂长度 r，核心 c 需满足 |c-pi|<=r 且 |c-pj|<=r 才能双臂抓住 i,j。
 *    则状态 (i,j) 可行 <=> dist(i,j) <= 2r。
 *  - 机器人持有 (i,j) 时，核心可在透镜 D(i,j)=B(i,r)∩B(j,r) 内任意移动。
 *    要从 (i,j) 转移到 (i,k)，需存在 c∈D(i,j) 且 |c-pk|<=r，即三圆盘 B(i,r)∩B(j,r)∩B(k,r) 非空。
 *    三圆盘交非空 <=> 以三点为顶点的最小包围圆半径 <= r。
 *    对3点，最小包围圆半径 = max(最长边/2, 外接圆半径[仅当三角形为锐角时])。
 *    因此转移条件：三点两两距离 <=2r，且若为锐角三角形则外接圆半径 <= r。
 *  - 状态图：顶点为所有可行对 (i,j)，边由上述三点条件决定（无向）。
 *    起点为 (0,1)（题目 p1,p2），终点为任意包含 n-1 的对（含n即可立即用第三臂再抓n完成）。
 *  - 判定 r 可行 <=> 在该无向图中起点可达终点（BFS）。
 *  - 答案为使判定成立的最小 r，二分搜索 r。
 *
 * 复杂度：
 *  - 设 n 为岩点数，预计算距离矩阵 O(n^2)。
 *  - 单次可行性 BFS：最坏访问 O(n^2) 个对，每个对枚举 O(n) 个 k 做几何检查 -> O(n^3)，
 *    但受限于 2r 邻域过滤，实际远小于理论最坏；n<=1500 且大 n 仅 3 组，在 4s 限制内通过
 *    二分约 50 次迭代可达到 1e-7 精度。可用 1e-9 eps 控制几何误差。
 *  - 总时间： O(logPrec * (n^2 + BFS))，空间 O(n^2) 距离矩阵 + O(n^2) visited。
 */
public class Main {

    static final double EPS = 1e-9;
    static int n;
    static double[][] dist; // n x n

    // 计算三点最小包围圆半径是否 <= r，已知三边 a=|jk|, b=|ik|, c=|ij|
    // 调用前已保证三边 <=2r（否则直接 false）
    static boolean tripleFeasible(double a, double b, double c, double r) {
        // 若钝角或直角，最小包围圆为最长边的一半，已知 <=r，直接 true
        double a2 = a * a, b2 = b * b, c2 = c * c;
        double max2 = Math.max(a2, Math.max(b2, c2));
        double sum2 = a2 + b2 + c2 - max2;
        // max^2 >= sum others -> 钝角/直角
        if (max2 + 1e-12 >= sum2) {
            return true; // max/2 <= r 已由 pairwise 保证
        }
        // 锐角三角形，需计算外接圆半径 R = a*b*c / (4*Area)
        double s = (a + b + c) * 0.5;
        double area2 = s * (s - a) * (s - b) * (s - c);
        if (area2 <= 0) return true; // 共线退化，按钝角处理
        double area = Math.sqrt(area2);
        double R = a * b * c / (4.0 * area);
        return R <= r + 1e-9;
    }

    // 判定 r 是否可行（BFS 在对状态图上）
    static boolean feasible(double r) {
        double twoR = 2.0 * r;
        // 起点对不可行直接失败
        if (dist[0][1] > twoR + 1e-9) return false;
        // 特殊情况：若 n-1 已在起点中，且起点可行即成功？但题目要求两臂同时抓住 n，
        // 起点为 (p1,p2)，若 n==1 或 2 的情况不出现（n>=3），所以仍需 BFS。
        boolean[][] visited = new boolean[n][n];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        visited[0][1] = visited[1][0] = true;
        q.add(new int[]{0, 1});
        // 若起点已含 n-1（n=2 时）直接成功，但 n>=3 不会
        if (0 == n - 1 || 1 == n - 1) return true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int u = cur[0], v = cur[1];
            // 到达终点
            if (u == n - 1 || v == n - 1) return true;
            // 枚举 k 生成邻接 (u,k) 和 (v,k)
            // 为了减少重复，我们对每个 k 尝试两个候选
            for (int k = 0; k < n; k++) {
                if (k == u || k == v) continue;
                // 候选 (u,k)
                if (!visited[u][k]) {
                    double duk = dist[u][k];
                    if (duk <= twoR + 1e-9) {
                        double dvk = dist[v][k];
                        if (dvk <= twoR + 1e-9) {
                            // 此时两两距离已满足 (u,v)<=2r 已在队列中保证)
                            // 检查三圆交
                            double cuv = dist[u][v];
                            // 快速剪枝：若任一对 >2r 已排除
                            if (tripleFeasible(dvk, duk, cuv, r)) {
                                visited[u][k] = visited[k][u] = true;
                                if (k == n - 1 || u == n - 1) return true;
                                q.add(new int[]{Math.min(u, k), Math.max(u, k)});
                            }
                        }
                    }
                }
                // 候选 (v,k) — 注意避免重复处理 (u,k) 已处理过 symmetrically，
                // 但 (v,k) 与 (u,k) 是不同对，需分别检查
                if (!visited[v][k]) {
                    double dvk = dist[v][k];
                    if (dvk <= twoR + 1e-9) {
                        double duk = dist[u][k];
                        if (duk <= twoR + 1e-9) {
                            double cuv = dist[u][v];
                            if (tripleFeasible(duk, dvk, cuv, r)) {
                                visited[v][k] = visited[k][v] = true;
                                if (k == n - 1 || v == n - 1) return true;
                                q.add(new int[]{Math.min(v, k), Math.max(v, k)});
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取 t，跳空行
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int t = Integer.parseInt(line.trim());
        StringBuilder out = new StringBuilder();
        for (int tc = 0; tc < t; tc++) {
            // 读 n
            do {
                line = br.readLine();
            } while (line != null && line.trim().isEmpty());
            if (line == null) break;
            n = Integer.parseInt(line.trim());
            int[] xs = new int[n];
            int[] ys = new int[n];
            for (int i = 0; i < n; i++) {
                do {
                    line = br.readLine();
                } while (line != null && line.trim().isEmpty());
                StringTokenizer st = new StringTokenizer(line);
                // 防御：坐标可能分行
                while (st.countTokens() < 2) {
                    String extra = br.readLine();
                    if (extra == null) break;
                    line += " " + extra;
                    st = new StringTokenizer(line);
                }
                xs[i] = Integer.parseInt(st.nextToken());
                ys[i] = Integer.parseInt(st.nextToken());
            }
            // 预计算距离矩阵
            dist = new double[n][n];
            double maxD = 0;
            for (int i = 0; i < n; i++) {
                dist[i][i] = 0;
                for (int j = i + 1; j < n; j++) {
                    double dx = xs[i] - xs[j];
                    double dy = ys[i] - ys[j];
                    double d = Math.sqrt(dx * dx + dy * dy);
                    dist[i][j] = dist[j][i] = d;
                    if (d > maxD) maxD = d;
                }
            }

            double lo = 0, hi = maxD; // hi 取 maxDist 已足够（pair 需要 maxD/2，三圆也 <= maxD）
            // 边界：若 hi 仍不可行（理论不应发生，除非孤立），扩大
            // 确保 hi 可行
            // 极端情况 maxD 可能为0? 但 n>=3 且坐标不同不会
            if (hi < 1) hi = 1;
            // 若当前 hi 不可行，翻倍直至可行（防止精度问题）
            int expand = 0;
            while (!feasible(hi) && expand < 60) {
                hi *= 2;
                if (hi > 3e6) break;
                expand++;
            }
            // 二分 60 次保证 1e-7 精度
            for (int iter = 0; iter < 60; iter++) {
                double mid = (lo + hi) * 0.5;
                if (feasible(mid)) hi = mid;
                else lo = mid;
            }
            double ans = hi;
            out.append(String.format(Locale.US, "%.11f", ans));
            if (tc < t - 1) out.append('\n');
        }
        System.out.print(out.toString());
    }
}
