package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class YouCanGoYourOwnWay {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            String moves = br.readLine();
            char firstMove = moves.charAt(0);
            char lastMove = moves.charAt(2 * n - 3);
            System.out.printf("Case #%s: ", t + 1);

            if (firstMove != lastMove) {
                if (firstMove == 'E') {
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('S');
                    }
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('E');
                    }
                } else {
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('E');
                    }
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('S');
                    }
                }
            } else {
                if (firstMove == 'E') {
                    int pos = find2SouthPos(moves);
                    int row = 0;
                    for (; row < pos; row++) {
                        System.out.print('S');
                    }
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('E');
                    }
                    for (; row < n - 1; row++) {
                        System.out.print('S');
                    }
                } else {
                    int pos = find2EastPos(moves);
                    int col = 0;
                    for (; col < pos; col++) {
                        System.out.print('E');
                    }
                    for (int i = 0; i < n - 1; i++) {
                        System.out.print('S');
                    }
                    for (; col < n - 1; col++) {
                        System.out.print('E');
                    }
                }
            }
            System.out.println();
        }
    }

    private static int find2SouthPos(String moves) {
        int pos = 0;
        char prev = moves.charAt(0);
        for (int i = 1; i < moves.length(); i++) {
            char c = moves.charAt(i);
            if (c == 'S') {
                if (prev == 'S') {
                    break;
                } else {
                    pos++;
                }
            }
            prev = c;
        }
        return pos;
    }

    private static int find2EastPos(String moves) {
        int pos = 0;
        char prev = moves.charAt(0);
        for (int i = 1; i < moves.length(); i++) {
            char c = moves.charAt(i);
            if (c == 'E') {
                if (prev == 'E') {
                    break;
                } else {
                    pos++;
                }
            }
            prev = c;
        }
        return pos;
    }
}
