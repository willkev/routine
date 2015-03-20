package rna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FinderMS {

//    public static void main(String[] args) throws Exception {
//        //new FinderMS(",7,16,33,42,50,58,");
//        new FinderMS();
//    }

    public FinderMS(String toFind) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(new File("src").getAbsolutePath(), "ms.txt"));
        int max = 1;
        for (String line : lines) {
            int count = 0;
            int i = 0;
            for (String number : line.split(",")) {
                if (i >= 2) {
                    if (toFind.contains("," + number + ",")) {
                        count++;
                    }
                }
                i++;
            }
            if (count >= max) {
                max = count;
                System.out.println("[" + max + "] " + line);
            }
        }
    }

    public FinderMS() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(new File("src").getAbsolutePath(), "ms.txt"));
        int[] numeros = new int[61];
        for (String line : lines) {
            int i = 0;
            int num;
            for (String number : line.split(",")) {
                if (i >= 2) {
                    num = Integer.parseInt(number);
                    numeros[num]++;
                }
                i++;
            }
        }
        for (int x = 1; x < numeros.length; x++) {
            System.out.format("[%s]\t%s\n", x, numeros[x]);
        }
    }
}
