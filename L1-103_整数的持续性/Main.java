import java.util.*;
/** L1-103：持续性通过反复将各位相乘直到个位数获得，扫描短区间维护最大值。 */
public class Main { static int p(long x){int c=0;while(x>9){long q=1;while(x>0){q*=x%10;x/=10;}x=q;c++;}return c;} public static void main(String[]v){Scanner s=new Scanner(System.in);long a=s.nextLong(),b=s.nextLong();int best=-1;StringBuilder r=new StringBuilder();for(long x=a;x<=b;x++){int q=p(x);if(q>best){best=q;r.setLength(0);}if(q==best){if(r.length()>0)r.append(' ');r.append(x);}}System.out.println(best);System.out.println(r);} }
