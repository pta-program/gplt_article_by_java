import java.util.Arrays;
import java.util.Scanner;

/**
 * L1-010 比较大小
 * 实现原理：读入三个数后用库排序升序排列，再按题目分隔符输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] values = {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
        Arrays.sort(values);
        System.out.println(values[0] + "->" + values[1] + "->" + values[2]);
    }
}
