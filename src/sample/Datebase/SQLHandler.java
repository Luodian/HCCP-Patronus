package sample.Datebase;


import sample.Entity.*;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class SQLHandler {
    public static String url = "jdbc:mysql://localhost:3306/Patronus" ;
    public static String username = "root" ;
    public static String password = "wyz123348377";
    public static String UserNodeInsert = "INSERT INTO CLIENTNODES(user_name, user_id, email, password) values (?,?,?,?) ";
    public static String DataNodeInsert = "INSERT INTO DATANODES(user_id, data_name, data_type, row_nums, attr_nums, file_path) values(?,?,?,?,?,?)";
    public static String GroupInsert = "INSERT INTO GROUPS(group_name, data_type, group_id, member_nums, creator_id, create_date, description) values(?,?,?,?,?,?,?)";
    public static String GroupUserRelationInsert = "INSERT INTO belongs_to (user_id, group_id) values (?,?)";
    public static String GroupDataRegisterRelationInsert = "INSERT INTO contain (group_id, user_id, dataset_name) values (?,?,?)";
    public static String ComputeTaskInsert = "INSERT INTO COMPUTETASK(task_id, data_type, cost, initiator_id, security_score, start_time, end_time, state, task_name) values(?,?,?,?,?,?,?,?,?)";
    public static String WorksOnInsert = "insert into works_on (task_id, master_id, slave_id, slave_data_name, state) values(?,?,?,?,?)";
    public static Statement query;
    public static Connection con;

    public static void init() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        con = DriverManager.getConnection(url, username, password);
        query = con.createStatement();
    }

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
        String sql = "select * from CLIENTNODES where email = \'" + userNode.getEmail() + "\' and password = \'" + userNode.getPassword() + "\';";
        ResultSet resultSet = query.executeQuery(sql);
        if (resultSet.next()){//不为空
            return resultSet.getString("user_id");
        }
        return "NOTFIND";
    }

    /**根据用户id查询用户，若找到返回UserNode，否则返回null**/
    public static UserNode queryUserByID(String user_id){
        String sql = "SELECT * from CLIENTNODES where user_id = '" + user_id + "';";
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
            insertGroup.setString(7, groupNode.getDescription()); //插入中文会出错
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
    public static ArrayList<DataNode> queryDataNodesByID(String user_id) {
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
    public static ArrayList<DataNode> queryRegisterdDataNodesByGroupID(String group_id) {
        ArrayList<DataNode> result = new ArrayList<DataNode>();
        try {
            String sql = "SELECT s1.dataset_name, s2.row_nums, s2.attr_nums, s3.user_name, s3.user_id " +
                    "FROM (contain as s1 JOIN DATANODES as s2 ON " +
                    "(s1.user_id = s2.user_id and s1.dataset_name = s2.data_name) ) " +
                    "JOIN CLIENTNODES as s3 ON s1.user_id = s3.user_id " +
                    "where s1.group_id = '" + group_id + "';";
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()){
                DataNode dataNode = new DataNode(
                        resultSet.getString("s1.dataset_name"),
                        resultSet.getInt("s2.row_nums"),
                        resultSet.getInt("s2.attr_nums"),
                        resultSet.getString("s3.user_name"));
                dataNode.setUser_id(resultSet.getString("s3.user_id"));
                result.add(dataNode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**新添计算任务元组
     * state = 0 表示未启动
     * state = 1 表示正在工作
     * state = 2 表示工作结束**/
    public static boolean insertComputeTask(ComputeTask computeTask){
        try {
            PreparedStatement insert = con.prepareStatement(ComputeTaskInsert);
            insert.setString(1, computeTask.getTask_id());
            insert.setString(2, computeTask.getData_type());
            insert.setDouble(3, computeTask.getCost());
            insert.setString(4, computeTask.getInitiator_id());
            insert.setDouble(5, computeTask.getSecurity_score());
            insert.setString(6, computeTask.getStart_time());
            insert.setString(7, computeTask.getEnd_time());
            insert.setInt(8, computeTask.getState());
            insert.setString(9, computeTask.getTask_name());

            insert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**修改计算任务的属性**/
    public static boolean alterComputeTask(){return true;}

    /**通过状态和发起者id查询任务
     * state = 0 表示未启动
     * state = 1 表示正在工作
     * state = 2 表示工作结束
     * state = -1 表示获得所有状态的计算任务**/
    public static ArrayList<ComputeTask> queryComputeTaskByInitiatorIDAndState(String initiator_id, int state) {
        String sql;
        if (state != -1)
            sql = "SELECT * FROM COMPUTETASK WHERE initiator_id = " + initiator_id + " And state = " + state;
        else sql = "SELECT * FROM COMPUTETASK WHERE initiator_id = " + initiator_id;
        ArrayList<ComputeTask> result = new ArrayList<ComputeTask>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()) {
                ComputeTask computeTask = new ComputeTask();
                computeTask.setTask_id(resultSet.getString("task_id"));
                computeTask.setInitiator_id(resultSet.getString("initiator_id"));
                computeTask.setData_type(resultSet.getString("data_type"));
                computeTask.setCost(resultSet.getDouble("cost"));
                computeTask.setSecurity_score(resultSet.getDouble("security_score"));
                computeTask.setStart_time(resultSet.getString("start_time"));
                computeTask.setEnd_time(resultSet.getString("end_time"));
                computeTask.setState(resultSet.getInt("state"));
                computeTask.setTask_name(resultSet.getString("task_name"));
                computeTask.setGroup_id(resultSet.getString("group_id"));
                result.add(computeTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    //把slaver和task的工作关系插入到works_on中
    public static void insertWorksOn(String taskId, String master_id, String slaverId, String slave_data_name) throws SQLException {
        PreparedStatement insert_works_on = con.prepareStatement(WorksOnInsert);
        insert_works_on.setString(1, taskId);
        insert_works_on.setString(2, master_id);
        insert_works_on.setString(3, slaverId);
        insert_works_on.setString(4, slave_data_name);
        insert_works_on.setInt(5, 0);
        insert_works_on.executeUpdate();
    }

    /**
     * 修改works_on中元组的state
     **/
    public static boolean alterWorksOnTupleState(String task_id, String master_id, String slave_id, String slave_data_name, int state) {
        String sql = "update works_on set state = ? where task_id = ? and master_id = ? and slave_id = ? and slave_data_name = ?";
        try {
            PreparedStatement alter = con.prepareStatement(sql);
            alter.setInt(1, state);
            alter.setString(2, task_id);
            alter.setString(3, master_id);
            alter.setString(4, slave_id);
            alter.setString(5, slave_data_name);
            alter.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //根据群组的id去查找群组
    public static GroupNode queryGroupByGroupId(String id) {
        String sql = "select * from groups where group_id = '" + id + "';";
        try {
            ResultSet resultSet = query.executeQuery(sql);
            if (resultSet.next()) {
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
                return temp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    //查询一个ID是否已经存在,-1表示出错，0表示不存在，1表示存在
    public static int isExistID(String type, String id) {

        String sql;
        switch (type) {
            case "user":
                sql = "SELECT * from clientnodes where user_id = '" + id + "';";
                break;
            case "group":
                sql = "SELECT * from groups where group_id = '" + id + "';";
                break;
            case "task":
                sql = "SELECT  * from computetask where task_id = '" + id + "';";
                break;
            default:
                return -1;
        }
        try {
            ResultSet rs = query.executeQuery(sql);
            if (rs.next()) {
                return 1;
            } else
                return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 根据task_id查找task
     **/
    public static ComputeTask queryTaskByTaskId(String task_id) {
        String sql = "select * from computetask where task_id = '" + task_id + "';";
        ComputeTask computeTask = null;
        try {
            ResultSet rs = query.executeQuery(sql);
            if (rs.next()) {
                String data_type = rs.getString("data_type");
                Double cost = rs.getDouble("cost");
                String initiator_id = rs.getString("initiator_id");
                Double securityScore = rs.getDouble("security_score");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                int state = rs.getInt("state");
                String taskName = rs.getString("task_name");
                String groupId = rs.getString("group_id");
//                String code = rs.getString("code");
                computeTask = new ComputeTask(task_id, data_type, cost, initiator_id, securityScore, startTime, endTime,
                        state, taskName, groupId, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return computeTask;
    }

    /*根据用户id和群组id来查到数据集*/
    public static ArrayList<String> queryDataSetNameByUserIdAndGroupID(String user_id, String group_id) {
        String sql = "SELECT dataset_name FROM contain WHERE group_id = '" + group_id + "' and user_id = '" + user_id + "';";
        ArrayList<String> result = new ArrayList<String>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()) {
                String temp = resultSet.getString("dataset_name");
                result.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 根据task_id找到对应的所有slaver_id
     */
    public static ArrayList<String> querySlaverIdByTaskId(String taskId) {
        String sql = "select slaver_id from works_on where task_id = " + taskId;
        ArrayList<String> result = new ArrayList<String>();
        try {
            ResultSet resultSet = query.executeQuery(sql);
            while (resultSet.next()) {
                String temp = resultSet.getString("slaver_id");
                result.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 修改task绑定的群组
     **/
    public static boolean alterTaskBindGroup(String task_id, String group_id) {
        String sql = "update COMPUTETASK set group_id = ? where task_id = ?";
        PreparedStatement alter = null;
        try {
            alter = con.prepareStatement(sql);
            alter.setString(1, group_id);
            alter.setString(2, task_id);
            alter.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改task的状态
     **/
    public static boolean alterTaskState(String task_id, int state) {
        String sql = "update COMPUTETASK set state = ? where task_id = ?";
        try {
            PreparedStatement alter = con.prepareStatement(sql);
            alter.setInt(1, state);
            alter.setString(2, task_id);
            alter.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据master_id,查询所有正在工作的slaves
     **/
    public static ArrayList<TaskPaneItem> queryWorkingSlaves(String master_id) {
        String sql = "SELECT s1.slave_data_name, s2.user_name, s3.task_name, s1.master_id, s1.task_id, s1.slave_id" +
                " from works_on as s1 " +
                "JOIN clientnodes as s2 on s1.slave_id = s2.user_id JOIN computetask as s3 on " +
                "s1.task_id = s3.task_id where s1.state = 0 and s1.master_id = " + master_id;
        try {
            ResultSet resultSet = query.executeQuery(sql);
            ArrayList<TaskPaneItem> results = new ArrayList<TaskPaneItem>();
            while (resultSet.next()) {
                TaskPaneItem tmp = new TaskPaneItem();
                String slave_data_name = resultSet.getString("s1.slave_data_name");
                String slave_name = resultSet.getString("s2.user_name");
                String task_name = resultSet.getString("s3.task_name");
                String slave_id = resultSet.getString("s1.slave_id");
                String task_id = resultSet.getString("s1.task_id");
                tmp.setData_name(slave_data_name);
                tmp.setTask_name(task_name);
                tmp.setUser_name(slave_name);
                tmp.setUser_id(slave_id);
                tmp.setTask_id(task_id);
                results.add(tmp);
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据slave_id，查询所有在其上的masters
     **/
    public static ArrayList<TaskPaneItem> queryWorkingMasters(String slave_id) {
        String sql = "SELECT s1.slave_data_name, s2.user_name, s3.task_name, s1.master_id, s1.task_id, s1.slave_id from works_on as s1 " +
                "JOIN clientnodes as s2 on s1.master_id = s2.user_id JOIN computetask as s3 on " +
                "s1.task_id = s3.task_id where s1.state = 0 and  s1.slave_id = " + slave_id;
        try {
            ResultSet resultSet = query.executeQuery(sql);
            ArrayList<TaskPaneItem> results = new ArrayList<TaskPaneItem>();
            while (resultSet.next()) {
                TaskPaneItem tmp = new TaskPaneItem();
                String master_name = resultSet.getString("s2.user_name");
                String task_name = resultSet.getString("s3.task_name");
                String slave_data_name = resultSet.getString("s1.slave_data_name");
                String master_id = resultSet.getString("s1.master_id");
                String task_id = resultSet.getString("s1.task_id");
                tmp.setData_name(slave_data_name);
                tmp.setTask_name(task_name);
                tmp.setUser_name(master_name);
                tmp.setTask_id(task_id);
                tmp.setUser_id(master_id);
                results.add(tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给定salve_id,slave_data_name判断master_id是否在黑名单中
     **/
    public static boolean isBlackRulesHitted(String master_id, String slave_id, String slave_data_name) {
        String sql = "SELECT * from blackrules where master_id = " + master_id + " and slave_id = " + slave_id + " and slave_data_name = " + "slave_data_name";
        try {
            ResultSet resultSet = query.executeQuery(sql);
            if (resultSet.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 给定salve_id,slave_data_name判断master_id是否在白名单中
     **/
    public static boolean isWhiteRulesHitted(String master_id, String slave_id, String slave_data_name) {
        String sql = "SELECT * from whiterules where master_id = " + master_id + " and slave_id = " + slave_id + " and slave_data_name = " + "slave_data_name";
        try {
            ResultSet set = query.executeQuery(sql);
            if (set.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 添加白名单项,主体是数据实体，每个数据实体都有对应的黑白名单
     **/
    public static boolean insertWhiteRulesItem(String master_id, String slave_id, String slave_data_name) {
        String sql = "INSERT into whiterules (master_id, slave_id, slave_data_name) values (?,?,?)";
        try {
            PreparedStatement insert = con.prepareStatement(sql);
            insert.setString(1, master_id);
            insert.setString(2, slave_id);
            insert.setString(3, slave_data_name);
            insert.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加黑名单项,主体是数据实体，每个数据实体都有对应的黑白名单
     **/
    public static boolean insertBlackRulesItem(String master_id, String slave_id, String slave_data_name) {
        String sql = "INSERT into blackrules (master_id, slave_id, slave_data_name) values (?,?,?)";
        try {
            PreparedStatement insert = con.prepareStatement(sql);
            insert.setString(2, slave_id);
            insert.setString(3, slave_data_name);
            insert.setString(1, master_id);
            insert.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
