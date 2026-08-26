import java.util.*;
import java.io.*;

// L2-014 列车调度: 求最少轨道数 = 最长上升子序列长度
// 时间复杂度 O(N log N)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        int[] a=new int[N];
        int idx=0;
        while(idx<N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st=new StringTokenizer(line);
            while(st.hasMoreTokens() && idx<N) a[idx++]=Integer.parseInt(st.nextToken());
        }
        // tails[i] 为长度 i+1 的LIS的最小尾
        List<Integer> tails=new ArrayList<>();
        for(int x: a){
            int l=0,r=tails.size();
            while(l<r){
                int mid=(l+r)/2;
                if(tails.get(mid) >= x) r=mid;
                else l=mid+1;
            }
            if(l==tails.size()) tails.add(x);
            else tails.set(l, x);
        }
        System.out.println(tails.size());
    }
}
