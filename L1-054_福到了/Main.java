import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * L1-054 福到了
 * 实现原理：
 * 将 N×N 网格绕中心旋转 180 度：目标位置 (i,j) 取原位置 (N-1-i, N-1-j)，
 * 并将原字符 '@' 替换为裁判指定字符，空格保持不变。
 * 先比较旋转前后图形是否完全一致（以 '@' 与空格判断形状），若一致则先输出
 * "bu yong dao le" 再输出旋转后的图形。为兼容输入行末尾空格被截断的情况，
 * 读取每行后若长度不足 N 则用空格补齐。
 * 时间复杂度 O(N^2)，空间复杂度 O(N^2)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        char replacement = tokenizer.nextToken().charAt(0);
        int n = Integer.parseInt(tokenizer.nextToken());
        char[][] grid = new char[n][n];
        for (int i = 0; i < n; i++) {
            String line = reader.readLine();
            if (line == null) line = "";
            // 补齐因行末空格被省略而导致的长度不足
            if (line.length() < n) {
                StringBuilder sb = new StringBuilder(line);
                while (sb.length() < n) sb.append(' ');
                line = sb.toString();
            } else if (line.length() > n) {
                line = line.substring(0, n);
            }
            for (int j = 0; j < n; j++) {
                grid[i][j] = line.charAt(j);
            }
        }

        boolean same = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != grid[n - 1 - i][n - 1 - j]) {
                    same = false;
                }
            }
        }
        if (same) System.out.println("bu yong dao le");
        for (int i = n - 1; i >= 0; i--) {
            StringBuilder line = new StringBuilder();
            for (int j = n - 1; j >= 0; j--) {
                line.append(grid[i][j] == '@' ? replacement : ' ');
            }
            System.out.println(line);
        }
    }
}
