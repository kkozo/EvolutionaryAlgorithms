/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 *
 * @author Andi
 */
public class LogStatsReader {

    public static void main(String[] args) {
        float average = 0f;
        float n = 0;
        try {
            Scanner io = new Scanner(new FileInputStream(new File("state2.txt")));
            String line ="";
            while (io.hasNextLine()) {
                String currLine = io.nextLine();
                if (currLine.contains("GENERATION:")) {
                    System.out.println(line);
                    line = currLine.substring(currLine.indexOf(":")+2);
                }
                if (currLine.contains("BEST FITNESS:")) {
                        n=n+1;
                        line = line+"\t"+currLine.substring(currLine.indexOf(":")+2);
                        average += Float.parseFloat(currLine.substring(currLine.indexOf(":")+2));
                        line = line.replaceAll("\\.",",");
                }
            }
            System.out.println(average/n);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
