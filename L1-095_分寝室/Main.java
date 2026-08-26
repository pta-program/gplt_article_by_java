import java.util.Scanner;

/**
 * L1-095 分寝室
 * 实现原理：枚举女生寝室数 g，男生寝室数即 n-g。两者都必须整除各自人数且每间至少
 * 两人；在合法方案中选择每间人数差绝对值最小的一组。
 * 时间复杂度 O(n)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int girls = scanner.nextInt(), boys = scanner.nextInt(), rooms = scanner.nextInt();
        int bestGirlsRooms = -1, bestDifference = Integer.MAX_VALUE;
        for (int girlRooms = 1; girlRooms < rooms; girlRooms++) {
            int boyRooms = rooms - girlRooms;
            if (girls % girlRooms != 0 || boys % boyRooms != 0) continue;
            int girlsPerRoom = girls / girlRooms, boysPerRoom = boys / boyRooms;
            if (girlsPerRoom < 2 || boysPerRoom < 2) continue;
            int difference = Math.abs(girlsPerRoom - boysPerRoom);
            if (difference < bestDifference) {
                bestDifference = difference;
                bestGirlsRooms = girlRooms;
            }
        }
        if (bestGirlsRooms == -1) System.out.println("No Solution");
        else System.out.println(bestGirlsRooms + " " + (rooms - bestGirlsRooms));
    }
}
