package ro.duclad.logparser.support;

import ro.duclad.logparser.Parser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TestFileGenerator {


    public static void main(String[] args) throws IOException {
        File logFile = new File("src/test/resources/test.log");
        logFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(logFile);
        for (int i = 0; i < 1000000; i++) {
            StringBuilder builder = new StringBuilder();
            builder.append(generateDate());
            builder.append("|");
            builder.append(generateIP());
            builder.append("|");
            builder.append("GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
            printWriter.println(builder.toString());
        }
        printWriter.flush();
        printWriter.close();
    }

    private static String generateIP() {
        return "174.129.239." + new Random().nextInt(256);
    }

    private static String generateDate() {
        Random r = new Random();
        int year = 2017;
        int month = r.nextInt(11);
        int hour = r.nextInt(23);
        int min = r.nextInt(59);
        int sec = r.nextInt(59);
        GregorianCalendar gc = new GregorianCalendar(year, month, 1);
        int day = r.nextInt(gc.getActualMaximum(Calendar.DAY_OF_MONTH) - 1) + 1;

        return LocalDateTime.of(year, month + 1, day + 1, hour + 1, min + 1, sec + 1).format(Parser.DATE_FORMAT);

    }

}
