import java.io.*;

// L2-055 胖达的山头
// 闭区间最大重叠数，时间离散到秒 0..86399，用差分数组
// 时间 O(n + 86400)，空间 O(86400)
public class Main {
    static int toSec(String t) {
        // hh:mm:ss
        int h = Integer.parseInt(t.substring(0, 2));
        int m = Integer.parseInt(t.substring(3, 5));
        int s = Integer.parseInt(t.substring(6, 8));
        return h * 3600 + m * 60 + s;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        if (line == null) return;
        line = line.trim();
        if (line.isEmpty()) return;
        int n = Integer.parseInt(line);
        int[] diff = new int[86402];
        for (int i = 0; i < n; i++) {
            String s = br.readLine();
            while (s != null && s.trim().isEmpty()) s = br.readLine();
            if (s == null) break;
            String[] parts = s.trim().split("\\s+");
            int st = toSec(parts[0]);
            int ed = toSec(parts[1]);
            diff[st] += 1;
            if (ed + 1 <= 86400) diff[ed + 1] -= 1;
        }
        int cur = 0, ans = 0;
        for (int i = 0; i < 86400; i++) {
            cur += diff[i];
            if (cur > ans) ans = cur;
        }
        System.out.println(ans);
    }
}
