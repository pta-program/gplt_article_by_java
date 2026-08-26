import java.io.*;
import java.util.*;

// L3-017 森森快递
// 算法：按区间右端点排序，线段树维护区间最小剩余容量 + 区间减
// 每个订单取区间最小可运输量，累计答案并对区间做减法
// 时间复杂度 O((N+Q) log N)， N,Q<=1e5
public class Main {
    static class Order {
        int l, r;
        Order(int l, int r) { this.l = l; this.r = r; }
    }
    static class SegTree {
        int n;
        long[] mn, lazy;
        SegTree(long[] arr) {
            n = arr.length;
            mn = new long[4 * n];
            lazy = new long[4 * n];
            build(1, 0, n - 1, arr);
        }
        void build(int node, int l, int r, long[] arr) {
            if (l == r) { mn[node] = arr[l]; return; }
            int mid = (l + r) >>> 1;
            build(node << 1, l, mid, arr);
            build(node << 1 | 1, mid + 1, r, arr);
            mn[node] = Math.min(mn[node << 1], mn[node << 1 | 1]);
        }
        void apply(int node, long v) {
            mn[node] += v;
            lazy[node] += v;
        }
        void push(int node) {
            if (lazy[node] != 0) {
                apply(node << 1, lazy[node]);
                apply(node << 1 | 1, lazy[node]);
                lazy[node] = 0;
            }
        }
        long queryMin(int L, int R) { return queryMin(1, 0, n - 1, L, R); }
        long queryMin(int node, int l, int r, int L, int R) {
            if (L <= l && r <= R) return mn[node];
            push(node);
            int mid = (l + r) >>> 1;
            long ans = Long.MAX_VALUE;
            if (L <= mid) ans = Math.min(ans, queryMin(node << 1, l, mid, L, R));
            if (R > mid) ans = Math.min(ans, queryMin(node << 1 | 1, mid + 1, r, L, R));
            return ans;
        }
        void rangeAdd(int L, int R, long v) { rangeAdd(1, 0, n - 1, L, R, v); }
        void rangeAdd(int node, int l, int r, int L, int R, long v) {
            if (L <= l && r <= R) { apply(node, v); return; }
            push(node);
            int mid = (l + r) >>> 1;
            if (L <= mid) rangeAdd(node << 1, l, mid, L, R, v);
            if (R > mid) rangeAdd(node << 1 | 1, mid + 1, r, L, R, v);
            mn[node] = Math.min(mn[node << 1], mn[node << 1 | 1]);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        StringTokenizer st;
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        long[] C = new long[Math.max(0, N - 1)];
        int idx = 0;
        while (idx < C.length) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens() && idx < C.length) {
                C[idx++] = Long.parseLong(st.nextToken());
            }
        }
        List<Order> orders = new ArrayList<>(Q);
        for (int i = 0; i < Q; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            st = new StringTokenizer(line);
            int s = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            int l = Math.min(s, t);
            int r = Math.max(s, t) - 1; // 边的区间 [l,r]
            if (l <= r && r < C.length && l >= 0) {
                orders.add(new Order(l, r));
            } else if (l <= r) {
                // 区间非法时仍需处理（若 N=1 无边？但 N>=2）
                // 若越界忽略
            }
        }
        if (C.length == 0) {
            System.out.println(0);
            return;
        }
        // 按右端点升序，右相同按左降序（区间短的先）
        orders.sort((a, b) -> {
            if (a.r != b.r) return Integer.compare(a.r, b.r);
            return Integer.compare(b.l, a.l);
        });

        SegTree seg = new SegTree(C);
        long ans = 0;
        for (Order o : orders) {
            long curMin = seg.queryMin(o.l, o.r);
            if (curMin > 0) {
                ans += curMin;
                seg.rangeAdd(o.l, o.r, -curMin);
            } else if (curMin < 0) {
                // C 可能是负？题目说非负，忽略
            }
        }
        System.out.println(ans);
    }
}
