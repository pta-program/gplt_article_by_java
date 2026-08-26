import java.util.*;
import java.io.*;

// L2-043 龙龙送外卖 - 树上增量 Steiner 最短路
// 答案 = 2*E - maxDist, E为包含根与所有已送地址的最小子树边数
// 时间复杂度 O(N + M α(N))
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        int[] parent=new int[N+1];
        List<Integer>[] children=new ArrayList[N+1];
        for(int i=1;i<=N;i++) children[i]=new ArrayList<>();
        int root=1;
        // 读父节点数组
        int filled=0;
        int idx=1;
        while(filled < N){
            if(line!=null && filled==0){
                // 已有第一行含 N M，需读下一行
            }
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens() && idx<=N){
                int p=Integer.parseInt(st.nextToken());
                parent[idx]=p;
                if(p==-1) root=idx;
                else if(p>=1 && p<=N) children[p].add(idx);
                idx++; filled++;
            }
        }
        // 计算深度 BFS
        int[] depth=new int[N+1];
        Arrays.fill(depth, -1);
        depth[root]=0;
        Deque<Integer> q=new ArrayDeque<>();
        q.offer(root);
        while(!q.isEmpty()){
            int u=q.poll();
            for(int v: children[u]){
                depth[v]=depth[u]+1;
                q.offer(v);
            }
        }
        // 对于孤立或未连通但题目保证连通

        boolean[] inTree=new boolean[N+1];
        inTree[root]=true;
        long E=0;
        int maxD=0;
        Set<Integer> distinct=new HashSet<>();
        StringBuilder out=new StringBuilder();
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            int x=Integer.parseInt(line.trim());
            if(!distinct.contains(x)){
                distinct.add(x);
                // 更新max
                if(depth[x] > maxD) maxD=depth[x];
                // 向上爬直到遇到已在树中的节点
                int cur=x;
                while(cur!=-1 && !inTree[cur]){
                    inTree[cur]=true;
                    E++;
                    cur=parent[cur];
                }
                // 注意：上面把节点标记，边数等于新增节点数（除根外）
                // 但我们从x开始标记，若x的父已标记，则x到父的边算一条，对应标记x
                // 若x是根不算，但x不会是根（送餐地点不含外卖站）
                // 需要修正：根已标记，E不应把根计入，所以E计数即新增标记节点数（不含根初始）
                // 上面的while若cur是新节点就+1，若最终遇到已标记节点停止，计数正确
                // 只是当x本身已在树中（作为祖先），while不会进入，E不变
            }
            long ans=2*E - maxD;
            out.append(ans).append("\n");
        }
        System.out.print(out.toString());
    }
}
