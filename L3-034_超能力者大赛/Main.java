import java.util.*;
import java.io.*;

/**
 * L3-034 超能力者大赛
 * 模拟题：贪心选择 + 最短路 + 联盟合并
 * 规则要点：
 * - 每座城市维护对手列表（包括联盟），联盟视为单个对手
 * - 每次击败对手后，所在城市剩余所有 E<=自己 的个体合并为一个联盟（能力值=总和）
 * - 若城市存在 E>自己的对手则必须离开，否则可连日击败该城残余
 * - 贪心步骤：全局中选 E<=自己 且最接近自己的对手（E最大），并列按离当前城市最短时间、途经城市最少、城市编号最小；
 *            若当前城市无威胁则优先清空当前城市
 * - 移动/战斗日程：到达城市后第二天战斗，战斗结束第二天出发，旅行时间含出发当天，战斗 = 出发 + 旅行时间
 * - 终止：击败全部 -> WIN；无路可逃（无可击败）-> Lose；超出 D 天 -> Game over
 *
 * 复杂度：
 * - Floyd 最短路：O(M^3)，M<=200 => 约 8e6
 * - 模拟：天数 D<=1000，每次全局扫描 N<=1e5，O(D*N) 最坏 1e8，实际可接受
 * - 空间：O(M^2 + N)
 */
