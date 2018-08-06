import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Abbreviation {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        StringBuilder results = new StringBuilder();

        for (int q = 0; q < Q; q++) {
            String a = br.readLine();
            String b = br.readLine();

            List<Integer> finishPositions = new ArrayList<>();
            finishPositions.add(-1);

            for (int i = 0; i < b.length(); i++) {
                char bChar = b.charAt(i);
                int finishPosIndex = 0;

                List<Integer> newPositions = new ArrayList<>();

                if (finishPositions.isEmpty()) {
                    break;
                }

                for (int j = finishPositions.get(finishPosIndex) + 1; j < a.length(); j++) {
                    char aChar = a.charAt(j);

                    if (finishPositions.size() - 1 > finishPosIndex && j > finishPositions.get(finishPosIndex+1)) {
                        finishPosIndex++;
                    }

                    if (aChar >= 'a' && aChar <= 'z') {
                        if (aChar - 'a' + 'A' == bChar) {
                            newPositions.add(j);
                        }
                    } else {
                        if (aChar == bChar) {
                            newPositions.add(j);
                        }

                        if (finishPositions.size() - 1 > finishPosIndex) {
                            finishPosIndex++;
                            j = finishPositions.get(finishPosIndex);
                        } else {
                            break;
                        }
                    }
                }

                finishPositions = newPositions;
            }


            if (finishPositions.isEmpty()) {
                results.append("NO\n");
            } else {
                int i = finishPositions.get(finishPositions.size() - 1) + 1;
                for (; i < a.length() && a.charAt(i) >= 'a' && a.charAt(i) <= 'z'; i++);
                if (i < a.length()) {
                    results.append("NO\n");
                } else {
                    results.append("YES\n");
                }
            }
        }

        System.out.println(results.toString());
    }
}
