import java.util.*;
import java.io.*;

// L2-045 堆宝塔 - 双柱模拟
// 时间复杂度 O(N^2) 最坏但 N<=1000
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        int[] a=new int[N];
        int cnt=0;
        while(cnt<N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st=new StringTokenizer(line);
            while(st.hasMoreTokens() && cnt<N) a[cnt++]=Integer.parseInt(st.nextToken());
        }
        if(N==0){
            System.out.println("0 0");
            return;
        }
        Deque<Integer> A=new ArrayDeque<>();
        Deque<Integer> B=new ArrayDeque<>();
        List<Integer> heights=new ArrayList<>();
        A.push(a[0]);
        for(int i=1;i<N;i++){
            int C=a[i];
            if(!A.isEmpty() && C < A.peek()){
                A.push(C);
            }else{
                if(B.isEmpty() || C > B.peek()){
                    B.push(C);
                }else{
                    // 完成 A 宝塔
                    heights.add(A.size());
                    A.clear();
                    // 把 B 中比 C 大的移到 A
                    while(!B.isEmpty() && B.peek() > C){
                        A.push(B.pop());
                    }
                    A.push(C);
                }
            }
        }
        if(!A.isEmpty()) heights.add(A.size());
        if(!B.isEmpty()) heights.add(B.size());
        int towerCount=heights.size();
        int maxH=0;
        for(int h: heights) maxH=Math.max(maxH, h);
        System.out.println(towerCount+" "+maxH);
    }
}
