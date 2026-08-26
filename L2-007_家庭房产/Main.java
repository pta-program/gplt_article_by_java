import java.util.*;
import java.io.*;

// L2-007 家庭房产: 并查集统计家族
// 时间复杂度 O(N α(N))
public class Main {
    static int[] parent = new int[10000];
    static boolean[] exists = new boolean[10000];
    static int[] houseCnt = new int[10000];
    static int[] houseArea = new int[10000];
    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    static void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra != rb) parent[rb] = ra;
    }
    static class Family implements Comparable<Family> {
        int minId; int size; double avgSets; double avgArea;
        Family(int m,int s,double aS,double aA){minId=m;size=s;avgSets=aS;avgArea=aA;}
        public int compareTo(Family o) {
            if (Math.abs(o.avgArea - avgArea) > 1e-9) return Double.compare(o.avgArea, avgArea);
            return Integer.compare(minId, o.minId);
        }
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        for (int i = 0; i < 10000; i++) parent[i] = i;

        for (int i = 0; i < N; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            // 行可能分多次? 但按格式一行完整
            int id = Integer.parseInt(st.nextToken());
            int fa = Integer.parseInt(st.nextToken());
            int mo = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            List<Integer> children = new ArrayList<>();
            for (int j = 0; j < k; j++) children.add(Integer.parseInt(st.nextToken()));
            int sets = Integer.parseInt(st.nextToken());
            int area = Integer.parseInt(st.nextToken());

            exists[id] = true;
            houseCnt[id] += sets;
            houseArea[id] += area;
            if (fa != -1) {
                exists[fa] = true;
                union(id, fa);
            }
            if (mo != -1) {
                exists[mo] = true;
                union(id, mo);
            }
            for (int c : children) {
                exists[c] = true;
                union(id, c);
            }
        }

        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < 10000; i++) if (exists[i]) {
            int r = find(i);
            groups.computeIfAbsent(r, k -> new ArrayList<>()).add(i);
        }

        List<Family> fams = new ArrayList<>();
        for (List<Integer> g : groups.values()) {
            int min = Collections.min(g);
            int sz = g.size();
            int totalSets = 0, totalArea = 0;
            for (int id : g) { totalSets += houseCnt[id]; totalArea += houseArea[id]; }
            double avgS = totalSets * 1.0 / sz;
            double avgA = totalArea * 1.0 / sz;
            fams.add(new Family(min, sz, avgS, avgA));
        }
        Collections.sort(fams);
        StringBuilder sb = new StringBuilder();
        sb.append(fams.size()).append('\n');
        for (Family f : fams) {
            sb.append(String.format("%04d %d %.3f %.3f\n", f.minId, f.size, f.avgSets, f.avgArea));
        }
        System.out.print(sb.toString());
    }
}
