import java.util.*;
import java.io.*;

// L2-010 排座位: 并查集维护朋友圈，直接矩阵标记死对头
// 时间复杂度 O(N α(N) + K)
public class Main {
    static int[] parent;
    static int find(int x){ return parent[x]==x?x:(parent[x]=find(parent[x])); }
    static void union(int a,int b){ int ra=find(a),rb=find(b); if(ra!=rb) parent[rb]=ra; }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        parent = new int[N+1];
        for (int i = 1; i <= N; i++) parent[i]=i;
        boolean[][] enemy = new boolean[N+1][N+1];
        for (int i = 0; i < M; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            st = new StringTokenizer(line);
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            if (r == 1) union(a,b);
            else { enemy[a][b]=enemy[b][a]=true; }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < K; i++) {
            line = br.readLine();
            while (line != null && line.trim().isEmpty()) line = br.readLine();
            st = new StringTokenizer(line);
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            boolean isFriend = find(a)==find(b);
            boolean isEnemy = enemy[a][b];
            if (isFriend && !isEnemy) sb.append("No problem\n");
            else if (!isFriend && !isEnemy) sb.append("OK\n");
            else if (isFriend && isEnemy) sb.append("OK but...\n");
            else sb.append("No way\n");
        }
        System.out.print(sb.toString());
    }
}
