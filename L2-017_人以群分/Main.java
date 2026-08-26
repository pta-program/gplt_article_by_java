import java.util.*;
import java.io.*;

// L2-017 人以群分: 排序后按人数均分求差
// 时间复杂度 O(N log N)
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int[] a=new int[N];
        int idx=0;
        while(idx<N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens() && idx<N){
                a[idx++]=Integer.parseInt(st.nextToken());
            }
        }
        Arrays.sort(a);
        int n1,n2;
        long sumOut=0,sumIn=0;
        // 排序后前半为Introverted(小), 后半为Outgoing(大)
        // N偶: 各N/2 ; N奇: Outgoing多1
        if(N%2==0){
            n1=N/2;
            n2=N/2;
            for(int i=0;i<N/2;i++) sumIn+=a[i];
            for(int i=N/2;i<N;i++) sumOut+=a[i];
        }else{
            n1=N/2+1; // outgoing 多1
            n2=N/2;
            for(int i=0;i<N/2;i++) sumIn+=a[i];
            for(int i=N/2;i<N;i++) sumOut+=a[i];
            // 但上述把小的当intro, 大的当outgoing; 对于奇数，intro为 N/2个小值, outgoing为N/2+1个大值
            // 重新计算: 实际上 sumIn 应为前 N/2个，sumOut为后N/2+1个
            // 上面循环已满足 (因为 N/2 = floor)
        }
        long diff = Math.abs(sumOut - sumIn);
        System.out.println("Outgoing #: "+n1);
        System.out.println("Introverted #: "+n2);
        System.out.println("Diff = "+diff);
    }
}
