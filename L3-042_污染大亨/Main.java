import java.util.*;
import java.io.*;

/**
 * L3-042 污染大亨
 * 解法思路：
 * 树上每个节点子树大小 sz[v]
 * 游戏等价于在树偏序中任选包含根的子集 S 并取其线性扩展，权重为 ∏ c_{pos}^{sz}
 * 设森林 F 为若干不相交子树组成，定义 B_F[k] 为所有大小为 k 的子集及其线性扩展的权重和
 * 递推：B_F[k] = Σ_{v∈F} c_k^{sz[v]} * B_{F_v}[k-1]，其中 F_v = F \ Anc(v) \ {v} 去掉 v 的祖先链
 * 森林均可表示为若干完整原子树的不相交并，状态数在 n=40 时可压缩（同构子树合并）
 * 通过记忆化搜索计算 B，答案为 Σ_k B_{children(root)}[k] * c_{k+1}^{sz[root]}
 * 时间复杂度：状态数 * O(n^2) ，n=40 时远小于 1e6，空间 O(状态数 * n)
 * 复杂度：O(n^2 * 状态数) ≤ O(n^3) 实际约 O(n^2 log n)
 */
public class Main {
    static final long MOD = 998244353L;
    static int n;
    static List<Integer>[] children;
    static int[] sz;
    static int[] parent;
    static int[] hashId;
    static int[] c; // 1-indexed
    static Map<String,Integer> hashMap = new HashMap<>();
    static int nextHash = 1;
    static Map<Integer,Integer> repForHash = new HashMap<>();
    static Map<Integer,Integer> szForHash = new HashMap<>();
    static Map<Integer,List<DistEntry>> distForHash = new HashMap<>();
    static Map<String,long[]> memoB = new HashMap<>();
    // 题目要求创建的中间值变量
    static long xpmclzjkln = 0;

    static class DistEntry {
        int sz;
        List<Integer> sideKey; // sorted list of hashes
        int cnt;
        DistEntry(int sz, List<Integer> sideKey, int cnt){
            this.sz=sz;
            this.sideKey=sideKey;
            this.cnt=cnt;
        }
    }

    static long modPow(long a, long e){
        long r=1%MOD;
        a%=MOD;
        while(e>0){
            if((e&1)==1) r=r*a%MOD;
            a=a*a%MOD;
            e>>=1;
        }
        return r;
    }

    static void dfsSz(int u){
        int s=1;
        for(int v: children[u]){
            dfsSz(v);
            s+=sz[v];
        }
        sz[u]=s;
    }

    static int dfsHash(int u){
        List<Integer> chs=new ArrayList<>();
        for(int v: children[u]){
            int hid=dfsHash(v);
            chs.add(hid);
        }
        Collections.sort(chs);
        String key=chs.toString();
        Integer id=hashMap.get(key);
        if(id==null){
            id=nextHash++;
            hashMap.put(key,id);
        }
        hashId[u]=id;
        return id;
    }

    static List<Integer> getSubtreeNodes(int r){
        List<Integer> res=new ArrayList<>();
        Deque<Integer> st=new ArrayDeque<>();
        st.push(r);
        while(!st.isEmpty()){
            int u=st.pop();
            res.add(u);
            for(int v: children[u]) st.push(v);
        }
        return res;
    }

