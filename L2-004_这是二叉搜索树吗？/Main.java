import java.util.*;
import java.io.*;

// L2-004 这是二叉搜索树吗？: 判断前序是否为 BST 或镜像 BST, 并输出后序
// BST 规则: 左子树 < 根, 右子树 >= 根
// 镜像: 左 >= 根, 右 < 根
// 时间复杂度 O(N^2) 最坏, N<=1000 可接受; 可优化 O(N)
public class Main {
    static int[] pre;
    static List<Integer> post;
    static boolean ok;

    static void solve(int l, int r, boolean mirror) { // [l,r)
        if (!ok) return;
        if (l >= r) return;
        if (l + 1 == r) { post.add(pre[l]); return; }
        int root = pre[l];
        int split = -1;
        if (!mirror) {
            // 第一个 >= root
            int i = l + 1;
            while (i < r && pre[i] < root) i++;
            split = i;
            // 检查右半部分都 >= root
            for (int j = split; j < r; j++) if (pre[j] < root) { ok = false; return; }
        } else {
            int i = l + 1;
            while (i < r && pre[i] >= root) i++;
            split = i;
            for (int j = split; j < r; j++) if (pre[j] >= root) { ok = false; return; }
        }
        solve(l + 1, split, mirror);
        solve(split, r, mirror);
        post.add(root);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        pre = new int[N];
        int idx = 0;
        StringTokenizer st = null;
        while (idx < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens() && idx < N) pre[idx++] = Integer.parseInt(st.nextToken());
        }
        if (N == 0) { System.out.println("NO"); return; }

        // 先尝试正常 BST
        post = new ArrayList<>();
        ok = true;
        solve(0, N, false);
        if (ok) {
            System.out.println("YES");
            printPost();
            return;
        }
        // 尝试镜像
        post = new ArrayList<>();
        ok = true;
        solve(0, N, true);
        if (ok) {
            System.out.println("YES");
            printPost();
        } else {
            System.out.println("NO");
        }
    }

    static void printPost() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < post.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(post.get(i));
        }
        System.out.println(sb.toString());
    }
}
