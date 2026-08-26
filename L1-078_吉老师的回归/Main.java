import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * L1-078 吉老师的回归
 * 实现原理：按顺序跳过包含 qiandao 或 easy 的签到题。每遇到非签到题，若此前
 * 已完成数量正好为 M，该题就是当前题；否则将已完成数加一。
 * 时间复杂度 O(N*每行长度)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(tokenizer.nextToken());
        int done = Integer.parseInt(tokenizer.nextToken());
        for (int i = 0; i < n; i++) {
            String problem = reader.readLine();
            if (problem.contains("qiandao") || problem.contains("easy")) continue;
            if (done == 0) {
                System.out.println(problem);
                return;
            }
            done--;
        }
        System.out.println("Wo AK le");
    }
}
