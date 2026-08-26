import java.io.*;
import java.util.*;

// L3-013 非常弹的球
// 物理模型：初动能 E0=1000J, 质量 m=w/100 kg, g=9.8
// 初速 v0 = sqrt(2E0/m), 水平分量 vx0=v0*cosA, 竖直 vy0=v0*sinA
// 每次弹跳损失 p% 动能，速度缩放因子 s = sqrt(1-p/100)
// 第k次弹跳水平位移 dx_k = vx_k * (2*vy_k/g) = v0^2*sin2A/g * s^{2k}
// 总位移 D = v0^2*sin2A/g * 1/(1-s^2) = v0^2*sin2A*100/(g*p)
// 最大值在 sin2A=1 (A=45°) 时取得
// 时间复杂度 O(1)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        List<String> toks = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) toks.add(st.nextToken());
        }
        if (toks.size() < 2) return;
        long w = Long.parseLong(toks.get(0));
        long p = Long.parseLong(toks.get(1));
        double m = w / 100.0;
        double E0 = 1000.0;
        double g = 9.8;
        double v02 = 2 * E0 / m; // v0^2
        double Dmax;
        if (p == 0) {
            // 理论上p>=1，但防御
            Dmax = Double.POSITIVE_INFINITY;
        } else {
            Dmax = v02 * 100.0 / (g * p);
            // 等价于 20000000/(w*g*p)
        }
        System.out.printf("%.3f%n", Dmax);
    }
}
