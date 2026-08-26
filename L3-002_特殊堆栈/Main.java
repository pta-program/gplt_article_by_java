import java.util.*;
import java.io.*;

// L3-002 特殊堆栈: 栈 + 树状数组求中值 (第 (N+1)/2 小元)
// 时间复杂度 O(N log C), C=1e5
public class Main {
    static class BIT {
        int n;
        int[] tree;
        BIT(int n) {
            this.n = n;
            tree = new int[n + 2];
        }
        void add(int idx, int delta) {
            for (int i = idx; i <= n; i += i & -i) tree[i] += delta;
        }
        int sum(int idx) {
            int s = 0;
            for (int i = idx; i > 0; i -= i & -i) s += tree[i];
            return s;
        }
        // 找第 k 小 (k>=1)
        int kth(int k) {
            int idx = 0;
            int bitMask = Integer.highestOneBit(n);
            for (int d = bitMask; d != 0; d >>= 1) {
                int next = idx + d;
                if (next <= n && tree[next] < k) {
                    idx = next;
                    k -= tree[next];
                }
            }
            return idx + 1;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim().split("\\s+")[0]);
        // 栈存储实际值
        Deque<Integer> stack = new ArrayDeque<>();
        BIT bit = new BIT(100000);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < N; i++) {
            line = br.readLine();
            // 跳过空行（若有）
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.startsWith("Push")) {
                String[] parts = line.split("\\s+");
                int key = Integer.parseInt(parts[1]);
                stack.push(key);
                bit.add(key, 1);
            } else if (line.startsWith("Pop")) {
                if (stack.isEmpty()) {
                    out.append("Invalid\n");
                } else {
                    int v = stack.pop();
                    bit.add(v, -1);
                    out.append(v).append('\n');
                }
            } else if (line.startsWith("PeekMedian")) {
                if (stack.isEmpty()) {
                    out.append("Invalid\n");
                } else {
                    int sz = stack.size();
                    int k = (sz + 1) / 2; // 第 k 小
                    int med = bit.kth(k);
                    out.append(med).append('\n');
                }
            } else {
                // 未知指令，忽略并重试?
                i--;
            }
        }
        System.out.print(out.toString());
    }
}
