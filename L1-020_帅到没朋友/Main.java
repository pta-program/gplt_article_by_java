import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * L1-020 帅到没朋友
 * 实现原理：只要某人出现于人数大于 1 的朋友圈，就标记为“有朋友”。
 * 查询时以集合去重，输出尚未标记的 ID，并保持原查询顺序。
 * 时间复杂度 O(朋友圈人数+M)，空间复杂度 O(不同 ID 数)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Set<String> hasFriends = new HashSet<>();
        int groups = scanner.nextInt();
        for (int i = 0; i < groups; i++) {
            int k = scanner.nextInt();
            for (int j = 0; j < k; j++) {
                String id = scanner.next();
                if (k > 1) hasFriends.add(id);
            }
        }
        int m = scanner.nextInt();
        Set<String> printed = new HashSet<>();
        StringBuilder answer = new StringBuilder();
        for (int i = 0; i < m; i++) {
            String id = scanner.next();
            if (!hasFriends.contains(id) && printed.add(id)) {
                if (answer.length() > 0) answer.append(' ');
                answer.append(id);
            }
        }
        System.out.println(answer.length() == 0 ? "No one is handsome" : answer);
    }
}
