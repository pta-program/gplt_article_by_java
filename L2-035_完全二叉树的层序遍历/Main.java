import java.util.*;
import java.io.*;

// L2-035 完全二叉树的层序遍历 - 由后序还原完全二叉树
// 思路：完全二叉树的数组表示中下标准确，后序遍历对应对该数组的后序访问顺序
// 用DFS按后序填层序数组
// 时间复杂度 O(N)
public class Main {
    static int N;
    static int[] post;
    static int[] level;
    static int idx;
    static void dfs(int p){
        if(p >= N) return;
        dfs(2*p+1);
        dfs(2*p+2);
        level[p]=post[idx++];
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        N=Integer.parseInt(line.trim());
        post=new int[N];
        level=new int[N];
        int cnt=0;
        while(cnt<N){
            line=br.readLine();
            if(line==null) break;
            if(line.trim().isEmpty()) continue;
            StringTokenizer st=new StringTokenizer(line);
            while(st.hasMoreTokens() && cnt<N){
                post[cnt++]=Integer.parseInt(st.nextToken());
            }
        }
        idx=0;
        dfs(0);
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<N;i++){
            if(i>0) sb.append(' ');
            sb.append(level[i]);
        }
        System.out.println(sb.toString());
    }
}
