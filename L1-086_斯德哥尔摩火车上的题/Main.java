import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-086 斯德哥尔摩火车上的题
 * 实现原理：逐对相邻数字检查奇偶性；奇偶相同则向结果追加二者较大的字符。分别处理
 * 两条原串后，结果相等只输出一次，否则依序输出两个结果。
 * 时间复杂度 O(|A|+|B|)，空间复杂度 O(|A|+|B|)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String first = transform(reader.readLine());
        String second = transform(reader.readLine());
        System.out.println(first);
        if (!first.equals(second)) System.out.println(second);
    }

    private static String transform(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i < text.length(); i++) {
            int current = text.charAt(i) - '0';
            int previous = text.charAt(i - 1) - '0';
            if (current % 2 == previous % 2) result.append(Math.max(current, previous));
        }
        return result.toString();
    }
}
