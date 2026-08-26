import java.util.Scanner;
/** L1-100：若无人全能，每人至多贡献 m-1 个技能点；超出的技能点数即至少全能人数。 */
public class Main { public static void main(String[] args){Scanner s=new Scanner(System.in);int n=s.nextInt(),m=s.nextInt(),sum=0;for(int i=0;i<m;i++)sum+=s.nextInt();System.out.println(Math.max(0,sum-n*(m-1)));} }
