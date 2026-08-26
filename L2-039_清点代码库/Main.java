import java.util.*;
import java.io.*;

// L2-039 清点代码库 - 去重+排序
// 时间复杂度 O(N log N * M)
public class Main {
    static class Entry{
        int cnt;
        int[] vec;
        Entry(int c,int[] v){cnt=c; vec=v;}
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        Map<String,int[]> vecMap=new HashMap<>();
        Map<String,Integer> cntMap=new HashMap<>();

        // 读取 N 行，每行 M 个整数
        for(int i=0;i<N;i++){
            int[] vec=new int[M];
            int filled=0;
            while(filled < M){
                line=br.readLine();
                if(line==null) break;
                if(line.trim().isEmpty()) continue;
                StringTokenizer st2=new StringTokenizer(line);
                while(st2.hasMoreTokens() && filled<M){
                    vec[filled++]=Integer.parseInt(st2.nextToken());
                }
            }
            String key=buildKey(vec);
            if(cntMap.containsKey(key)){
                cntMap.put(key, cntMap.get(key)+1);
            }else{
                cntMap.put(key,1);
                vecMap.put(key, vec);
            }
        }
        List<Entry> list=new ArrayList<>();
        for(String k: cntMap.keySet()){
            list.add(new Entry(cntMap.get(k), vecMap.get(k)));
        }
        list.sort((a,b)->{
            if(a.cnt!=b.cnt) return Integer.compare(b.cnt, a.cnt);
            for(int i=0;i<M;i++){
                if(a.vec[i]!=b.vec[i]) return Integer.compare(a.vec[i], b.vec[i]);
            }
            return 0;
        });
        StringBuilder sb=new StringBuilder();
        sb.append(list.size()).append("\n");
        for(Entry e: list){
            sb.append(e.cnt);
            for(int v: e.vec) sb.append(' ').append(v);
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }
    static String buildKey(int[] vec){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<vec.length;i++){
            if(i>0) sb.append(',');
            sb.append(vec[i]);
        }
        return sb.toString();
    }
}
