import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Влада on 19.02.2018.
 */
public class WeightedUniformString {
    //97-122
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();

        Set<Integer> weights = new HashSet<>();
        int prevChar = -1;
        int weight = 0;

        for (int i = 0; i < s.length(); i++) {
            int currentChar = s.charAt(i);
            int currentWeight = currentChar - 96;
            if (currentChar == prevChar) {
                weight += currentWeight;
            } else {
                weight = currentWeight;
            }
            weights.add(weight);
            prevChar = currentChar;
        }

        int quests = Integer.parseInt(br.readLine());
        StringBuilder buil = new StringBuilder();

        for (int q = 0; q < quests; q++) {
            int w = Integer.parseInt(br.readLine());
            buil.append(weights.contains(w) ? "YES" : "NO").append("\n");
        }

        System.out.println(buil.toString());
    }
}
