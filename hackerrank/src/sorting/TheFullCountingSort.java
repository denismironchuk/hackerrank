package sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class TheFullCountingSort {
    private static class Data {
        private int num;
        private String str;

        public Data(int num, String str) {
            this.num = num;
            this.str = str;
        }

        public int getNum() {
            return num;
        }

        public String getStr() {
            return str;
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        Data[] data = new Data[n];
        int max = 100;

        for (int i = 0; i < n / 2; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(tkn.nextToken());
            data[i] = new Data(num, "-");
        }

        for (int i = n / 2; i < n; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(tkn.nextToken());
            String str = tkn.nextToken();
            data[i] = new Data(num, str);
        }

        int[] count = new int[max + 1];

        for (Data d : data) {
            count[d.getNum()]++;
        }

        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }

        Data[] res = new Data[n];

        for (int i = n - 1; i >= 0; i--) {
            res[count[data[i].getNum()] - 1] = data[i];
            count[data[i].getNum()]--;
        }

        StringBuilder build = new StringBuilder();
        for (int i = 0; i < n; i++) {
            build.append(String.format("%s ", res[i].getStr()));
        }
        System.out.println(build.toString());
    }
}
