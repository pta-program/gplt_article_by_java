import java.util.Scanner;

/**
 * L1-004 计算摄氏温度
 * 实现原理：按照题目给定的整型公式 C=5*(F-32)/9 直接计算并格式化输出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int fahrenheit = scanner.nextInt();
        int celsius = 5 * (fahrenheit - 32) / 9;
        System.out.println("Celsius = " + celsius);
    }
}
