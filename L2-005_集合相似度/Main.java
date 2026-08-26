import java.util.*;
import java.io.*;

// L2-005 集合相似度: Nc/Nt *100%
// 时间复杂度 O(N*M + K * min(|A|,|B|))
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        List<HashSet<Integer>> sets = new ArrayList<>();
        sets.add(null); // 1-indexed
        for (int i = 1; i <= N; i++) {
            // 读取集合, 可能跨行
            HashSet<Integer> s = new HashSet<>();
            // 需要读取 M + M 个数，可能跨多行
            int M = -1;
            List<Integer> tokens = new ArrayList<>();
            while (tokens.size() == 0 || (M != -1 && tokens.size() - 1 < M)) {
                // 如果已读到 M 且已够数，跳出
                if (M != -1 && tokens.size() - 1 >= M) break;
                // 否则继续读取一行
                line = br.readLine();
                while (line != null && line.trim().isEmpty()) line = br.readLine();
                if (line == null) break;
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) tokens.add(Integer.parseInt(st.nextToken()));
                if (M == -1 && tokens.size() > 0) {
                    M = tokens.get(0);
                    // 如果 tokens 已包含所有
                    if (tokens.size() - 1 >= M) break;
                }
            }
            // tokens[0]=M, 接下来 M 个
            for (int j = 1; j <= M && j < tokens.size(); j++) s.add(tokens.get(j));
            // 若跨行未读完, 继续补充（已在循环处理）
            // 但上述循环已保证读完, 若 tokens 包含多余? 不会, 因为按行补充, 可能刚好
            // 对于剩余不足的情况, 外层 while 已处理
            sets.add(s);
        }
        // 读取 K
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int K = Integer.parseInt(line.trim());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < K; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            // 保证两个编号
            while (st.countTokens() < 2) {
                String extra = br.readLine();
                if (extra != null) line += " " + extra;
                st = new StringTokenizer(line);
            }
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            HashSet<Integer> sa = sets.get(a);
            HashSet<Integer> sbset = sets.get(b);
            // 计算交集
            int nc = 0;
            if (sa.size() < sbset.size()) {
                for (int v : sa) if (sbset.contains(v)) nc++;
            } else {
                for (int v : sbset) if (sa.contains(v)) nc++;
            }
            int nt = sa.size() + sbset.size() - nc;
            double sim = nt == 0 ? 0 : (nc * 100.0 / nt);
            sb.append(String.format("%.2f%%\n", sim));
        }
        System.out.print(sb.toString());
    }
}
