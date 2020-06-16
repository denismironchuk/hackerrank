package codejam.year2020.round2.emacs;

import codejam.year2020.round2.emacs.dataStructures.MoveTime;
import codejam.year2020.round2.emacs.dataStructures.Parenthesis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Emacs {
        public static void main(String[] aggs) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int k = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());
                String p = br.readLine();
                Parenthesis[] tree = new Parenthesis[k];
                Parenthesis root = new Parenthesis(p, tree);

                StringTokenizer leftTkn = new StringTokenizer(br.readLine());
                StringTokenizer rightTkn = new StringTokenizer(br.readLine());
                StringTokenizer teleportTkn = new StringTokenizer(br.readLine());

                for (int i = 0; i < k; i++) {
                    MoveTime moveTime = p.charAt(i) == '(' ? tree[i].openTime : tree[i].closeTime;
                    moveTime.toLeft = Long.parseLong(leftTkn.nextToken());
                    moveTime.toRight = Long.parseLong(rightTkn.nextToken());
                    moveTime.teleport = Long.parseLong(teleportTkn.nextToken());
                }

                root.calculateTiming();

                System.out.println();
            }
        }
    }


}
