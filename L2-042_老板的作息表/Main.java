import java.util.*;
import java.io.*;

// L2-042 老板的作息表 - 区间查漏
// 时间复杂度 O(N log N)
public class Main {
    static int toSec(String s){
        String[] p=s.split(":");
        return Integer.parseInt(p[0])*3600 + Integer.parseInt(p[1])*60 + Integer.parseInt(p[2]);
    }
    static String toStr(int sec){
        int h=sec/3600;
        int m=(sec%3600)/60;
        int s=sec%60;
        return String.format("%02d:%02d:%02d", h,m,s);
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        int[][] segs=new int[N][2];
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            // 格式 hh:mm:ss - hh:mm:ss
            // 可能空格不固定，用正则分割
            String[] parts=line.split("-");
            // parts[0] 含开始时间，parts[1] 结束
            String start=parts[0].trim();
            String end=parts[1].trim();
            segs[i][0]=toSec(start);
            segs[i][1]=toSec(end);
        }
        Arrays.sort(segs, (a,b)-> Integer.compare(a[0], b[0]));
        StringBuilder out=new StringBuilder();
        int DAY_START=0;
        int DAY_END=23*3600+59*60+59;
        // 起始前的空隙
        if(segs[0][0] != DAY_START){
            out.append(toStr(DAY_START)).append(" - ").append(toStr(segs[0][0])).append("\n");
        }
        for(int i=0;i<N-1;i++){
            int end=segs[i][1];
            int nextStart=segs[i+1][0];
            if(end != nextStart){
                out.append(toStr(end)).append(" - ").append(toStr(nextStart)).append("\n");
            }
        }
        if(segs[N-1][1] != DAY_END){
            out.append(toStr(segs[N-1][1])).append(" - ").append(toStr(DAY_END)).append("\n");
        }
        System.out.print(out.toString());
    }
}
