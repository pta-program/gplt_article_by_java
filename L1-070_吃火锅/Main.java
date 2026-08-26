import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-070 吃火锅
 * 实现原理：逐行读取到单独句点为止，统计总消息数与包含目标子串的消息数，
 * 并在首次包含时记录其从 1 开始的位置。
 * 时间复杂度 O(总字符数)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int total = 0, hits = 0, first = -1;
        String line;
        while (!(line = reader.readLine()).equals(".")) {
            total++;
            if (line.contains("chi1 huo3 guo1")) {
                hits++;
                if (first == -1) first = total;
            }
        }
        System.out.println(total);
        System.out.println(hits == 0 ? "-_-#" : first + " " + hits);
    }
}
