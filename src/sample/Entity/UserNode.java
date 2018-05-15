package sample.Entity;

public class UserNode {
    private String user_name;

    private String password;

    private String email;

    private String user_id;

    public UserNode(String user_name, String password, String email, String user_id) {
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.user_id = user_id;
    }

    public UserNode(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public UserNode(){

    }

    public UserNode(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
