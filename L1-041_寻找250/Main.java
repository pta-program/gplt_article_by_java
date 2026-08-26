import java.util.Scanner;

/**
 * L1-041 寻找250
 * 实现原理：按输入顺序计数，首次读取到 250 时输出其一开始的序号并结束。
 * 时间复杂度 O(首次出现位置)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int position = 0;
        while (scanner.hasNextInt()) {
            position++;
            if (scanner.nextInt() == 250) {
                System.out.println(position);
                return;
            }
        }
    }
}
