import java.io.*;
import java.util.*;

// L3-012 水果忍者 - 穿过所有竖直线段的直线
// 算法：参数化直线 y = k*x + b，k = num/den
// 对固定k，b的可行区间为 [max(yLow - k*x), min(yHigh - k*x)]
// 可行k区间通过所有点对约束的交集确定；采用迭代收缩法：
// 每次取当前k计算最紧的上下界p,q，若可行则输出；否则由p,q给出
// 的斜率界限更新k的可行区间 [low, high]
// 时间复杂度 O(iter * N)，iter~100
public class Main {
    static long gcd(long a, long b) { a = Math.abs(a); b = Math.abs(b); while (b != 0) { long t = a % b; a = b; b = t; } return a; }
    // 比较分数 a/b 与 c/d，返回 -1/0/1
    static int cmpFrac(long a, long b, long c, long d) {
        // b,d >0, a,c 可负
        // a/b ? c/d  <=> a*d ? c*b
        // 用128位避免溢出：范围约 1e6*1e6=1e12，乘积约1e24 超过64位，需用BigInteger
        // 此处数值较小，可用64位（y差2e6，x差2e6，乘积4e12 fits）
        long left = a * d;
        long right = c * b;
        // 但a*d可能溢出64位? a~2e6, d~2e6 =>4e12 fits
        // 若low/high经过平均，分子可能变大至4e12仍 fits
        // 为安全用BigInteger
        java.math.BigInteger L = java.math.BigInteger.valueOf(a).multiply(java.math.BigInteger.valueOf(d));
        java.math.BigInteger R = java.math.BigInteger.valueOf(c).multiply(java.math.BigInteger.valueOf(b));
        return L.compareTo(R);
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        List<Long> vals = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) vals.add(Long.parseLong(st.nextToken()));
        }
        if (vals.isEmpty()) return;
        int n = vals.get(0).intValue();
        long[] xs = new long[n];
        long[] yLow = new long[n];
        long[] yHigh = new long[n];
        int idx = 1;
        for (int i = 0; i < n; i++) {
            long x = vals.get(idx++);
            long y1 = vals.get(idx++);
            long y2 = vals.get(idx++);
            xs[i] = x;
            yLow[i] = Math.min(y1, y2);
            yHigh[i] = Math.max(y1, y2);
        }
        // 分数表示斜率 k = num/den
        long lowNum = 0, lowDen = 0; // -inf
        long highNum = 0, highDen = 0; // +inf
        boolean lowInf = true, highInf = true;
        long curNum = 0, curDen = 1;
        for (int iter = 0; iter < 200; iter++) {
            long bestL = Long.MIN_VALUE;
            long bestU = Long.MAX_VALUE;
            int p = -1, q = -1;
            for (int i = 0; i < n; i++) {
                long L = yLow[i] * curDen - curNum * xs[i];
                long U = yHigh[i] * curDen - curNum * xs[i];
                if (p == -1 || L > bestL) { bestL = L; p = i; }
                if (q == -1 || U < bestU) { bestU = U; q = i; }
            }
            if (bestL <= bestU) {
                // 可行，输出直线经过 (xs[p], yLow[p]) 斜率为 cur
                long x1 = xs[p];
                long y1 = yLow[p];
                long x2 = x1 + curDen;
                long y2 = y1 + curNum;
                // 若cur为0，x2 = x1+1, y2=y1 仍可行
                System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
                return;
            }
            // 不可行，由p,q给出界限
            long numB = yHigh[q] - yLow[p];
            long denB = xs[q] - xs[p];
            if (denB == 0) {
                // 同x且区间不相交则无解（题目保证有解，不会出现）
                // 尝试换一个k
                // 随机扰动
                curNum = 1; curDen = 1;
                continue;
            }
            if (denB < 0) { numB = -numB; denB = -denB; }
            // 此时 bestL > bestU 意味着 cur > bound (当 denB>0) 或 cur < bound (denB<0已归一)
            // 推导：bestL > bestU => yLow[p]-cur*xs[p] > yHigh[q]-cur*xs[q]
            // => cur*(xs[q]-xs[p]) > yHigh[q]-yLow[p] => cur > bound (若 denB>0)
            // 所以需要 cur <= bound => high = min(high, bound)
            // 若 denB>0已归一为正，则为 high 界
            // 若原 denB<0已转为正且分子取反，则对应 low 界？统一处理：
            // 实际上经过归一化 denB>0，条件 cur > bound => high = bound
            // 但若原 denB<0，归一后仍为正，但不等式方向已翻转？让我们直接用原始推导：
            // bestL > bestU => yLow[p]-cur*xp > yHigh[q]-cur*xq
            // => cur*(xq - xp) > yHigh[q] - yLow[p]
            // 若 xq - xp >0 => cur > (yHigh[q]-yLow[p])/(xq-xp) => high = bound
            // 若 xq - xp <0 => cur < bound => low = bound
            long origDen = xs[q] - xs[p];
            long origNum = yHigh[q] - yLow[p];
            if (origDen > 0) {
                // cur > bound => high = bound
                if (highInf || cmpFrac(numB, denB, highNum, highDen) < 0) {
                    highNum = numB; highDen = denB; highInf = false;
                }
            } else {
                if (lowInf || cmpFrac(numB, denB, lowNum, lowDen) > 0) {
                    lowNum = numB; lowDen = denB; lowInf = false;
                }
            }
            if (!lowInf && !highInf && cmpFrac(lowNum, lowDen, highNum, highDen) > 0) {
                // 区间为空，理论上不应发生
                break;
            }
            // 选择下一个cur
            if (lowInf && highInf) { curNum = 0; curDen = 1; }
            else if (lowInf) { curNum = highNum; curDen = highDen; }
            else if (highInf) { curNum = lowNum; curDen = lowDen; }
            else {
                // 取中点 (low+high)/2
                // low = a/b, high = c/d => mid = (ad+cb)/(2bd)
                long n1 = lowNum * highDen + highNum * lowDen;
                long d1 = 2 * lowDen * highDen;
                // 约分
                long g = gcd(n1, d1);
                curNum = n1 / g;
                curDen = d1 / g;
                if (curDen < 0) { curNum = -curNum; curDen = -curDen; }
                // 防止数值过大，适当缩小
                if (Math.abs(curNum) > 4_000_000_000L || curDen > 4_000_000_000L) {
                    // 缩放
                    curNum /= 2; curDen /= 2;
                }
            }
        }
        // 兜底：若迭代未找到，输出任意可行解（取low）
        // 尝试用low/high中点再试一次
        // 简单输出第一个水果的下端点与斜率0的水平线（题目保证有解，此分支很少执行）
        System.out.println(xs[0] + " " + yLow[0] + " " + (xs[0]+1) + " " + yLow[0]);
    }
}
