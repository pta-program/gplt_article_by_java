import java.io.*;
import java.util.*;

/**
 * L3-031 千手观音
 * 
 * 题意：N 个 10000 进制数有序递增，每个数由 '.' 分隔 1~10 个符号（1~3 小写字母）组成。
 *      符号对应未知数字 0..9999，数的大小按高位在左、低位在右的数值比较。
 *      给定递增序列，推断符号的相对大小；无法确定时按英文字典序升序。
 * 
 * 观察：
 *  - 若两数长度不等，则较长者恒大于较短者（首位非零且进制足够大），与符号取值无关，不产生偏序。
 *  - 仅当相邻两数长度相等时，首个不同位置的符号可确定大小关系 a < b，建边 a -> b。
 *    相邻比较已足够捕获全部偏序（传递性）。
 *  - 对收集到的符号建有向图，题目保证无环。求字典序最小的拓扑排序：
 *    每次从入度为 0 的节点中取字典序最小者（PriorityQueue / Kahn）。
 *    这满足“无法确定时按字典序”要求。
 * 
 * 时间复杂度：设 N <= 1e5，L <=10 为单个数最多位数，M <= 1e4 为符号种类，E <= N 为边数
 *  - 读入与建图：O(N*L)
 *  - 拓扑排序（堆）：O((M+E) log M)
 * 空间复杂度：O(N*L + M + E)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 跳过空行读取 N
        line = null;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) break;
        }
        if (line == null) return;
        int N;
        try {
            N = Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            // 若首行包含多余内容，只取第一个整数
            StringTokenizer st = new StringTokenizer(line);
            N = Integer.parseInt(st.nextToken());
        }

        List<String[]> nums = new ArrayList<>(N);
        Set<String> symbolSet = new HashSet<>(8192);

        int readCnt = 0;
        while (readCnt < N) {
            String s = br.readLine();
            if (s == null) break;
            s = s.trim();
            if (s.isEmpty()) continue; // 跳过空行（不计入 N）
            // 按 '.' 切分，手写切分避免正则开销并精确控制
            List<String> tokens = new ArrayList<>(10);
            int start = 0;
            for (int i = 0; i <= s.length(); i++) {
                if (i == s.length() || s.charAt(i) == '.') {
                    String tok = s.substring(start, i);
                    tokens.add(tok);
                    symbolSet.add(tok);
                    start = i + 1;
                }
            }
            nums.add(tokens.toArray(new String[0]));
            readCnt++;
        }

        // 若实际读到的不足 N（输入含空行或截断），按已有数量处理
        if (nums.size() != N) {
            N = nums.size();
        }
        if (N == 0) return;

        // 建立符号到 id 的映射
        List<String> idToWord = new ArrayList<>(symbolSet);
        Map<String, Integer> idMap = new HashMap<>(idToWord.size() * 2);
        for (int i = 0; i < idToWord.size(); i++) {
            idMap.put(idToWord.get(i), i);
        }
        int M = idToWord.size();

        List<Set<Integer>> adj = new ArrayList<>(M);
        for (int i = 0; i < M; i++) adj.add(new HashSet<>());
        int[] indeg = new int[M];

        // 仅长度相等时产生偏序
        for (int i = 0; i + 1 < N; i++) {
            String[] a = nums.get(i);
            String[] b = nums.get(i + 1);
            if (a.length != b.length) continue; // 较长必大，不推断
            for (int k = 0; k < a.length; k++) {
                if (!a[k].equals(b[k])) {
                    int u = idMap.get(a[k]);
                    int v = idMap.get(b[k]);
                    if (adj.get(u).add(v)) {
                        indeg[v]++;
                    }
                    break;
                }
            }
        }

        // 字典序最小的拓扑排序（Kahn + 最小堆）
        PriorityQueue<Integer> pq = new PriorityQueue<>((x, y) -> idToWord.get(x).compareTo(idToWord.get(y)));
        for (int i = 0; i < M; i++) if (indeg[i] == 0) pq.offer(i);

        List<Integer> result = new ArrayList<>(M);
        while (!pq.isEmpty()) {
            int u = pq.poll();
            result.add(u);
            for (int v : adj.get(u)) {
                if (--indeg[v] == 0) pq.offer(v);
            }
        }

        // 按题目保证无环，若有环（输入非法）则按已有结果 + 剩余字典序补充
        if (result.size() != M) {
            // 收集未入结果的节点，按字典序补齐（避免无输出）
            List<Integer> rest = new ArrayList<>();
            boolean[] inRes = new boolean[M];
            for (int x : result) inRes[x] = true;
            for (int i = 0; i < M; i++) if (!inRes[i]) rest.add(i);
            rest.sort((x, y) -> idToWord.get(x).compareTo(idToWord.get(y)));
            result.addAll(rest);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            if (i > 0) sb.append('.');
            sb.append(idToWord.get(result.get(i)));
        }
        System.out.println(sb.toString());
    }
}
