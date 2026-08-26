import java.util.*;
import java.io.*;

// L2-037 包装机 - 轨道+筐栈模拟
// 时间复杂度 O(N*M + 操作数)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        int Smax=Integer.parseInt(st.nextToken());
        char[][] tracks=new char[N+1][M];
        int[] ptr=new int[N+1];
        for(int i=1;i<=N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) line="";
            // 行可能包含空格？题面是大写字母无空格
            line=line.trim();
            // 若长度不足M，继续读？
            while(line.length() < M){
                String extra=br.readLine();
                if(extra==null) break;
                line+=extra.trim();
            }
            for(int j=0;j<M && j<line.length(); j++) tracks[i][j]=line.charAt(j);
        }
        // 读取操作序列直到-1
        List<Integer> ops=new ArrayList<>();
        while(true){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st2=new StringTokenizer(line);
            while(st2.hasMoreTokens()){
                int v=Integer.parseInt(st2.nextToken());
                if(v==-1){ break; }
                ops.add(v);
            }
            if(ops.size()>0 && line.contains("-1")) break;
            // 检查是否已包含-1结束符：需要看原始line是否含-1
            if(line.trim().contains("-1")) break;
        }
        Deque<Character> basket=new ArrayDeque<>();
        StringBuilder out=new StringBuilder();
        // 为了正确识别结束，重新解析ops时已过滤-1
        // 但上述循环可能多读，这里简化：已收集ops直到遇到-1为止
        // 实际题面操作在一行，这里足够

        // 如果操作可能跨多行，上面已处理；但需确保ops完整：若最后一行含-1已break
        for(int op: ops){
            if(op==0){
                if(!basket.isEmpty()){
                    out.append(basket.pop());
                }
            }else if(op>=1 && op<=N){
                if(ptr[op] >= M){
                    // 轨道已空，不发生
                    continue;
                }
                char item=tracks[op][ptr[op]];
                if(basket.size() >= Smax){
                    // 筐满，强制先出筐
                    if(!basket.isEmpty()){
                        out.append(basket.pop());
                    }
                    // 再推入
                    basket.push(item);
                    ptr[op]++;
                }else{
                    basket.push(item);
                    ptr[op]++;
                }
            }
        }
        System.out.println(out.toString());
    }
}
