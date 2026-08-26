import java.util.Scanner;

/**
 * L1-055 谁是赢家
 * 实现原理：统计投给 a 的评委人数。a 在观众领先且至少一票评委支持，或观众落后
 * 但三票评委全支持时胜出；否则 b 胜出。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pa = scanner.nextInt(), pb = scanner.nextInt();
        int judgeA = 0;
        for (int i = 0; i < 3; i++) if (scanner.nextInt() == 0) judgeA++;
        int judgeB = 3 - judgeA;
        boolean aWins = (pa > pb && judgeA >= 1) || (pa < pb && judgeA == 3);
        if (aWins) System.out.println("The winner is a: " + pa + " + " + judgeA);
        else System.out.println("The winner is b: " + pb + " + " + judgeB);
    }
}
