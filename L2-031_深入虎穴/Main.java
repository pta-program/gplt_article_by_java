import java.util.*;
import java.io.*;

// L2-031 深入虎穴 - 找树中最深节点（入口为入度0的根）
// 时间复杂度 O(N) BFS
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        List<Integer>[] children = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) children[i] = new ArrayList<>();
        boolean[] hasParent = new boolean[N + 1];
        for (int i = 1; i <= N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) line = "0";
            StringTokenizer st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) {
                continue;
            }
            int K = Integer.parseInt(st.nextToken());
            for (int j = 0; j < K; j++) {
                // 可能跨行？但题面每行足够
                while (!st.hasMoreTokens()) {
                    line = br.readLine();
                    if (line == null) break;
                    st = new StringTokenizer(line);
                }
                if (!st.hasMoreTokens()) break;
                int v = Integer.parseInt(st.nextToken());
                children[i].add(v);
                if (v >= 1 && v <= N) hasParent[v] = true;
            }
        }
        int root = 1;
        for (int i = 1; i <= N; i++) if (!hasParent[i]) { root = i; break; }
        // BFS
        Queue<Integer> q = new ArrayDeque<>();
        q.offer(root);
        int deepest = root;
        // 为了求最深，可分层
        // 也可用 BFS 记录深度
        int[] depth = new int[N + 1];
        Arrays.fill(depth, -1);
        depth[root] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int u = queue.poll();
            deepest = u;
            for (int v : children[u]) {
                if (depth[v] == -1) {
                    depth[v] = depth[u] + 1;
                    queue.offer(v);
                }
            }
        }
        // 上述 BFS 按层次顺序，最后一个不一定是最深的（需要按深度最大）
        // 更严谨：找到深度最大的节点（题目保证唯一）
        int maxD = -1;
        int ans = root;
        for (int i = 1; i <= N; i++) {
            if (depth[i] > maxD) {
                maxD = depth[i];
                ans = i;
            }
        }
        System.out.println(ans);
    }
}
