import java.util.*;
import java.io.*;

// L2-040 哲哲打游戏 - 模拟存档读档
// 时间复杂度 O(N + M + sum K)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        List<int[]> next=new ArrayList<>(N+1);
        next.add(new int[0]); // 0
        for(int i=1;i<=N;i++){
            // 读剧情点i的设定，可能跨行
            List<Integer> vals=new ArrayList<>();
            while(vals.isEmpty()){
                line=br.readLine();
                if(line==null) break;
                if(line.trim().isEmpty()) continue;
                StringTokenizer st2=new StringTokenizer(line);
                while(st2.hasMoreTokens()) vals.add(Integer.parseInt(st2.nextToken()));
            }
            int K=vals.get(0);
            // 如果 vals 数量不足 K+1，继续读
            while(vals.size() < K+1){
                line=br.readLine();
                if(line==null) break;
                StringTokenizer st2=new StringTokenizer(line);
                while(st2.hasMoreTokens()) vals.add(Integer.parseInt(st2.nextToken()));
            }
            int[] arr=new int[K];
            for(int j=0;j<K;j++) arr[j]=vals.get(j+1);
            next.add(arr);
        }
        int cur=1;
        int[] save=new int[105]; // 档位1..100
        StringBuilder out=new StringBuilder();
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st2=new StringTokenizer(line);
            int type=Integer.parseInt(st2.nextToken());
            int j=0;
            if(st2.hasMoreTokens()) j=Integer.parseInt(st2.nextToken());
            else{
                // 读下一行？但理论上同行
                line=br.readLine();
                if(line!=null) j=Integer.parseInt(line.trim());
            }
            if(type==0){
                // 选择
                int[] arr=next.get(cur);
                // j是第j个选择，1-indexed
                cur=arr[j-1];
            }else if(type==1){
                save[j]=cur;
                out.append(cur).append("\n");
            }else if(type==2){
                cur=save[j];
            }
        }
        out.append(cur).append("\n");
        System.out.print(out.toString());
    }
}
