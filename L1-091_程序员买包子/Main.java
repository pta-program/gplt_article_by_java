import java.util.Scanner;

/**
 * L1-091 程序员买包子
 * 实现原理：根据最终购买数量分别与原计划 N、看到商品后的数量 M 比较，
 * 决定三种固定结论之一。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String item = scanner.next();
        int m = scanner.nextInt(), actual = scanner.nextInt();
        String result = actual == n ? "mei you" : actual == m ? "kan dao le" : "wang le zhao";
        System.out.println(result + " mai " + item + " de");
    }
}
