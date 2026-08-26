import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-025 正整数A+B
 * 实现原理：第一个空格将整行分为 A 与 B；分别检查是否仅含数字且数值在 1..1000。
 * 两者合法才计算和，否则把非法位置和结果替换为问号。
 * 时间复杂度 O(|A|+|B|)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
        int split = line.indexOf(' ');
        String aText = line.substring(0, split);
        String bText = line.substring(split + 1);
        Integer a = parsePositive(aText);
        Integer b = parsePositive(bText);
        String left = a == null ? "?" : String.valueOf(a);
        String right = b == null ? "?" : String.valueOf(b);
        String sum = a == null || b == null ? "?" : String.valueOf(a + b);
        System.out.println(left + " + " + right + " = " + sum);
    }

    private static Integer parsePositive(String text) {
        if (text.isEmpty()) return null;
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) return null;
        }
        try {
            int value = Integer.parseInt(text);
            return value >= 1 && value <= 1000 ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
