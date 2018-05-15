package sample.Entity;

import java.sql.Date;
import java.util.ArrayList;

public class GroupNode {
    private String group_id;
    private String group_name;
    private String type;
    private UserNode owner;
    private int member_num;
    private ArrayList<UserNode> members;
    private String description;
    private Date creat_date;
    private String owner_id;

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public Date getCreat_date() {
        return creat_date;
    }

    public void setCreat_date(Date creat_date) {
        this.creat_date = creat_date;
    }

    public GroupNode(String group_id, String group_name, String type, UserNode owner, int member_num) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.type = type;
        this.owner = owner;
        this.member_num = member_num;
    }

    public GroupNode(String group_id, String group_name, String type) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.type = type;
    }

    public GroupNode(String group_id, String group_name) {
        this.group_id = group_id;
        this.group_name = group_name;
    }

    public GroupNode(String group_id) {
        this.group_id = group_id;
    }

    public GroupNode(){}


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserNode getOwner() {
        return owner;
    }

    public void setOwner(UserNode owner_id) {
        this.owner = owner_id;
    }

    public int getMember_num() {
        return member_num;
    }

    public void setMember_num(int member_num) {
        this.member_num = member_num;
    }

    public ArrayList<UserNode> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<UserNode> members) {
        this.members = members;
    }
}
