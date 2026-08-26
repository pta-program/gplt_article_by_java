import java.io.*;
import java.util.*;

/**
 * L2-051 满树的遍历
 * 建树统计度，判断满树：所有非叶节点度相等且等于最大度
 * 前序遍历：根 -> 孩子按编号升序
 * 时间复杂度 O(n log n)（排序孩子），空间 O(n)
 */
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int n = Integer.parseInt(line.trim());
        List<Integer>[] children = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) children[i] = new ArrayList<>();
        int root = -1;
        for (int i = 1; i <= n; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            int p = Integer.parseInt(line.trim());
            if (p == 0) root = i;
            else children[p].add(i);
        }
        int maxDeg = 0;
        for (int i = 1; i <= n; i++) maxDeg = Math.max(maxDeg, children[i].size());

        boolean isFull = true;
        if (maxDeg == 0) {
            isFull = true; // 单节点视为满
        } else {
            for (int i = 1; i <= n; i++) {
                int d = children[i].size();
                if (d != 0 && d != maxDeg) { isFull = false; break; }
            }
        }

        // 孩子按编号升序
        for (int i = 1; i <= n; i++) Collections.sort(children[i]);

        // 迭代前序
        List<Integer> order = new ArrayList<>(n);
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);
            List<Integer> ch = children[u];
            for (int i = ch.size() - 1; i >= 0; i--) stack.push(ch.get(i));
        }

        StringBuilder sb = new StringBuilder();
        sb.append(maxDeg).append(' ').append(isFull ? "yes" : "no").append('\n');
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(order.get(i));
        }
        sb.append('\n');
        System.out.print(sb.toString());
    }
}
