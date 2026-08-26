import java.util.*;
import java.io.*;

// L3-004 肿瘤诊断: 三维连通块 BFS (6 连通)
// M 行 N 列 L 层, 阈值 T, 统计体积 >=T 的连通块总体积
// 时间复杂度 O(M*N*L), 空间 O(M*N*L)
// 数据规模约 1e7 需用一维数组降低开销
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取所有整数
        List<Integer> vals = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
        }
        if (vals.size() < 4) return;
        int M = vals.get(0);
        int N = vals.get(1);
        int L = vals.get(2);
        int T = vals.get(3);
        int total = M * N * L;
        // 网格：1 表示肿瘤
        byte[] grid = new byte[total];
        int idx = 4;
        // 填充 L 层，每层 M*N 个数，按输入顺序 M*N per slice, slice 0..L-1, 每 slice M 行 N 列
        //  vals 中剩余数量可能正好 total，若不足则补 0
        for (int i = 0; i < total && idx < vals.size(); i++) {
            grid[i] = (byte)(vals.get(idx++) == 1 ? 1 : 0);
        }
        // 若 vals 读取方式是以 M 为行，需要映射：我们已按线性顺序读，输入顺序也是按层、行、列依次给出，正好对应 i = z*M*N + x*N + y
        boolean[] vis = new boolean[total];
        int[] queue = new int[total];
        // 方向：  dx, dy, dz 对应 x(0..M-1), y(0..N-1), z(0..L-1)
        // 线性 id = z*M*N + x*N + y
        long totalVolume = 0;
        int NM = N * M;
        // 为减少除法开销，BFS 时通过坐标计算邻接时使用算术
        for (int z = 0; z < L; z++) {
            for (int x = 0; x < M; x++) {
                for (int y = 0; y < N; y++) {
                    int id = z * M * N + x * N + y;
                    if (grid[id] == 0 || vis[id]) continue;
                    // BFS
                    int head = 0, tail = 0;
                    queue[tail++] = id;
                    vis[id] = true;
                    int cnt = 0;
                    while (head < tail) {
                        int cur = queue[head++];
                        cnt++;
                        // 解码
                        int curY = cur % N;
                        int tmp = cur / N;
                        int curX = tmp % M;
                        int curZ = tmp / M;
                        // 6 邻域
                        // x+1
                        if (curX + 1 < M) {
                            int nid = cur + N;
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                        // x-1
                        if (curX - 1 >= 0) {
                            int nid = cur - N;
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                        // y+1
                        if (curY + 1 < N) {
                            int nid = cur + 1;
                            // 防止跨行: 确保同 x,z 行内
                            // 由于 y 在同一行，curY+1 < N 保证不跨行
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                        // y-1
                        if (curY - 1 >= 0) {
                            int nid = cur - 1;
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                        // z+1
                        if (curZ + 1 < L) {
                            int nid = cur + M * N;
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                        // z-1
                        if (curZ - 1 >= 0) {
                            int nid = cur - M * N;
                            if (!vis[nid] && grid[nid]==1) { vis[nid]=true; queue[tail++]=nid; }
                        }
                    }
                    if (cnt >= T) totalVolume += cnt;
                }
            }
        }
        System.out.println(totalVolume);
    }
}
