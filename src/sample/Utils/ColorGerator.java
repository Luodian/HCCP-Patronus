package sample.Utils;

import java.util.Random;

public class ColorGerator {
    public static String getRandomColor() {
        long t = System.currentTimeMillis();
//        Random rand = new Random(t);
        Random rand = new Random();
        StringBuilder rs = new StringBuilder();
        rs.append('#');
        for (int i = 0; i < 6; i++) {
            int num = rand.nextInt(15);
            if (num >= 9) {
                char chr = (char) ('a' + (num + 1 - 10));
                rs.append(chr);
            } else
                rs.append(String.valueOf(num + 1));
            //加1预防白色的边
        }
        return rs.toString();
    }
}
