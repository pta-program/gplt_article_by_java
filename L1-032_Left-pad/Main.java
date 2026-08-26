import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * L1-032 Left-pad
 * 实现原理：若原串过长，取其最后 N 个字符；否则在前面补足 N-长度 个填充字符。
 * 用 BufferedReader 读取第二行，确保原串内空格被保留。
 * 时间复杂度 O(N)，空间复杂度 O(N)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(tokenizer.nextToken());
        char pad = tokenizer.nextToken().charAt(0);
        String text = reader.readLine();
        if (text.length() >= n) System.out.println(text.substring(text.length() - n));
        else System.out.println(String.valueOf(pad).repeat(n - text.length()) + text);
    }
}
