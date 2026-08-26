import java.util.*;
import java.io.*;

// L2-026 小字辈: 找最深叶子, BFS层序
// 时间复杂度 O(N)
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        int[] parent=new int[N+1];
        List<Integer>[] children=new ArrayList[N+1];
        for(int i=1;i<=N;i++) children[i]=new ArrayList<>();
        int root=-1;
        int idx=1;
        // 第二行可能跨多行
        while(idx<=N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st=new StringTokenizer(line);
            while(st.hasMoreTokens() && idx<=N){
                int p=Integer.parseInt(st.nextToken());
                parent[idx]=p;
                if(p==-1) root=idx;
                else children[p].add(idx);
                idx++;
            }
        }
        // BFS求深度
        Queue<Integer> q=new ArrayDeque<>();
        q.offer(root);
        int[] depth=new int[N+1];
        depth[root]=1;
        int maxDepth=1;
        while(!q.isEmpty()){
            int u=q.poll();
            for(int v: children[u]){
                depth[v]=depth[u]+1;
                maxDepth=Math.max(maxDepth, depth[v]);
                q.offer(v);
            }
        }
        List<Integer> ans=new ArrayList<>();
        for(int i=1;i<=N;i++) if(depth[i]==maxDepth) ans.add(i);
        Collections.sort(ans);
        StringBuilder sb=new StringBuilder();
        sb.append(maxDepth).append('\n');
        for(int i=0;i<ans.size();i++){
            if(i>0) sb.append(' ');
            sb.append(ans.get(i));
        }
        System.out.print(sb.toString());
    }
}
