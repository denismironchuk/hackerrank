package codejam.year2018.round3;

public class Network10 {

    public static void main(String[] args) {
        int[][] graph = new int[][] {
              // 0 1 2 3 4 5 6 7 8 9
         /*0*/  {0,0,1,1,1,1,0,0,0,0},
         /*1*/  {0,0,0,0,0,0,1,1,1,1},
         /*2*/  {1,0,0,1,1,0,1,0,0,0},
         /*3*/  {1,0,1,0,0,0,1,1,0,0},
         /*4*/  {1,0,1,0,0,0,0,0,1,1},
         /*5*/  {1,0,0,0,0,0,0,1,1,1},
         /*6*/  {0,1,1,1,0,0,0,0,1,0},
         /*7*/  {0,1,0,1,0,1,0,0,0,1},
         /*8*/  {0,1,0,0,1,1,1,0,0,0},
         /*9*/  {0,1,0,0,1,1,0,1,0,0},
        };

        int cnt = 0;
        for (int v1 = 0; v1 < 9; v1++) {
            for (int v2 = v1 + 1; v2 < 10; v2++) {
                if (graph[v1][v2] == 1) {
                    continue;
                }

                boolean hasCommon = false;
                for (int i = 0; !hasCommon && i < 10; i++) {
                    if (graph[v1][i] == 1 && graph[v2][i] == 1) {
                        hasCommon = true;
                    }
                }
                if (!hasCommon) {
                    System.out.println(v1 + " " + v2);
                    cnt++;
                }
            }
        }
        System.out.println(cnt);
    }
}
