import java.util.Scanner;

/**
 * L1-107 高温补贴
 * 原理：判断是否达到高温补贴条件：T>=35 且 t>=33
 *   - 若高温且露天工作(S==1) -> Bu Tie
 *   - 若高温且室内(S==0) -> Shi Nei
 *   - 否则若露天 -> Bu Re
 *   - 否则 -> Shu Shi
 * 第二行输出：高温时输出 T，否则输出 t（与题面“BuTie/ShiNei->T, BuRe/ShuShi->t”一致）
 * 时间复杂度 O(1)，空间 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        int S = sc.nextInt();
        int t = sc.nextInt();
        sc.close();
        boolean high = (T >= 35 && t >= 33);
        if (S == 1) {
            if (high) System.out.println("Bu Tie");
            else System.out.println("Bu Re");
        } else {
            if (high) System.out.println("Shi Nei");
            else System.out.println("Shu Shi");
        }
        // 高温输出 T，否则输出 t
        System.out.println(high ? T : t);
    }
}
