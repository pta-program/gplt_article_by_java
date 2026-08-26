import java.io.*;
import java.util.*;

/**
 * L2-050 懂蛇语
 * 关键：提取每句首字母缩写作为 key（按空格分词，取每词首字母，支持多空格）
 * 词典句映射 key -> 列表，预排序；查询时直接查表
 * 时间复杂度 O(N * L + M * L + 排序)，空间 O(N)
 */
public class Main {
    static String getKey(String s) {
        StringBuilder kb = new StringBuilder();
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (c != ' ' && (i == 0 || s.charAt(i - 1) == ' ')) {
                kb.append(c);
            }
        }
        return kb.toString();
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        if (line == null) return;
        line = line.trim();
        if (line.isEmpty()) return;
        int N = Integer.parseInt(line);
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            String s = br.readLine();
            if (s == null) s = "";
            // 保留原始行（不做 trim，以免丢失首尾空格语义；但题面句首尾无空格）
            // 去掉可能的行尾 \r
            // s 保持原样（含内部多空格）
            String key = getKey(s);
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        // 预排序
        for (List<String> lst : map.values()) {
            Collections.sort(lst);
        }
        String mLine = br.readLine();
        while (mLine != null && mLine.trim().isEmpty()) mLine = br.readLine();
        if (mLine == null) return;
        int M = Integer.parseInt(mLine.trim());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < M; i++) {
            String q = br.readLine();
            if (q == null) q = "";
            String key = getKey(q);
            List<String> lst = map.get(key);
            if (lst == null) {
                out.append(q).append('\n');
            } else if (lst.size() == 1) {
                out.append(lst.get(0)).append('\n');
            } else {
                for (int k = 0; k < lst.size(); k++) {
                    if (k > 0) out.append('|');
                    out.append(lst.get(k));
                }
                out.append('\n');
            }
        }
        System.out.print(out.toString());
    }
}
