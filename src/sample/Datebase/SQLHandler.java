package sample.Datebase;

import javafx.fxml.Initializable;
import sample.Entity.DataNode;
import sample.Entity.GroupNode;
import sample.Entity.UserNode;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class SQLHandler {
    public static String url = "jdbc:mysql://localhost:3306/Patronus" ;
    public static String username = "root" ;
    public static String password = "wyz123348377" ;
    public static String UserNodeInsert = "INSERT INTO CLIENTNODES(user_name, user_id, email, password) values (?,?,?,?) ";
    public static String DataNodeInsert = "INSERT INTO DATANODES(user_id, data_name, data_type, row_nums, attr_nums, file_path) values(?,?,?,?,?,?)";
    public static String GroupInsert = "INSERT INTO GROUPS(group_name, data_type, group_id, member_nums, creator_id, create_date, description) values(?,?,?,?,?,?,?)";
    public static String GroupUserRelationInsert = "INSERT INTO belongs_to (user_id, group_id) values (?,?)";
    public static String GroupDataRegisterRelationInsert = "INSERT INTO contain (group_id, user_id, dataset_name) values (?,?,?)";
    public static Statement query;
    public static Connection con;


    public static void insertUser(UserNode userNode) throws SQLException {
        PreparedStatement insert_user = con.prepareStatement(UserNodeInsert);
        insert_user.setString(1, userNode.getUser_name());
        insert_user.setString(2, userNode.getUser_id());
        insert_user.setString(3, userNode.getEmail());
        insert_user.setString(4, userNode.getPassword());
        insert_user.executeUpdate();
    }

    /**根据邮箱和密码查询用户是否存在，若存在，则返回用户ID，否则返回字符串NOTFIND**/
    public static String isUserExistedByUserNode(UserNode userNode)throws SQLException{
        String sql = "select * from CLIENTNODES where email = \'" + userNode.getEmail() + "\' and password = \'" + userNode.getPassword() + "\'";
        ResultSet resultSet = query.executeQuery(sql);
        if (resultSet.next()){//不为空
            return resultSet.getString("user_id");
        }
        return "NOTFIND";
    }

    /**根据用户id查询用户，若找到返回UserNode，否则返回null**/
    public static UserNode queryUserByID(String user_id){
        String sql = "SELECT * from CLIENTNODES where user_id = " + user_id;
        try {
            ResultSet resultSet = query.executeQuery(sql);
            UserNode userNode = new UserNode();
            if (resultSet.next()){
                userNode.setUser_id(resultSet.getString("user_id"));
                userNode.setUser_name(resultSet.getString("user_name"));
                userNode.setEmail(resultSet.getString("email"));
                userNode.setPassword(resultSet.getString("password"));
                return userNode;
            }
            else return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**新添数据集**/
    public static boolean insertDataNode(DataNode dataNode) {
        PreparedStatement insert_datanode = null;
        try {
            insert_datanode = con.prepareStatement(DataNodeInsert);
            insert_datanode.setString(1, dataNode.getUser_id());
            insert_datanode.setString(2, dataNode.getData_name());
            insert_datanode.setString(3, dataNode.getData_type());
            insert_datanode.setInt(4, dataNode.getRow_nums());
            insert_datanode.setInt(5, dataNode.getAttr_nums());
            insert_datanode.setString(6, dataNode.getFile_path());
            insert_datanode.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**新添群组**/
    public static boolean insertGroup(GroupNode groupNode) {
        try {
            PreparedStatement insertGroup = con.prepareStatement(GroupInsert);
            insertGroup.setString(1, groupNode.getGroup_name());
            insertGroup.setString(2, groupNode.getType());
            insertGroup.setString(3, groupNode.getGroup_id());
            insertGroup.setInt(4, groupNode.getMember_num());
            insertGroup.setString(5, groupNode.getOwner_id());
            insertGroup.setDate(6, groupNode.getCreat_date());
            insertGroup.setString(7, groupNode.getDescription());
            insertGroup.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**查询当前用户拥有的数据集**/
    public static ArrayList<File> queryLoaclDataFile(String user_id){
        ArrayList<File> arrayList = new ArrayList<File>();
        String sql = "select file_path from DATANODES where user_id = \'" + user_id + "\'";
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                String file = resultSet.getString("file_path");
                File filetemp = new File(file);
                if (filetemp.exists()){
                    arrayList.add(filetemp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**插入群组用户联系元组(user_id, group_id)**/
    public static boolean insertGroupUserRelation(String user_id, String group_id){
        try {
            PreparedStatement insert = con.prepareStatement(GroupUserRelationInsert);
            insert.setString(1, user_id);
            insert.setString(2, group_id);
            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**根据用户id查询其所在的若干群组
     * 如果抛异常，返回null
     * 否则至少返回0**/
    public static ArrayList<GroupNode> queryGroupsByUserID(String user_id){
        String sql = "SELECT group_name, data_type, GROUPS.group_id, member_nums, creator_id, create_date, description " +
                "FROM GROUPS JOIN belongs_to ON GROUPS.group_id = belongs_to.group_id WHERE user_id = " + user_id;
        ArrayList<GroupNode> results = new ArrayList<GroupNode>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                /**获取群元组信息**/
                String group_name = resultSet.getString("group_name");
                String data_type = resultSet.getString("data_type");
                String group_id = resultSet.getString("group_id");
                int member_nums = resultSet.getInt("member_nums");
                String creator_id = resultSet.getString("creator_id");
                Date date = resultSet.getDate("create_date");
                String description = resultSet.getString("description");
                /**这里不能插入一段查询，因为若在resultSet还没关闭时进行一次查询，前一个resultSet就会被强制关闭**/
                //UserNode owner = SQLHandler.queryUserByID(creator_id);
                /**构建群结点**/
                GroupNode temp = new GroupNode();
                temp.setGroup_name(group_name);
                temp.setType(data_type);
                temp.setGroup_id(group_id);
                temp.setOwner_id(creator_id);
                temp.setMember_num(member_nums);
                temp.setCreat_date(date);
                temp.setDescription(description);
//                if (owner!=null) temp.setOwner(owner);
//                else throw new Throwable("ONT FIND OWNER!");
                results.add(temp);
            }

            for (int i = 0; i < results.size(); i++) {
                GroupNode temp = results.get(i);
                UserNode owner = queryUserByID(temp.getOwner_id());
                temp.setOwner(owner);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return results;
    }

    /**根据user_id查找其注册的数据集
     * 如果查询失败，返回null，并抛出异常
     * 否则至少返回0**/
    public static ArrayList<DataNode> queryDataNodesByID(String user_id){
        String sql = "SELECT * FROM DATANODES WHERE user_id = "+ user_id;
        ArrayList<DataNode> result = new ArrayList<DataNode>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                String user_id_temp = resultSet.getString("user_id");
                String data_name = resultSet.getString("data_name");
                String data_type = resultSet.getString("data_type");
                int row_nums = resultSet.getInt("row_nums");
                int attr_nums = resultSet.getInt("attr_nums");
                String file_path = resultSet.getString("file_path");
                DataNode temp = new DataNode(user_id_temp, data_name, data_type, row_nums, attr_nums, file_path);
                result.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 新添群组数据注册元组(group_id, user_id, dataset_name)
     * 需要检查type是否一致，若不一致，返回false，并抛出异常
     * 添加成功返回true
     * 实际上只会在dataload界面的choose按钮点击时被使用
     * **/
    public static boolean insertGroupDataRegisterRelation(GroupNode groupNode,DataNode dataNode){
        try {
            if (groupNode.getType().equals(dataNode.getData_type())) {
                PreparedStatement insert = null;
                insert = con.prepareStatement(GroupDataRegisterRelationInsert);
                insert.setString(1, groupNode.getGroup_id());
                insert.setString(2, dataNode.getUser_id());
                insert.setString(3, dataNode.getData_name());
                insert.executeUpdate();
            } else {
                throw new Throwable("WRONG TYPE MATCH");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return false;
        }
        return true;

    }

    /**根据group_id,获取所有的在群组中注册了的数据
     * 若查询失败，则返回null
     * 否则至少返回一个长度为0的数组**/
    public static ArrayList<DataNode> queryRegisterdDataNodesByID(String group_id){
        ArrayList<DataNode> result = new ArrayList<DataNode>();
        try {
            String sql = "SELECT s1.dataset_name, s2.row_nums, s2.attr_nums, s3.user_name " +
                    "FROM (contain as s1 JOIN DATANODES as s2 ON " +
                    "(s1.user_id = s2.user_id and s1.dataset_name = s2.data_name) ) " +
                    "JOIN CLIENTNODES as s3 ON s1.user_id = s3.user_id " +
                    "where s1.group_id = " + group_id;
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                DataNode dataNode = new DataNode(
                        resultSet.getString("s1.dataset_name"),
                        resultSet.getInt("s2.row_nums"),
                        resultSet.getInt("s2.attr_nums"),
                        resultSet.getString("s3.user_name"));

                result.add(dataNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
