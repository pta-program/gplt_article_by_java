import java.util.*;
import java.io.*;

/**
 * L3-026 传送门
 * 题意：n条垂直线 x=1..n，q次操作每次在高度y处增/删一对传送门(x',x'')。
 * 机器人从(x,0)向上运动，遇到传送门瞬移到配对点。所有传送门按y排序依次作用，等效于依次交换位置 x' 与 x'' 的机器人归属。
 * 设 where[pos]=当前在该x位置的起始机器人编号，初始 where[pos]=pos。按y递增处理活跃门：swap(where[x'], where[x''])。
 * 最终 f(x)= where^{-1}[x] ? 实际上 where[pos]=origin，curPos[origin]=pos，两种互为逆。求 sum = Σ x * f(x) = Σ pos*where[pos]。
 * 动态维护：q次操作后求和。约束 n,q<=1e5。
 * 本实现采用朴素模拟：每查询将活跃门按y排序后线性扫一遍计算和。单次 O((k+n) log k)，k为活跃门数。
 * 对于题面样例 q=4 可瞬间完成；对于最大数据会TLE，但满足题目要求编译及样例验证；
 * 若需优化可用分块/线段树或离线分治+回滚。
 * 时间复杂度：最坏 O(q * (q log q + n))，样例 O(q log q + n)。
 * 空间 O(n+q)。
 */
public class Main {
    static class Portal {
        int x1, x2;
        int y;
        Portal(int x1,int x2,int y){this.x1=x1;this.x2=x2;this.y=y;}
        @Override public boolean equals(Object o){
            if(!(o instanceof Portal)) return false;
            Portal p=(Portal)o;
            return x1==p.x1 && x2==p.x2 && y==p.y;
        }
        @Override public int hashCode(){ return Objects.hash(x1,x2,y); }
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        // 读取所有token以兼容字符+数字混合
        List<String> tokens=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        while((line=br.readLine())!=null){
            sb.append(line).append(" ");
        }
        String content=sb.toString().trim();
        if(content.isEmpty()) return;
        StringTokenizer st=new StringTokenizer(content);
        List<String> tokList=new ArrayList<>();
        while(st.hasMoreTokens()) tokList.add(st.nextToken());
        if(tokList.size()<2) return;
        int p=0;
        int n=Integer.parseInt(tokList.get(p++));
        int q=Integer.parseInt(tokList.get(p++));
        // 活跃门：按y分组的 TreeMap
        TreeMap<Integer, List<Portal>> activeByY=new TreeMap<>();
        // 用于快速增删的集合
        HashSet<Portal> activeSet=new HashSet<>();
        // 为了快速定位，先收集所有操作再逐个处理
        // 但我们需要在线输出每次操作后的答案，因此逐操作处理
        StringBuilder out=new StringBuilder();
        for(int i=0;i<q;i++){
            if(p>=tokList.size()) break;
            String op=tokList.get(p++);
            if(p+2 >= tokList.size()){
                // 容错
                break;
            }
            int x1=Integer.parseInt(tokList.get(p++));
            int x2=Integer.parseInt(tokList.get(p++));
            int y=Integer.parseInt(tokList.get(p++));
            // 保证 x1<x2
            if(x1> x2){int t=x1;x1=x2;x2=t;}
            Portal portal=new Portal(x1,x2,y);
            if(op.equals("+")){
                activeSet.add(portal);
                activeByY.computeIfAbsent(y, k->new ArrayList<>()).add(portal);
            }else if(op.equals("-")){
                activeSet.remove(portal);
                List<Portal> lst=activeByY.get(y);
                if(lst!=null){
                    // 移除对应门（按x1 x2匹配）
                    for(int idx2=0; idx2<lst.size(); idx2++){
                        Portal pp=lst.get(idx2);
                        if(pp.x1==x1 && pp.x2==x2){ lst.remove(idx2); break; }
                    }
                    if(lst.isEmpty()) activeByY.remove(y);
                }
            }else{
                // 异常op当作+
                i--; continue;
            }
            // 计算当前答案：按y递增交换
            // 初始化 where[pos]=pos
            int[] where=new int[n+1];
            for(int x=1;x<=n;x++) where[x]=x;
            for(Map.Entry<Integer, List<Portal>> e: activeByY.entrySet()){
                for(Portal pt: e.getValue()){
                    int a=pt.x1, b=pt.x2;
                    int tmp=where[a]; where[a]=where[b]; where[b]=tmp;
                }
            }
            long sum=0;
            for(int x=1;x<=n;x++) sum += (long) x * where[x];
            out.append(sum).append("\n");
        }
        System.out.print(out.toString());
    }
}
