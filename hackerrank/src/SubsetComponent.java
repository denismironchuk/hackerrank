import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SubsetComponent {
    private static final int BITS_CNT = 64;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        long[] d = new long[n];
        StringTokenizer tkn = new StringTokenizer(br.readLine());

        List<int[]> bitsRepres = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            d[i] = Long.parseLong(tkn.nextToken());
            long tmp = d[i];
            int bit = BITS_CNT - 1;
            int[] bits = new int[BITS_CNT];
            while (tmp != 0) {
                bits[bit] = (int)(tmp % 2);
                tmp /= 2;
                bit--;
            }
            bitsRepres.add(bits);
        }

        //Date start = new Date();

        List<int[]> combs = buildCombinations(bitsRepres);

        long res = 0;
        int[] distincts = new int[BITS_CNT];
        for (int[] comb : combs) {
            for (int i = 0; i< BITS_CNT; i++) {
                if (distincts[comb[i]] == 0) {
                    res++;
                    distincts[comb[i]] = 1;
                }
            }
            for (int i = 0; i< BITS_CNT; i++) {
                distincts[i] = 0;
            }
        }

        //Date end = new Date();
        System.out.println(res);
        //System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static List<int[]> buildCombinations(List<int[]> bitRepresentations) {
        if (bitRepresentations.size() == 0) {
            int[] res = new int[BITS_CNT];
            for (int i = 0; i < BITS_CNT; i++) {
                res[i] = i;
            }
            return Arrays.asList(res);
        }

        int[] bits = bitRepresentations.get(0);
        bitRepresentations.remove(0);
        List<int[]> combinations = buildCombinations(bitRepresentations);

        List<int[]> result = new ArrayList<>();

        for (int[] comb : combinations) {
            result.add(comb);
            result.add(combineRepresentations(comb, bits));
        }

        return result;
    }

    private static int[] combineRepresentations(int[] bits1, int[] bits2) {
        int[] commonPos = new int[BITS_CNT];
        int lastSubsetIndex = -1;
        for (int i = 0; i < BITS_CNT; i++) {
            if (bits1[i] != 0 && bits2[i] != 0) {
                commonPos[bits1[i]] = 1;
                lastSubsetIndex = bits1[i];
            }
        }

        int[] res = new int[BITS_CNT];

        for (int i = 0; i < BITS_CNT; i++) {
            if (commonPos[bits1[i]] == 1) {
                res[i] = lastSubsetIndex;
            } else {
                res[i] = bits1[i];
            }
        }

        return res;
    }
}
