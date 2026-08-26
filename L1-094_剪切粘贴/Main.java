import java.util.Scanner;

/**
 * L1-094 剪切粘贴
 * 实现原理：每次先按 1 基闭区间截取剪贴板并从正文删除；随后在正文中查找最早出现的
 * “前串+后串”，在二者之间插入剪贴板，查找失败则追加到末尾。
 * 时间复杂度 O(操作次数×字符串长度)，空间复杂度 O(字符串长度)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.next();
        int operations = scanner.nextInt();
        while (operations-- > 0) {
            int left = scanner.nextInt(), right = scanner.nextInt();
            String before = scanner.next(), after = scanner.next();
            String clipboard = text.substring(left - 1, right);
            text = text.substring(0, left - 1) + text.substring(right);
            int found = text.indexOf(before + after);
            if (found < 0) text += clipboard;
            else {
                int insertion = found + before.length();
                text = text.substring(0, insertion) + clipboard + text.substring(insertion);
            }
        }
        System.out.println(text);
    }
}
