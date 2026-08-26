import java.io.*;
import java.util.*;

// L3-023 计算图
// 算法：拓扑排序前向求值 + 反向传播求梯度
// 时间复杂度 O(N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        int[] type = new int[N];
        double[] val = new double[N];
        int[] in1 = new int[N];
        int[] in2 = new int[N];
        Arrays.fill(in1, -1);
        Arrays.fill(in2, -1);
        List<Integer>[] out = new List[N];
        for (int i = 0; i < N; i++) out[i] = new ArrayList<>();
        int[] indeg = new int[N];
        List<Integer> inputOrder = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            int t = Integer.parseInt(st.nextToken());
            type[i] = t;
            if (t == 0) {
                double v = Double.parseDouble(st.nextToken());
                val[i] = v;
                indeg[i] = 0;
                inputOrder.add(i);
            } else if (t == 1 || t == 2 || t == 3) {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                in1[i] = a; in2[i] = b;
                indeg[i] = 2;
                out[a].add(i);
                out[b].add(i);
            } else { // 4,5,6 单目
                int a = Integer.parseInt(st.nextToken());
                in1[i] = a;
                indeg[i] = 1;
                out[a].add(i);
            }
        }
        // 拓扑排序
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < N; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> topo = new ArrayList<>(N);
        while (!q.isEmpty()) {
            int u = q.poll();
            topo.add(u);
            for (int v : out[u]) {
                if (--indeg[v] == 0) q.add(v);
            }
        }
        // 若图中存在环或未全部入队（可能输入已按拓扑序但我们用 Kahn 已处理）
        if (topo.size() != N) {
            // 退化：按索引顺序作为拓扑（若仍有环则无法计算）
            topo.clear();
            for (int i = 0; i < N; i++) topo.add(i);
        }
        // 前向求值（按 topo 顺序）
        for (int u : topo) {
            int t = type[u];
            if (t == 0) continue;
            else if (t == 1) {
                val[u] = val[in1[u]] + val[in2[u]];
            } else if (t == 2) {
                val[u] = val[in1[u]] - val[in2[u]];
            } else if (t == 3) {
                val[u] = val[in1[u]] * val[in2[u]];
            } else if (t == 4) {
                val[u] = Math.exp(val[in1[u]]);
            } else if (t == 5) {
                val[u] = Math.log(val[in1[u]]);
            } else if (t == 6) {
                val[u] = Math.sin(val[in1[u]]);
            }
        }
        // 找输出节点（无出边）
        int outNode = -1;
        for (int i = 0; i < N; i++) if (out[i].isEmpty()) outNode = i;
        if (outNode == -1) outNode = topo.get(topo.size() - 1);
        double funcVal = val[outNode];

        // 反向传播
        double[] grad = new double[N];
        grad[outNode] = 1.0;
        for (int idx = topo.size() - 1; idx >= 0; idx--) {
            int u = topo.get(idx);
            double g = grad[u];
            if (g == 0) continue;
            int t = type[u];
            if (t == 1) {
                grad[in1[u]] += g;
                grad[in2[u]] += g;
            } else if (t == 2) {
                grad[in1[u]] += g;
                grad[in2[u]] -= g;
            } else if (t == 3) {
                grad[in1[u]] += g * val[in2[u]];
                grad[in2[u]] += g * val[in1[u]];
            } else if (t == 4) {
                // exp' = exp
                grad[in1[u]] += g * val[u];
            } else if (t == 5) {
                grad[in1[u]] += g / val[in1[u]];
            } else if (t == 6) {
                grad[in1[u]] += g * Math.cos(val[in1[u]]);
            }
        }
        System.out.printf(Locale.ROOT, "%.3f%n", funcVal);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < inputOrder.size(); i++) {
            if (i > 0) sb.append(' ');
            double gv = grad[inputOrder.get(i)];
            // 处理 -0.000
            if (Math.abs(gv) < 0.0005) gv = 0;
            sb.append(String.format(Locale.ROOT, "%.3f", gv));
        }
        System.out.println(sb.toString());
    }
}
