import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * L1-064 估值一亿的AI核心代码
 * 实现原理：
 * 1) 原样打印输入行。
 * 2) 规范化空格并转换大小写：压缩单词间多余空格、删除行首尾空格、删除标点前空格；
 *    将所有大写字母转小写，但独立的 'I'（大写）保持不变。
 * 3) 替换独立短语与代词：先用占位符暂存 "can you"/"could you" 的替换结果
 *    "I can"/"I could"，再将独立的 "I" 与 "me" 换成 "you"，最后还原占位符，
 *    以避免新产生的 "I" 被二次替换。独立指被非字母数字字符（空格或标点）分隔。
 * 4) 将 '?' 全部换成 '!' 并以 "AI: " 前缀输出。
 * 时间复杂度 O(每行长度)，空间复杂度 O(每行长度)。
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine());
        while (n-- > 0) {
            String original = reader.readLine();
            if (original == null) original = "";
            System.out.println(original);
            String answer = normalizeAndLowercase(original);
            // 用不可见占位符保护 "I can"/"I could" 中的 I 避免被后续 I->you 覆盖
            final String PH_CAN = "\u0001";
            final String PH_COULD = "\u0002";
            answer = answer.replaceAll("(?<![A-Za-z0-9])can you(?![A-Za-z0-9])", PH_CAN);
            answer = answer.replaceAll("(?<![A-Za-z0-9])could you(?![A-Za-z0-9])", PH_COULD);
            answer = answer.replaceAll("(?<![A-Za-z0-9])I(?![A-Za-z0-9])", "you");
            answer = answer.replaceAll("(?<![A-Za-z0-9])me(?![A-Za-z0-9])", "you");
            answer = answer.replace(PH_CAN, "I can");
            answer = answer.replace(PH_COULD, "I could");
            answer = answer.replace('?', '!');
            System.out.println("AI: " + answer);
        }
    }

    /**
     * 规范化空格并转换大小写：
     * - 多个空格压缩为一个，且仅在字母数字间保留；
     * - 删除行首尾空格及标点前的空格；
     * - 除 'I' 外所有大写转小写。
     */
    private static String normalizeAndLowercase(String text) {
        StringBuilder result = new StringBuilder();
        boolean pendingSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ') {
                if (result.length() > 0) pendingSpace = true;
                continue;
            }
            // 仅在后继为字母数字时补空格，标点前不保留
            if (pendingSpace && Character.isLetterOrDigit(c)) result.append(' ');
            pendingSpace = false;
            result.append(c == 'I' ? 'I' : Character.toLowerCase(c));
        }
        return result.toString();
    }
}
