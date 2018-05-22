package sample.Entity;

import javafx.scene.layout.Pane;

public class TaskPaneItem {
    String user_name;
    String task_name;
    String data_name;
    String user_id;
    String task_id;
    Pane showPane;

    public TaskPaneItem(String user_name, String task_name, String data_name, String user_id, String task_id) {
        this.user_name = user_name;
        this.task_name = task_name;
        this.data_name = data_name;
        this.user_id = user_id;
        this.task_id = task_id;
    }

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public Pane getShowPane() {
        return showPane;
    }

    public void setShowPane(Pane showPane) {
        this.showPane = showPane;
    }
}
