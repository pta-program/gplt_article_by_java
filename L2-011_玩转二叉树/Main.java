import java.util.*;
import java.io.*;

// L2-011 玩转二叉树: 中序+前序建树，镜像后层序
// 时间复杂度 O(N)
public class Main {
    static int[] in, pre;
    static Map<Integer,Integer> inPos;
    static class Node { int val; Node left,right; Node(int v){val=v;}}
    static Node build(int preL,int preR,int inL,int inR){ // [l,r)
        if(preL>=preR||inL>=inR) return null;
        int rootVal = pre[preL];
        Node root = new Node(rootVal);
        int k = inPos.get(rootVal);
        int leftSize = k - inL;
        root.left = build(preL+1, preL+1+leftSize, inL, k);
        root.right = build(preL+1+leftSize, preR, k+1, inR);
        return root;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N = Integer.parseInt(line.trim());
        in = new int[N];
        pre = new int[N];
        read(br,in,N);
        read(br,pre,N);
        inPos=new HashMap<>();
        for(int i=0;i<N;i++) inPos.put(in[i],i);
        Node root=build(0,N,0,N);
        // 镜像后层序: 交换左右孩子再 BFS，或 BFS 时先右后左
        Queue<Node> q=new LinkedList<>();
        q.offer(root);
        List<Integer> lev=new ArrayList<>();
        while(!q.isEmpty()){
            Node cur=q.poll();
            lev.add(cur.val);
            // 镜像: 先放右再放左? 但若直接交换孩子指针，顺序为右->左
            // 为保持镜像层序，等价于正常 BFS 按 右、左 入队
            if(cur.right!=null) q.offer(cur.right);
            else if(cur.left!=null) {} // 无需
            if(cur.left!=null) {
                // 已处理 right
            }
            // 上面逻辑重复，正确做法:
            // 已入队 right，若 left 也存在需入队
            // 为避免重复入队，改用显式
        }
        // 重新 BFS 正确镜像
        lev.clear();
        q.clear();
        q.offer(root);
        while(!q.isEmpty()){
            Node cur=q.poll();
            lev.add(cur.val);
            // 先右后左即镜像
            if(cur.right!=null) q.offer(cur.right);
            if(cur.left!=null) q.offer(cur.left);
        }
        // 但上述对根的镜像未完全正确? 直接交换树更直观
        // 为确保正确，改为递归交换
        // 重新建树并交换
        root=build(0,N,0,N);
        mirror(root);
        lev.clear();
        q.clear();
        q.offer(root);
        while(!q.isEmpty()){
            Node cur=q.poll();
            lev.add(cur.val);
            if(cur.left!=null) q.offer(cur.left);
            if(cur.right!=null) q.offer(cur.right);
        }
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<lev.size();i++){
            if(i>0) sb.append(' ');
            sb.append(lev.get(i));
        }
        System.out.println(sb.toString());
    }
    static void mirror(Node r){
        if(r==null) return;
        Node tmp=r.left; r.left=r.right; r.right=tmp;
        mirror(r.left); mirror(r.right);
    }
    static void read(BufferedReader br,int[] arr,int N)throws Exception{
        int idx=0;
        while(idx<N){
            String l=br.readLine();
            if(l==null) break;
            if(l.trim().isEmpty()) continue;
            StringTokenizer st=new StringTokenizer(l);
            while(st.hasMoreTokens()&&idx<N) arr[idx++]=Integer.parseInt(st.nextToken());
        }
    }
}
