package sample.Entity;

public class TaskPaneItem {
    String user_name;
    String task_name;
    String data_name;

    public TaskPaneItem(String user_name, String task_name, String data_name) {
        this.user_name = user_name;
        this.task_name = task_name;
        this.data_name = data_name;
    }

    public TaskPaneItem() {
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getData_name() {
        return data_name;
    }

    public void setData_name(String data_name) {
        this.data_name = data_name;
    }
}
