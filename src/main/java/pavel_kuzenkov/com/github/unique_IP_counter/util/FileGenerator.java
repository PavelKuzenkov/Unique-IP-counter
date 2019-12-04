package pavel_kuzenkov.com.github.unique_IP_counter.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {

    public static void main(String[] args) {

        File file = new File("IP-addresses.txt");
        try{
            FileWriter fileWriter = new FileWriter(file);
            for (int i = 0; i <= 255 ; i++) {
                for(int j = 0; j <= 255 ; j++) {
                    for(int k = 0; k <= 255 ; k++) {
                        for(int l = 0; l <= 255; l++) {
                            StringBuilder ip = new StringBuilder();
                            if (i < 10) ip.append("00");
                            if (i < 100 && i >= 10) ip.append("0");
                            ip.append(i).append(".");
                            if (j < 10) ip.append("00");
                            if (j < 100 && j >= 10) ip.append("0");
                            ip.append(j).append(".");
                            if (k < 10) ip.append("00");
                            if (k < 100 && k >= 10) ip.append("0");
                            ip.append(k).append(".");
                            if (l < 10) ip.append("00");
                            if (l < 100 && l >= 10) ip.append("0");
                            ip.append(l);
                            fileWriter.write(ip.append(System.lineSeparator()).toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
        }
    }
}
