import java.util.*;

// L3-014 周游世界
// 多公司线路的换乘最优路径：先经停站最少（边数最少），再换乘最少
// 算法：对每个查询在扩展图（站点, 上一条线路公司）上 Dijkstra，
// 主权重为边数，次权重为换乘次数
// 时间复杂度 O(K * (V+E) log V)
public class Main {
    static class Edge {
        int to, company;
        Edge(int to, int company) { this.to = to; this.company = company; }
    }
    static class State implements Comparable<State> {
        int node, lastCompany, edges, transfers;
        State(int node, int lastCompany, int edges, int transfers) {
            this.node = node; this.lastCompany = lastCompany; this.edges = edges; this.transfers = transfers;
        }
        public int compareTo(State o) {
            if (edges != o.edges) return Integer.compare(edges, o.edges);
            return Integer.compare(transfers, o.transfers);
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;
        int N = sc.nextInt();
        Map<Integer,Integer> stationToIdx = new HashMap<>();
        List<Integer> idxToStation = new ArrayList<>();
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            int M = sc.nextInt();
            int[] sts = new int[M];
            for (int j = 0; j < M; j++) {
                String s = sc.next();
                int st = Integer.parseInt(s);
                sts[j] = st;
                if (!stationToIdx.containsKey(st)) {
                    stationToIdx.put(st, idxToStation.size());
                    idxToStation.add(st);
                    adj.add(new ArrayList<>());
                }
            }
            for (int j = 0; j + 1 < M; j++) {
                int a = sts[j], b = sts[j+1];
                int ia = stationToIdx.get(a);
                int ib = stationToIdx.get(b);
                adj.get(ia).add(new Edge(ib, i));
                adj.get(ib).add(new Edge(ia, i));
            }
        }
        int K = sc.nextInt();
        StringBuilder out = new StringBuilder();
        for (int qi = 0; qi < K; qi++) {
            String sStr = sc.next();
            String tStr = sc.next();
            int sVal = Integer.parseInt(sStr);
            int tVal = Integer.parseInt(tStr);
            Integer sIdxObj = stationToIdx.get(sVal);
            Integer tIdxObj = stationToIdx.get(tVal);
            if (sIdxObj == null || tIdxObj == null) {
                out.append("Sorry, no line is available.\n");
                continue;
            }
            int sIdx = sIdxObj, tIdx = tIdxObj;
            int V = idxToStation.size();
            int maxC = N + 1;
            int[][] distEdges = new int[V][maxC];
            int[][] distTrans = new int[V][maxC];
            int[][] prevNode = new int[V][maxC];
            int[][] prevComp = new int[V][maxC];
            for (int i = 0; i < V; i++) {
                Arrays.fill(distEdges[i], Integer.MAX_VALUE/4);
                Arrays.fill(distTrans[i], Integer.MAX_VALUE/4);
                Arrays.fill(prevNode[i], -1);
                Arrays.fill(prevComp[i], -1);
            }
            PriorityQueue<State> pq = new PriorityQueue<>();
            distEdges[sIdx][0] = 0;
            distTrans[sIdx][0] = 0;
            pq.offer(new State(sIdx, 0, 0, 0));
            while (!pq.isEmpty()) {
                State cur = pq.poll();
                if (cur.edges != distEdges[cur.node][cur.lastCompany] || cur.transfers != distTrans[cur.node][cur.lastCompany]) continue;
                for (Edge e : adj.get(cur.node)) {
                    int nv = e.to;
                    int nc = e.company;
                    int ne = cur.edges + 1;
                    int nt = cur.transfers + (cur.lastCompany != 0 && cur.lastCompany != nc ? 1 : 0);
                    if (ne < distEdges[nv][nc] || (ne == distEdges[nv][nc] && nt < distTrans[nv][nc])) {
                        distEdges[nv][nc] = ne;
                        distTrans[nv][nc] = nt;
                        prevNode[nv][nc] = cur.node;
                        prevComp[nv][nc] = cur.lastCompany;
                        pq.offer(new State(nv, nc, ne, nt));
                    }
                }
            }
            int bestC = -1, bestE = Integer.MAX_VALUE/4, bestT = Integer.MAX_VALUE/4;
            for (int c = 0; c < maxC; c++) {
                if (distEdges[tIdx][c] < bestE || (distEdges[tIdx][c] == bestE && distTrans[tIdx][c] < bestT)) {
                    bestE = distEdges[tIdx][c];
                    bestT = distTrans[tIdx][c];
                    bestC = c;
                }
            }
            if (bestC == -1 || bestE == Integer.MAX_VALUE/4) {
                out.append("Sorry, no line is available.\n");
                continue;
            }
            List<Integer> stations = new ArrayList<>();
            List<Integer> comps = new ArrayList<>();
            int curNode = tIdx, curComp = bestC;
            stations.add(curNode);
            while (!(curNode == sIdx && curComp == 0)) {
                int pn = prevNode[curNode][curComp];
                int pc = prevComp[curNode][curComp];
                if (pn == -1) break;
                comps.add(curComp);
                stations.add(pn);
                curNode = pn;
                curComp = pc;
            }
            Collections.reverse(stations);
            Collections.reverse(comps);
            int edges = bestE;
            out.append(edges).append("\n");
            if (comps.isEmpty()) {
                // 起点即终点情况
                continue;
            }
            int segCompany = comps.get(0);
            int segStartStation = stations.get(0);
            for (int i = 1; i < comps.size(); i++) {
                if (comps.get(i) != segCompany) {
                    int segEndStation = stations.get(i);
                    out.append(String.format("Go by the line of company #%d from %04d to %04d.\n", segCompany, idxToStation.get(segStartStation), idxToStation.get(segEndStation)));
                    segCompany = comps.get(i);
                    segStartStation = segEndStation;
                }
            }
            int lastStation = stations.get(stations.size() - 1);
            out.append(String.format("Go by the line of company #%d from %04d to %04d.\n", segCompany, idxToStation.get(segStartStation), idxToStation.get(lastStation)));
        }
        System.out.print(out.toString());
    }
}
