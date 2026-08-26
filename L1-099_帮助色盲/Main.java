import java.util.Scanner;
/** L1-099：黄灯或有人同行不提示；行动仅绿灯时为 move。 */
public class Main { public static void main(String[] args){Scanner s=new Scanner(System.in);int a=s.nextInt(),b=s.nextInt();System.out.println(b==0&&a<2?(a==0?"biii":"dudu"):"-");System.out.println(a==1?"move":"stop");} }
