import java.io.*;
import java.util.*;

// L2-054 三点共线
// 三点 y 分别为 0,1,2 共线等价于 x0 + x2 == 2*x1
// 全去重后按 y 分组，枚举 y=1 的点与较小一侧的组合，用布尔数组 O(1) 判存在
// 时间 O( X_range + |S1|*min(|S0|,|S2|) )，空间 O(X_range)
public class Main {
    static final int OFFSET = 1000000;
    static final int SIZE = 2000001;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        if (line == null) return;
        int n = Integer.parseInt(line.trim());
        boolean[] has0 = new boolean[SIZE];
        boolean[] has1 = new boolean[SIZE];
        boolean[] has2 = new boolean[SIZE];
        for (int i = 0; i < n; i++) {
            String s = br.readLine();
            while (s != null && s.trim().isEmpty()) s = br.readLine();
            if (s == null) break;
            StringTokenizer st = new StringTokenizer(s);
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int idx = x + OFFSET;
            if (y == 0) has0[idx] = true;
            else if (y == 1) has1[idx] = true;
            else has2[idx] = true;
        }
        List<Integer> list0 = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            if (has0[i]) list0.add(i - OFFSET);
            if (has1[i]) list1.add(i - OFFSET);
            if (has2[i]) list2.add(i - OFFSET);
        }
        if (list0.isEmpty() || list1.isEmpty() || list2.isEmpty()) {
            System.out.println(-1);
            return;
        }
        List<int[]> res = new ArrayList<>();
        // 选择较小一侧迭代以减少循环次数
        if (list0.size() <= list2.size()) {
            for (int b : list1) {
                for (int a : list0) {
                    int c = 2 * b - a;
                    if (c < -1000000 || c > 1000000) continue;
                    if (has2[c + OFFSET]) {
                        res.add(new int[]{a, b, c});
                        if (res.size() > 100000) {} // 题目保证不超过1e5
                    }
                }
            }
        } else {
            for (int b : list1) {
                for (int c : list2) {
                    int a = 2 * b - c;
                    if (a < -1000000 || a > 1000000) continue;
                    if (has0[a + OFFSET]) {
                        res.add(new int[]{a, b, c});
                    }
                }
            }
        }
        if (res.isEmpty()) {
            System.out.println(-1);
            return;
        }
        // 按 y=1 的 x 升序，其次 y=0 的 x 升序
        res.sort((p, q) -> {
            if (p[1] != q[1]) return Integer.compare(p[1], q[1]);
            return Integer.compare(p[0], q[0]);
        });
        StringBuilder sb = new StringBuilder();
        for (int[] t : res) {
            sb.append('[').append(t[0]).append(", 0] ")
              .append('[').append(t[1]).append(", 1] ")
              .append('[').append(t[2]).append(", 2]")
              .append('\n');
        }
        System.out.print(sb.toString());
    }
}
