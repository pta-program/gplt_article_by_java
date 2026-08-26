import java.util.Scanner;

/**
 * L1-007 念数字
 * 实现原理：将输入保留为字符串，避免负数绝对值边界问题；负号单独映射为 fu，
 * 其他字符利用字符与 '0' 的差值查表输出。
 * 时间复杂度 O(k)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String[] pinyin = {"ling", "yi", "er", "san", "si", "wu", "liu", "qi", "ba", "jiu"};
        String input = new Scanner(System.in).next();
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            String word = input.charAt(i) == '-' ? "fu" : pinyin[input.charAt(i) - '0'];
            if (output.length() > 0) output.append(' ');
            output.append(word);
        }
        System.out.println(output);
    }
}
