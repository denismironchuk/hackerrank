import java.util.Arrays;

/**
 * Created by Denis_Mironchuk on 3/29/2018.
 */
public class Equal {
    public static void main(String[] args) {
        int[] arr = {520, 862, 10, 956, 498, 956, 991, 542, 523, 664,
                378, 194, 76, 90, 753, 868, 837, 830, 932, 814,
                616, 78, 103, 882, 452, 397, 899, 488, 149, 108,
                723, 22, 323, 733, 330, 821, 41, 322, 715, 917,
                986, 93, 111, 63, 535, 864, 931, 372, 47, 215,
                539, 15, 294, 642, 897, 98, 391, 796, 939, 540,
                257, 662, 562, 580, 747, 893, 401, 789, 215, 468,
                58, 553, 561, 169, 616, 448, 385, 900, 173, 432,
                115, 712};

        Arrays.sort(arr);
        int k = 0;
        for (int i : arr) {
            if (k % 10 == 0) {
                System.out.println();
            }
            System.out.printf("%4d", i);
            k++;
        }

        System.out.println();

        int[] results = new int[6];

        results[0] = count(arr, 0);
        arr[0] -= 1;
        results[1] = count(arr, 1);
        arr[0] -= 1;
        results[2] = count(arr, 1);
        arr[0] -= 1;
        results[3] = count(arr, 2);
        arr[0] -= 1;
        results[4] = count(arr, 2);
        arr[0] -= 1;
        results[5] = count(arr, 1);

        Arrays.sort(results);

        System.out.println(results[0]);
    }

    private static int count(int[] arr, int res) {
        int min = arr[0];

        for (int i = 1; i< arr.length; i++) {
            int el = arr[i];
            el -= min;

            res+=el / 5;
            el %= 5;

            res+=el / 2;
            el %= 2;

            res+=el;
        }

        return res;
    }
}
