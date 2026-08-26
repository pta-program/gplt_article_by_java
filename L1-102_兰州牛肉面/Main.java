import java.util.*;
import java.io.*;

/**
 * L1-102 兰州牛肉面
 * 原理：
 * 1) 价格以元为单位，精确到分，需避免 double 二进制误差。改为按字符串解析为“分”（整数）。
 *    解析规则：若含小数点，分 = 元*100 + 分位；补齐/截断到两位小数。
 * 2) 统计每种面卖出碗数 c[i]，总营业额 sum(分) = Σ p[i]*c[i]，p[i]为单价分。
 * 3) 最后按分格式化为 元.分（两位小数）。
 * 时间复杂度 O(N + 销售记录数)，空间 O(N)。
 */
public class Main {
    // 将价格字符串转为分整数，避免浮点误差
    private static int toCents(String s) {
        s = s.trim();
        boolean neg = false;
        if (s.startsWith("-")) { neg = true; s = s.substring(1); }
        int dot = s.indexOf('.');
        int cents;
        if (dot < 0) {
            cents = Integer.parseInt(s) * 100;
        } else {
            String yuanPart = s.substring(0, dot);
            String fenPart = s.substring(dot + 1);
            // 补齐到至少两位，超过两位按原输入截断/四舍五入？题目保证精确到分，故直接补/截
            if (fenPart.length() == 0) fenPart = "00";
            else if (fenPart.length() == 1) fenPart = fenPart + "0";
            else if (fenPart.length() > 2) fenPart = fenPart.substring(0, 2);
            int yuan = yuanPart.isEmpty() ? 0 : Integer.parseInt(yuanPart);
            int fen = Integer.parseInt(fenPart);
            cents = yuan * 100 + fen;
        }
        return neg ? -cents : cents;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // 使用 Scanner 风格但基于字符串分词，避免 double
        // 为简化，仍用 Scanner 读取 token，但在价格处使用字符串解析
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNext()) return;
        int n = sc.nextInt();
        int[] price = new int[n];
        for (int i = 0; i < n; i++) {
            String token = sc.next(); // 价格 token 保持字符串形式
            price[i] = toCents(token);
        }
        int[] cnt = new int[n];
        long sum = 0;
        while (sc.hasNextInt()) {
            int x = sc.nextInt();
            int q = sc.nextInt();
            if (x == 0) break;
            // 题目保证 1<=x<=N
            cnt[x - 1] += q;
            sum += (long) price[x - 1] * q;
        }
        for (int c : cnt) System.out.println(c);
        // 格式化为 元.分，保留两位小数
        long yuan = sum / 100;
        long fen = Math.abs(sum % 100);
        // sum 非负（价格非负），直接 printf 也可；此处兼容
        if (sum >= 0) {
            System.out.printf("%d.%02d", yuan, fen);
        } else {
            System.out.printf("-%d.%02d", Math.abs(yuan), fen);
        }
    }
}
