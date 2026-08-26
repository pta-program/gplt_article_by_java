import java.util.*;
import java.io.*;

// L2-013 红色警报: 逐个攻占城市判断连通分量变化
// 时间复杂度 O(K*(N+M))
public class Main {
    static int N, M;
    static List<Integer>[] adj;
    static boolean[] lost;
    static int countComponents() {
        boolean[] vis = new boolean[N];
        int comp = 0;
        for (int i = 0; i < N; i++) if (!lost[i] && !vis[i]) {
            comp++;
            // BFS
            Queue<Integer> q = new LinkedList<>();
            q.offer(i); vis[i]=true;
            while(!q.isEmpty()){
                int u=q.poll();
                for(int v: adj[u]) if(!lost[v] && !vis[v]) {vis[v]=true; q.offer(v);}
            }
        }
        return comp;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        N=Integer.parseInt(st.nextToken());
        M=Integer.parseInt(st.nextToken());
        adj=new ArrayList[N];
        for(int i=0;i<N;i++) adj[i]=new ArrayList<>();
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            st=new StringTokenizer(line);
            int a=Integer.parseInt(st.nextToken());
            int b=Integer.parseInt(st.nextToken());
            if(a<0||a>=N||b<0||b>=N) continue;
            adj[a].add(b); adj[b].add(a);
        }
        // 读取 K 和攻击序列，可能跨行
        // 剩余输入的 token
        List<Integer> tokens=new ArrayList<>();
        while((line=br.readLine())!=null){
            if(line.trim().isEmpty()) continue;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) tokens.add(Integer.parseInt(st.nextToken()));
        }
        if(tokens.isEmpty()) return;
        int K=tokens.get(0);
        List<Integer> attacks=new ArrayList<>();
        for(int i=1;i<=K && i<tokens.size();i++) attacks.add(tokens.get(i));
        // 若 tokens 数量不足，说明输入异常，但一般足够

        lost=new boolean[N];
        int prevComp = countComponents();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<K;i++){
            int city=attacks.get(i);
            lost[city]=true;
            int curComp = countComponents();
            // 判断是否需要红警: curComp > prevComp
            if(curComp > prevComp){
                sb.append(String.format("Red Alert: City %d is lost!\n", city));
            } else {
                sb.append(String.format("City %d is lost.\n", city));
            }
            prevComp = curComp;
            if(i==K-1 || curComp==0){
                // 检查是否失去最后一个城市? 当所有城市 lost 后输出 Game Over
                boolean allLost=true;
                for(boolean b: lost) if(!b){allLost=false;break;}
                if(allLost && i==K-1){
                    sb.append("Game Over.\n");
                }
            }
        }
        // 如果 K 攻占后还有未攻占城市但所有城市已 lost? 已处理
        // 按题目: 若该国失去最后一个城市则增加一行 Game Over.
        // 我们的判断基于 lost 全 true
        // 若 attacks 长度 < N 但已全部 lost? 已包含
        System.out.print(sb.toString());
    }
}
