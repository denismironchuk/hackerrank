package utils.ukkonnen;

/**
 * Created by Denis_Mironchuk on 4/10/2018.
 */
public class UkkonnenSuffixTree {
    public static void main(String[] args) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            b.append((char)('a' + (int)(Math.random() * 3)));
        }
        b.append("#");
        String s = b.toString();
    }
}
