import java.util.Scanner;

/**
 * L1-061 新胖子公式
 * 实现原理：BMI=体重/身高²；按一位小数输出，再依据未舍入的数值是否大于 25 判断。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double weight = scanner.nextDouble(), height = scanner.nextDouble();
        double bmi = weight / (height * height);
        System.out.printf("%.1f%n", bmi);
        System.out.println(bmi > 25 ? "PANG" : "Hai Xing");
    }
}
