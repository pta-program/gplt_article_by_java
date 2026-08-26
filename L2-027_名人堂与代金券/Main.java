import java.util.*;
import java.io.*;

// L2-027 名人堂与代金券: 排序与券统计
// 时间复杂度 O(N log N)
public class Main {
    static class Student{
        String id;
        int score;
        Student(String i,int s){id=i;score=s;}
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int G=Integer.parseInt(st.nextToken());
        int K=Integer.parseInt(st.nextToken());
        List<Student> list=new ArrayList<>();
        long totalCoupon=0;
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            String id=st.nextToken();
            int score=Integer.parseInt(st.nextToken());
            list.add(new Student(id,score));
            if(score>=G) totalCoupon+=50;
            else if(score>=60) totalCoupon+=20;
        }
        // 按成绩降序, 账号升序
        list.sort((a,b)->{
            if(a.score!=b.score) return Integer.compare(b.score,a.score);
            return a.id.compareTo(b.id);
        });
        // 计算排名
        int[] rank=new int[N];
        for(int i=0;i<N;i++){
            if(i==0) rank[i]=1;
            else{
                if(list.get(i).score==list.get(i-1).score) rank[i]=rank[i-1];
                else rank[i]=i+1;
            }
        }
        int cutoffRank = rank[Math.min(K-1, N-1)];
        // 若K名次内有并列，需要扩大到所有同排名
        // 但按定义 cutoffRank = rank[K-1], 所有 rank <= cutoffRank 的都要输出
        StringBuilder sb=new StringBuilder();
        sb.append(totalCoupon).append('\n');
        for(int i=0;i<N;i++){
            if(rank[i]<=cutoffRank){
                sb.append(rank[i]).append(' ').append(list.get(i).id).append(' ').append(list.get(i).score).append('\n');
            }else break; // 由于rank递增，超过即止
        }
        System.out.print(sb.toString());
    }
}
