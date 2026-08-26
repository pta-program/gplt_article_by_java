import java.util.*;
import java.io.*;

// L2-034 口罩发放
// 时间复杂度 O(D * T log T)
public class Main {
    static class Record {
        String name;
        String id;
        int ill;
        int time; // 分钟
        int order;
        Record(String n,String i,int ill,int t,int o){name=n;id=i;this.ill=ill;time=t;order=o;}
    }
    static boolean isValidId(String s){
        if(s.length()!=18) return false;
        for(int i=0;i<18;i++){
            char c=s.charAt(i);
            if(c<'0'||c>'9') return false;
        }
        return true;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        int D=Integer.parseInt(st.nextToken());
        int P=Integer.parseInt(st.nextToken());
        // 保存所有记录按出现顺序用于第二部分
        List<Record> allRecords=new ArrayList<>();
        Map<String,Integer> lastSuccess=new HashMap<>();
        List<String> outputGive=new ArrayList<>();

        for(int day=1; day<=D; day++){
            // 读 Ti Si
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            while(st.countTokens()<2){
                String extra=br.readLine();
                if(extra==null) break;
                line=line+" "+extra;
                st=new StringTokenizer(line);
            }
            int Ti=Integer.parseInt(st.nextToken());
            int Si=Integer.parseInt(st.nextToken());
            List<Record> dayList=new ArrayList<>();
            for(int j=0;j<Ti;j++){
                line=br.readLine();
                while(line!=null && line.trim().isEmpty()) line=br.readLine();
                if(line==null) break;
                StringTokenizer st2=new StringTokenizer(line);
                String name=st2.hasMoreTokens()?st2.nextToken():"";
                String id=st2.hasMoreTokens()?st2.nextToken():"";
                int ill=0;
                String timeStr="";
                if(st2.hasMoreTokens()) ill=Integer.parseInt(st2.nextToken());
                if(st2.hasMoreTokens()) timeStr=st2.nextToken();
                int minutes=0;
                if(timeStr.contains(":")){
                    String[] parts=timeStr.split(":");
                    minutes=Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);
                }
                Record r=new Record(name,id,ill,minutes,j);
                dayList.add(r);
                allRecords.add(r);
            }
            // 按提交时间排序，时间相同按出现顺序
            List<Record> sorted=new ArrayList<>(dayList);
            sorted.sort((a,b)->{
                if(a.time!=b.time) return Integer.compare(a.time,b.time);
                return Integer.compare(a.order,b.order);
            });
            int given=0;
            for(Record r: sorted){
                if(given>=Si) break;
                if(!isValidId(r.id)) continue;
                Integer last=lastSuccess.get(r.id);
                if(last!=null && day <= last + P) continue; // P天内不能再次申请
                // 成功
                outputGive.add(r.name+" "+r.id);
                lastSuccess.put(r.id, day);
                given++;
            }
        }
        StringBuilder out=new StringBuilder();
        for(String s: outputGive) out.append(s).append("\n");
        // 第二部分：有合法记录且身体状况为1的申请人，按出现顺序去重
        Set<String> seen=new HashSet<>();
        List<String> illPersons=new ArrayList<>();
        Map<String,String> idToName=new HashMap<>();
        for(Record r: allRecords){
            if(r.ill!=1) continue;
            if(!isValidId(r.id)) continue;
            if(seen.contains(r.id)) continue;
            seen.add(r.id);
            // 顺序即首次出现的健康为1的合法记录顺序
            illPersons.add(r.name+" "+r.id);
        }
        for(String s: illPersons) out.append(s).append("\n");
        System.out.print(out.toString());
    }
}
