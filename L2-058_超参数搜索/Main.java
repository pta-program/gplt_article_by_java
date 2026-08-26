import java.io.*;
import java.util.*;

// L2-058 超参数搜索
// 首行输出最大值得分对应最小编号集合；后续每次查询 x 找 >x 的得分中最小者
// 若同分多编号取最小编号；不存在输出 0
// 时间 O(n log n + m log n)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int n = Integer.parseInt(line.trim());
        int[] score = new int[n + 1];
        // 读取 n 个分数，可能分多行
        int idx = 1;
        while (idx <= n) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens() && idx <= n) {
                score[idx++] = Integer.parseInt(st.nextToken());
            }
        }
        // 最大值
        int max = -1;
        for (int i = 1; i <= n; i++) max = Math.max(max, score[i]);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 1; i <= n; i++) if (score[i] == max) {
            if (!first) sb.append(' ');
            sb.append(i);
            first = false;
        }
        sb.append('\n');

        // 建立 分值 -> 最小编号 映射
        Map<Integer, Integer> minIdx = new HashMap<>();
        for (int i = 1; i <= n; i++) {
            int s = score[i];
            minIdx.merge(s, i, Math::min);
        }
        List<Integer> uniq = new ArrayList<>(minIdx.keySet());
        Collections.sort(uniq); // 升序

        // 读取 m
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) {
            System.out.print(sb.toString());
            return;
        }
        int m = Integer.parseInt(line.trim());
        // 读取 m 个查询，每个一行
        for (int i = 0; i < m; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            int x = Integer.parseInt(line.trim());
            // 上界 > x
            int pos = Collections.binarySearch(uniq, x);
            int up;
            if (pos >= 0) up = pos + 1;
            else up = -pos - 1;
            if (up >= uniq.size()) sb.append(0).append('\n');
            else {
                int val = uniq.get(up);
                sb.append(minIdx.get(val)).append('\n');
            }
        }
        System.out.print(sb.toString());
    }
}
