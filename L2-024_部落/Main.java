import java.util.*;
import java.io.*;

// L2-024 部落: 并查集统计部落
// 时间复杂度 O((N*K) α + Q α)
public class Main {
    static class DSU{
        int[] p,r;
        DSU(int n){ p=new int[n+1]; r=new int[n+1]; for(int i=0;i<=n;i++) p[i]=i; }
        int find(int x){ if(p[x]==x) return x; return p[x]=find(p[x]); }
        void union(int a,int b){ a=find(a); b=find(b); if(a==b) return; if(r[a]<r[b]) p[a]=b; else if(r[a]>r[b]) p[b]=a; else {p[b]=a; r[a]++;}}
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        DSU dsu=new DSU(10000);
        Set<Integer> persons=new HashSet<>();
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            // 可能K后的成员跨行
            List<Integer> vals=new ArrayList<>();
            while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            while(vals.size()>0 && vals.get(0) > vals.size()-1){
                // K未满足
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
            }
            int K=vals.get(0);
            // vals[1..K] are IDs
            for(int j=1;j<=K;j++){
                if(j<vals.size()) persons.add(vals.get(j));
            }
            for(int j=2;j<=K;j++){
                int a=vals.get(1);
                int b=vals.get(j);
                dsu.union(a,b);
            }
        }
        // 统计部落数
        Set<Integer> roots=new HashSet<>();
        for(int id: persons) roots.add(dsu.find(id));
        StringBuilder sb=new StringBuilder();
        sb.append(persons.size()).append(' ').append(roots.size()).append('\n');
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null){ System.out.print(sb.toString().trim()); return; }
        int Q=Integer.parseInt(line.trim());
        for(int i=0;i<Q;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            // 可能查询跨行
            List<Integer> q=new ArrayList<>();
            while(st.hasMoreTokens()) q.add(Integer.parseInt(st.nextToken()));
            while(q.size()<2){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) q.add(Integer.parseInt(st.nextToken()));
            }
            int a=q.get(0), b=q.get(1);
            if(!persons.contains(a) || !persons.contains(b)){
                // 若任一人不在部落? 题目总人数是按出现的人定义的，未出现的人视为不同部落? 按DSU找根不同
                // 题目查询的人可能是未出现过的? 但按样例查询的人都在部落内
            }
            char ans = (dsu.find(a)==dsu.find(b)) ? 'Y':'N';
            sb.append(ans).append('\n');
        }
        System.out.print(sb.toString());
    }
}
