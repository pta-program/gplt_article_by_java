import java.io.*;
import java.util.*;

// L2-057 姥姥改作业
// 模拟栈式批改：cur 为当前待批栈（自顶向下顺序）
// 每次扫描 cur，>T 入 leftEncounter，<=T 入答案顺序
// 新阈值 T = floor( sum(leftEncounter)/size )，下一轮 cur = reverse(leftEncounter)
// 时间 O(n * 轮数)，最坏轮数 O(n)，但 n<=1000故可接受
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String first = br.readLine();
        while (first != null && first.trim().isEmpty()) first = br.readLine();
        if (first == null) return;
        StringTokenizer st = new StringTokenizer(first);
        int n = Integer.parseInt(st.nextToken());
        long T = Long.parseLong(st.nextToken());
        // 读取 n 个 c
        List<Integer> cVals = new ArrayList<>();
        while (cVals.size() < n) {
            String line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st2 = new StringTokenizer(line);
            while (st2.hasMoreTokens() && cVals.size() < n) {
                cVals.add(Integer.parseInt(st2.nextToken()));
            }
        }
        // c 数组 1-indexed 对应编号
        int[] c = new int[n + 1];
        for (int i = 1; i <= n; i++) c[i] = cVals.get(i - 1);

        List<Integer> cur = new ArrayList<>();
        for (int i = 1; i <= n; i++) cur.add(i); // 顶到底 1..n

        List<Integer> order = new ArrayList<>();
        long curT = T;

        while (!cur.isEmpty()) {
            List<Integer> nextLeft = new ArrayList<>();
            for (int id : cur) {
                if (c[id] > curT) {
                    nextLeft.add(id);
                } else {
                    order.add(id);
                }
            }
            if (nextLeft.isEmpty()) break;
            long sum = 0;
            for (int id : nextLeft) sum += c[id];
            curT = sum / nextLeft.size(); // floor 平均，不除以2
            // 下一轮为栈顶到底 = reverse(nextLeft)
            Collections.reverse(nextLeft);
            cur = nextLeft;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(order.get(i));
        }
        System.out.println(sb.toString());
    }
}
