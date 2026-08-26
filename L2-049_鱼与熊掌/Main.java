import java.io.*;
import java.util.*;

/**
 * L2-049 鱼与熊掌
 * 倒排索引：对每种物品记录拥有该物品的人员列表（已按人员编号升序）
 * 查询 (a,b) 时对两列表求交集大小，双指针归并 O(|A|+|B|)
 * 总体时间 O(总拥有条目 + Q*(平均列表长))，Q<=100 时非常轻量
 * 空间 O(总条目)
 */
public class Main {
    // 快速扫描器
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        FastScanner(InputStream in) { this.in = in; }
        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }
        int nextInt() throws IOException {
            int c, sign = 1, val = 0;
            do { c = readByte(); } while (c <= ' ' && c != -1);
            if (c == '-') { sign = -1; c = readByte(); }
            while (c > ' ') { val = val * 10 + (c - '0'); c = readByte(); }
            return val * sign;
        }
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);
        int n, m;
        try { n = fs.nextInt(); } catch (Exception e) { return; }
        m = fs.nextInt();

        @SuppressWarnings("unchecked")
        ArrayList<Integer>[] inv = new ArrayList[m + 1];
        for (int i = 1; i <= m; i++) inv[i] = new ArrayList<>();

        for (int person = 1; person <= n; person++) {
            int K = fs.nextInt();
            for (int k = 0; k < K; k++) {
                int item = fs.nextInt();
                if (item >= 1 && item <= m) inv[item].add(person);
            }
        }
        int Q = fs.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int qi = 0; qi < Q; qi++) {
            int a = fs.nextInt();
            int b = fs.nextInt();
            // 若 a==b 则拥有该物品的人数即为答案（兼得视为同种？题面说任意一对，可能a!=b，但防一下）
            if (a == b) {
                sb.append(inv[a].size()).append('\n');
                continue;
            }
            List<Integer> A = inv[a];
            List<Integer> B = inv[b];
            // 双指针求交
            int i = 0, j = 0, cnt = 0;
            int szA = A.size(), szB = B.size();
            // 为提升速度，让 A 为较短者可提前跳？双指针本身对长度不敏感
            while (i < szA && j < szB) {
                int va = A.get(i), vb = B.get(j);
                if (va == vb) { cnt++; i++; j++; }
                else if (va < vb) i++;
                else j++;
            }
            sb.append(cnt).append('\n');
        }
        System.out.print(sb.toString());
    }
}
