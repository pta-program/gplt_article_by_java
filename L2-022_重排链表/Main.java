import java.util.*;
import java.io.*;

// L2-022 重排链表: 链表重排 Ln->L1->Ln-1->L2...
// 时间复杂度 O(N)
public class Main {
    static class Node{
        int addr;
        int data;
        int next;
        Node(int a,int d,int n){addr=a;data=d;next=n;}
    }
    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line;
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        String firstAddrStr=st.nextToken();
        int firstAddr = Integer.parseInt(firstAddrStr);
        int N=Integer.parseInt(st.nextToken());
        Map<Integer, Node> map=new HashMap<>();
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            int addr=Integer.parseInt(st.nextToken());
            int data=Integer.parseInt(st.nextToken());
            int nxt=Integer.parseInt(st.nextToken());
            map.put(addr,new Node(addr,data,nxt));
        }
        // 重建链表顺序
        List<Node> seq=new ArrayList<>();
        int cur=firstAddr;
        Set<Integer> visited=new HashSet<>();
        while(cur!=-1 && map.containsKey(cur) && !visited.contains(cur)){
            Node nd=map.get(cur);
            seq.add(nd);
            visited.add(cur);
            cur=nd.next;
        }
        int L=seq.size();
        List<Node> reordered=new ArrayList<>();
        int l=0,r=L-1;
        while(l<r){
            reordered.add(seq.get(r));
            reordered.add(seq.get(l));
            l++; r--;
        }
        if(l==r) reordered.add(seq.get(l));
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<reordered.size();i++){
            Node nd=reordered.get(i);
            int nextAddr = (i+1<reordered.size()) ? reordered.get(i+1).addr : -1;
            String curAddrStr = String.format("%05d", nd.addr);
            String nextStr = nextAddr==-1? "-1" : String.format("%05d", nextAddr);
            sb.append(curAddrStr).append(' ').append(nd.data).append(' ').append(nextStr);
            if(i+1<reordered.size()) sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}
