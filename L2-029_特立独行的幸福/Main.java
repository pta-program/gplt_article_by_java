import java.util.*;
import java.io.*;

// L2-029 特立独行的幸福: 幸福数判断与独立性
// 时间复杂度 O((B-A)*迭代长度)
public class Main {
    static int squareSum(int x){
        int s=0;
        while(x>0){ int d=x%10; s+=d*d; x/=10; }
        return s;
    }
    static boolean isPrime(int n){
        if(n<2) return false;
        if(n%2==0) return n==2;
        for(int i=3;i*i<=n;i+=2) if(n%i==0) return false;
        return true;
    }
    static class Info{
        boolean happy;
        List<Integer> chain; // 包含1
        Info(boolean h, List<Integer> c){happy=h;chain=c;}
    }
    static Info getInfo(int n){
        Set<Integer> visited=new HashSet<>();
        List<Integer> chain=new ArrayList<>();
        int cur=n;
        visited.add(cur);
        while(true){
            int nxt=squareSum(cur);
            if(nxt==1){
                chain.add(1);
                return new Info(true, chain);
            }
            if(visited.contains(nxt)){
                return new Info(false, chain);
            }
            visited.add(nxt);
            chain.add(nxt);
            cur=nxt;
            // 防止无限循环过大
            if(chain.size()>1000) return new Info(false, chain);
        }
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        List<Integer> vals=new ArrayList<>();
        while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
        while(vals.size()<2){
            line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.hasMoreTokens()) vals.add(Integer.parseInt(st.nextToken()));
        }
        int A=vals.get(0), B=vals.get(1);
        Map<Integer, Info> infoMap=new HashMap<>();
        Set<Integer> happySet=new HashSet<>();
        Map<Integer, List<Integer>> chainMap=new HashMap<>();
        for(int i=A;i<=B;i++){
            Info inf=getInfo(i);
            infoMap.put(i, inf);
            if(inf.happy){
                happySet.add(i);
                chainMap.put(i, inf.chain);
            }
        }
        // 找出依附关系: 哪些数出现在其他数的chain中
        Set<Integer> dependent=new HashSet<>();
        for(int h: happySet){
            List<Integer> ch=chainMap.get(h);
            for(int v: ch){
                if(v!=h && v>=A && v<=B && happySet.contains(v)){
                    dependent.add(v);
                }
            }
        }
        List<Integer> specials=new ArrayList<>();
        for(int h: happySet) if(!dependent.contains(h)) specials.add(h);
        Collections.sort(specials);
        if(specials.isEmpty()){
            System.out.println("SAD");
            return;
        }
        StringBuilder sb=new StringBuilder();
        for(int v: specials){
            List<Integer> ch=chainMap.get(v);
            int indep = ch.size(); // 包含1
            if(isPrime(v)) indep*=2;
            sb.append(v).append(' ').append(indep).append('\n');
        }
        System.out.print(sb.toString());
    }
}
