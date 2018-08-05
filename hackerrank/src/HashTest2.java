/**
 * Created by Влада on 11.03.2018.
 */
public class HashTest2 {
    private static final long HASH_BASE = 31;
    private static final long MODULO = 1000000007l;

    public static String notStr(String str) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'a') {
                res.append('b');
            } else {
                res.append('a');
            }
        }

        return res.toString();
    }

    public static long hash(String s) {
        long res = 0;
        long base = 1;

        for (int i = 0; i < s.length(); i++) {
            res += (s.charAt(i) * base) % MODULO;
            res %= MODULO;
            base *= HASH_BASE;
            base %= MODULO;
        }

        return res;
    }

    public static void main(String[] args) {
        String s = "a";

        for (int i = 0; i < 30; i++) {
            String notS = notStr(s);
            System.out.println(hash(s) == hash(notS));
            s = s + notS;
            //System.out.println(s);
        }
    }
}
