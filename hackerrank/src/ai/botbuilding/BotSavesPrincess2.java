package ai.botbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BotSavesPrincess2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        StringTokenizer botPosTkn = new StringTokenizer(br.readLine());
        int botRow = Integer.parseInt(botPosTkn.nextToken());
        int botCol = Integer.parseInt(botPosTkn.nextToken());

        int princessRow = 0;
        int princessCol = 0;

        for (int row = 0; row < n; row++) {
            String line = br.readLine();
            int col = 0;
            for (char c : line.toCharArray()) {
                if (c == 'p') {
                    princessRow = row;
                    princessCol = col;
                }
                col++;
            }
        }

        String horizontal = botCol < princessCol ? "RIGHT" : "LEFT";
        String vertical = botRow < princessRow ? "DOWN" : "UP";

        for (int i = 0; i < Math.abs(botCol - princessCol); i++) {
            System.out.println(horizontal);
            return;
        }

        for (int i = 0; i < Math.abs(botRow - princessRow); i++) {
            System.out.println(vertical);
            return;
        }
    }
}
