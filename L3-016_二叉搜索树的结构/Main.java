import java.io.*;
import java.util.*;

// L3-016 二叉搜索树的结构
// 算法：按序插入构建BST，记录parent、depth、left/right，查询时解析6种陈述
// 时间复杂度 O(N^2) 最坏插入 + O(M * L)，N,M<=100，可接受
public class Main {
    static class Node {
        int val;
        Node left, right, parent;
        int depth;
        Node(int v, int d, Node p) { val=v; depth=d; parent=p; }
    }
    static Map<Integer, Node> map = new HashMap<>();
    static Node root;

    static void insert(int v) {
        if (root == null) {
            root = new Node(v, 0, null);
            map.put(v, root);
            return;
        }
        Node cur = root;
        while (true) {
            if (v < cur.val) {
                if (cur.left == null) {
                    Node nd = new Node(v, cur.depth + 1, cur);
                    cur.left = nd;
                    map.put(v, nd);
                    break;
                } else cur = cur.left;
            } else { // v > cur.val
                if (cur.right == null) {
                    Node nd = new Node(v, cur.depth + 1, cur);
                    cur.right = nd;
                    map.put(v, nd);
                    break;
                } else cur = cur.right;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        // 读取 N
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        // 读取 N 个整数，可能跨行
        List<Integer> vals = new ArrayList<>();
        while (vals.size() < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens() && vals.size() < N) {
                vals.add(Integer.parseInt(st.nextToken()));
            }
        }
        for (int v : vals) insert(v);

        // 读取 M
        line = br.readLine();
        while (line != null && line.trim().isEmpty()) line = br.readLine();
        if (line == null) return;
        int M = Integer.parseInt(line.trim());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < M; i++) {
            String s = br.readLine();
            while (s != null && s.isEmpty()) s = br.readLine(); // keep spaces? but statement lines have fixed format; empty not expected
            if (s == null) s = "";
            s = s.trim();
            boolean ans = judge(s);
            out.append(ans ? "Yes" : "No").append('\n');
        }
        System.out.print(out.toString());
    }

    static boolean judge(String s) {
        // 分词，保留原符号但用空格切分
        // 6种句型通过关键词区分
        String[] t = s.split("\\s+");
        if (s.contains("is the root")) {
            // A is the root
            int A = Integer.parseInt(t[0]);
            Node nd = map.get(A);
            if (nd == null) return false;
            return nd == root;
        } else if (s.contains("are siblings")) {
            // A and B are siblings
            int A = Integer.parseInt(t[0]);
            int B = Integer.parseInt(t[2]);
            Node na = map.get(A);
            Node nb = map.get(B);
            if (na == null || nb == null) return false;
            if (na.parent == null || nb.parent == null) return false;
            return na.parent == nb.parent;
        } else if (s.contains("is the parent of")) {
            // A is the parent of B
            int A = Integer.parseInt(t[0]);
            int B = Integer.parseInt(t[t.length - 1]);
            Node na = map.get(A);
            Node nb = map.get(B);
            if (na == null || nb == null) return false;
            return nb.parent == na;
        } else if (s.contains("is the left child of")) {
            int A = Integer.parseInt(t[0]);
            int B = Integer.parseInt(t[t.length - 1]);
            Node na = map.get(A);
            Node nb = map.get(B);
            if (na == null || nb == null) return false;
            return nb.left == na;
        } else if (s.contains("is the right child of")) {
            int A = Integer.parseInt(t[0]);
            int B = Integer.parseInt(t[t.length - 1]);
            Node na = map.get(A);
            Node nb = map.get(B);
            if (na == null || nb == null) return false;
            return nb.right == na;
        } else if (s.contains("are on the same level")) {
            int A = Integer.parseInt(t[0]);
            int B = Integer.parseInt(t[2]);
            Node na = map.get(A);
            Node nb = map.get(B);
            if (na == null || nb == null) return false;
            return na.depth == nb.depth;
        }
        return false;
    }
}
