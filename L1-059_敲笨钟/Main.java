import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-059 敲笨钟
 * 实现原理：分别取上下半句的末个拼音（去掉末句句点）判断是否都以 ong 结尾。
 * 合韵时保留上半句，并将下半句最后三个词替换成 qiao ben zhong.
 * 时间复杂度 O(每行长度)，空间复杂度 O(每行长度)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        while (n-- > 0) {
            String line = reader.readLine();
            int comma = line.indexOf(',');
            String first = line.substring(0, comma).trim();
            String second = line.substring(comma + 1).trim();
            String firstLast = lastWord(first);
            String secondLast = lastWord(second.substring(0, second.length() - 1));
            if (!firstLast.endsWith("ong") || !secondLast.endsWith("ong")) {
                System.out.println("Skipped");
                continue;
            }
            String[] words = second.substring(0, second.length() - 1).split(" ");
            StringBuilder changed = new StringBuilder(first).append(", ");
            for (int i = 0; i < words.length - 3; i++) changed.append(words[i]).append(' ');
            changed.append("qiao ben zhong.");
            System.out.println(changed);
        }
    }

    private static String lastWord(String sentence) {
        int space = sentence.lastIndexOf(' ');
        return sentence.substring(space + 1);
    }
}
