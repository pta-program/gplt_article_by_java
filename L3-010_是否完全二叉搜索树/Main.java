import java.io.*;
import java.util.*;

// L3-010 是否完全二叉搜索树
// 规则：左子树键值大，右子树键值小（与常规相反）
// 算法：按序插入构建BST，层序遍历判断完全性
// 完全性判断：BFS中一旦遇到空孩子，后续所有节点必须为叶子
// 时间复杂度 O(N^2) 最坏插入，N<=20 可接受；BFS O(N)
public class Main {
    static class Node {
        int val;
        Node left, right;
        Node(int v) { val = v; }
    }
    static Node insert(Node root, int v) {
        if (root == null) return new Node(v);
        Node cur = root;
        while (true) {
            if (v > cur.val) {
                if (cur.left == null) { cur.left = new Node(v); break; }
                else cur = cur.left;
            } else {
                if (cur.right == null) { cur.right = new Node(v); break; }
                else cur = cur.right;
            }
        }
        return root;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        List<Integer> nums = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) nums.add(Integer.parseInt(st.nextToken()));
        }
        if (nums.isEmpty()) return;
        int n = nums.get(0);
        Node root = null;
        for (int i = 1; i <= n && i < nums.size(); i++) {
            root = insert(root, nums.get(i));
        }
        if (root == null) return;
        // 层序遍历 + 完全性判断（标准方法：遇空后不应再有非空）
        Queue<Node> q2 = new LinkedList<>();
        q2.offer(root);
        List<Integer> order = new ArrayList<>();
        boolean seenNull = false;
        boolean complete = true;
        while (!q2.isEmpty()) {
            Node cur = q2.poll();
            if (cur == null) {
                seenNull = true;
            } else {
                if (seenNull) complete = false;
                order.add(cur.val);
                q2.offer(cur.left);
                q2.offer(cur.right);
                // 提前终止：若队列中剩余全为null则结束
                boolean allNull = true;
                for (Node nn : q2) if (nn != null) { allNull = false; break; }
                if (allNull) break;
            }
        }
        // 输出层序（order已按BFS顺序加入非空节点）
        // 上面第二次BFS的order是正确的层序
        // 但我们 need to ensure order is BFS without null gaps: 实际上标准层序就是按队列顺序访问非空节点
        // 上面已按此生成
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(order.get(i));
        }
        System.out.println(sb.toString());
        System.out.println(complete ? "YES" : "NO");
    }
}
