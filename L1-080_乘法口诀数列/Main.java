import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * L1-080 乘法口诀数列
 * 实现原理：序列当前相邻两项相乘；积为两位数时依次追加十位、个位，否则追加一位，
 * 直到项数达到 n。
 * 时间复杂度 O(n)，空间复杂度 O(n)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> sequence = new ArrayList<>();
        sequence.add(scanner.nextInt());
        sequence.add(scanner.nextInt());
        int n = scanner.nextInt();
        int index = 0;
        while (sequence.size() < n) {
            int product = sequence.get(index) * sequence.get(index + 1);
            if (product >= 10) sequence.add(product / 10);
            if (sequence.size() < n) sequence.add(product % 10);
            index++;
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) System.out.print(' ');
            System.out.print(sequence.get(i));
        }
    }
}
