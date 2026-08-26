import java.io.*;
import java.util.*;

/**
 * L2-046 天梯赛的赛场安排
 * 算法：优先队列按剩余人数降序调度 + 赛场剩余容量管理
 *  - 使用最大堆每次取未安排人数最多的学校
 *  - 对于 n>=C 直接新开满赛场
 *  - 对于 n<C 在已有非满赛场中寻找剩余容量 >=n 的编号最小赛场（C<=50，用按剩余容量分组的 TreeSet 实现 O(C log R) 查询）
 *  - 每个学校至多一个零散块，故联系数 = 满块数 + (是否还有零散块)
 * 时间复杂度：O((总分配次数)*(log N + C log R))，总分配次数 <= 总人数/C + N <= 2.5e5
 * 空间复杂度：O(N + R)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读第一行，跳过空行
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());

        String[] names = new String[N];
        int[] cnt = new int[N];
        for (int i = 0; i < N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            st = new StringTokenizer(line);
            names[i] = st.nextToken();
            cnt[i] = Integer.parseInt(st.nextToken());
        }

        int[] remain = Arrays.copyOf(cnt, N);
        int[] contact = new int[N];

        // 最大堆：剩余大优先，剩余相同编号小优先（确定性）
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> {
            if (remain[a] != remain[b]) return Integer.compare(remain[b], remain[a]);
            return Integer.compare(a, b);
        });
        for (int i = 0; i < N; i++) if (remain[i] > 0) pq.offer(i);

        // 赛场管理：roomRemain[1..R]，按剩余容量分组的 TreeSet
        // 下标1开始，方便取最小编号
        ArrayList<Integer> roomRem = new ArrayList<>();
        roomRem.add(0); // 占位
        @SuppressWarnings("unchecked")
        TreeSet<Integer>[] sets = new TreeSet[C];
        for (int i = 0; i < C; i++) sets[i] = new TreeSet<>();
        int roomCnt = 0;

        while (!pq.isEmpty()) {
            int idx = pq.poll();
            int n = remain[idx];
            if (n <= 0) continue;
            // 可能存在堆中过期条目（已更新后重复入堆），但我们是取出后直接处理并重入，无过期问题
            // 由于我们只在 remain 变化时重入，且堆中只有一个条目每学校，暂时不需要惰性校验
            // 然而为了安全，若堆顶 remain 已改变（因为我们直接用数组作比较），比较器会乱；所以每次 poll 后校验
            // 实际上 PriorityQueue 的 ordering 在 remain 变化后不会自动调整，故需要重新入堆后保持正确
            // 我们保证 remain 修改后会重新 offer，因此堆内元素 remain 与存储一致
            if (n >= C) {
                // 新开满赛场
                roomCnt++;
                roomRem.add(0); // 剩余0
                // 满场不加入 sets
                contact[idx]++;
                remain[idx] -= C;
                if (remain[idx] > 0) pq.offer(idx);
            } else {
                // n < C，寻找可用赛场
                int bestIdx = -1;
                int bestRoom = Integer.MAX_VALUE;
                for (int r = n; r < C; r++) {
                    if (!sets[r].isEmpty()) {
                        int cand = sets[r].first();
                        if (cand < bestRoom) {
                            bestRoom = cand;
                            bestIdx = r;
                        }
                    }
                }
                if (bestRoom != Integer.MAX_VALUE) {
                    // 找到
                    int roomId = bestRoom;
                    int oldRem = bestIdx;
                    sets[oldRem].remove(roomId);
                    int newRem = oldRem - n;
                    roomRem.set(roomId, newRem);
                    if (newRem > 0) sets[newRem].add(roomId);
                    contact[idx]++;
                    remain[idx] -= n;
                    if (remain[idx] > 0) pq.offer(idx);
                } else {
                    // 新开赛场装 n
                    roomCnt++;
                    int newRem = C - n;
                    roomRem.add(newRem);
                    if (newRem > 0) sets[newRem].add(roomCnt);
                    contact[idx]++;
                    remain[idx] -= n;
                    if (remain[idx] > 0) pq.offer(idx);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(names[i]).append(' ').append(contact[i]).append('\n');
        }
        sb.append(roomCnt).append('\n');
        System.out.print(sb.toString());
    }
}
