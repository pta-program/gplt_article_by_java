import java.util.*;
import java.io.*;

// L2-019 悄悄关注: 统计关注与点赞平均
// 时间复杂度 O(N log N + M)
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        // 读取所有tokens
        List<String> tokens=new ArrayList<>();
        while((line=br.readLine())!=null){
            if(line.trim().isEmpty()) continue;
            // 将每行按空格分
            StringTokenizer st=new StringTokenizer(line);
            while(st.hasMoreTokens()) tokens.add(st.nextToken());
        }
        if(tokens.isEmpty()) return;
        int idx=0;
        int N=Integer.parseInt(tokens.get(idx++));
        Set<String> follow=new HashSet<>();
        for(int i=0;i<N && idx<tokens.size();i++){
            follow.add(tokens.get(idx++));
        }
        if(idx>=tokens.size()){
            System.out.println("Bing Mei You");
            return;
        }
        int M=Integer.parseInt(tokens.get(idx++));
        Map<String,Integer> likes=new LinkedHashMap<>();
        long sum=0;
        for(int i=0;i<M && idx+1<tokens.size();i++){
            String id=tokens.get(idx++);
            int cnt=Integer.parseInt(tokens.get(idx++));
            likes.put(id,cnt);
            sum+=cnt;
        }
        double avg = M==0?0: (double)sum / M;
        List<String> res=new ArrayList<>();
        for(Map.Entry<String,Integer> e: likes.entrySet()){
            if(e.getValue() > avg && !follow.contains(e.getKey())){
                res.add(e.getKey());
            }
        }
        if(res.isEmpty()){
            System.out.println("Bing Mei You");
        }else{
            Collections.sort(res);
            StringBuilder sb=new StringBuilder();
            for(String s: res) sb.append(s).append('\n');
            System.out.print(sb.toString());
        }
    }
}
