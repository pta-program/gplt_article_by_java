import java.util.Scanner;

/**
 * L1-115 就挺突然的
 * 原理：寿命 = B - A；分三档输出提示语
 *   age<=0 -> hai sheng ma? ; 1..250 -> nin tai cong ming le! ; >250 -> jiu ting tu ran de...
 * 时间复杂度 O(1)
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int A = sc.nextInt();
        int B = sc.nextInt();
        sc.close();
        int age = B - A;
        System.out.println(age);
        if (age <= 0) {
            System.out.println("hai sheng ma?");
        } else if (age <= 250) {
            System.out.println("nin tai cong ming le!");
        } else {
            System.out.println("jiu ting tu ran de...");
        }
    }
}