public class Main {
    static final int INF = 1_000_000_000;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取所有整数（兼容多行、额外空格）
        List<Long> nums = new ArrayList<>();
        // 先按行读，处理空行
        // 为便于解析 N M Me D 等，我们直接用 token 扫描
        String allText = "";
        StringBuilder sbAll = new StringBuilder();
        // 由于 BufferedReader 已创建，逐行读取并拼接
        // 但需要先尝试读取第一行判断是否为空输入
        // 采用 Scanner 风格读取所有 token
        List<String> tokens = new ArrayList<>();
        String l;
        // 重新打开 System.in 的缓冲读取：已有一个 br，循环读取剩余
        // 注意：br 已在上面创建，直接使用它
        while ((l = br.readLine()) != null) {
            if (l.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(l);
            while (st.hasMoreTokens()) tokens.add(st.nextToken());
        }
        if (tokens.size() < 4) return;
        int idx = 0;
        int N = Integer.parseInt(tokens.get(idx++));
        int M = Integer.parseInt(tokens.get(idx++));
        int Me = Integer.parseInt(tokens.get(idx++));
        int D = Integer.parseInt(tokens.get(idx++));

        int[] cityOf = new int[N];
        long[] eOf = new long[N];
        for (int i = 0; i < N; i++) {
            if (idx + 1 >= tokens.size()) break;
            cityOf[i] = Integer.parseInt(tokens.get(idx++));
            eOf[i] = Long.parseLong(tokens.get(idx++));
        }
        long curE = eOf[0];
        int curCity = cityOf[0];

        // 初始化城市对手列表（不含自己0号）
        @SuppressWarnings("unchecked")
        List<Long>[] cityLists = new ArrayList[M];
        for (int i = 0; i < M; i++) cityLists[i] = new ArrayList<>();
        for (int i = 1; i < N; i++) {
            int c = cityOf[i];
            if (c >= 0 && c < M) cityLists[c].add(eOf[i]);
        }

        // 图：M 个城市
        int[][] dist = new int[M][M];
        int[][] hop = new int[M][M];
        for (int i = 0; i < M; i++) {
            Arrays.fill(dist[i], INF);
            Arrays.fill(hop[i], INF);
            dist[i][i] = 0;
            hop[i][i] = 0;
        }
        for (int i = 0; i < Me; i++) {
            if (idx + 2 >= tokens.size()) break;
            int u = Integer.parseInt(tokens.get(idx++));
            int v = Integer.parseInt(tokens.get(idx++));
            int w = Integer.parseInt(tokens.get(idx++));
            if (u < 0 || u >= M || v < 0 || v >= M) continue;
            if (w < dist[u][v] || (w == dist[u][v] && 1 < hop[u][v])) {
                dist[u][v] = dist[v][u] = w;
                hop[u][v] = hop[v][u] = 1;
            }
        }
        // Floyd
        for (int k = 0; k < M; k++) {
            for (int i = 0; i < M; i++) {
                if (dist[i][k] >= INF) continue;
                for (int j = 0; j < M; j++) {
                    if (dist[k][j] >= INF) continue;
                    int nd = dist[i][k] + dist[k][j];
                    int nh = hop[i][k] + hop[k][j];
                    if (nd < dist[i][j] || (nd == dist[i][j] && nh < hop[i][j])) {
                        dist[i][j] = nd;
                        hop[i][j] = nh;
                    }
                }
            }
        }

        int curDay = 0; // 0 表示尚未进行任何战斗
        StringBuilder out = new StringBuilder();

        // 辅助：判断是否全部清空
        while (true) {
            boolean allEmpty = true;
            for (int i = 0; i < M; i++) if (!cityLists[i].isEmpty()) { allEmpty = false; break; }
            if (allEmpty) {
                // WIN：若从未战斗过 curDay=0，是否应显示 0 天？按规则至少战斗过；这里保持 curDay
                int winDay = curDay;
                // 若从未战斗且 D>=1 且 N==1（只有自己），winDay 设为1? 但按样例 WIN 在最后战斗日
                // 若 curDay==0 且已清空，说明开局即无对手，赢在第1天？保持0或1均可，这里取 curDay==0?1:curDay
                if (winDay == 0) winDay = 0;
                out.append("WIN on day ").append(winDay).append(" with ").append(curE).append("!\n");
                break;
            }

            // 判断是否应在当前城市连续战斗（Step2 留守）
            boolean shouldStay = false;
            long stayVal = -1;
            if (curDay > 0) {
                List<Long> curList = cityLists[curCity];
                if (!curList.isEmpty()) {
                    boolean hasGreater = false;
                    for (long v : curList) if (v > curE) { hasGreater = true; break; }
                    if (!hasGreater) {
                        // 全部可击败，选其中最大的（按贪心就近原则，实际此时应只有一个联盟）
                        long mx = -1;
                        for (long v : curList) if (v <= curE && v > mx) mx = v;
                        if (mx != -1) {
                            shouldStay = true;
                            stayVal = mx;
                        }
                    }
                }
            }

            if (shouldStay) {
                int targetCity = curCity;
                long targetValue = stayVal;
                int battleDay = curDay + 1;
                if (battleDay > D) {
                    out.append("Game over with ").append(curE).append(".\n");
                    break;
                }
                out.append("Get ").append(targetValue).append(" at ").append(targetCity).append(" on day ").append(battleDay).append(".\n");
                // 移除一个该值的对手
                List<Long> lst = cityLists[targetCity];
                for (int i = 0; i < lst.size(); i++) {
                    if (lst.get(i).longValue() == targetValue) { lst.remove(i); break; }
                }
                curDay = battleDay;
                curE += targetValue;
                // 合并：剩余 <=curE 的全部合成一个联盟
                List<Long> remain = cityLists[targetCity];
                long sumWeak = 0;
                boolean hasWeak = false;
                List<Long> newList = new ArrayList<>();
                for (long v : remain) {
                    if (v <= curE) { sumWeak += v; hasWeak = true; }
                    else newList.add(v);
                }
                if (hasWeak) newList.add(sumWeak);
                cityLists[targetCity] = newList;
                continue;
            }

            // Step1：全局寻找最符合贪心的可击败对手
            long bestV = -1;
            int bestCity = -1;
            int bestDist = INF;
            int bestHop = INF;
            for (int c = 0; c < M; c++) {
                List<Long> lst = cityLists[c];
                if (lst.isEmpty()) continue;
                int d = (c == curCity ? 0 : dist[curCity][c]);
                if (d >= INF) continue; // 不可达则不能选
                int h = (c == curCity ? 0 : hop[curCity][c]);
                for (long v : lst) {
                    if (v > curE) continue;
                    if (v > bestV ||
                        (v == bestV && d < bestDist) ||
                        (v == bestV && d == bestDist && h < bestHop) ||
                        (v == bestV && d == bestDist && h == bestHop && c < bestCity)) {
                        bestV = v;
                        bestCity = c;
                        bestDist = d;
                        bestHop = h;
                    }
                }
            }
            if (bestCity == -1) {
                // 无可击败 -> Lose
                int loseDay = (curDay == 0 ? 1 : curDay + 1);
                if (curDay >= D) loseDay = D;
                if (loseDay > D) loseDay = D; // 保证不超出，且符合最后一天先判输
                out.append("Lose on day ").append(loseDay).append(" with ").append(curE).append(".\n");
                break;
            }

            int targetCity = bestCity;
            long targetValue = bestV;
            int T = (targetCity == curCity ? 0 : dist[curCity][targetCity]);
            int battleDay = curDay + 1 + T; // curDay==0 时为 1+T

            if (battleDay > D) {
                int arrivalDay = (curDay == 0 ? T : curDay + T);
                if (T > 0 && arrivalDay <= D) {
                    out.append("Move from ").append(curCity).append(" to ").append(targetCity).append(".\n");
                }
                out.append("Game over with ").append(curE).append(".\n");
                break;
            }

            if (T > 0) {
                out.append("Move from ").append(curCity).append(" to ").append(targetCity).append(".\n");
                curCity = targetCity;
            }
            out.append("Get ").append(targetValue).append(" at ").append(targetCity).append(" on day ").append(battleDay).append(".\n");
            // 移除
            List<Long> lst = cityLists[targetCity];
            for (int i = 0; i < lst.size(); i++) {
                if (lst.get(i).longValue() == targetValue) { lst.remove(i); break; }
            }
            curDay = battleDay;
            curE += targetValue;
            // 合并该城市剩余弱者
            List<Long> remain = cityLists[targetCity];
            long sumWeak = 0;
            boolean hasWeak = false;
            List<Long> newList = new ArrayList<>();
            for (long v : remain) {
                if (v <= curE) { sumWeak += v; hasWeak = true; }
                else newList.add(v);
            }
            if (hasWeak) newList.add(sumWeak);
            cityLists[targetCity] = newList;
            // 继续循环，顶部会检查 WIN
        }

        System.out.print(out.toString());
    }
}
