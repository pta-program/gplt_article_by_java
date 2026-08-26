import java.util.*;
import java.io.*;

// L3-007 天梯地图: 双准则最短路
// 时间最优（次短距离）与距离最优（次少节点）两遍 Dijkstra
// 时间复杂度 O((N+M) log N)
public class Main {
    static class Edge {
        int to, len, time;
        Edge(int to,int len,int time){this.to=to;this.len=len;this.time=time;}
    }
    static class StateTime implements Comparable<StateTime>{
        int u; int t; int d;
        StateTime(int u,int t,int d){this.u=u;this.t=t;this.d=d;}
        public int compareTo(StateTime o){
            if(t!=o.t) return Integer.compare(t,o.t);
            return Integer.compare(d,o.d);
        }
    }
    static class StateDist implements Comparable<StateDist>{
        int u; int d; int cnt;
        StateDist(int u,int d,int cnt){this.u=u;this.d=d;this.cnt=cnt;}
        public int compareTo(StateDist o){
            if(d!=o.d) return Integer.compare(d,o.d);
            return Integer.compare(cnt,o.cnt);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        // 读取所有整数及图
        List<Integer> nums=new ArrayList<>();
        // 需要保留图结构，先按行解析
        List<String> lines=new ArrayList<>();
        while((line=br.readLine())!=null){
            if(line.trim().isEmpty()) continue;
            lines.add(line);
        }
        if(lines.isEmpty()) return;
        // 第一行 N M
        StringTokenizer st=new StringTokenizer(lines.get(0));
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        List<Edge>[] adj=new ArrayList[N];
        for(int i=0;i<N;i++) adj[i]=new ArrayList<>();
        int idx=1;
        for(int i=0;i<M;i++){
            if(idx>=lines.size()) break;
            st=new StringTokenizer(lines.get(idx++));
            // 可能一行被拆开，但按题目每边一行完整
            while(st.countTokens()<4 && idx<lines.size()){
                // 合并下一行（容错）
                String extra=lines.get(idx++);
                lines.set(idx-1, lines.get(idx-1)+" "+extra);
                st=new StringTokenizer(lines.get(idx-1));
            }
            int v1=Integer.parseInt(st.nextToken());
            int v2=Integer.parseInt(st.nextToken());
            int one=Integer.parseInt(st.nextToken());
            int len=Integer.parseInt(st.nextToken());
            int tim=Integer.parseInt(st.nextToken());
            adj[v1].add(new Edge(v2,len,tim));
            if(one==0) adj[v2].add(new Edge(v1,len,tim));
        }
        int S=0,D=0;
        if(idx<lines.size()){
            st=new StringTokenizer(lines.get(idx));
            if(st.countTokens()>=2){
                S=Integer.parseInt(st.nextToken());
                D=Integer.parseInt(st.nextToken());
            } else if(st.countTokens()==1){
                S=Integer.parseInt(st.nextToken());
                if(idx+1<lines.size()){
                    st=new StringTokenizer(lines.get(idx+1));
                    D=Integer.parseInt(st.nextToken());
                }
            }
        }
        // Dijkstra for Time
        final int INF=Integer.MAX_VALUE/4;
        int[] distT=new int[N], distLenT=new int[N], preT=new int[N];
        Arrays.fill(distT, INF); Arrays.fill(distLenT, INF); Arrays.fill(preT, -1);
        distT[S]=0; distLenT[S]=0;
        PriorityQueue<StateTime> pqT=new PriorityQueue<>();
        pqT.offer(new StateTime(S,0,0));
        boolean[] vis=new boolean[N];
        while(!pqT.isEmpty()){
            StateTime cur=pqT.poll();
            int u=cur.u;
            if(cur.t!=distT[u] || cur.d!=distLenT[u]) continue;
            for(Edge e: adj[u]){
                int v=e.to;
                int nt=cur.t + e.time;
                int nd=cur.d + e.len;
                if(nt < distT[v] || (nt==distT[v] && nd < distLenT[v])){
                    distT[v]=nt; distLenT[v]=nd; preT[v]=u;
                    pqT.offer(new StateTime(v, nt, nd));
                }
            }
        }
        // Dijkstra for Distance
        int[] distD=new int[N], cntD=new int[N], preD=new int[N];
        Arrays.fill(distD, INF); Arrays.fill(cntD, INF); Arrays.fill(preD, -1);
        distD[S]=0; cntD[S]=1;
        PriorityQueue<StateDist> pqD=new PriorityQueue<>();
        pqD.offer(new StateDist(S,0,1));
        while(!pqD.isEmpty()){
            StateDist cur=pqD.poll();
            int u=cur.u;
            if(cur.d!=distD[u] || cur.cnt!=cntD[u]) continue;
            for(Edge e: adj[u]){
                int v=e.to;
                int nd=cur.d + e.len;
                int nc=cur.cnt + 1;
                if(nd < distD[v] || (nd==distD[v] && nc < cntD[v])){
                    distD[v]=nd; cntD[v]=nc; preD[v]=u;
                    pqD.offer(new StateDist(v, nd, nc));
                }
            }
        }
        // 重建路径
        List<Integer> pathT=buildPath(preT,S,D);
        List<Integer> pathD=buildPath(preD,S,D);
        boolean same = pathT.equals(pathD);
        if(same){
            System.out.print("Time = "+distT[D]+"; Distance = "+distD[D]+": ");
            printPath(pathT);
        }else{
            System.out.print("Time = "+distT[D]+": ");
            printPath(pathT);
            System.out.print("Distance = "+distD[D]+": ");
            printPath(pathD);
        }
    }
    static List<Integer> buildPath(int[] pre,int s,int t){
        List<Integer> p=new ArrayList<>();
        int cur=t;
        while(cur!=-1){
            p.add(cur);
            if(cur==s) break;
            cur=pre[cur];
        }
        Collections.reverse(p);
        return p;
    }
    static void printPath(List<Integer> p){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<p.size();i++){
            if(i>0) sb.append(" => ");
            sb.append(p.get(i));
        }
        System.out.println(sb.toString());
    }
}
