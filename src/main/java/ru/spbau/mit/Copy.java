/**
 * Created by Наталья on 01.03.2016.
 */

import java.io.*;

public class Copy {

    private static final int MAX_SIZE = 1024;

    public static void main(String[] args) {

        String src = args[0];
        String dst = args[1];

        try (BufferedInputStream  is = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dst))) {
            byte[] buffer = new byte[MAX_SIZE];
            while((is.read(buffer)) != -1){
                os.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
