import java.io.*;
import java.util.*;

// L2-053 算式拆解
// 解析形如 (对象 操作 对象) 的全括号表达式，对象可为数字或子算式。
// 构建二叉树后以后序遍历得到执行顺序；对每个操作节点，若子节点是叶
// 则输出其数字串，否则省略（结果已计算）。
// 时间复杂度 O(L)，空间 O(L)，L<=100
public class Main {
    static String s;
    static int pos;

    static class Node {
        boolean isLeaf;
        String val; // leaf
        char op;
        Node left, right;
    }

    static Node parseExpr() {
        // s[pos] == '('
        pos++; // '('
        Node left;
        if (pos < s.length() && s.charAt(pos) == '(') {
            left = parseExpr();
        } else {
            left = parseNumber();
        }
        char op = s.charAt(pos++);
        Node right;
        if (pos < s.length() && s.charAt(pos) == '(') {
            right = parseExpr();
        } else {
            right = parseNumber();
        }
        pos++; // ')'
        Node cur = new Node();
        cur.isLeaf = false;
        cur.op = op;
        cur.left = left;
        cur.right = right;
        return cur;
    }

    static Node parseNumber() {
        int start = pos;
        while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
        // 题目数字为非负整数，若出现负号可扩展，但此处仅数字
        String v = s.substring(start, pos);
        Node nd = new Node();
        nd.isLeaf = true;
        nd.val = v;
        return nd;
    }

    static void collect(Node u, List<Node> out) {
        if (u == null || u.isLeaf) return;
        collect(u.left, out);
        collect(u.right, out);
        out.add(u);
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = null;
        // 读取非空行
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) break;
        }
        if (line == null || line.isEmpty()) return;
        s = line.replaceAll("\\s+", ""); // 去空格
        // 若表达式为单个数字，无括号
        if (!s.contains("(")) {
            return; // 无操作
        }
        pos = 0;
        Node root = parseExpr();
        List<Node> ops = new ArrayList<>();
        collect(root, ops);
        StringBuilder sb = new StringBuilder();
        for (Node nd : ops) {
            String left = nd.left.isLeaf ? nd.left.val : "";
            String right = nd.right.isLeaf ? nd.right.val : "";
            sb.append(left).append(nd.op).append(right).append('\n');
        }
        System.out.print(sb.toString());
    }
}
