package sample.Entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class DataNode extends RecursiveTreeObject<DataNode> {
    private String user_id;
    private String data_name;
    private String data_type;
    private int row_nums;
    private int attr_nums;
    private String file_path;
    /**平时这个字段一般不需要这个字段，慎用**/
    private String user_name;

    public DataNode(String user_id, String data_name, String data_type, int row_nums, int attr_nums, String file_path) {
        this.user_id = user_id;
        this.data_name = data_name;
        this.data_type = data_type;
        this.row_nums = row_nums;
        this.attr_nums = attr_nums;
        this.file_path = file_path;
    }

    /**这个构造函数是专门为了queryRegisterdDataNodesByID函数构建的
     * 平时尽量不要用这个**/
    public DataNode(String data_name, int row_nums, int attr_nums, String user_name) {
        this.data_name = data_name;
        this.row_nums = row_nums;
        this.attr_nums = attr_nums;
        this.user_name = user_name;
    }

    public DataNode() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public int getRow_nums() {
        return row_nums;
    }

    public void setRow_nums(int row_nums) {
        this.row_nums = row_nums;
    }

    public int getAttr_nums() {
        return attr_nums;
    }

    public void setAttr_nums(int attr_nums) {
        this.attr_nums = attr_nums;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
