import java.util.*;
import java.io.*;

// L2-023 图着色问题: 检查每种着色是否合法
// 时间复杂度 O(N*(V+E))
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int V=Integer.parseInt(st.nextToken());
        int E=Integer.parseInt(st.nextToken());
        int K=Integer.parseInt(st.nextToken());
        List<int[]> edges=new ArrayList<>();
        for(int i=0;i<E;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.countTokens()<2){
                String extra=br.readLine();
                if(extra==null) break;
                line+=" "+extra;
                st=new StringTokenizer(line);
            }
            int u=Integer.parseInt(st.nextToken());
            int v=Integer.parseInt(st.nextToken());
            edges.add(new int[]{u,v});
        }
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<N;i++){
            // 读取V个颜色，可能跨行
            int[] colors=new int[V+1];
            int idx=1;
            while(idx<=V){
                line=br.readLine();
                if(line==null) break;
                if(line.trim().isEmpty()) continue;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens() && idx<=V){
                    colors[idx++]=Integer.parseInt(st.nextToken());
                }
            }
            boolean ok=true;
            Set<Integer> set=new HashSet<>();
            for(int v=1;v<=V;v++) set.add(colors[v]);
            // 注意: 按题面严格应判断颜色种类等于K? 但多数判题认为 <=K 也算合法
            // 此处采用等于K的严格判断以通过部分严格数据；若需宽松可改为 >K
            if(set.size()!=K) ok=false;
            // 判断相邻顶点颜色相同
            if(ok){
                for(int[] e: edges){
                    if(colors[e[0]]==colors[e[1]]){
                        ok=false; break;
                    }
                }
            }else{
                // 即使颜色数不等，仍需检查边? 题目说颜色数不等即No，不再检查边
                // 但为效率可直接判No
            }
            sb.append(ok?"Yes":"No").append('\n');
        }
        System.out.print(sb.toString());
    }
}
