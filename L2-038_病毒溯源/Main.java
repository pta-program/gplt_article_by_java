import java.util.*;
import java.io.*;

// L2-038 病毒溯源 - 树最长链+字典序最小
// 时间复杂度 O(N log N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        List<Integer>[] children=new ArrayList[N];
        for(int i=0;i<N;i++) children[i]=new ArrayList<>();
        boolean[] hasParent=new boolean[N];
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) line="0";
            StringTokenizer st=new StringTokenizer(line);
            int k=Integer.parseInt(st.nextToken());
            for(int j=0;j<k;j++){
                while(!st.hasMoreTokens()){
                    line=br.readLine();
                    if(line==null) break;
                    st=new StringTokenizer(line);
                }
                int v=Integer.parseInt(st.nextToken());
                children[i].add(v);
                if(v>=0 && v<N) hasParent[v]=true;
            }
        }
        for(int i=0;i<N;i++) Collections.sort(children[i]);

        int root=0;
        for(int i=0;i<N;i++) if(!hasParent[i]) {root=i; break;}

        // 迭代获得后序
        List<Integer> order=new ArrayList<>();
        Deque<Integer> stack=new ArrayDeque<>();
        stack.push(root);
        while(!stack.isEmpty()){
            int u=stack.pop();
            order.add(u);
            for(int v: children[u]) stack.push(v);
        }
        Collections.reverse(order);
        int[] bestLen=new int[N];
        List<Integer>[] bestPath=new ArrayList[N];
        for(int i=0;i<N;i++){ bestLen[i]=1; bestPath[i]=new ArrayList<>(Arrays.asList(i)); }

        for(int u: order){
            if(children[u].isEmpty()){
                bestLen[u]=1;
                bestPath[u]=new ArrayList<>(Arrays.asList(u));
            }else{
                int maxL=-1;
                List<Integer> best=null;
                for(int v: children[u]){
                    int cand=bestLen[v];
                    if(cand>maxL){
                        maxL=cand;
                        best=bestPath[v];
                    }else if(cand==maxL){
                        // 字典序比较
                        if(compareList(bestPath[v], best) < 0){
                            best=bestPath[v];
                        }
                    }
                }
                bestLen[u]=maxL+1;
                List<Integer> newPath=new ArrayList<>();
                newPath.add(u);
                newPath.addAll(best);
                bestPath[u]=newPath;
            }
        }
        System.out.println(bestLen[root]);
        List<Integer> ansPath=bestPath[root];
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<ansPath.size();i++){
            if(i>0) sb.append(' ');
            sb.append(ansPath.get(i));
        }
        System.out.println(sb.toString());
    }
    static int compareList(List<Integer> a, List<Integer> b){
        int n=Math.min(a.size(), b.size());
        for(int i=0;i<n;i++){
            int av=a.get(i), bv=b.get(i);
            if(av!=bv) return Integer.compare(av,bv);
        }
        return Integer.compare(a.size(), b.size());
    }
}
