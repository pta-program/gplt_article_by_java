import java.util.*;
import java.io.*;

// L2-030 冰岛人: 5代亲属判断 (不包括第5代)
// 时间复杂度 O(N + M*2^4)
public class Main {
    static class Person{
        String first;
        String base;
        String key; // first+" "+base
        char gender; // M/F
        String parentFirst; // 维京才有
        Person(String f,String b,String k,char g,String pf){first=f;base=b;key=k;gender=g;parentFirst=pf;}
    }
    static Map<String, Person> keyMap=new HashMap<>();
    static Map<String, Person> firstMap=new HashMap<>();

    static Set<String> ancestorsSet(Person p){
        Set<String> set=new HashSet<>();
        // 收集到 great-grandparent (深度3包含self)
        // self depth0, parent1, grand2, great3
        Queue<Person> q=new ArrayDeque<>();
        Queue<Integer> d=new ArrayDeque<>();
        Set<String> visited=new HashSet<>();
        q.offer(p); d.offer(0);
        visited.add(p.first);
        while(!q.isEmpty()){
            Person cur=q.poll();
            int depth=d.poll();
            if(depth>3) continue;
            set.add(cur.first);
            if(depth==3) continue;
            if(cur.parentFirst==null) continue;
            Person parent=firstMap.get(cur.parentFirst);
            if(parent==null) continue;
            if(visited.contains(parent.first)) continue;
            visited.add(parent.first);
            q.offer(parent); d.offer(depth+1);
        }
        return set;
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            // 人名可能包含空格: 名 姓带后缀 两个token
            List<String> toks=new ArrayList<>();
            while(st.hasMoreTokens()) toks.add(st.nextToken());
            while(toks.size()<2){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) toks.add(st.nextToken());
            }
            String first=toks.get(0);
            String lastWith=toks.get(1);
            String base=null;
            char gender='M';
            String parentFirst=null;
            if(lastWith.endsWith("sson")){
                base=lastWith.substring(0,lastWith.length()-4);
                gender='M';
                parentFirst=base;
            }else if(lastWith.endsWith("sdottir")){
                base=lastWith.substring(0,lastWith.length()-7);
                gender='F';
                parentFirst=base;
            }else if(lastWith.endsWith("m")){
                base=lastWith.substring(0,lastWith.length()-1);
                gender='M';
                parentFirst=null;
            }else if(lastWith.endsWith("f")){
                base=lastWith.substring(0,lastWith.length()-1);
                gender='F';
                parentFirst=null;
            }else{
                base=lastWith;
                gender='M';
            }
            String key=first+" "+base;
            Person p=new Person(first,base,key,gender,parentFirst);
            keyMap.put(key,p);
            // firstMap: 若first重复，后者覆盖? 假设first唯一
            firstMap.put(first,p);
        }
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        int M=Integer.parseInt(line.trim());
        StringBuilder out=new StringBuilder();
        for(int i=0;i<M;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            List<String> toks=new ArrayList<>();
            while(st.hasMoreTokens()) toks.add(st.nextToken());
            while(toks.size()<4){
                String extra=br.readLine();
                if(extra==null) break;
                st=new StringTokenizer(extra);
                while(st.hasMoreTokens()) toks.add(st.nextToken());
            }
            String f1=toks.get(0), b1=toks.get(1), f2=toks.get(2), b2=toks.get(3);
            String key1=f1+" "+b1;
            String key2=f2+" "+b2;
            Person p1=keyMap.get(key1);
            Person p2=keyMap.get(key2);
            if(p1==null || p2==null){
                out.append("NA\n");
                continue;
            }
            if(p1.gender==p2.gender){
                out.append("Whatever\n");
                continue;
            }
            Set<String> s1=ancestorsSet(p1);
            Set<String> s2=ancestorsSet(p2);
            boolean intersect=false;
            for(String s: s1) if(s2.contains(s)){ intersect=true; break; }
            out.append(intersect? "No\n":"Yes\n");
        }
        System.out.print(out.toString());
    }
}
