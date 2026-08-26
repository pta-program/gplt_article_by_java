import java.util.*;
import java.io.*;

/**
 * L3-028 森森旅游
 * 题意：有向图，边有现金费用c和旅游金费用d，点有汇率a_i。
 * 从1到n，可在途中某点v将剩余现金全部兑换为旅游金（汇率a_v），之前用现金支付，之后用旅游金支付。
 * 求每次汇率更新后的最少初始现金。
 * 解法：
 *  distC[v] = 1到v的最小现金花费（以c为权）
 *  distD[v] = v到n的最小旅游金花费（以d为权，反图Dijkstra）
 *  则在v兑换所需现金 = distC[v] + ceil(distD[v]/a_v)
 *  答案 = min_v 该值。也包含全现金方案(v=n, distD=0)。
 *  distC/distD 静态，汇率a动态。维护每个v的val，支持点更新 + 全局最小。
 *  使用线段树/优先队列。复杂度 O((n+m)log n + q log n)
 */
public class Main {
    static class Edge{
        int to; long w;
        Edge(int to,long w){this.to=to;this.w=w;}
    }
    static long INF = (long)4e18;
    static long[] dijkstra(int n, List<Edge>[] g, int src){
        long[] dist=new long[n+1];
        Arrays.fill(dist, INF);
        dist[src]=0;
        PriorityQueue<long[]> pq=new PriorityQueue<>(Comparator.comparingLong(a->a[0]));
        pq.offer(new long[]{0, src});
        boolean[] vis=new boolean[n+1];
        while(!pq.isEmpty()){
            long[] cur=pq.poll();
            long d=cur[0]; int u=(int)cur[1];
            if(vis[u]) continue;
            vis[u]=true;
            if(d!=dist[u]) continue;
            for(Edge e: g[u]){
                int v=e.to;
                long nd=d + e.w;
                if(nd < dist[v]){
                    dist[v]=nd;
                    pq.offer(new long[]{nd, v});
                }
            }
        }
        return dist;
    }
    // 线段树求最小
    static class SegTree{
        int n; long[] t;
        SegTree(long[] arr){
            n=arr.length-1; // 1-indexed
            t=new long[4*n];
            build(1,1,n,arr);
        }
        void build(int v,int l,int r,long[] arr){
            if(l==r){ t[v]=arr[l]; return; }
            int m=(l+r)/2;
            build(v*2,l,m,arr); build(v*2+1,m+1,r,arr);
            t[v]=Math.min(t[v*2], t[v*2+1]);
        }
        void update(int idx,long val){ update(1,1,n,idx,val); }
        void update(int v,int l,int r,int idx,long val){
            if(l==r){ t[v]=val; return; }
            int m=(l+r)/2;
            if(idx<=m) update(v*2,l,m,idx,val);
            else update(v*2+1,m+1,r,idx,val);
            t[v]=Math.min(t[v*2], t[v*2+1]);
        }
        long queryMin(){ return t[1]; }
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        StringTokenizer st;
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        st=new StringTokenizer(line);
        int n=Integer.parseInt(st.nextToken());
        int m=Integer.parseInt(st.nextToken());
        int q=Integer.parseInt(st.nextToken());
        List<Edge>[] gC=new ArrayList[n+1];
        List<Edge>[] gDRev=new ArrayList[n+1];
        for(int i=1;i<=n;i++){ gC[i]=new ArrayList<>(); gDRev[i]=new ArrayList<>(); }
        for(int i=0;i<m;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            st=new StringTokenizer(line);
            int u=Integer.parseInt(st.nextToken());
            int v=Integer.parseInt(st.nextToken());
            long c=Long.parseLong(st.nextToken());
            long d=Long.parseLong(st.nextToken());
            gC[u].add(new Edge(v,c));
            gDRev[v].add(new Edge(u,d)); // 反图：从n出发沿反向边
        }
        long[] a=new long[n+1];
        // 读取a数组，可能跨多行
        int cnt=0;
        while(cnt < n){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens() && cnt < n){
                a[++cnt]=Long.parseLong(st.nextToken());
            }
        }
        long[] distC=dijkstra(n, gC, 1);
        long[] distD=dijkstra(n, gDRev, n);
        long[] val=new long[n+1];
        for(int i=1;i<=n;i++){
            if(distC[i]>=INF/2 || distD[i]>=INF/2){
                // 若dist为INF，是否仍可作为兑换点？若distD INF则无法从该点到n，需 INF
                // 若distC INF则无法到达该点，跳过
                val[i]=INF;
            }else{
                long needD=distD[i];
                if(needD==0) val[i]=distC[i];
                else{
                    long ceil = (needD + a[i] -1)/ a[i];
                    // 防止溢出：distC up to 1e14, ceil up to 1e14
                    if(distC[i] > INF - ceil) val[i]=INF;
                    else val[i]=distC[i] + ceil;
                }
            }
        }
        SegTree seg=new SegTree(val);
        StringBuilder out=new StringBuilder();
        for(int i=0;i<q;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            int x=Integer.parseInt(st.nextToken());
            long na=Long.parseLong(st.nextToken());
            a[x]=na;
            long cur;
            if(distC[x]>=INF/2 || distD[x]>=INF/2) cur=INF;
            else{
                if(distD[x]==0) cur=distC[x];
                else{
                    long ceil=(distD[x] + na -1)/ na;
                    cur=distC[x] + ceil;
                }
            }
            seg.update(x, cur);
            long ans=seg.queryMin();
            out.append(ans).append("\n");
        }
        System.out.print(out.toString());
    }
}
