import java.io.*;
import java.util.*;

/**
 * L3-038 工业园区建设
 * 思路：对每个仓库位置 i，可新建的工厂必取离 i 最近的 M 个空地（距离最小的 M 个），
 *       因此 i 的答案是 “已有工厂 + 最近 M 个空地” 这一多重集里 k 个最小距离之和。
 *       利用位置 1..N 的前缀计数/前缀和，可 O(1) 求任意区间 [i-R,i+R] 内已有工厂/空地的
 *       数量与距离和。通过二分查找：
 *         1) Re = 使空地数量 >= M 的最小半径，求出最近 M 个空地的距离和 sumM[i]；
 *         2) Rk = 使 已有工厂数 + min(空地数,M) >= k 的最小半径，答案为
 *            sum(Rk-1) + (k - cnt(Rk-1)) * Rk。
 *       全部使用前缀和 O(1) 查询，整体 O(N log N)。
 * 时间复杂度: 每组 O(N log N)，ΣN ≤ 5e5
 * 空间复杂度: O(N)
 */
public class Main {

    // 区间 [l,r] 内数量
    static int cntIn(int[] pref, int l, int r) {
        if (l > r) return 0;
        return pref[r] - pref[l - 1];
    }

    // 区间 [l,r] 内已工厂/空地到 i 的距离和，prefC 为计数前缀，prefS 为位置和前缀
    static long sumDistIn(int i, int R, int N, int[] prefC, long[] prefS) {
        int l = Math.max(1, i - R);
        int r = Math.min(N, i + R);
        // 左侧 [l, i]
        int cntL = prefC[i] - prefC[l - 1];
        long sumL = prefS[i] - prefS[l - 1];
        long dL = (long) cntL * i - sumL;
        // 右侧 [i+1, r]
        int cntR = prefC[r] - prefC[i];
        long sumR = prefS[r] - prefS[i];
        long dR = sumR - (long) cntR * i;
        return dL + dR;
    }

    static int cntEInside(int i, int R, int N, int[] prefCntE) {
        int l = Math.max(1, i - R);
        int r = Math.min(N, i + R);
        return prefCntE[r] - prefCntE[l - 1];
    }

    static int cntAInside(int i, int R, int N, int[] prefCntA) {
        int l = Math.max(1, i - R);
        int r = Math.min(N, i + R);
        return prefCntA[r] - prefCntA[l - 1];
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out, "UTF-8"));
        String line;
        // 读取 T，跳过空行
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int T = Integer.parseInt(line.trim());
        // 为防止字符串中包含空格，按 token 读取更稳健，但 S 是 0/1 串，我们用行读取
        for (int tc = 0; tc < T; tc++) {
            // 读取 N M k
            do {
                line = br.readLine();
            } while (line != null && line.trim().isEmpty());
            if (line == null) break;
            StringTokenizer st = new StringTokenizer(line);
            while (st.countTokens() < 3) {
                // 若一行未读完，继续读下一行（防御）
                String extra = br.readLine();
                if (extra == null) break;
                line += " " + extra;
                st = new StringTokenizer(line);
            }
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            // 读取 S，可能包含空格
            String S = "";
            while (S.length() < N) {
                String sLine = br.readLine();
                if (sLine == null) break;
                sLine = sLine.trim().replaceAll("\\s+", "");
                if (sLine.isEmpty()) continue;
                S += sLine;
            }
            if (S.length() > N) S = S.substring(0, N);

            int[] prefCntA = new int[N + 1];
            long[] prefSumA = new long[N + 1];
            int[] prefCntE = new int[N + 1];
            long[] prefSumE = new long[N + 1];
            int totalE = 0;
            for (int i = 1; i <= N; i++) {
                char ch = S.charAt(i - 1);
                boolean isA = (ch == '1');
                prefCntA[i] = prefCntA[i - 1] + (isA ? 1 : 0);
                prefSumA[i] = prefSumA[i - 1] + (isA ? i : 0);
                prefCntE[i] = prefCntE[i - 1] + (isA ? 0 : 1);
                prefSumE[i] = prefSumE[i - 1] + (isA ? 0 : i);
                if (!isA) totalE++;
            }
            // 如果 M 大于空地总数，实际可建数量为 totalE
            int effM = Math.min(M, totalE);

            long[] sumM = null;
            if (effM > 0) {
                sumM = new long[N + 1];
                // 计算每个 i 的最近 effM 个空地距离和
                for (int i = 1; i <= N; i++) {
                    // 二分 Re
                    int lo = 0, hi = N; // hi 足够大
                    while (lo < hi) {
                        int mid = (lo + hi) >>> 1;
                        int cnt = cntEInside(i, mid, N, prefCntE);
                        if (cnt >= effM) hi = mid;
                        else lo = mid + 1;
                    }
                    int Re = lo;
                    if (Re == 0) {
                        sumM[i] = 0;
                    } else {
                        long sumBefore = sumDistIn(i, Re - 1, N, prefCntE, prefSumE);
                        int cntBefore = cntEInside(i, Re - 1, N, prefCntE);
                        long need = effM - cntBefore;
                        sumM[i] = sumBefore + need * Re;
                    }
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= N; i++) {
                // 二分 Rk
                int lo = 0, hi = N;
                while (lo < hi) {
                    int mid = (lo + hi) >>> 1;
                    int cntA = cntAInside(i, mid, N, prefCntA);
                    int cntE = cntEInside(i, mid, N, prefCntE);
                    int total = cntA + Math.min(cntE, effM);
                    if (total >= k) hi = mid;
                    else lo = mid + 1;
                }
                int Rk = lo;
                long ans;
                if (Rk == 0) {
                    ans = 0;
                } else {
                    int cntA_before = cntAInside(i, Rk - 1, N, prefCntA);
                    long sumA_before = sumDistIn(i, Rk - 1, N, prefCntA, prefSumA);
                    int cntE_before = cntEInside(i, Rk - 1, N, prefCntE);
                    long totalSumBefore;
                    int totalCntBefore;
                    if (effM == 0) {
                        totalSumBefore = sumA_before;
                        totalCntBefore = cntA_before;
                    } else if (cntE_before >= effM) {
                        totalSumBefore = sumA_before + sumM[i];
                        totalCntBefore = cntA_before + effM;
                    } else {
                        long sumE_before = sumDistIn(i, Rk - 1, N, prefCntE, prefSumE);
                        totalSumBefore = sumA_before + sumE_before;
                        totalCntBefore = cntA_before + cntE_before;
                    }
                    int need = k - totalCntBefore;
                    ans = totalSumBefore + (long) need * Rk;
                }
                sb.append(ans);
                if (i < N) sb.append(' ');
            }
            bw.write(sb.toString());
            if (tc < T - 1) bw.newLine();
        }
        bw.flush();
    }
}
