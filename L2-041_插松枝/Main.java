import java.util.*;
import java.io.*;

// L2-041 插松枝 - 栈+队列模拟
// 时间复杂度 O(N*M)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        int K=Integer.parseInt(st.nextToken());
        int[] a=new int[N];
        int filled=0;
        while(filled<N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st2=new StringTokenizer(line);
            while(st2.hasMoreTokens() && filled<N){
                a[filled++]=Integer.parseInt(st2.nextToken());
            }
        }
        Deque<Integer> box=new ArrayDeque<>();
        List<Integer> cur=new ArrayList<>();
        List<List<Integer>> result=new ArrayList<>();
        int idx=0;
        while(true){
            // 判断终止：推送器空、盒子空、当前枝空 => 完成
            if(idx>=N && box.isEmpty() && cur.isEmpty()) break;

            boolean boxCan = !box.isEmpty() && (cur.isEmpty() || box.peek() <= cur.get(cur.size()-1));
            if(boxCan){
                int v=box.pop();
                cur.add(v);
                if(cur.size()==K){
                    result.add(new ArrayList<>(cur));
                    cur.clear();
                }
                continue;
            }else{
                if(idx>=N){
                    // 推送器空且盒子不可用
                    if(!cur.isEmpty()){
                        result.add(new ArrayList<>(cur));
                        cur.clear();
                        // 循环继续，若盒子仍有则下一轮会从盒子取
                        if(box.isEmpty()) break;
                        continue;
                    }else{
                        if(box.isEmpty()) break;
                        // cur空但盒子有，下一轮boxCan会成立，继续
                        continue;
                    }
                }else{
                    int c=a[idx++];
                    boolean canInsert = cur.isEmpty() || c <= cur.get(cur.size()-1);
                    if(canInsert){
                        cur.add(c);
                        if(cur.size()==K){
                            result.add(new ArrayList<>(cur));
                            cur.clear();
                        }
                    }else{
                        if(box.size() >= M){
                            // 盒子满，结束当前枝，C压回
                            if(!cur.isEmpty()){
                                result.add(new ArrayList<>(cur));
                                cur.clear();
                            }else{
                                // cur为空但盒子满且C不满足，是否也算结束空枝？此时不应产生空行，直接压回并继续
                                // 但按题意“小盒子已经满了，但推送器上取到的松针仍然不满足要求。此时将手中的松枝放到成品篮里”
                                // 若cur为空，可能产生空行？实际不会因为cur为空时盒子满但C比null? cur为空时任何C满足canInsert，所以不会进入此分支
                            }
                            idx--; // 压回
                            // 若cur为空且idx回退，可能无限循环？但此情况理论上不会出现
                            // 若cur为空，加保护避免死循环
                            if(cur.isEmpty() && box.size()>=M && idx < N){
                                // 实际上此时canInsert应为true，所以不会到这
                            }
                        }else{
                            box.push(c);
                        }
                    }
                }
            }
        }
        // 输出
        StringBuilder sb=new StringBuilder();
        for(List<Integer> branch: result){
            for(int i=0;i<branch.size();i++){
                if(i>0) sb.append(' ');
                sb.append(branch.get(i));
            }
            sb.append("\n");
        }
        System.out.print(sb.toString());
    }
}
