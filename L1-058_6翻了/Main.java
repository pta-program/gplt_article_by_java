import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-058 6翻了
 * 实现原理：从左到右识别每段连续的字符 6，根据长度决定保留原串、替换成 9 或 27，
 * 其他字符直接追加。
 * 时间复杂度 O(n)，空间复杂度 O(n)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String text = new BufferedReader(new InputStreamReader(System.in)).readLine();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < text.length();) {
            if (text.charAt(i) != '6') {
                output.append(text.charAt(i++));
                continue;
            }
            int end = i;
            while (end < text.length() && text.charAt(end) == '6') end++;
            int count = end - i;
            if (count <= 3) output.append("6".repeat(count));
            else if (count <= 9) output.append('9');
            else output.append("27");
            i = end;
        }
        System.out.println(output);
    }
}
