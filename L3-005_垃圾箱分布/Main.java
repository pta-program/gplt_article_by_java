import java.util.*;
import java.io.*;

// L3-005 垃圾箱分布: 对每个候选垃圾箱做 Dijkstra
// 评价指标：到居民点最小距离最大化，阈值 DS 过滤，次级最小平均距离
// 时间复杂度 O(M * ( (N+M) log(N+M) + K ) ), N<=1000,M<=10
public class Main {
    static class Edge {
        int to; int w;
        Edge(int to, int w){this.to=to;this.w=w;}
    }
    static class Node implements Comparable<Node>{
        int id; int dist;
        Node(int id,int dist){this.id=id;this.dist=dist;}
        public int compareTo(Node o){return Integer.compare(dist, o.dist);}
    }
    static int parseNode(String s, int N){
        s=s.trim();
        if (s.charAt(0)=='G' || s.charAt(0)=='g'){
            int num=Integer.parseInt(s.substring(1));
            return N + num;
        }else return Integer.parseInt(s);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st = new StringTokenizer(line);
        List<Integer> head = new ArrayList<>();
        while(st.hasMoreTokens()) head.add(Integer.parseInt(st.nextToken()));
        while(head.size()<4){
            line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) head.add(Integer.parseInt(st.nextToken()));
        }
        int N=head.get(0), M=head.get(1), K=head.get(2), DS=head.get(3);
        int V = N + M + 1; // 1-indexed, 0 unused
        List<Edge>[] adj = new ArrayList[V];
        for(int i=0;i<V;i++) adj[i]=new ArrayList<>();
        for(int i=0;i<K;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.countTokens()<3){
                String extra=br.readLine();
                if(extra==null) break;
                line+=" "+extra;
                st=new StringTokenizer(line);
            }
            String p1=st.nextToken(), p2=st.nextToken();
            int w=Integer.parseInt(st.nextToken());
            int u=parseNode(p1,N), v=parseNode(p2,N);
            adj[u].add(new Edge(v,w));
            adj[v].add(new Edge(u,w));
        }
        final int INF = Integer.MAX_VALUE/4;
        int bestIdx=-1;
        double bestMinDist=-1;
        double bestAvg=Double.MAX_VALUE;
        int bestMinInt=-1;
        double bestAvgVal=0;

        for(int g=1; g<=M; g++){
            int src = N+g;
            int[] dist=new int[V];
            Arrays.fill(dist, INF);
            dist[src]=0;
            PriorityQueue<Node> pq=new PriorityQueue<>();
            pq.offer(new Node(src,0));
            boolean[] vis=new boolean[V];
            while(!pq.isEmpty()){
                Node cur=pq.poll();
                int u=cur.id;
                if(vis[u]) continue;
                vis[u]=true;
                for(Edge e: adj[u]){
                    int v=e.to;
                    if(dist[u]+e.w < dist[v]){
                        dist[v]=dist[u]+e.w;
                        pq.offer(new Node(v, dist[v]));
                    }
                }
            }
            // 评估
            boolean ok=true;
            int minDist=Integer.MAX_VALUE;
            long sum=0;
            for(int i=1;i<=N;i++){
                if(dist[i]==INF || dist[i]>DS){ ok=false; break; }
                if(dist[i]<minDist) minDist=dist[i];
                sum+=dist[i];
            }
            if(!ok) continue;
            double avg = sum * 1.0 / N;
            // 选择最大 minDist, 次小 avg, 次小编号
            if(bestIdx==-1 || minDist > bestMinInt || (minDist==bestMinInt && avg < bestAvg -1e-9) ){
                bestIdx=g;
                bestMinInt=minDist;
                bestMinDist=minDist;
                bestAvg=avg;
                bestAvgVal=avg;
            }
        }
        if(bestIdx==-1){
            System.out.println("No Solution");
        }else{
            System.out.println("G"+bestIdx);
            System.out.printf("%.1f %.1f\n", bestMinDist, bestAvgVal);
        }
    }
}
