import java.util.Scanner;

/**
 * L1-044 稳赢
 * 实现原理：普通回合输出克制对方的手势；每累计 K 次普通回合后，下一个回合原样输出
 * 对方手势以形成平局，然后重新计数。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        int winsSinceDraw = 0;
        while (scanner.hasNext()) {
            String opponent = scanner.next();
            if (opponent.equals("End")) break;
            if (winsSinceDraw == k) {
                System.out.println(opponent);
                winsSinceDraw = 0;
            } else {
                if (opponent.equals("ChuiZi")) System.out.println("Bu");
                else if (opponent.equals("JianDao")) System.out.println("ChuiZi");
                else System.out.println("JianDao");
                winsSinceDraw++;
            }
        }
    }
}