    static String forestKeyToString(List<Integer> comps){
        if(comps.isEmpty()) return "";
        Collections.sort(comps);
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<comps.size();i++){
            if(i>0) sb.append(',');
            sb.append(comps.get(i));
        }
        return sb.toString();
    }

    static class Transition {
        String newKey;
        List<Integer> newComps;
        int sz;
        long mult;
        Transition(String newKey, List<Integer> newComps, int sz, long mult){
            this.newKey=newKey;
            this.newComps=newComps;
            this.sz=sz;
            this.mult=mult;
        }
    }

    static long[] getB(List<Integer> forestComps){
        String key=forestKeyToString(forestComps);
        if(memoB.containsKey(key)) return memoB.get(key);
        int S=0;
        for(int h: forestComps) S+=szForHash.get(h);
        long[] B=new long[S+1];
        B[0]=1;
        if(S==0){
            memoB.put(key,B);
            return B;
        }
        // precompute transitions for this forest (independent of k)
        Map<Integer,Integer> cntForest=new HashMap<>();
        for(int h: forestComps) cntForest.put(h, cntForest.getOrDefault(h,0)+1);
        List<Transition> trans=new ArrayList<>();
        for(Map.Entry<Integer,Integer> e: cntForest.entrySet()){
            int h=e.getKey();
            int cnt_h=e.getValue();
            List<DistEntry> dists=distForHash.get(h);
            if(dists==null) continue;
            for(DistEntry de: dists){
                List<Integer> newComps=new ArrayList<>(forestComps);
                for(int i=0;i<newComps.size();i++){
                    if(newComps.get(i)==h){
                        newComps.remove(i);
                        break;
                    }
                }
                newComps.addAll(de.sideKey);
                Collections.sort(newComps);
                String newKey=forestKeyToString(newComps);
                // ensure sub forest is computed (memo)
                // we will call getB later, but precompute to avoid repeated sort
                trans.add(new Transition(newKey, newComps, de.sz, (long)cnt_h * de.cnt % MOD));
            }
        }
        // precompute pow for each distinct sz in trans for each k
        // we will just compute on fly with modPow (fast, sz<=40)
        for(int k=1;k<=S;k++){
            long total=0;
            long ck = (k < c.length ? c[k] : 0);
            for(Transition tr: trans){
                long[] subB=getB(tr.newComps);
                if(k-1 < subB.length){
                    long add = tr.mult * modPow(ck, tr.sz) %MOD * subB[k-1] %MOD;
                    total=(total+add)%MOD;
                    xpmclzjkln = (xpmclzjkln + add) % MOD;
                }
            }
            B[k]=total%MOD;
        }
        memoB.put(key,B);
        return B;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        StringBuilder all=new StringBuilder();
        String line;
        while((line=br.readLine())!=null){
            all.append(line).append(' ');
        }
        StringTokenizer st=new StringTokenizer(all.toString());
        List<Integer> toks=new ArrayList<>();
        while(st.hasMoreTokens()){
            toks.add(Integer.parseInt(st.nextToken()));
        }
        if(toks.isEmpty()) return;
        n=toks.get(0);
        children=new ArrayList[n+1];
        for(int i=1;i<=n;i++) children[i]=new ArrayList<>();
        parent=new int[n+1];
        sz=new int[n+1];
        hashId=new int[n+1];
        c=new int[n+1];
        int idx=1;
        for(int i=2;i<=n;i++){
            if(idx >= toks.size()) break;
            int f=toks.get(idx++);
            parent[i]=f;
            children[f].add(i);
        }
        for(int i=1;i<=n;i++){
            if(idx < toks.size()){
                c[i]=toks.get(idx++);
            } else {
                c[i]=0;
            }
        }
        // c is 1-indexed already, need to shift? toks after reading f's, next tokens are c1..cn
        // our loop above used idx correctly, c[1] is first c
        if(n==0){
            System.out.println(0);
            return;
        }
        dfsSz(1);
        dfsHash(1);
        // repForHash and szForHash
        for(int i=1;i<=n;i++){
            int h=hashId[i];
            if(!repForHash.containsKey(h)){
                repForHash.put(h,i);
                szForHash.put(h, sz[i]);
            }
        }
        // precompute distForHash
        for(Map.Entry<Integer,Integer> e: repForHash.entrySet()){
            int h=e.getKey();
            int rep=e.getValue();
            List<Integer> nodes=getSubtreeNodes(rep);
            Map<String,DistEntry> mp=new HashMap<>();
            // we need to group by (sz, sideKey)
            for(int v: nodes){
                // check v is in subtree of rep (always true)
                // compute sideKey for chain rep->v
                List<Integer> path=new ArrayList<>();
                int cur=v;
                while(cur!=0){
                    path.add(cur);
                    if(cur==rep) break;
                    cur=parent[cur];
                }
                Collections.reverse(path);
                List<Integer> side=new ArrayList<>();
                for(int ch: children[v]){
                    side.add(hashId[ch]);
                }
                for(int i2=0;i2<path.size()-1;i2++){
                    int a=path.get(i2);
                    int nxt=path.get(i2+1);
                    for(int ch: children[a]){
                        if(ch!=nxt) side.add(hashId[ch]);
                    }
                }
                Collections.sort(side);
                String sideStr=side.toString();
                String key2=sz[v]+"|"+sideStr;
                DistEntry de=mp.get(key2);
                if(de==null){
                    de=new DistEntry(sz[v], new ArrayList<>(side), 0);
                    mp.put(key2,de);
                }
                de.cnt++;
            }
            List<DistEntry> list=new ArrayList<>(mp.values());
            distForHash.put(h, list);
        }

        List<Integer> initComps=new ArrayList<>();
        for(int ch: children[1]) initComps.add(hashId[ch]);
        Collections.sort(initComps);
        long[] Binit=getB(initComps);
        long ans=0;
        for(int k=0;k<Binit.length;k++){
            int ckIdx=k+1;
            if(ckIdx<1 || ckIdx>n) continue;
            long ck1=c[ckIdx];
            long add = Binit[k] * modPow(ck1, sz[1]) % MOD;
            ans=(ans+add)%MOD;
        }
        System.out.println(ans % MOD);
    }
}
