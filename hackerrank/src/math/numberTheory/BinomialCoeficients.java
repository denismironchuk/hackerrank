package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BinomialCoeficients {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            BigInteger n = new BigInteger(tkn1.nextToken());
            long p = Long.parseLong(tkn1.nextToken());
            BigInteger pBig = BigInteger.valueOf(p);
            List<BigInteger> rems = new ArrayList<>();
            BigInteger nBckp = n;

            while (!n.equals(BigInteger.ZERO)) {
                BigInteger[] res = n.divideAndRemainder(pBig);
                rems.add(res[1]);
                n = res[0];
            }

            BigInteger allSmallerOrEqual = BigInteger.ONE;
            for (BigInteger rem : rems) {
                allSmallerOrEqual = allSmallerOrEqual.multiply(rem.add(BigInteger.ONE));
            }

            BigInteger res = nBckp.subtract(allSmallerOrEqual).add(BigInteger.ONE);
            System.out.println(res);
        }
    }
    
}
