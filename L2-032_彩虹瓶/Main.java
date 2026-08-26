import java.util.*;
import java.io.*;

// L2-032 彩虹瓶 - 栈模拟彩虹瓶发货
// 时间复杂度 O(K*N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        StringBuilder out = new StringBuilder();
        for (int t = 0; t < K; t++) {
            // 读取 N 个数的排列，可能跨行
            List<Integer> seq = new ArrayList<>();
            while (seq.size() < N) {
                line = br.readLine();
                if (line == null) break;
                if (line.trim().isEmpty()) continue;
                StringTokenizer st2 = new StringTokenizer(line);
                while (st2.hasMoreTokens() && seq.size() < N) {
                    seq.add(Integer.parseInt(st2.nextToken()));
                }
            }
            boolean ok = canFinish(seq, N, M);
            out.append(ok ? "YES\n" : "NO\n");
        }
        System.out.print(out.toString());
    }

    static boolean canFinish(List<Integer> a, int N, int M) {
        Deque<Integer> stack = new ArrayDeque<>();
        int expect = 1;
        for (int c : a) {
            if (c == expect) {
                expect++;
                while (!stack.isEmpty() && stack.peek() == expect) {
                    stack.pop();
                    expect++;
                }
            } else {
                // 需要入栈
                if (stack.size() >= M) return false; // 容量已满无法入栈
                // 还有一个判断：如果栈顶已经可以出栈但c不等于expect，其实按流程c已经作为新到货物判断
                // 但若c != expect，应该入栈；但如果入栈后超过容量已处理
                stack.push(c);
                // 注意：如果c入栈后，其实不存在立即出栈的情况，因为c != expect
                // 但为了严谨，检查是否栈顶等于expect的情况不会发生因为c != expect
            }
            // 额外的容量检查：如果栈大小超过M直接失败
            if (stack.size() > M) return false;
        }
        while (!stack.isEmpty()) {
            if (stack.peek() == expect) {
                stack.pop();
                expect++;
            } else {
                return false;
            }
        }
        return expect == N + 1;
    }
}
