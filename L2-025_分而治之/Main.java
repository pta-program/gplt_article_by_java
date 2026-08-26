import java.util.*;
import java.io.*;

// L2-025 分而治之: 判断是否为点覆盖使剩余图无边
// 时间复杂度 O(K*(M + Np))
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        // 保证读到N M
        List<Integer> first=new ArrayList<>();
        while(st.hasMoreTokens()) first.add(Integer.parseInt(st.nextToken()));
        while(first.size()<2){
            line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) first.add(Integer.parseInt(st.nextToken()));
        }
        int N=first.get(0), M=first.get(1);
        List<int[]> edges=new ArrayList<>();
        for(int i=0;i<M;i++){
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
        int K=Integer.parseInt(line.trim());
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<K;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            List<Integer> vals=new ArrayList<>();
            while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            // 可能跨行
            int Np=vals.get(0);
            while(vals.size()-1 < Np){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            }
            Set<Integer> attacked=new HashSet<>();
            for(int j=1;j<=Np && j<vals.size();j++) attacked.add(vals.get(j));
            boolean ok=true;
            for(int[] e: edges){
                if(!attacked.contains(e[0]) && !attacked.contains(e[1])){
                    ok=false; break;
                }
            }
            sb.append(ok?"YES":"NO").append('\n');
        }
        System.out.print(sb.toString());
    }
}
