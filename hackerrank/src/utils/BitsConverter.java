package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Denis_Mironchuk on 9/19/2018.
 */
public class BitsConverter {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("D:/roads15out.txt"));
        String s = br.readLine();

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            res.append(s.charAt(i)).append("\n");
        }

        try (FileWriter fwr = new FileWriter(new File("D:/roads15outEnters.txt"))) {
            fwr.write(res.toString());
            fwr.flush();
        }

    }
}
