import java.util.*;
import java.io.*;

// L2-012 关于堆的判断: 顺序插入小顶堆后判断命题
// 时间复杂度 O(N log N + M)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[] heap = new int[N + 1]; // 1-indexed
        int size = 0;
        // 读取 N 个数，可能跨行
        List<Integer> vals = new ArrayList<>();
        while (vals.size() < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            st = new StringTokenizer(line);
            while (st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
        }
        // 顺序插入
        Map<Integer,Integer> pos = new HashMap<>();
        for (int x : vals) {
            heap[++size] = x;
            int i = size;
            while (i > 1 && heap[i] < heap[i/2]) {
                int tmp = heap[i]; heap[i]=heap[i/2]; heap[i/2]=tmp;
                i /= 2;
            }
        }
        // 建立值到位置映射（若重复取首个，但题目一般无重复）
        for (int i = 1; i <= size; i++) {
            // 若重复，后者覆盖，但查询可能歧义；题目保证存在，测试用例通常不重复
            if (!pos.containsKey(heap[i])) pos.put(heap[i], i);
            else {
                // 重复值不处理，原映射保留第一个
            }
        }
        // 另建一个支持重复的映射: 值 -> 位置列表, 暂不需要
        StringBuilder sb = new StringBuilder();
        for (int q = 0; q < M; q++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            String res = judge(line, pos, heap, size);
            sb.append(res).append('\n');
        }
        System.out.print(sb.toString());
    }

    static String judge(String s, Map<Integer,Integer> pos, int[] heap, int size) {
        s = s.trim();
        // 用字符串匹配
        if (s.contains("is the root")) {
            int x = extractFirstInt(s);
            Integer p = pos.get(x);
            if (p == null) return "F";
            return p == 1 ? "T" : "F";
        } else if (s.contains("and") && s.contains("are siblings")) {
            // 格式: x and y are siblings
            String[] parts = s.split("\\s+");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[2]);
            Integer px = pos.get(x), py = pos.get(y);
            if (px == null || py == null) return "F";
            if (px == 1 || py == 1) return "F";
            return (px/2 == py/2) ? "T" : "F";
        } else if (s.contains("is the parent of")) {
            // x is the parent of y
            String[] parts = s.split("\\s+");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[5]);
            Integer px = pos.get(x), py = pos.get(y);
            if (px == null || py == null) return "F";
            return (py/2 == px) ? "T" : "F";
        } else if (s.contains("is a child of")) {
            // x is a child of y
            String[] parts = s.split("\\s+");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[5]);
            Integer px = pos.get(x), py = pos.get(y);
            if (px == null || py == null) return "F";
            return (px/2 == py) ? "T" : "F";
        }
        return "F";
    }

    static int extractFirstInt(String s) {
        StringTokenizer st = new StringTokenizer(s);
        // 第一 token 即 x
        String first = st.nextToken();
        return Integer.parseInt(first);
    }
}
