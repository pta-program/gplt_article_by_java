import java.util.*;
import java.io.*;

/**
 * L3-029 还原文件
 * 题意：主断口高度序列H长N，M条纸条每条序列长度K_i。H由纸条按某排列首尾重叠1个高度拼接而成。
 * 求能还原H的纸条排列（保证唯一）。
 * 解法：贪心+回溯。H的拼接是重叠1的划分，起始位置pos决定下一纸条必须恰好匹配H[pos .. pos+K_i-1]。
 * 于是从pos=0开始，在未使用的纸条中寻找能匹配当前位置的候选，若唯一则贪心前进；若多候选则回溯DFS。
 * 由于M<=100，N<=1e5，单次匹配O(K)，整体 O(M * N) 在唯一解时很快。
 * 使用DFS+剪枝，回溯深度M，分支有限。
 * 时间复杂度：最坏指数但实际接近 O(M * avgK)，对唯一解为线性。
 * 空间 O(N + sumK)
 */
public class Main {
    static int N;
    static int[] H;
    static int M;
    static int[][] strips;
    static int[] K;
    static boolean[] used;
    static int[] order;
    static int[] result;
    static boolean found;

    static boolean matchesAt(int pos, int idx){
        int k=K[idx];
        if(pos + k > N) return false;
        int[] s=strips[idx];
        for(int i=0;i<k;i++) if(H[pos+i]!=s[i]) return false;
        return true;
    }

    static void dfs(int pos, int depth){
        if(found) return;
        if(depth==M){
            // 检查是否正好覆盖到末尾（pos == N-1 表示最后重叠点已到末尾，或pos==N）
            // 由于重叠1，最后pos应为N-1（最后纸条末尾）
            // 我们的pos更新是 pos + K -1，下一步pos为N表示已超出1？实际完成时pos == N-1 +? 需判断
            // 更简单：覆盖长度应为 N，最后纸条结束位置 pos+K-1 == N-1
            // 在递归中pos是下一纸条起始位置，最后一步后pos = N（超出）或 N
            // 我们在调用时 pos 为下一段起始，结束条件为 depth==M 且 pos==N（已超出）或 pos==N-1+? 精确判断末尾匹配
            // 对于最后纸条，起始pos_prev + K_last == N  (因为重叠1时总长 sumK-(M-1)=N => 最后起始+ K_last = N)
            // 而我们的pos在放置纸条后更新为 pos_next = pos + K -1 （若非最后）或 pos+K（若检测结束）
            // DFS中我们在放置后计算 nextPos，若 depth+1==M 则要求 pos+K == N 才算成功
            // 因此在这里pos应为下一位置，已在放置时检查
            result=order.clone();
            found=true;
            return;
        }
        // 收集候选
        List<Integer> cand=new ArrayList<>();
        for(int i=0;i<M;i++) if(!used[i] && matchesAt(pos,i)) cand.add(i);
        if(cand.isEmpty()) return;
        // 按候选尝试，回溯
        for(int idx: cand){
            used[idx]=true;
            order[depth]=idx;
            int k=K[idx];
            int nextPos;
            if(depth+1==M){
                // 最后一条需恰好到末尾
                if(pos + k != N){ used[idx]=false; continue; }
                nextPos = N; // 结束
            }else{
                nextPos = pos + k -1;
            }
            dfs(nextPos, depth+1);
            used[idx]=false;
            if(found) return;
        }
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        // 使用Scanner式读取所有整数
        List<Integer> all=new ArrayList<>();
        String line;
        StringBuilder sb=new StringBuilder();
        while((line=br.readLine())!=null) sb.append(line).append(" ");
        StringTokenizer st=new StringTokenizer(sb.toString());
        while(st.hasMoreTokens()){
            try{ all.add(Integer.parseInt(st.nextToken())); }catch(Exception e){}
        }
        if(all.isEmpty()) return;
        int p=0;
        N=all.get(p++);
        H=new int[N];
        for(int i=0;i<N;i++) H[i]=all.get(p++);
        M=all.get(p++);
        strips=new int[M][];
        K=new int[M];
        for(int i=0;i<M;i++){
            int k=all.get(p++);
            K[i]=k;
            strips[i]=new int[k];
            for(int j=0;j<k;j++) strips[i][j]=all.get(p++);
        }
        used=new boolean[M];
        order=new int[M];
        found=false;
        dfs(0,0);
        if(result!=null){
            StringBuilder out=new StringBuilder();
            for(int i=0;i<M;i++){
                if(i>0) out.append(" ");
                out.append(result[i]+1); // 编号从1开始
            }
            System.out.println(out.toString());
        }else{
            // 备用贪心（若DFS未找到，尝试贪心）
            // 按起始位置排序的近似
            // 此处直接输出空
        }
    }
}
