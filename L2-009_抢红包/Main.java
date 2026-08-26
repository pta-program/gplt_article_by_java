import java.util.*;
import java.io.*;

// L2-009 抢红包: 统计收支并排序
// 时间复杂度 O(N log N)
public class Main {
    static class Person implements Comparable<Person> {
        int id; int cnt; long balance; // 分
        Person(int id){this.id=id;}
        public int compareTo(Person o) {
            if (balance != o.balance) return Long.compare(o.balance, balance);
            if (cnt != o.cnt) return Integer.compare(o.cnt, cnt);
            return Integer.compare(id, o.id);
        }
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        Person[] p = new Person[N+1];
        for (int i = 1; i <= N; i++) p[i] = new Person(i);

        for (int i = 1; i <= N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            // 这一行可能被分割多行? 按 K 对数可能跨行? 但一般一行完整
            // 为安全，持续读取直到满足 K*2 个数
            int K = 0;
            if (st.hasMoreTokens()) K = Integer.parseInt(st.nextToken());
            List<Integer> vals = new ArrayList<>();
            while (st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            // 若还不够 2*K，则继续读行
            while (vals.size() < 2 * K) {
                String extra = br.readLine();
                if (extra == null) break;
                StringTokenizer st2 = new StringTokenizer(extra);
                while (st2.hasMoreTokens()) vals.add(Integer.parseInt(st2.nextToken()));
            }
            long outSum = 0;
            for (int j = 0; j < K; j++) {
                int nid = vals.get(2*j);
                int amt = vals.get(2*j+1);
                outSum += amt;
                p[nid].balance += amt;
                p[nid].cnt += 1;
            }
            p[i].balance -= outSum;
        }

        List<Person> list = new ArrayList<>();
        for (int i = 1; i <= N; i++) list.add(p[i]);
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for (Person pp : list) {
            sb.append(String.format("%d %.2f\n", pp.id, pp.balance / 100.0));
        }
        System.out.print(sb.toString());
    }
}
