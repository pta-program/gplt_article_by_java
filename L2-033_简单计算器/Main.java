import java.util.*;
import java.io.*;

// L2-033 简单计算器 - 双栈逆序计算
// 时间复杂度 O(N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        // 读 N 个数字
        List<Integer> nums = new ArrayList<>();
        while (nums.size() < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens() && nums.size() < N) {
                nums.add(Integer.parseInt(st.nextToken()));
            }
        }
        // 读 N-1 个运算符
        List<String> ops = new ArrayList<>();
        while (ops.size() < N - 1) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens() && ops.size() < N - 1) {
                ops.add(st.nextToken());
            }
        }
        Deque<Integer> s1 = new ArrayDeque<>();
        Deque<String> s2 = new ArrayDeque<>();
        for (int v : nums) s1.push(v);
        for (String op : ops) s2.push(op);

        while (!s1.isEmpty() && !s2.isEmpty()) {
            // 至少需要 2 个数字和 1 个运算符
            if (s1.size() < 2) break;
            int n1 = s1.pop();
            int n2 = s1.pop();
            String op = s2.pop();
            if (op.equals("/")) {
                if (n1 == 0) {
                    System.out.println("ERROR: " + n2 + "/0");
                    return;
                }
                int res = n2 / n1;
                s1.push(res);
            } else if (op.equals("+")) {
                s1.push(n2 + n1);
            } else if (op.equals("-")) {
                s1.push(n2 - n1);
            } else if (op.equals("*")) {
                s1.push(n2 * n1);
            }
        }
        if (!s1.isEmpty()) {
            System.out.println(s1.peek());
        }
    }
}
