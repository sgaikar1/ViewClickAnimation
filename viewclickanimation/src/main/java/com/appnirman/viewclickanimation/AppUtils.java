package com.appnirman.viewclickanimation;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {


    public static boolean isLowRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        boolean sizeFlag=false;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
                sizeFlag=false;
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
                sizeFlag=false;
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
                sizeFlag=true;
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
                sizeFlag=true;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return sizeFlag;
    }

}
