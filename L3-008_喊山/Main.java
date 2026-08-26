import java.util.*;
import java.io.*;

// L3-008 喊山: 无权图 BFS 求最远可达点，距离最远编号最小
// 时间复杂度 O(K*(N+M)), K<=10, N<=1e4
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        StringBuilder all=new StringBuilder();
        String line;
        while((line=br.readLine())!=null){
            all.append(line).append(' ');
        }
        StringTokenizer st=new StringTokenizer(all.toString());
        List<Integer> toks=new ArrayList<>();
        while(st.hasMoreTokens()) toks.add(Integer.parseInt(st.nextToken()));
        if(toks.size()<3) return;
        int n=toks.get(0), m=toks.get(1), k=toks.get(2);
        int idx=3;
        List<Integer>[] adj=new ArrayList[n+1];
        for(int i=1;i<=n;i++) adj[i]=new ArrayList<>();
        for(int i=0;i<m;i++){
            if(idx+1>=toks.size()) break;
            int u=toks.get(idx++), v=toks.get(idx++);
            if(u<1||u>n||v<1||v>n) continue;
            adj[u].add(v);
            adj[v].add(u);
        }
        List<Integer> queries=new ArrayList<>();
        for(int i=0;i<k && idx<toks.size();i++) queries.add(toks.get(idx++));
        StringBuilder out=new StringBuilder();
        int[] dist=new int[n+1];
        int[] q=new int[n+1];
        for(int qi=0; qi<queries.size(); qi++){
            int s=queries.get(qi);
            Arrays.fill(dist, -1);
            int head=0,tail=0;
            q[tail++]=s;
            dist[s]=0;
            while(head<tail){
                int u=q[head++];
                for(int v: adj[u]){
                    if(dist[v]==-1){
                        dist[v]=dist[u]+1;
                        q[tail++]=v;
                    }
                }
            }
            int maxD=-1, ans=0;
            for(int i=1;i<=n;i++){
                if(i==s) continue;
                if(dist[i]==-1) continue;
                if(dist[i] > maxD){
                    maxD=dist[i];
                    ans=i;
                }else if(dist[i]==maxD && i < ans){
                    ans=i;
                }
            }
            if(maxD==-1) out.append(0).append('\n');
            else out.append(ans).append('\n');
        }
        System.out.print(out.toString());
    }
}
