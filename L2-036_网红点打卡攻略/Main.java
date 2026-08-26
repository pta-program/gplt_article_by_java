import java.util.*;
import java.io.*;

// L2-036 网红点打卡攻略 - 图论+TSP路径验证
// 时间复杂度 O(N^2*K + Floyd? 本题直接查边)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        int SZ=N+1; // 包含0
        long INF=Long.MAX_VALUE/4;
        long[][] g=new long[SZ][SZ];
        for(int i=0;i<SZ;i++) Arrays.fill(g[i], INF);
        for(int i=0;i<SZ;i++) g[i][i]=0;
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            int u=Integer.parseInt(st.nextToken());
            int v=Integer.parseInt(st.nextToken());
            long w=Long.parseLong(st.nextToken());
            if(w < g[u][v]){
                g[u][v]=w;
                g[v][u]=w;
            }
        }
        line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        int K=Integer.parseInt(line.trim());
        int validCount=0;
        long bestCost=Long.MAX_VALUE;
        int bestIdx=-1;
        for(int k=1;k<=K;k++){
            // 读取一条攻略，可能跨行
            List<Integer> vals=new ArrayList<>();
            // 需要读到 n+1 个数（n和V1..Vn）
            // 先读一行
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            // 如果数量不足，继续读
            int n=vals.size()>0? vals.get(0):0;
            while(vals.size() < n+1){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            }
            // 若vals size可能因跨行读多了下一个攻略？但每条攻略的n固定，vals正好n+1
            // 若多读了，需要截断并把多余放回？简化：假设每条攻略在一行内
            // 上述while已经保证，接下来若vals.size()>n+1说明把下一行也读了，但不会发生因为我们在内层while后不再继续
            // 为保险，做调整
            if(vals.size()> n+1){
                // 多余的属于下一条，但本题假定每条一行，不会
            }
            if(n != N){
                // 未访问全部点，无效
                continue;
            }
            // 检查是否恰好包含1..N各一次
            boolean[] seen=new boolean[N+1];
            boolean ok=true;
            List<Integer> path=new ArrayList<>();
            for(int i=1;i<=n;i++){
                int v=vals.get(i);
                path.add(v);
                if(v<1||v> N) { ok=false; break; }
                if(seen[v]) { ok=false; break; }
                seen[v]=true;
            }
            if(!ok) continue;
            for(int i=1;i<=N;i++) if(!seen[i]) ok=false;
            if(!ok) continue;
            // 检查路径连通性并计算费用
            long cost=0;
            int cur=0;
            boolean connected=true;
            for(int v: path){
                if(g[cur][v] >= INF){ connected=false; break; }
                cost+=g[cur][v];
                cur=v;
            }
            if(!connected) continue;
            if(g[cur][0] >= INF) continue;
            cost+=g[cur][0];
            validCount++;
            if(cost < bestCost){
                bestCost=cost;
                bestIdx=k;
            }
        }
        System.out.println(validCount);
        System.out.println(bestIdx+" "+bestCost);
    }
}
