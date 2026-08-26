import java.util.*;
import java.io.*;

// L2-003 月饼: 分数背包按单价贪心
// 时间复杂度 O(N log N)
public class Main {
    static class Cake {
        double stock;
        double price;
        double unit;
        Cake(double s, double p) { stock = s; price = p; unit = p / s; }
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        double D = Double.parseDouble(st.nextToken());
        double[] stock = new double[N];
        double[] price = new double[N];
        // 读取库存
        int idx = 0;
        while (idx < N) {
            if (!st.hasMoreTokens()) {
                line = br.readLine();
                if (line == null) break;
                st = new StringTokenizer(line);
                continue;
            }
            stock[idx++] = Double.parseDouble(st.nextToken());
        }
        // 读取总售价
        idx = 0;
        // 可能已在同一行剩余? 重新读取
        // 如果上一轮结束后 st 还有剩余, 已处理; 现在读取下一行
        // 为简单重新按行读满 N 个
        List<Double> prices = new ArrayList<>();
        // 先把剩余 tokens 加入
        while (st.hasMoreTokens()) prices.add(Double.parseDouble(st.nextToken()));
        while (prices.size() < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens()) prices.add(Double.parseDouble(st.nextToken()));
        }
        for (int i = 0; i < N; i++) price[i] = prices.get(i);

        List<Cake> list = new ArrayList<>();
        for (int i = 0; i < N; i++) list.add(new Cake(stock[i], price[i]));
        list.sort((a,b) -> Double.compare(b.unit, a.unit));

        double remain = D;
        double ans = 0;
        for (Cake c : list) {
            if (remain <= 1e-9) break;
            if (c.stock <= remain) {
                ans += c.price;
                remain -= c.stock;
            } else {
                ans += c.unit * remain;
                remain = 0;
            }
        }
        System.out.printf("%.2f\n", ans);
    }
}
