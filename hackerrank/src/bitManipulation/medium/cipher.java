package bitManipulation.medium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Cipher {
    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:\\cipher.txt"));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());
        String s = br.readLine();
        n = s.length() - k + 1;
        char[] sArr = s.toCharArray();
        int[] encode = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            encode[i] = sArr[i] - '0';
        }

        StringBuilder res = new StringBuilder();
        int[] decode = new int[n];
        decode[0] = encode[0];
        res.append(decode[0]);
        for (int i = 1; i < Math.min(k, n); i++) {
            decode[i] = encode[i] ^ encode[i - 1];
            res.append(decode[i]);
        }

        for (int i = k; i < n; i++) {
            decode[i] = encode[i] ^ encode[i - 1] ^ decode[i - k];
            res.append(decode[i]);
        }

        System.out.println(res.toString());
    }
}
