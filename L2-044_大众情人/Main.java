import java.util.*;
import java.io.*;

// L2-044 大众情人 - Floyd 最短路 + minimax
// 时间复杂度 O(N^3)
public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        int N=Integer.parseInt(line.trim());
        long INF= (long)1e12;
        long[][] dist=new long[N+1][N+1];
        for(int i=1;i<=N;i++){
            Arrays.fill(dist[i], INF);
            dist[i][i]=0;
        }
        char[] gender=new char[N+1];
        for(int i=1;i<=N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            StringTokenizer st=new StringTokenizer(line);
            String gStr=st.nextToken();
            gender[i]=gStr.charAt(0);
            int K=Integer.parseInt(st.nextToken());
            for(int k=0;k<K;k++){
                while(!st.hasMoreTokens()){
                    line=br.readLine();
                    if(line==null) break;
                    st=new StringTokenizer(line);
                }
                String token=st.nextToken(); // 格式 朋友:距离
                int colon=token.indexOf(':');
                int v=Integer.parseInt(token.substring(0, colon));
                long w=Long.parseLong(token.substring(colon+1));
                if(w < dist[i][v]) dist[i][v]=w;
            }
        }
        // Floyd
        for(int k=1;k<=N;k++){
            for(int i=1;i<=N;i++){
                if(dist[i][k]==INF) continue;
                for(int j=1;j<=N;j++){
                    if(dist[k][j]==INF) continue;
                    long nd=dist[i][k]+dist[k][j];
                    if(nd < dist[i][j]) dist[i][j]=nd;
                }
            }
        }
        long[] maxDist=new long[N+1];
        Arrays.fill(maxDist, -1);
        for(int i=1;i<=N;i++){
            long mx=-1;
            boolean unreachable=false;
            for(int j=1;j<=N;j++){
                if(gender[j]==gender[i]) continue;
                long d=dist[j][i];
                if(d==INF){
                    mx=INF;
                    break;
                }
                if(d > mx) mx=d;
            }
            if(mx==-1) mx=INF; // 没有异性？题目保证有，但处理
            maxDist[i]=mx;
        }
        // 找女性中最优
        long bestF=INF;
        for(int i=1;i<=N;i++) if(gender[i]=='F') bestF=Math.min(bestF, maxDist[i]);
        long bestM=INF;
        for(int i=1;i<=N;i++) if(gender[i]=='M') bestM=Math.min(bestM, maxDist[i]);

        StringBuilder sb=new StringBuilder();
        boolean first=true;
        for(int i=1;i<=N;i++) if(gender[i]=='F' && maxDist[i]==bestF){
            if(!first) sb.append(' ');
            sb.append(i);
            first=false;
        }
        first=true;
        StringBuilder sb2=new StringBuilder();
        for(int i=1;i<=N;i++) if(gender[i]=='M' && maxDist[i]==bestM){
            if(!first) sb2.append(' ');
            sb2.append(i);
            first=false;
        }
        System.out.print(sb.toString());
        System.out.print("\n");
        System.out.println(sb2.toString());
    }
}
