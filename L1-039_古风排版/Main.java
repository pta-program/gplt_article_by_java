import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-039 古风排版
 * 实现原理：
 * 古文从右向左竖排，每列 N 个字符。
 * 设文本长 len，列数 cols = ceil(len / N)，总容量 rows*cols，
 * 不足部分在末尾（最左列的下方）补空格，即 padded = text + 空格。
 * 然后按从右列到左列、每列从上到下依次填入 padded 的字符，
 * 最后逐行输出。
 * 时间复杂度 O(rows*cols)，空间复杂度 O(rows*cols)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String first = reader.readLine();
        if (first == null || first.trim().isEmpty()) return;
        int rows = Integer.parseInt(first.trim());
        String text = reader.readLine();
        if (text == null) text = "";
        int len = text.length();
        int cols = (len + rows - 1) / rows;
        if (cols == 0) cols = 1;
        int total = rows * cols;
        // 末尾补空格至 total 长度（而非开头补）
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < total) sb.append(' ');
        String padded = sb.toString();

        char[][] page = new char[rows][cols];
        int idx = 0;
        for (int c = cols - 1; c >= 0; c--) {
            for (int r = 0; r < rows; r++) {
                page[r][c] = padded.charAt(idx++);
            }
        }
        for (int r = 0; r < rows; r++) {
            System.out.println(new String(page[r]));
        }
    }
}
