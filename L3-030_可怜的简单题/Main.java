import java.util.*;
import java.io.*;
import java.math.BigInteger;

/**
 * L3-030 可怜的简单题
 * 期望长度公式推导：
 * 设 cnt_d = floor(n/d)，则 E = 1 - Σ_{d>=2} mu(d) * cnt_d / (n - cnt_d)
 * 证明见题解：E = Σ_{k>=0} Pr(T>k)，Pr(gcd_k>1)= -Σ_{d>1} mu(d)(cnt_d/n)^k，几何级数求和即得。
 * 需要计算 S = Σ mu(d) * cnt_d * inv(n - cnt_d) 模 p (p为质数>n)。
 * 对 cnt_d 分组：cnt_d 在区间 [l,r] 常数，S = Σ_k k*inv(n-k) * (M(r)-M(l-1))，其中M为 Mertens 函数。
 * n up to 1e11，需快速求M(x)至1e11，使用 Du Jiao 筛 O(n^{2/3})。
 * 本实现：线性筛预处理 mu至LIMIT=4e6，求前缀M；对大值用记忆化递归公式 M(n)=1 - Σ_{l=2}^{n} (r-l+1)M(n/l)。
 * 分组求和时对每个区间计算 sumMu * k * inv(n-k) mod p，inv用扩展欧几里德。
 * 时间复杂度：筛 O(LIMIT)，M(n) 计算约 O(n^{2/3}) ~ 5e6 次运算，适用于 n=1e11 在数秒内完成。
 * 空间 O(LIMIT)
 */
public class Main {
    static int LIMIT = 4000000;
    static int[] mu;
    static long[] pref;
    static Map<Long, Long> memo = new HashMap<>();

    static void sieve(){
        mu = new int[LIMIT+1];
        pref = new long[LIMIT+1];
        int[] primes = new int[LIMIT+1];
        boolean[] isComp = new boolean[LIMIT+1];
        mu[1]=1;
        int pcnt=0;
        for(int i=2;i<=LIMIT;i++){
            if(!isComp[i]){ primes[pcnt++]=i; mu[i]=-1; }
            for(int j=0;j<pcnt && i*primes[j]<=LIMIT;j++){
                isComp[i*primes[j]]=true;
                if(i % primes[j]==0){ mu[i*primes[j]]=0; break; }
                else mu[i*primes[j]]=-mu[i];
            }
        }
        pref[0]=0;
        for(int i=1;i<=LIMIT;i++) pref[i]=pref[i-1]+mu[i];
    }
    static long getM(long n){
        if(n<=LIMIT) return pref[(int)n];
        Long cached=memo.get(n);
        if(cached!=null) return cached;
        long res=1;
        long l=2;
        while(l<=n){
            long q=n / l;
            long r=n / q;
            long sub=getM(q);
            res -= (r - l + 1) * sub;
            l = r + 1;
        }
        memo.put(n, res);
        return res;
    }
    static long modPow(long a,long e,long mod){
        long r=1%mod; a%=mod;
        while(e>0){
            if((e&1)==1) r=mulMod(r,a,mod);
            a=mulMod(a,a,mod);
            e>>=1;
        }
        return r;
    }
    // (a*b) % mod 安全乘法，利用 BigInteger（a,b <1e12，乘积<1e24，直接用BigInteger可接受）
    static long mulMod(long a,long b,long mod){
        // 快速路径：a,b < 1e6时直接乘
        // 但通用用BigInteger
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
        // 若需更快可用拆分：
    }
    static long invMod(long a,long mod){
        // mod为质数，a<mod，使用扩展欧几里德更快
        long m=mod, u=1, v=0;
        long aa=a, bb=mod;
        // 扩展欧几里德求逆：aa*u + bb*v = gcd
        long x=0, y=1;
        // 使用标准迭代
        long x0=1, y0=0, x1=0, y1=1;
        // 简化：求 a^{-1} mod mod
        long b=mod;
        long u0=1, u1=0;
        // 实际上用经典
        long a0=a, m0=mod;
        long inv=0;
        // 使用BigInteger的modInverse亦可
        return BigInteger.valueOf(a).modInverse(BigInteger.valueOf(mod)).longValue();
    }

    public static void main(String[] args) throws Exception{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in,"UTF-8"));
        String line=br.readLine();
        while(line!=null && line.trim().isEmpty()) line=br.readLine();
        if(line==null) return;
        StringTokenizer st=new StringTokenizer(line);
        long n=Long.parseLong(st.nextToken());
        long p=Long.parseLong(st.nextToken());
        if(n==1){
            System.out.println(1 % p);
            return;
        }
        sieve();
        memo.clear();
        // 预热计算M(n)以填充memo，利于后续区间查询命中
        getM(n);
        long total=0; // Σ mu(d)*k/(n-k) 模p
        long l=2;
        while(l<=n){
            long k=n / l;
            if(k==0) break;
            long r=n / k;
            if(r>n) r=n;
            // 区间 [l,r] cnt=k
            long sumMu = getM(r) - getM(l-1);
            if(sumMu!=0){
                long sumMuMod = ((sumMu % p) + p) % p;
                long kMod = k % p;
                long denom = n - k; // 1..n-1
                long inv = invMod(denom % p, p);
                long term = mulMod(mulMod(sumMuMod, kMod, p), inv, p);
                total += term;
                if(total>=p) total-=p;
                if(total<0) total+=p;
                total %= p;
            }
            l = r + 1;
        }
        long E = (1 - total) % p;
        if(E<0) E+=p;
        System.out.println(E % p);
    }
}
