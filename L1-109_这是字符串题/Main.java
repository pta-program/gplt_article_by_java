import java.util.Scanner;

/**
 * L1-109 这是字符串题
 * 原理：统计字符串 S 中每个小写字母出现次数，累加权值得到美观分
 * 计数数组 cnt[26]，遍历 S 计数；再读入 26 个权值 w[i]，sum += cnt[i]*w[i]
 * 时间复杂度 O(|S|+26)，空间 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.next();
        int[] cnt = new int[26];
        for (char ch : s.toCharArray()) {
            cnt[ch - 'a']++;
        }
        long sum = 0;
        for (int i = 0; i < 26; i++) {
            int w = sc.nextInt();
            sum += (long) cnt[i] * w;
        }
        sc.close();
        // 输出计数
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (i > 0) sb.append(' ');
            sb.append(cnt[i]);
        }
        System.out.println(sb.toString());
        System.out.println(sum);
    }
}
