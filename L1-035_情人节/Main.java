import java.util.Scanner;

/**
 * L1-035 情人节
 * 实现原理：顺序读取名单，只记录第 2 个和第 14 个名字，读到句点结束后按记录情况
 * 输出对应文案。
 * 时间复杂度 O(N)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String second = null, fourteenth = null;
        int count = 0;
        while (scanner.hasNext()) {
            String name = scanner.next();
            if (name.equals(".")) break;
            count++;
            if (count == 2) second = name;
            if (count == 14) fourteenth = name;
        }
        if (fourteenth != null) System.out.println(second + " and " + fourteenth + " are inviting you to dinner...");
        else if (second != null) System.out.println(second + " is the only one for you...");
        else System.out.println("Momo... No one is for you ...");
    }
}
