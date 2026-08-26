import java.util.*;
import java.io.*;

// L2-020 功夫传人: 树上DFS计算功力
// 时间复杂度 O(N)
public class Main {
    static class Node{
        List<Integer> children=new ArrayList<>();
        boolean isLeaf=false;
        int mult=0;
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        double Z=Double.parseDouble(st.nextToken());
        double r=Double.parseDouble(st.nextToken());
        double factor = 1 - r/100.0;
        Node[] nodes=new Node[N];
        for(int i=0;i<N;i++) nodes[i]=new Node();
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            if(!st.hasMoreTokens()){ i--; continue; }
            int K=Integer.parseInt(st.nextToken());
            if(K==0){
                nodes[i].isLeaf=true;
                if(st.hasMoreTokens()) nodes[i].mult=Integer.parseInt(st.nextToken());
                else{
                    // 可能跨行
                    line=br.readLine();
                    if(line!=null) nodes[i].mult=Integer.parseInt(line.trim());
                }
            }else{
                List<Integer> list=new ArrayList<>();
                while(list.size()<K){
                    while(!st.hasMoreTokens()){
                        line=br.readLine();
                        if(line==null) break;
                        st=new StringTokenizer(line);
                    }
                    if(st.hasMoreTokens()) list.add(Integer.parseInt(st.nextToken()));
                }
                nodes[i].children=list;
            }
        }
        // BFS
        double total=0;
        Queue<Integer> q=new ArrayDeque<>();
        Queue<Double> pw=new ArrayDeque<>();
        q.offer(0); pw.offer(Z);
        // 为避免递归栈, 用队列
        boolean[] visited=new boolean[N];
        visited[0]=true;
        // 需要层次因子: 子节点功力 = 父功力 * factor
        // 但根的功力Z, 子代依次衰减
        // BFS中每层衰减一次，所以子功力 = 父功力*factor
        while(!q.isEmpty()){
            int u=q.poll();
            double power=pw.poll();
            Node nd=nodes[u];
            if(nd.isLeaf){
                total += power * nd.mult;
            }else{
                double childPower = power * factor;
                for(int v: nd.children){
                    if(!visited[v]){
                        visited[v]=true;
                        q.offer(v);
                        pw.offer(childPower);
                    }
                }
            }
        }
        // 也可能有未访问得道者? 但树保证连通
        System.out.println((long)total); // 截断整数
    }
}
