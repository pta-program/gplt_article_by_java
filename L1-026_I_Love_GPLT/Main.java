/**
 * L1-026 I Love GPLT
 * 实现原理：遍历固定字符串，逐字符（含空格）独占一行输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        String text = "I Love GPLT";
        for (char c : text.toCharArray()) System.out.println(c);
    }
}
