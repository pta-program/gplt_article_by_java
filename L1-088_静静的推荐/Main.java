import java.util.Scanner;

/**
 * L1-088 静静的推荐
 * 实现原理：PAT 达线者可与同分前项并列进入，直接计入；其余合格者每个天梯赛分数
 * 最多只能在 K 个批次各出现一次，故对每个分数最多计 min(人数,K)。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(), k = scanner.nextInt(), interviewLine = scanner.nextInt();
        int[] ordinary = new int[291];
        int recommended = 0;
        while (n-- > 0) {
            int score = scanner.nextInt(), pat = scanner.nextInt();
            if (score < 175) continue;
            if (pat >= interviewLine) recommended++;
            else ordinary[score]++;
        }
        for (int score = 175; score <= 290; score++) recommended += Math.min(k, ordinary[score]);
        System.out.println(recommended);
    }
}
