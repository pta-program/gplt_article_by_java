import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-011 A-B
 * 实现原理：使用布尔数组标记 B 中出现的字符，遍历 A 仅保留未被标记的字符。
 * A、B 可能包含空格，需用 BufferedReader.readLine 保留空白；B 可能为空行或缺失，需做 null 保护。
 * 字符集按 Unicode BMP 处理（65536），兼容可见 ASCII 与空白字符。
 * 时间复杂度 O(|A|+|B|)，空间复杂度 O(C) C=字符集大小。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String a = reader.readLine();
        String b = reader.readLine();
        if (a == null) a = "";
        if (b == null) b = "";
        // 使用 65536 覆盖所有单 char，避免 128 越界（若 B 含扩展字符）
        boolean[] removed = new boolean[65536];
        for (int i = 0; i < b.length(); i++) {
            removed[b.charAt(i)] = true;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            char c = a.charAt(i);
            if (!removed[c]) result.append(c);
        }
        System.out.println(result.toString());
    }
}
