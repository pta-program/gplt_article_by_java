import java.util.*;
import java.io.*;

// L2-015 互评成绩: 去掉最高最低取平均，输出最高的M个非递减
// 时间复杂度 O(N k + N log N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int k=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        double[] scores=new double[N];
        for(int i=0;i<N;i++){
            // 读取 k 个成绩，可能跨行
            int cnt=0;
            int sum=0, mn=Integer.MAX_VALUE, mx=Integer.MIN_VALUE;
            while(cnt<k){
                line=br.readLine();
                if(line==null) break;
                if(line.trim().isEmpty()) continue;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens() && cnt<k){
                    int v=Integer.parseInt(st.nextToken());
                    sum+=v;
                    mn=Math.min(mn,v);
                    mx=Math.max(mx,v);
                    cnt++;
                }
            }
            scores[i]=(sum - mn - mx) * 1.0 / (k-2);
        }
        Arrays.sort(scores);
        StringBuilder sb=new StringBuilder();
        for(int i=N-M;i<N;i++){
            if(i> N-M) sb.append(' ');
            sb.append(String.format("%.3f", scores[i]));
        }
        System.out.println(sb.toString());
    }
}
