import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Gemstones {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        List<String> rocks = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            rocks.add(br.readLine());
        }

        List<Set<Character>> elements = new ArrayList<>();

        for (String rock : rocks) {
            Set<Character> rockElements = new HashSet<>();

            for (char c : rock.toCharArray()) {
                rockElements.add(c);
            }

            elements.add(rockElements);
        }

        Iterator<Set<Character>> itr = elements.iterator();

        Set<Character> result = itr.next();

        while (itr.hasNext()) {
            result.retainAll(itr.next());
        }

        System.out.println(result.size());

    }
}
