package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Cryptopangrams {
    private static String generateValues(int n, int l) {
        int[] prime = new int[n + 1];
        int[] processed= new int[n + 1];
        for (int i = 2; i <= n; i++) {
            if (processed[i] == 0) {
                prime[i] = 1;
                for (int j = 1; i * j <= n; j++) {
                    processed[i * j] = 1;
                }
            }
        }

        List<Integer> primes = new ArrayList<>();
        Set<Integer> usedPrimes = new HashSet<>();

        while (usedPrimes.size() != 25) {
            int primeCandidate = (int)(n * Math.random());
            if (prime[primeCandidate] == 1 && !usedPrimes.contains(primeCandidate)) {
                usedPrimes.add(primeCandidate);
                primes.add(primeCandidate);
            }
        }

        List<Integer> message = new ArrayList<>();
        message.addAll(primes);

        while (message.size() < l + 1) {
            message.add(primes.get((int)(Math.random() * primes.size())));
        }

        List<Integer> crypt = new ArrayList<>();

        for (int i = 1; i <= l; i++) {
            crypt.add(message.get(i) * message.get(i - 1));
        }

        return crypt.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                BigInteger n = new BigInteger(tkn1.nextToken());
                int l = Integer.parseInt(tkn1.nextToken());
                //StringTokenizer values = new StringTokenizer(br.readLine());
            while(true) {
                StringTokenizer values = new StringTokenizer(generateValues(n.intValue(), l));
                BigInteger firstCrypt = new BigInteger(values.nextToken());
                BigInteger secondCrypt = new BigInteger(values.nextToken());

                List<BigInteger> mesDecrypt = new ArrayList<>();
                Set<BigInteger> primesToSort = new TreeSet<>();

                BigInteger second = firstCrypt.gcd(secondCrypt);
                BigInteger first = firstCrypt.divide(second);
                BigInteger last = secondCrypt.divide(second);

                mesDecrypt.add(first);
                mesDecrypt.add(second);
                mesDecrypt.add(last);
                primesToSort.add(first);
                primesToSort.add(second);
                primesToSort.add(last);


                for (int i = 2; i < l; i++) {
                    BigInteger next = new BigInteger(values.nextToken());
                    //try {
                        last = next.divide(last);
                    //} catch (Exception ex) {
                      //  System.out.println();
                    //}
                    mesDecrypt.add(last);
                    primesToSort.add(last);
                }

                Map<BigInteger, Character> charMap = new TreeMap<>();

                char c = 'A';
                for (BigInteger b : primesToSort) {
                    charMap.put(b, c);
                    c++;
                }

                //System.out.printf("Case #%s: %s\n", t + 1, mesDecrypt.stream().map(b -> charMap.get(b)).map(String::valueOf).collect(Collectors.joining()));

                StringBuilder build = new StringBuilder();
                build.append("Case #").append(t + 1).append(": ");
                for (BigInteger b : mesDecrypt) {
                    build.append(charMap.get(b));
                    //System.out.printf("%s", charMap.get(b));
                }
                System.out.println(build.toString());
            }
        }
    }
}
