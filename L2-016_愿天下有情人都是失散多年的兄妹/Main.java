import java.util.*;
import java.io.*;

// L2-016 愿天下有情人都是失散多年的兄妹: 五服内通婚判断，BFS收集5代祖先
// 时间复杂度 O(N + K * 2^4) 空间 O(N)
public class Main {
    static Map<String, String[]> parentMap = new HashMap<>();
    static Map<String, Character> genderMap = new HashMap<>();

    static Set<String> collectAncestors(String id, int limit) {
        // limit = 4 edges from self inclusive (5代)
        Set<String> set = new HashSet<>();
        // BFS with depth
        Queue<String> q = new ArrayDeque<>();
        Queue<Integer> d = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        q.offer(id); d.offer(0);
        visited.add(id);
        while(!q.isEmpty()){
            String cur = q.poll();
            int depth = d.poll();
            if(depth > limit) continue;
            if(cur == null || cur.equals("-1")) continue;
            set.add(cur);
            if(depth == limit) continue;
            String[] p = parentMap.get(cur);
            if(p == null) continue;
            for(String par : p){
                if(par == null || par.equals("-1")) continue;
                if(visited.contains(par)) continue;
                visited.add(par);
                q.offer(par);
                d.offer(depth+1);
            }
        }
        return set;
    }

    // 更精确的带深度map用于判断? 题目要求最近共同祖先在五代以内即不可，本质是是否有交集在5代内
    // 收集到深度limit=4即可
    static boolean withinFive(String a, String b){
        Set<String> sa = collectAncestors(a,4);
        Set<String> sb = collectAncestors(b,4);
        // 求交集, 去除可能的自交? 但a,b同辈不太会是对方祖先
        for(String x: sa){
            if(x.equals("-1")) continue;
            if(sb.contains(x)) return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取N
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        StringTokenizer st = new StringTokenizer(line);
        int N = Integer.parseInt(st.nextToken());
        for(int i=0;i<N;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            String id = st.nextToken();
            String gender = st.nextToken();
            String fid = st.nextToken();
            String mid = st.nextToken();
            genderMap.put(id, gender.charAt(0));
            parentMap.put(id, new String[]{fid, mid});
            // 为父亲母亲补充性别信息，若未在map中
            if(!fid.equals("-1") && !genderMap.containsKey(fid)){
                // 父亲必为M
                // 保留若后续有本人信息则覆盖
                genderMap.putIfAbsent(fid,'M');
                parentMap.putIfAbsent(fid, new String[]{"-1","-1"});
            }
            if(!mid.equals("-1") && !genderMap.containsKey(mid)){
                genderMap.putIfAbsent(mid,'F');
                parentMap.putIfAbsent(mid, new String[]{"-1","-1"});
            }
            // 也可通过角色推断，但若已存在则以本人行性别为准
            // 对于作为父亲出现的id，若已存在性别冲突，以第一次为准? 题目保证一致
        }
        // 读取K
        while((line=br.readLine())!=null && line.trim().isEmpty()){}
        if(line==null) return;
        st=new StringTokenizer(line);
        int K = Integer.parseInt(st.nextToken());
        StringBuilder out=new StringBuilder();
        for(int i=0;i<K;i++){
            line=br.readLine();
            while(line!=null && line.trim().isEmpty()) line=br.readLine();
            if(line==null) break;
            st=new StringTokenizer(line);
            // 可能一行只有1个id，跨行情况
            List<String> ids=new ArrayList<>();
            while(st.hasMoreTokens()) ids.add(st.nextToken());
            while(ids.size()<2){
                line=br.readLine();
                if(line==null) break;
                st=new StringTokenizer(line);
                while(st.hasMoreTokens()) ids.add(st.nextToken());
            }
            String a = ids.get(0);
            String b = ids.get(1);
            Character ga = genderMap.get(a);
            Character gb = genderMap.get(b);
            // 若查询id不在map中，性别未知，题目未明确但可认为按未出现处理? 实际上查询id多为已出现人
            // 若未知，则尝试判定? 默认为异性? 但根据题意查询人可能是任意? 我们若未知则无法判断同性，暂按未找到则视为异性但祖先为空交集=>Yes?
            // 更安全: 若未知性别则按未记录则不判Never Mind
            if(ga != null && gb != null && ga.equals(gb)){
                out.append("Never Mind\n");
            }else{
                // 若任一性别未知，视为异性
                boolean forbidden = withinFive(a,b);
                out.append(forbidden ? "No\n" : "Yes\n");
            }
        }
        System.out.print(out.toString());
    }
}
