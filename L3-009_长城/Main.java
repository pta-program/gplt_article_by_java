import java.io.*;
import java.util.*;

// L3-009 长城 - 最少烽火台
// 算法：维护可见栈（单调栈），按南到北扫描折线顶点。
// 若新点与栈顶两点构成非左转（cross <= 0），弹出栈顶，说明中间点被遮挡；
// 若未弹出任何点，则栈顶为必须设台的“凸点”，计入答案。
// 时间复杂度 O(N)，空间 O(N)
public class Main {
    static class Point {
        long x, y;
        Point(long x, long y) { this.x = x; this.y = y; }
    }
    static long cross(Point a, Point b, Point c) {
        // (b-a) x (c-a)
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取所有整数
        List<Long> vals = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) vals.add(Long.parseLong(st.nextToken()));
        }
        if (vals.isEmpty()) return;
        int n = vals.get(0).intValue();
        List<Point> pts = new ArrayList<>();
        int idx = 1;
        for (int i = 0; i < n; i++) {
            if (idx + 1 >= vals.size()) break;
            long x = vals.get(idx++);
            long y = vals.get(idx++);
            pts.add(new Point(x, y));
        }
        if (pts.size() != n) {
            // 若输入行数不足，按实际读取
            n = pts.size();
        }
        if (n <= 2) {
            System.out.println(0);
            return;
        }
        List<Point> st = new ArrayList<>();
        st.add(pts.get(0));
        st.add(pts.get(1));
        int ans = 0;
        for (int i = 2; i < n; i++) {
            Point cur = pts.get(i);
            boolean removed = false;
            while (st.size() >= 2) {
                Point a = st.get(st.size() - 2);
                Point b = st.get(st.size() - 1);
                long cr = cross(a, b, cur);
                if (cr <= 0) {
                    st.remove(st.size() - 1);
                    removed = true;
                } else break;
            }
            if (!removed) ans++;
            st.add(cur);
        }
        System.out.println(ans);
    }
}
