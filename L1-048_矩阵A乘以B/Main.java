import java.util.Scanner;

/**
 * L1-048 矩阵A乘以B
 * 实现原理：读入两个矩阵后检查 A 的列数与 B 的行数；匹配时按定义计算
 * C[i][j]=sum(A[i][k]*B[k][j])，否则输出规模错误。
 * 时间复杂度 O(Ra*Ca*Cb)，空间复杂度 O(Ra*Ca+Rb*Cb)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int ra = scanner.nextInt(), ca = scanner.nextInt();
        int[][] a = readMatrix(scanner, ra, ca);
        int rb = scanner.nextInt(), cb = scanner.nextInt();
        int[][] b = readMatrix(scanner, rb, cb);
        if (ca != rb) {
            System.out.println("Error: " + ca + " != " + rb);
            return;
        }
        System.out.println(ra + " " + cb);
        for (int i = 0; i < ra; i++) {
            for (int j = 0; j < cb; j++) {
                int sum = 0;
                for (int k = 0; k < ca; k++) sum += a[i][k] * b[k][j];
                if (j > 0) System.out.print(' ');
                System.out.print(sum);
            }
            System.out.println();
        }
    }

    private static int[][] readMatrix(Scanner scanner, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) for (int j = 0; j < cols; j++) matrix[i][j] = scanner.nextInt();
        return matrix;
    }
}
