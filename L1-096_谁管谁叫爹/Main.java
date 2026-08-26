import java.util.Scanner;
/** L1-096：分别求数位和并按题意比较整除关系；同时或均不满足时比较原数。 */
public class Main {
 public static void main(String[] args) { Scanner s=new Scanner(System.in); for(int t=s.nextInt();t-->0;){long a=s.nextLong(),b=s.nextLong();boolean x=a%sum(b)==0,y=b%sum(a)==0;System.out.println(x==y?(a>b?"A":"B"):(x?"A":"B"));} }
 static long sum(long x){long r=0;while(x>0){r+=x%10;x/=10;}return r;}
}
