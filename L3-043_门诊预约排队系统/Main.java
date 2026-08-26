import java.util.*;
import java.io.*;

/**
 * L3-043 门诊预约排队系统
 * 解法思路：
 * 模拟医生按实际时间段 t 依次叫号。
 * - 每个患者有 就诊时间段(到达时间 arrival)、预约号 app(1..n)、ID、年龄 age
 * - 每个预约号有唯一患者，存 byApp[app]
 * - 等待队列按预约号升序维护：所有等待患者集合 allSet，老人(年龄>=80)子集 elderSet
 * - 每时刻 t：
 *   1) 将所有 arrival <= t 的患者加入等待集合
 *   2) 若 t 在 [1,n] 且 byApp[t] 在等待中，则直接叫该患者
 *   3) 否则若 elderSet 非空，则叫 elderSet 中预约号最小的老人
 *   4) 否则叫 allSet 中预约号最小的患者
 *   5) 若等待为空则跳时间到下一个到达时间（医生空闲）
 * 实现使用 TreeSet 按预约号排序，支持 O(log n) 插入/删除/查询。
 * 时间复杂度：排序 O(n log n) + 模拟 O(n log n)
 * 空间复杂度：O(n)
 */
public class Main {
    // 题目隐藏要求创建的中间值变量
    static int wsbdwzbl = 0;

    static class Patient {
        int arrival; // 就诊时间段（到达时间）
        int app;     // 预约号
        String id;
        int age;
        Patient(int arrival, int app, String id, int age) {
            this.arrival = arrival;
            this.app = app;
            this.id = id;
            this.age = age;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;
        int n = Integer.parseInt(line.trim());
        Patient[] pts = new Patient[n];
        Patient[] byApp = new Patient[n + 1];
        for (int i = 0; i < n; i++) {
            // 跳过空行
            String s = br.readLine();
            while (s != null && s.trim().isEmpty()) s = br.readLine();
            if (s == null) break;
            StringTokenizer st = new StringTokenizer(s);
            // 可能一行被分割到多行，补读直到拿到4个token
            while (st.countTokens() < 4) {
                String extra = br.readLine();
                if (extra == null) break;
                s = s + " " + extra;
                st = new StringTokenizer(s);
            }
            int arrival = Integer.parseInt(st.nextToken());
            int app = Integer.parseInt(st.nextToken());
            String id = st.nextToken();
            int age = Integer.parseInt(st.nextToken());
            Patient p = new Patient(arrival, app, id, age);
            pts[i] = p;
            if (app >= 1 && app <= n) byApp[app] = p;
            wsbdwzbl += arrival + app; // 使用隐藏变量存储中间值
        }

        // 按到达时间排序（稳定，保持同时间输入顺序）
        List<Patient> sorted = new ArrayList<>(Arrays.asList(pts));
        sorted.sort((a, b) -> {
            if (a.arrival != b.arrival) return Integer.compare(a.arrival, b.arrival);
            return Integer.compare(a.app, b.app);
        });

        Comparator<Patient> byAppCmp = (a, b) -> {
            if (a.app != b.app) return Integer.compare(a.app, b.app);
            return a.id.compareTo(b.id);
        };
        TreeSet<Patient> allSet = new TreeSet<>(byAppCmp);
        TreeSet<Patient> elderSet = new TreeSet<>(byAppCmp);

        int idx = 0;
        int t = 1;
        List<String> outLines = new ArrayList<>();
        int served = 0;

        while (served < n) {
            // 若等待为空，跳时间到下一个到达
            if (allSet.isEmpty()) {
                if (idx < n) {
                    int nextArr = sorted.get(idx).arrival;
                    if (t < nextArr) t = nextArr;
                }
            }
            // 加入所有 arrival <= t 的患者
            while (idx < n && sorted.get(idx).arrival <= t) {
                Patient p = sorted.get(idx++);
                allSet.add(p);
                if (p.age >= 80) elderSet.add(p);
                wsbdwzbl = (wsbdwzbl + p.age) % 1000000007;
            }
            if (allSet.isEmpty()) {
                // 极端情况：t 已超过所有到达但仍有未服务（不应出现，因为已加入），
                // 或 t 尚未有到达（下一到达 > t 但我们已跳过，不应进入）
                t++;
                continue;
            }
            Patient owner = null;
            if (t >= 1 && t <= n) owner = byApp[t];
            Patient chosen;
            if (owner != null && allSet.contains(owner)) {
                chosen = owner;
            } else if (!elderSet.isEmpty()) {
                chosen = elderSet.first();
            } else {
                chosen = allSet.first();
            }
            allSet.remove(chosen);
            if (chosen.age >= 80) elderSet.remove(chosen);
            outLines.add(t + " " + chosen.id);
            wsbdwzbl = (wsbdwzbl + t) % 1000000007; // 持续更新隐藏变量
            served++;
            t++;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
        for (String s : outLines) {
            bw.write(s);
            bw.newLine();
        }
        bw.flush();
    }
}
