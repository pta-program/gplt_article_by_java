import java.util.*;
import java.io.*;

// L3-003 社交集群: 并查集 + 兴趣映射
// 时间复杂度 O(N * avgK * alpha(N)), N<=1000
public class Main {
    static int[] parent, rank;
    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    static void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra == rb) return;
        if (rank[ra] < rank[rb]) parent[ra] = rb;
        else if (rank[ra] > rank[rb]) parent[rb] = ra;
        else { parent[rb] = ra; rank[ra]++; }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim().split("\\s+")[0]);
        parent = new int[N + 1];
        rank = new int[N + 1];
        for (int i = 1; i <= N; i++) parent[i] = i;
        int[] hobbyOwner = new int[1001]; // 兴趣编号 1..1000
        Arrays.fill(hobbyOwner, 0);
        for (int i = 1; i <= N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            // 处理 "K: h1 h2 ..." 冒号
            line = line.replace(":", " ");
            StringTokenizer st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) { i--; continue; }
            int K = Integer.parseInt(st.nextToken());
            for (int k = 0; k < K; k++) {
                // 可能跨行？一般不跨行，但做容错
                while (!st.hasMoreTokens()) {
                    String extra = br.readLine();
                    if (extra == null) break;
                    extra = extra.replace(":", " ");
                    st = new StringTokenizer(extra);
                }
                if (!st.hasMoreTokens()) break;
                int h = Integer.parseInt(st.nextToken());
                if (h < 1 || h > 1000) continue;
                if (hobbyOwner[h] == 0) hobbyOwner[h] = i;
                else union(i, hobbyOwner[h]);
            }
        }
        Map<Integer,Integer> cnt = new HashMap<>();
        for (int i = 1; i <= N; i++) {
            int r = find(i);
            cnt.put(r, cnt.getOrDefault(r, 0) + 1);
        }
        List<Integer> sizes = new ArrayList<>(cnt.values());
        sizes.sort(Collections.reverseOrder());
        System.out.println(sizes.size());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb.toString());
    }
}
