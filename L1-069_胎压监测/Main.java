import java.util.Scanner;

/**
 * L1-069 胎压监测
 * 实现原理：以四轮最大胎压为参照，低于最低值或与最大值差超过阈值即为异常；
 * 根据异常轮胎数量输出正常、单轮或全轮检查提示。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] pressure = new int[4];
        int max = 0;
        for (int i = 0; i < 4; i++) {
            pressure[i] = scanner.nextInt();
            max = Math.max(max, pressure[i]);
        }
        int minimum = scanner.nextInt(), threshold = scanner.nextInt();
        int abnormalCount = 0, abnormalIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (pressure[i] < minimum || max - pressure[i] > threshold) {
                abnormalCount++;
                abnormalIndex = i;
            }
        }
        if (abnormalCount == 0) System.out.println("Normal");
        else if (abnormalCount == 1) System.out.println("Warning: please check #" + (abnormalIndex + 1) + "!");
        else System.out.println("Warning: please check all the tires!");
    }
}
