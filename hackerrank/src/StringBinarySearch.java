import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Влада on 08.03.2018.
 */
public class StringBinarySearch {
    public static void main(String[] args) throws IOException {
        int n = 10;

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int len = 1 + (int)(20 * Math.random());
            StringBuilder b = new StringBuilder();
            for (int j = 0; j < len; j++) {
                b.append((char)((char)(26 * Math.random()) + 'a'));
            }
            strings.add(b.toString());
            //System.out.println(b.toString());
        }
        strings.sort(String::compareTo);


        for (String s : strings) {
            System.out.println(s);
        }

        System.out.println("Enter search string");

        BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
        String search = br.readLine();
        System.out.println(findPos(search, strings, 0, strings.size() - 1));
    }

    private static int findPos(String s, List<String> strings, int start, int end) {
        if (end <= start) {
            return start;
        }

        int middle = (end + start) / 2;

        if (s.compareTo(strings.get(middle)) > 0) {
            return findPos(s, strings, middle + 1, end);
        } else {
            return findPos(s, strings, start, middle);
        }
    }
}
