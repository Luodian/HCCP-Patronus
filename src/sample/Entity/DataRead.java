package sample.Entity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataRead {

    private String filePath;

    private File file;

    private String[] attrs;

    public static String SEPARATOR_SPACE = " ";
    public static String SEPARATOR_COMMA = ",";

    private int column = -1;

    private int row = -1;

    public int getColumn() throws Throwable {
        if (column > 0)
            return column;
        else throw new Throwable("HAVEN'T READ DATA");
    }

    public int getRow() throws Throwable {
        if (column > 0)
            return row;
        else throw new Throwable("HAVEN'T READ DATA");
    }

    public DataRead(String filePath){
        this.filePath = filePath;
        file = new File(filePath);
    }

    public DataRead(File file){
        this.file = file;
        filePath = this.file.getPath();
    }

    public List readLines(int lineNum , String separator) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String tmp;
            tmp = bufferedReader.readLine();
            attrs = tmp.split(",");
            int count = 0;
            ArrayList<String[]> result = new ArrayList<String[]>();
            if (lineNum < 0) {
                while ((tmp = bufferedReader.readLine()) != null) {
                    String[] tmps = tmp.split(separator);
                    result.add(tmps);
                }
            }else{
                while ((tmp = bufferedReader.readLine()) != null && count < lineNum) {
                    String[] tmps = tmp.split(separator);
                    result.add(tmps);
                    count++;
                }
            }
            row = result.size();
            if (row > 0) column = result.get(0).length;
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public String getFileName(){
        return file.getName();
    }

    public String[] getAttrs() {
        return attrs;
    }
}
