import java.util.Scanner;

/**
 * L1-074 两小时学完C语言
 * 实现原理：已阅读字数为每分钟阅读量乘阅读分钟数，用总字数减去该值。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int total = scanner.nextInt(), perMinute = scanner.nextInt(), minutes = scanner.nextInt();
        System.out.println(total - perMinute * minutes);
    }
}
