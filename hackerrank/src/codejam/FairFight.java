package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class FairFight {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 1; t <= T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int k = Integer.parseInt(tkn1.nextToken());

            StringTokenizer cTkn = new StringTokenizer(br.readLine());
            StringTokenizer dTkn = new StringTokenizer(br.readLine());
            int[] c = new int[n];
            int[] d = new int[n];

            for (int i = 0; i < n; i++) {
                c[i] = Integer.parseInt(cTkn.nextToken());
                d[i] = Integer.parseInt(dTkn.nextToken());
            }
        }
    }

    private static void buildSegTree(int[] tree, int[] data, int v, int l, int r) {
        if (l == r) {
            tree[v] = data[l];
        } else {
            int mid = (l + r) / 2;
            buildSegTree(tree, data, 2 * v, l, mid);
            buildSegTree(tree, data, 2 * v + 1, mid + 1, r);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
        }
    }

    private static int getMax(int[] tree, int v, int l, int r, int reqL, int reqR) {
        if (l == reqL && r == reqR) {
            return tree[v];
        } else {
            int mid = (l + r) / 2;
            return Math.max(getMax(tree, 2 * v, l, mid, reqL, Math.min(mid, reqR)),
                    getMax(tree, 2 * v + 1, mid + 1, r, Math.max(mid + 1, reqL), reqR));
        }
    }
}
