import java.util.Scanner;

/**
 * L1-023 输出GPLT
 * 实现原理：统计 G、P、L、T（忽略大小写）的数量，然后循环按 GPLT 顺序
 * 每轮各输出一个仍有剩余的字符，直到所有计数归零。
 * 时间复杂度 O(n)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String input = new Scanner(System.in).next().toUpperCase();
        char[] order = {'G', 'P', 'L', 'T'};
        int[] count = new int[4];
        for (char c : input.toCharArray()) {
            for (int i = 0; i < 4; i++) {
                if (c == order[i]) count[i]++;
            }
        }
        StringBuilder answer = new StringBuilder();
        boolean remaining = true;
        while (remaining) {
            remaining = false;
            for (int i = 0; i < 4; i++) {
                if (count[i] > 0) {
                    answer.append(order[i]);
                    count[i]--;
                    remaining = true;
                }
            }
        }
        System.out.println(answer);
    }
}
