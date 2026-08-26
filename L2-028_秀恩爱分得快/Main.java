import java.util.*;
import java.io.*;

// L2-028 秀恩爱分得快: 计算亲密度
// 时间复杂度 O(M*K^2)
public class Main {
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int N=Integer.parseInt(st.nextToken());
        int M=Integer.parseInt(st.nextToken());
        // 用绝对值编号 0..N-1, 性别 map
        int[] gender=new int[N]; // 1 male, -1 female, 0 unknown
        // 需要记录字符串到绝对id的映射? 直接用int值
        // 亲密度矩阵
        double[][] close=new double[N][N];
        // 为了快速判断性别，用Map<abs, gender>
        // 读取照片
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            // 可能跨行
            List<String> parts=new ArrayList<>();
            while(st.hasMoreTokens()) parts.add(st.nextToken());
            while(parts.size()==0){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) parts.add(st.nextToken());
            }
            int K=Integer.parseInt(parts.get(0));
            List<Integer> ids=new ArrayList<>();
            List<Integer> signs=new ArrayList<>();
            for(int j=1;j<parts.size();j++){
                String s=parts.get(j);
                int val=Integer.parseInt(s);
                ids.add(Math.abs(val));
                signs.add(val<0 ? -1 : 1);
            }
            while(ids.size()<K){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens() && ids.size()<K){
                    String s=st.nextToken();
                    int val=Integer.parseInt(s);
                    ids.add(Math.abs(val));
                    signs.add(val<0 ? -1 : 1);
                }
            }
            // 记录性别
            for(int j=0;j<ids.size();j++){
                int abs=ids.get(j);
                if(abs>=0 && abs<N){
                    if(gender[abs]==0) gender[abs]=signs.get(j);
                    // 保证一致
                }
            }
            if(K<=1) continue;
            double w=1.0/K;
            // 两两亲密度，仅异性? 但先全计算，后续过滤异性也可
            for(int a=0;a<K;a++){
                for(int b=a+1;b<K;b++){
                    int ia=ids.get(a), ib=ids.get(b);
                    // 若异性才累加? 题目说所有人两两亲密度定义1/K，但后续只关心异性最高
                    // 累加所有也可
                    close[ia][ib]+=w;
                    close[ib][ia]+=w;
                }
            }
        }
        // 读取情侣 A B
        line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        st=new StringTokenizer(line);
        List<String> ab=new ArrayList<>();
        while(st.hasMoreTokens()) ab.add(st.nextToken());
        while(ab.size()<2){
            line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) ab.add(st.nextToken());
        }
        String sA=ab.get(0), sB=ab.get(1);
        int A=Integer.parseInt(sA);
        int B=Integer.parseInt(sB);
        int absA=Math.abs(A), absB=Math.abs(B);
        // 用于输出的带符号字符串
        String outA=sA, outB=sB;

        // 计算各自最亲密异性
        double maxA=-1, maxB=-1;
        for(int i=0;i<N;i++){
            if(gender[i]==0) continue;
            if(gender[absA]!=0 && gender[i]==gender[absA]) continue;
            // i是abs
            if(close[absA][i] > maxA) maxA=close[absA][i];
        }
        for(int i=0;i<N;i++){
            if(gender[i]==0) continue;
            if(gender[absB]!=0 && gender[i]==gender[absB]) continue;
            if(close[absB][i] > maxB) maxB=close[absB][i];
        }
        // 收集列表
        List<Integer> listA=new ArrayList<>();
        List<Integer> listB=new ArrayList<>();
        for(int i=0;i<N;i++){
            if(gender[i]==0) continue;
            if(gender[absA]!=0 && gender[i]==gender[absA]) continue;
            if(Math.abs(close[absA][i]-maxA) < 1e-9 && maxA>-0.5) listA.add(i);
        }
        for(int i=0;i<N;i++){
            if(gender[i]==0) continue;
            if(gender[absB]!=0 && gender[i]==gender[absB]) continue;
            if(Math.abs(close[absB][i]-maxB) < 1e-9 && maxB>-0.5) listB.add(i);
        }
        // 按绝对值递增排序
        Collections.sort(listA);
        Collections.sort(listB);
        // 判断是否互为最高
        boolean aLikesB=false,bLikesA=false;
        // 需要判断B是否在listA中, A是否在listB中
        // 但close[absA][absB]可能为maxA/B
        if(Math.abs(close[absA][absB]-maxA) < 1e-9 && Math.abs(close[absB][absA]-maxB) < 1e-9){
            // 且双方都在对方列表中视为互为最高
            if(listA.contains(absB) && listB.contains(absA)){
                System.out.println(outA+" "+outB);
                return;
            }
        }
        StringBuilder sb=new StringBuilder();
        // 输出 A 及其最亲密
        // 如果listA空? 可能无异性同框，maxA可能为0或未更新，则不输出? 按题意应至少有? 若无则不输出?
        // 但常规应输出所有最大亲密度为0的? 若maxA为0 可能所有异性亲密度0，则全部并列? 但K限制内可能未同框
        // 此时视为所有异性都是0，数量很多，不合理；题目保证情侣有异性最高
        // 我们按若maxA <0 (无异性)则不输出A部分
        // 否则输出每项
        // 格式化符号: 女性用负号，男性用正? 需要按性别恢复符号
        // gender: -1 female => 输出 "-"+abs ; 1 male => 输出 abs 不带号 (但情侣A/B输入符号需保留)
        // 对于list中的人，输出符号需根据其性别: 若female => "-"+abs, male => ""+abs ; 但0需特殊? 0的负号 "-0"？
        // 题面对于0可能有 "-0" 表示女性0? 按输入例子 "-3" etc. 保持性别符号
        for(int abs: listA){
            String sign = (gender[abs]==-1) ? "-"+abs : String.valueOf(abs);
            // 若abs==0且gender==-1, 应输出 "-0" ?
            if(abs==0 && gender[abs]==-1) sign="-0";
            sb.append(outA).append(' ').append(sign).append('\n');
        }
        for(int abs: listB){
            String sign = (gender[abs]==-1) ? "-"+abs : String.valueOf(abs);
            if(abs==0 && gender[abs]==-1) sign="-0";
            sb.append(outB).append(' ').append(sign).append('\n');
        }
        System.out.print(sb.toString());
    }
}
