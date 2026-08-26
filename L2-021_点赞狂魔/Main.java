import java.util.*;
import java.io.*;

// L2-021 点赞狂魔: 统计不同标签数与平均出现次数
// 时间复杂度 O(N*K)
public class Main {
    static class Person{
        String name;
        int distinct;
        double avg;
        Person(String n,int d,double a){name=n;distinct=d;avg=a;}
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        List<Person> list=new ArrayList<>();
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            // 可能标签跨行? 题目保证一行
            // 但稳妥起见处理跨行
            String name=st.nextToken();
            List<String> tokens=new ArrayList<>();
            while(st.hasMoreTokens()) tokens.add(st.nextToken());
            // 第一个是K
            int K=Integer.parseInt(tokens.get(0));
            Set<Integer> set=new HashSet<>();
            // 已有tokens包含K后面的标签
            int have = tokens.size()-1;
            for(int j=1;j<tokens.size();j++) set.add(Integer.parseInt(tokens.get(j)));
            // 若标签未读完，继续读行
            while(have < K){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens() && have < K){
                    set.add(Integer.parseInt(st.nextToken()));
                    have++;
                }
                // 如果此行恰是下一人的name行, 需要回退? 但标签是整数，name是小写字母，可区分
                // 简化: 假设标签都在同一行
            }
            int distinct=set.size();
            double avg = distinct==0?0: K*1.0/distinct;
            list.add(new Person(name,distinct,avg));
        }
        // 排序: distinct降序, avg升序
        list.sort((a,b)->{
            if(a.distinct!=b.distinct) return Integer.compare(b.distinct,a.distinct);
            int cmp=Double.compare(a.avg,b.avg);
            if(cmp!=0) return cmp;
            return a.name.compareTo(b.name);
        });
        List<String> out=new ArrayList<>();
        for(int i=0;i<Math.min(3,list.size());i++) out.add(list.get(i).name);
        while(out.size()<3) out.add("-");
        System.out.println(String.join(" ", out));
    }
}
