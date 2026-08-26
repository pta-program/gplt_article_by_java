import java.util.Scanner;

/**
 * L1-063 吃鱼还是吃肉
 * 实现原理：由性别确定身高和体重标准值，分别比较后输出两条建议。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n-- > 0) {
            int gender = scanner.nextInt();
            int height = scanner.nextInt();
            int weight = scanner.nextInt();
            int standardHeight = gender == 1 ? 130 : 129;
            int standardWeight = gender == 1 ? 27 : 25;
            String heightAdvice = height < standardHeight ? "duo chi yu!" : height > standardHeight ? "ni li hai!" : "wan mei!";
            String weightAdvice = weight < standardWeight ? "duo chi rou!" : weight > standardWeight ? "shao chi rou!" : "wan mei!";
            System.out.println(heightAdvice + " " + weightAdvice);
        }
    }
}
