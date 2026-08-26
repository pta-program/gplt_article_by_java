import java.util.*;

/**
 * L1-117 做什么都被骂怎么办
 * 原理：对每个编号记录是否曾被夸(1)。只出现过 0(被骂)而从未出现过 1 的编号即为答案。
 *   用 HashMap 记录，最后按编号升序输出；若无则输出 NONE
 * 时间复杂度 O(n log n) 主要为排序，空间 O(n)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Map<Integer, Boolean> praised = new HashMap<>();
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < n; i++) {
            int id = sc.nextInt();
            int rec = sc.nextInt();
            ids.add(id);
            if (rec == 1) praised.put(id, true);
            else {
                // 确保 id 在 map 中，但未被夸时保持 false
                praised.putIfAbsent(id, false);
            }
        }
        sc.close();
        List<Integer> ans = new ArrayList<>();
        for (int id : ids) {
            if (!praised.getOrDefault(id, false)) ans.add(id);
        }
        Collections.sort(ans);
        if (ans.isEmpty()) {
            System.out.println("NONE");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ans.size(); i++) {
                if (i > 0) sb.append(' ');
                sb.append(ans.get(i));
            }
            System.out.println(sb.toString());
        }
    }
}
