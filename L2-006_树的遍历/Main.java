import java.util.*;
import java.io.*;

// L2-006 树的遍历: 后序+中序 -> 层序
// 时间复杂度 O(N^2) N<=30
public class Main {
    static int[] post, in;
    static Map<Integer,Integer> inPos;
    static class Node { int val; Node left, right; Node(int v){val=v;} }
    static Node build(int postL, int postR, int inL, int inR) { // [l,r)
        if (postL >= postR || inL >= inR) return null;
        int rootVal = post[postR - 1];
        Node root = new Node(rootVal);
        int k = inPos.get(rootVal);
        int leftSize = k - inL;
        root.left = build(postL, postL + leftSize, inL, k);
        root.right = build(postL + leftSize, postR - 1, k + 1, inR);
        return root;
    }
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        while ((line = br.readLine()) != null && line.trim().isEmpty()) {}
        if (line == null) return;
        int N = Integer.parseInt(line.trim());
        post = new int[N];
        in = new int[N];
        readArray(br, post, N);
        readArray(br, in, N);
        inPos = new HashMap<>();
        for (int i = 0; i < N; i++) inPos.put(in[i], i);
        Node root = build(0, N, 0, N);
        // 层序
        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        List<Integer> lev = new ArrayList<>();
        while (!q.isEmpty()) {
            Node cur = q.poll();
            lev.add(cur.val);
            if (cur.left != null) q.offer(cur.left);
            if (cur.right != null) q.offer(cur.right);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lev.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(lev.get(i));
        }
        System.out.println(sb.toString());
    }
    static void readArray(BufferedReader br, int[] arr, int N) throws Exception {
        int idx = 0;
        String line;
        while (idx < N) {
            line = br.readLine();
            if (line == null) break;
            if (line.trim().isEmpty()) continue;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens() && idx < N) arr[idx++] = Integer.parseInt(st.nextToken());
        }
    }
}
