import java.util.*;
import java.io.*;

// L2-002 链表去重: 按绝对值去重，保留首次出现，删除其余
// 时间复杂度 O(N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        String headStr = st.nextToken();
        int head = headStr.equals("-1") ? -1 : Integer.parseInt(headStr);
        int N = Integer.parseInt(st.nextToken());

        int MAX = 100000;
        int[] data = new int[MAX];
        int[] nxt = new int[MAX];
        boolean[] exist = new boolean[MAX];
        Arrays.fill(nxt, -1);

        for (int i = 0; i < N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            st = new StringTokenizer(line);
            String addrStr = st.nextToken();
            int addr = Integer.parseInt(addrStr);
            int key = Integer.parseInt(st.nextToken());
            String nextStr = st.nextToken();
            int next = nextStr.equals("-1") ? -1 : Integer.parseInt(nextStr);
            data[addr] = key;
            nxt[addr] = next;
            exist[addr] = true;
        }

        Set<Integer> seen = new HashSet<>();
        List<Integer> keep = new ArrayList<>();
        List<Integer> removed = new ArrayList<>();

        int cur = head;
        // 防止非法环，计数安全
        int steps = 0;
        while (cur != -1 && steps <= N + 5) {
            if (cur < 0 || cur >= MAX || !exist[cur]) break;
            int abs = Math.abs(data[cur]);
            if (!seen.contains(abs)) {
                seen.add(abs);
                keep.add(cur);
            } else {
                removed.add(cur);
            }
            cur = nxt[cur];
            steps++;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keep.size(); i++) {
            int addr = keep.get(i);
            int k = data[addr];
            int nextAddr = (i + 1 < keep.size()) ? keep.get(i + 1) : -1;
            sb.append(String.format("%05d %d %s", addr, k, nextAddr == -1 ? "-1" : String.format("%05d", nextAddr)));
            sb.append('\n');
        }
        for (int i = 0; i < removed.size(); i++) {
            int addr = removed.get(i);
            int k = data[addr];
            int nextAddr = (i + 1 < removed.size()) ? removed.get(i + 1) : -1;
            sb.append(String.format("%05d %d %s", addr, k, nextAddr == -1 ? "-1" : String.format("%05d", nextAddr)));
            if (i != removed.size() - 1) sb.append('\n');
            else if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
                // keep trailing? 不要多余换行
            }
        }
        // 如果 keep 为空但有 removed, 避免开头空行
        String out = sb.toString();
        // 去掉末尾换行由 println 统一? 直接打印
        System.out.print(out);
        // 若有输出且末尾没有换行，补一个? 题目要求每结点一行，已包含
        if (!out.isEmpty() && !out.endsWith("\n")) System.out.println();
        else if (!out.isEmpty() && out.endsWith("\n")) {
            // trim last newline's extra print already done
        }
    }
}
