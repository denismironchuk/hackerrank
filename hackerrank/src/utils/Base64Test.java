package utils;


import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class Base64Test {
    public static void main(String[] args) throws IOException {
        File dir = new File("D:\\ecomStuff");
        File f = new File(dir, "some_new_file");
        System.out.println(f.getPath());
        /*BufferedReader br = new BufferedReader(new FileReader("D:/shippingLabel.txt"));
        String lines = br.lines().collect(Collectors.joining());
        FileOutputStream fos = new FileOutputStream(new File("D:/shippingLabelFixed.txt"));
        fos.write(lines.getBytes());

        fos.flush();
        fos.close();*/

        /*byte[] decoded = Base64.getDecoder().decode(lines);
        System.out.println(new String(decoded));*/

        /*byte[] encodedBytes = Base64.getEncoder().encode("Test".getBytes());
        System.out.println("encodedBytes " + new String(encodedBytes));
        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
        System.out.println("decodedBytes " + new String(decodedBytes));*/
    }
}
