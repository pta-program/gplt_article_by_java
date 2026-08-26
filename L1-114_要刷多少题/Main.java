import java.util.Scanner;

/**
 * L1-114 要刷多少题
 * 原理：每年 15 题，n 年共 n*15 题
 * 时间复杂度 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();
        System.out.println(n * 15);
    }
}
