import java.util.ArrayList;
import java.util.Scanner;

/**
 * L1-110 这不是字符串题
 * 原理：用可变序列模拟三种操作
 *  1查找替换：暴力查找第一个连续子序列等于 find，若找到则删除并插入 replace
 *  2插入均值：遍历相邻两数，若和为偶数则插入平均值，注意插入后跳过新元素避免本轮重复判断
 *  3翻转：双指针交换 [l-1, r-1] 区间
 * 长度保证不超过 100*N，N,M <=1000
 * 时间复杂度 最坏 O(M * L^2) 但约束内可通过，空间 O(L)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < N; i++) a.add(sc.nextInt());
        for (int opIdx = 0; opIdx < M; opIdx++) {
            int type = sc.nextInt();
            if (type == 1) {
                int L1 = sc.nextInt();
                int[] find = new int[L1];
                for (int i = 0; i < L1; i++) find[i] = sc.nextInt();
                int L2 = sc.nextInt();
                int[] repl = new int[L2];
                for (int i = 0; i < L2; i++) repl[i] = sc.nextInt();
                int pos = -1;
                // 查找第一个出现位置
                outer:
                for (int i = 0; i + L1 <= a.size(); i++) {
                    for (int j = 0; j < L1; j++) {
                        if (!a.get(i + j).equals(find[j])) continue outer;
                    }
                    pos = i;
                    break;
                }
                if (pos >= 0) {
                    // 删除 L1，插入 L2
                    for (int i = 0; i < L1; i++) a.remove(pos);
                    for (int i = 0; i < L2; i++) a.add(pos + i, repl[i]);
                }
            } else if (type == 2) {
                // 相邻和为偶数插入均值
                for (int i = 0; i + 1 < a.size(); i++) {
                    int x = a.get(i);
                    int y = a.get(i + 1);
                    if ((x + y) % 2 == 0) {
                        int mid = (x + y) / 2;
                        a.add(i + 1, mid);
                        i++; // 跳过刚插入的元素
                    }
                }
            } else { // type 3
                int l = sc.nextInt();
                int r = sc.nextInt();
                l--; r--; // 转 0 基
                while (l < r) {
                    int tmp = a.get(l);
                    a.set(l, a.get(r));
                    a.set(r, tmp);
                    l++; r--;
                }
            }
        }
        sc.close();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(a.get(i));
        }
        System.out.println(sb.toString());
    }
}
