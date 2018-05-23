package sample.Utils;
import sample.Datebase.SQLHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tools {
    //list转化为str
    public static String listToString(List<String> list) {
        if (list == null)
            return null;
        StringBuilder rs = new StringBuilder();
        boolean first = true;
        for (String str : list) {
            //第一个字符串值钱不拼接逗号
            if (first) {
                first = false;
            } else {
                rs.append(",");
            }
            rs.append(str);
        }
        return rs.toString();
    }

    //str转化为list
    public static List<String> stringToList(String str) {
        //拿逗号分隔开连接的字符串
        String[] strs = str.split(",");
        return Arrays.asList(strs);
    }

    //获得一个从来没使用过的随机数
    public static String getRandomID(String type) {
        boolean foundFreeID = false;
        String testID = "";
        while (!foundFreeID) {
            testID = "";
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                testID += String.valueOf(rand.nextInt(10));
            }
            int rs = SQLHandler.isExistID(type, testID);
            if (rs == -1) return null;
            else if (rs == 0) foundFreeID = true;
            else foundFreeID = false;
        }
        return testID;
    }
}
