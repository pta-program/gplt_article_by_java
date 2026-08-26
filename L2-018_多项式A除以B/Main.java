import java.util.*;
import java.io.*;

// L2-018 多项式A除以B: 长除法，保留1位小数
// 时间复杂度 O((degA-degB)*degB)
public class Main {
    static final double EPS = 1e-9;
    static TreeMap<Integer,Double> filter(TreeMap<Integer,Double> map){
        TreeMap<Integer,Double> res=new TreeMap<>(Collections.reverseOrder());
        for(Map.Entry<Integer,Double> e: map.entrySet()){
            double v=e.getValue();
            double r=Math.round(v*10.0)/10.0;
            if(Math.abs(r) < 0.05) continue; // 舍入后为0.0
            // 去除 -0.0 影响
            if(r==0.0) r=0.0;
            res.put(e.getKey(), v); // 保留原始v用于输出时格式化会再次舍入
        }
        return res;
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        // 读取两行多项式，可能跨行? 题目说每行一个多项式
        List<String> tokens=new ArrayList<>();
        // 读所有token
        String all="";
        while((line=br.readLine())!=null){
            if(line.trim().isEmpty()) continue;
            all+= " "+line;
        }
        if(all.trim().isEmpty()) return;
        StringTokenizer st=new StringTokenizer(all);
        List<String> ts=new ArrayList<>();
        while(st.hasMoreTokens()) ts.add(st.nextToken());
        // 解析两个多项式: t0=N, 然后2*N个数, 再下一个N
        int idx=0;
        if(idx>=ts.size()) return;
        int nA=Integer.parseInt(ts.get(idx++));
        TreeMap<Integer,Double> a=new TreeMap<>(Collections.reverseOrder());
        for(int i=0;i<nA;i++){
            int e=Integer.parseInt(ts.get(idx++));
            double c=Double.parseDouble(ts.get(idx++));
            a.put(e,c);
        }
        if(idx>=ts.size()){
            // 无B? 异常
            return;
        }
        int nB=Integer.parseInt(ts.get(idx++));
        TreeMap<Integer,Double> b=new TreeMap<>(Collections.reverseOrder());
        for(int i=0;i<nB;i++){
            int e=Integer.parseInt(ts.get(idx++));
            double c=Double.parseDouble(ts.get(idx++));
            b.put(e,c);
        }

        TreeMap<Integer,Double> q=new TreeMap<>(Collections.reverseOrder());
        TreeMap<Integer,Double> r=new TreeMap<>(Collections.reverseOrder());
        r.putAll(a);
        // 清除r中零系数干扰
        // 先过滤微小?
        int degB=b.firstKey();
        double leadB=b.get(degB);

        while(!r.isEmpty()){
            int degR=r.firstKey();
            double coeffR=r.get(degR);
            if(Math.abs(coeffR)<EPS){
                r.remove(degR);
                continue;
            }
            if(degR < degB) break;
            double coeffQ = coeffR / leadB;
            int degQ = degR - degB;
            // 合并到q
            q.put(degQ, q.getOrDefault(degQ,0.0)+coeffQ);
            // r = r - coeffQ * x^{degQ} * b
            for(Map.Entry<Integer,Double> be: b.entrySet()){
                int e = be.getKey()+degQ;
                double c = be.getValue()*coeffQ;
                double cur = r.getOrDefault(e,0.0) - c;
                if(Math.abs(cur)<EPS) r.remove(e);
                else r.put(e,cur);
            }
            // 上述循环已将degR项置0并移除
            // 为避免残留，确保删除degR (若还存在则误差)
            // 实际上上面已处理e=degR
            if(r.containsKey(degR) && Math.abs(r.get(degR))<EPS) r.remove(degR);
        }

        TreeMap<Integer,Double> qf=filter(q);
        TreeMap<Integer,Double> rf=filter(r);

        StringBuilder sb=new StringBuilder();
        if(qf.isEmpty()){
            sb.append("0 0 0.0\n");
        }else{
            sb.append(qf.size());
            for(Map.Entry<Integer,Double> e: qf.entrySet()){
                sb.append(' ').append(e.getKey()).append(' ');
                sb.append(String.format(Locale.US,"%.1f", e.getValue()));
            }
            sb.append('\n');
        }
        if(rf.isEmpty()){
            sb.append("0 0 0.0");
        }else{
            sb.append(rf.size());
            for(Map.Entry<Integer,Double> e: rf.entrySet()){
                sb.append(' ').append(e.getKey()).append(' ');
                sb.append(String.format(Locale.US,"%.1f", e.getValue()));
            }
        }
        System.out.print(sb.toString());
    }
}
