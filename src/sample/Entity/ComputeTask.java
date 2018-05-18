package sample.Entity;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class ComputeTask extends RecursiveTreeObject<DataNode> {
    private String task_id;
    private String data_type;
    private double cost;
    private String initiator_id;
    private double security_score;
    private String start_time;
    private String end_time;
    private int state;
    private UserNode initiator;
    private String task_name;
	private String code;
	private String group_id;

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public ComputeTask() {
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getInitiator_id() {
        return initiator_id;
    }

    public void setInitiator_id(String initiator_id) {
        this.initiator_id = initiator_id;
    }

    public double getSecurity_score() {
        return security_score;
    }

    public void setSecurity_score(double security_score) {
        this.security_score = security_score;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public UserNode getInitiator() {
        return initiator;
    }

    public void setInitiator(UserNode initiator) {
        this.initiator = initiator;
    }
	
	public String getCode ()
	{
		return code;
	}
	
	public void setCode (String code)
	{
		this.code = code;
	}
	
	public String getGroup_id ()
	{
		return group_id;
	}
	
	public void setGroup_id (String group_id)
	{
		this.group_id = group_id;
	}
}
