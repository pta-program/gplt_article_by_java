import java.util.Scanner;

/**
 * L1-083 谁能进图书馆
 * 实现原理：超过禁入年龄线可独立进入；未达线儿童仅能在另一人达到陪同年龄线时进入。
 * 根据两人的结果和是否依赖陪同，输出题目指定的说明。
 * 时间复杂度 O(1)，空间复杂度 O(1)。
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int forbidden = scanner.nextInt(), companion = scanner.nextInt();
        int age1 = scanner.nextInt(), age2 = scanner.nextInt();
        boolean together1 = age1 < forbidden && age2 >= companion;
        boolean together2 = age2 < forbidden && age1 >= companion;
        boolean can1 = age1 >= forbidden || together1;
        boolean can2 = age2 >= forbidden || together2;
        System.out.println(age1 + "-" + (can1 ? "Y" : "N") + " " + age2 + "-" + (can2 ? "Y" : "N"));
        if (together1) System.out.println("qing 2 zhao gu hao 1");
        else if (together2) System.out.println("qing 1 zhao gu hao 2");
        else if (can1 && can2) System.out.println("huan ying ru guan");
        else if (!can1 && !can2) System.out.println("zhang da zai lai ba");
        else System.out.println((can1 ? 1 : 2) + ": huan ying ru guan");
    }
}
