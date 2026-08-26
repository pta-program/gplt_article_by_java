import java.io.*;

// L2-008 最长对称子串: 中心扩展法
// 时间复杂度 O(N^2) N<=1000
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String s = br.readLine();
        if (s == null) s = "";
        // 注意题目输入是整行，可能包含空格，需读取完整一行；若为空再读
        // 但样例只有一行
        int n = s.length();
        if (n == 0) { System.out.println(0); return; }
        int max = 1;
        for (int center = 0; center < n; center++) {
            // 奇数
            int l = center, r = center;
            while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                max = Math.max(max, r - l + 1);
                l--; r++;
            }
            // 偶数
            l = center; r = center + 1;
            while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
                max = Math.max(max, r - l + 1);
                l--; r++;
            }
        }
        System.out.println(max);
    }
}
