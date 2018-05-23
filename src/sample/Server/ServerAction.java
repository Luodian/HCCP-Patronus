package sample.Server;

import com.oracle.javafx.jmx.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.Datebase.SQLHandler;
import sample.Entity.*;
import sample.Utils.Tools;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerAction {

    //处理注册事件
    public static JSONObject doRegister(JSONObject json) throws IOException, org.json.JSONException {
        //注意这里是Object，不是String
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String userName = json.getString("user_name");
            String password = json.getString("password");
            String email = json.getString("email");
            String userId = Tools.getRandomID("user");
            SQLHandler.insertUser(new UserNode(userName, password, email, userId));
            //成功插入
            hashMap.put("result", 1);
            hashMap.put("reply", 0);
            hashMap.put("user_id", userId);
        } catch (JSONException | SQLException e) {
            e.printStackTrace();
            return sendErrorMsg(0);
        }
        return new JSONObject(hashMap);
    }

    //处理登录事件
    public static JSONObject doLogin(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String email = json.getString("email");
            String password = json.getString("password");
            String result = SQLHandler.isUserExistedByUserNode(new UserNode(password, email));
            if (result.equals("NOTFIND")) {
//                hashMap.put("result",0);
                return sendErrorMsg(1);
            } else {
                hashMap.put("result", 1);
                hashMap.put("reply", 1);
                hashMap.put("user_id", result);
                hashMap.put("user_name", SQLHandler.queryUserByID(result).getUser_name());
            }
        } catch (JSONException | SQLException e) {
            e.printStackTrace();
//            hashMap.put("result",0);
            return sendErrorMsg(1);
        }
        return new JSONObject(hashMap);
    }

    //处理通过ID查询用户的请求
    public static JSONObject doQueryUser(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String user_id = json.getString("user_id");
            UserNode userNode = SQLHandler.queryUserByID(user_id);
            if (userNode == null) {
                return sendErrorMsg(2);
            } else {
                hashMap.put("result", 1);
                hashMap.put("reply", 2);
                hashMap.put("user_id", user_id);
                hashMap.put("user_name", userNode.getUser_name());
                hashMap.put("email", userNode.getEmail());
                hashMap.put("password", userNode.getPassword());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return sendErrorMsg(2);
        }
        return new JSONObject(hashMap);
    }

    //处理创建群组的请求
    public static JSONObject doCreateGroup(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String creatorId = json.getString("creator_id");
            String groupName = json.getString("group_name");
            String dataType = json.getString("data_type");
            String description = json.getString("description");
            Date date = Date.valueOf(LocalDate.now());
            int memberNum = json.getInt("member_num");

            String groupId = Tools.getRandomID("group");
            UserNode userNode = SQLHandler.queryUserByID(creatorId);
            if (userNode == null) {
                hashMap.put("result", 0);
                hashMap.put("reply", 3);
            } else {
                GroupNode groupNode = new GroupNode(groupId, groupName, dataType);
                groupNode.setOwner(userNode);
                groupNode.setOwner_id(creatorId);
                groupNode.setDescription(description);
                groupNode.setMember_num(memberNum);
                groupNode.setCreat_date(date);
                SQLHandler.insertGroup(groupNode);
                hashMap.put("result", 1);
                hashMap.put("reply", 3);
                hashMap.put("group_id", groupId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hashMap.put("result", 0);
            hashMap.put("reply", 3);
        }
        return new JSONObject(hashMap);
    }

    //处理用户加入群组的请求
    public static JSONObject doJoinGroup(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String userId = json.getString("user_id");
            String groupId = json.getString("group_id");
            boolean success = SQLHandler.insertGroupUserRelation(userId, groupId);
            if (success) {
                hashMap.put("result", 1);
                hashMap.put("reply", 4);
            } else {
                hashMap.put("result", 0);
                hashMap.put("reply", 4);
            }
        } catch (JSONException e) {
            hashMap.put("result", 0);
            hashMap.put("reply", 4);
            e.printStackTrace();
        }
        return new JSONObject(hashMap);
    }

    //处理查询用户分组的请求
    public static JSONObject doQueryGroupByUserId(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            List<GroupNode> groupNodes = SQLHandler.queryGroupsByUserID(json.getString("user_id"));
            if (groupNodes == null) {
                jsonObject.put("result", 0);
                jsonObject.put("reply", 5);
            } else {
                JSONArray jsonArray = new JSONArray();
                for (GroupNode groupNode : groupNodes) {
                    JSONObject group = new JSONObject();
                    group.put("group_id", groupNode.getGroup_id());
                    group.put("group_name", groupNode.getGroup_name());
                    group.put("creator_id", groupNode.getOwner_id());
                    group.put("create_date", groupNode.getCreat_date().toString());
                    group.put("description", groupNode.getDescription());
                    group.put("member_num", groupNode.getMember_num());
                    group.put("data_type", groupNode.getType());
                    jsonArray.put(group);
                }
                jsonObject.put("groups", jsonArray);
                jsonObject.put("result", 1);
                jsonObject.put("reply", 5);
            }
        } catch (JSONException e) {
            //JSON异常处理中，不可用json
            hashMap.put("result", 0);
            hashMap.put("reply", 5);
            e.printStackTrace();
        }
        if (hashMap.containsKey("result")) {
            return new JSONObject(hashMap);
        } else
            return jsonObject;
    }

    //处理查询用户数据集的请求
    public static JSONObject doQueryDataNodesByUserId(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            List<DataNode> dataNodes = SQLHandler.queryDataNodesByID(json.getString("user_id"));
            if (dataNodes == null) {
                jsonObject.put("result", 0);
                jsonObject.put("reply", 6);
            } else {
                JSONArray jsonArray = new JSONArray();
                for (DataNode dataNode : dataNodes) {
                    JSONObject data = new JSONObject();
                    data.put("user_id", dataNode.getUser_id());
                    data.put("data_name", dataNode.getData_name());
                    data.put("data_type", dataNode.getData_type());
                    data.put("row_nums", dataNode.getRow_nums());
                    data.put("attr_nums", dataNode.getAttr_nums());
                    data.put("file_path", dataNode.getFile_path());
                    jsonArray.put(data);
                }
                jsonObject.put("datas", jsonArray);
                jsonObject.put("result", 1);
                jsonObject.put("reply", 6);
            }
        } catch (JSONException e) {
            //JSON异常处理中，不可用json
            hashMap.put("result", 0);
            hashMap.put("reply", 6);
            e.printStackTrace();
        }
        if (hashMap.containsKey("result")) {
            return new JSONObject(hashMap);
        } else
            return jsonObject;
    }

    //把数据集添加进群组
    public static JSONObject doInsertGroupDataRegisterRelation(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            String groupId = json.getString("group_id");
            String userId = json.getString("user_id");
            String dataName = json.getString("data_name");
            String dataType = json.getString("data_type");
            DataNode dataNode = new DataNode();
            dataNode.setUser_id(userId);
            dataNode.setData_name(dataName);
            dataNode.setData_type(dataType);

            //假设这里一定能找到群组吧
            GroupNode groupNode = SQLHandler.queryGroupByGroupId(groupId);
            boolean success = SQLHandler.insertGroupDataRegisterRelation(groupNode, dataNode);
            if (success) {
                hashMap.put("result", 1);
                hashMap.put("reply", 7);
            } else {
                hashMap.put("result", 0);
                hashMap.put("reply", 7);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hashMap.put("result", 0);
            hashMap.put("reply", 7);
        }
        return new JSONObject(hashMap);
    }

    //处理查询群组数据集的请求
    public static JSONObject doQueryDataNodesByGroupId(JSONObject json) throws org.json.JSONException {
        HashMap<String, Object> hashMap = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        try {
            List<DataNode> dataNodes = SQLHandler.queryRegisterdDataNodesByGroupID(json.getString("group_id"));
            if (dataNodes == null || dataNodes.size() == 0) {
                jsonObject.put("result", 0);
                jsonObject.put("reply", 8);
            } else {
                JSONArray jsonArray = new JSONArray();
                for (DataNode dataNode : dataNodes) {
                    JSONObject data = new JSONObject();
                    data.put("user_name", dataNode.getUser_name());
                    data.put("data_name", dataNode.getData_name());
                    data.put("row_nums", dataNode.getRow_nums());
                    data.put("attr_nums", dataNode.getAttr_nums());
                    jsonArray.put(data);
                }
                jsonObject.put("datas", jsonArray);
                jsonObject.put("result", 1);
                jsonObject.put("reply", 8);
            }
        } catch (JSONException e) {
            //JSON异常处理中，不可用json
            hashMap.put("result", 0);
            hashMap.put("reply", 8);
            e.printStackTrace();
        }
        if (hashMap.containsKey("result")) {
            return new JSONObject(hashMap);
        } else
            return jsonObject;
    }

    //处理任务创建
    public static JSONObject doTask(JSONObject json) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String taskId = Tools.getRandomID("task");
            String taskName = json.getString("task_name");
            String initiatorId = json.getString("initiator_id");
            String groupId = json.getString("group_id");
            String code = json.getString("code");
            int state = json.getInt("state");
            double cost = json.getDouble("cost");
            double score = json.getDouble("security_score");
            String data_type = json.getString("data_type");
            /**insertComputeTask里面还没加group_id字段**/
            ComputeTask computeTask = new ComputeTask();
            computeTask.setInitiator_id(initiatorId);
            computeTask.setCode(code);
            computeTask.setTask_name(taskName);
            computeTask.setTask_id(taskId);
            computeTask.setState(state);
            computeTask.setGroup_id(groupId);
            computeTask.setCost(cost);
            computeTask.setSecurity_score(score);
            computeTask.setData_type(data_type);

            Boolean success = SQLHandler.insertComputeTask(computeTask);
            if (success) {
                jsonObject.put("result", 1);
                jsonObject.put("reply", 9);
            } else {
                jsonObject.put("result", 0);
                jsonObject.put("reply", 9);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return sendErrorMsg(9);
        }
        return jsonObject;
    }

    /**
     * type 100，启动神经网络
     * 给group中所有注册了的数据集发送启动运算消息，reply = 101
     * 将启动的数据节点信息写入数据库
     * 该日master回传消息 reply = 100，以及result = 1表示启动成功
     * master收到消息后调用doQueryWorkingSlaves获取slave信息
     **/
    public static JSONObject doPowerSlaver(JSONObject json, Map<String, Socket> userMap) {
        JSONObject jsonObject = new JSONObject();
        try {
            String taskId = json.getString("task_id");
            String action = json.getString("action");
            if (action.equals("run")) {
                ComputeTask computeTask = SQLHandler.queryTaskByTaskId(taskId);
                /**若没找到task，直接返回10，0
                 * 101代表抵达请求**/
                if (computeTask == null) return sendErrorMsg(10);

                String master_id = computeTask.getInitiator_id();
                String group_id = computeTask.getGroup_id();
                String code = computeTask.getCode();
                String task_name = computeTask.getTask_name();


                /**查找该任务所绑定的群组的所有注册数据
                 * 并根据返回的数据节点找到这些数据的所有者id*
                 * 再根据这些id找到对应的socket连接*/
                String binded_group_id = SQLHandler.queryTaskByTaskId(taskId).getGroup_id();
                List<DataNode> slaveDatas = SQLHandler.queryRegisterdDataNodesByGroupID(binded_group_id);

                /**修改任务状态**/
                SQLHandler.alterTaskState(taskId, 1);

                for (DataNode slaveData : slaveDatas) {

                    String slave_id = slaveData.getUser_id();
                    String slave_data_name = slaveData.getData_name();
                    Socket slave_socket = userMap.get(slave_id);
                    JSONObject powerMsg = new JSONObject();

                    /**
                     * 权限判断
                     * 首先进行白名单过滤，若命中，则添加字段jurisdiction:authorized以及reply:101
                     * 否则，进行黑名单过滤，若命中，则dropout
                     * 否则将消息转发给用户，添加字段jurisdiction:unauthorized以及reply:102
                     * **/

                    if (SQLHandler.isWhiteRulesHitted(master_id, slave_id, slave_data_name)) {
                        powerMsg.put("jurisdiction", "authorized");
                        powerMsg.put("reply", 101);
                    } else if (SQLHandler.isBlackRulesHitted(master_id, slave_id, slave_data_name)) continue;
                    else {
                        powerMsg.put("jurisdiction", "unauthorized");
                        powerMsg.put("reply", 102);
                    }

                    String master_name = SQLHandler.queryUserByID(master_id).getUser_name();

                    powerMsg.put("result", 1);
                    powerMsg.put("task_id", taskId);
                    powerMsg.put("master_id", master_id);
                    powerMsg.put("group_id", group_id);
                    powerMsg.put("task_name", task_name);
                    powerMsg.put("master_name", master_name);
                    powerMsg.put("data_name", slave_data_name);
                    powerMsg.put("code", code);


                    if (slave_socket != null && slave_socket.isConnected()) {
                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(slave_socket.getOutputStream());
                            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                            /**向slave转发请求**/
                            bufferedWriter.write(powerMsg.toString() + "\r\n");
                            bufferedWriter.flush();
                            /**添加正在工作信息**/
                            SQLHandler.insertWorksOn(taskId, master_id, slave_id, slaveData.getData_name());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (slaveDatas.size() != 0) {//如果有一个slave，就可以正常执行
                    jsonObject.put("result", 1);
                    jsonObject.put("reply", 100);
                } else {
                    jsonObject.put("result", 0);
                    jsonObject.put("reply", 100);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //处理slaver报告的中间过程消息
    public static JSONObject doProcessMsg(JSONObject json, Map<String, Socket> userMap) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String slaverId = json.getString("slaver_id");
            String taskId = json.getString("task_id");
            String text = json.getString("text");
            String masterId = SQLHandler.queryTaskByTaskId(taskId).getInitiator_id();
            if (userMap.keySet().contains(masterId)) {
                Socket socket = userMap.get(masterId);
                if (socket.isConnected()) {
                    JSONObject midMsg = new JSONObject();
                    midMsg.put("push", 1);
                    midMsg.put("slaver_id", slaverId);
                    midMsg.put("text", text);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.write(midMsg.toString().getBytes());
                    dataOutputStream.flush();
                    jsonObject.put("result", 1);
                    jsonObject.put("reply", 11);
                }
            }
            //

        } catch (JSONException e) {
            e.printStackTrace();
            return sendErrorMsg(11);
        } catch (IOException e) {
            e.printStackTrace();
            return sendErrorMsg(11);
        }
        return jsonObject;
    }

    /**
     * 这个是强行终止，我感觉可以先放放
     **/
    public static JSONObject doStop(JSONObject json, Map<String, Socket> userMap) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String task_id = json.getString("task_id");
            String action = json.getString("action");
            String master = json.getString("initiator_id");
            if (action.equals("stop")) {
                ArrayList<String> slavers = SQLHandler.querySlaverIdByTaskId(task_id);
                for (String slaverId : slavers) {
                    if (userMap.keySet().contains(slaverId)) {
                        Socket slaverSocekt = userMap.get(slaverId);
                        if (slaverSocekt.isConnected()) {
                            JSONObject stopMsg = new JSONObject();
                            DataOutputStream dataOutputStream = new DataOutputStream(slaverSocekt.getOutputStream());
                            stopMsg.put("push", 2);
                            stopMsg.put("result", 1);
                            dataOutputStream.write(stopMsg.toString().getBytes());
                            dataOutputStream.flush();
                        }
                    }
                }
                jsonObject.put("result", 1);
                jsonObject.put("reply", 12);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return sendErrorMsg(12);
        } catch (IOException e) {
            e.printStackTrace();
            return sendErrorMsg(12);
        }
        return jsonObject;
    }

    /**
     * 发起任务还没有检测类型
     **/
    public static JSONObject doQueryTaskByStateAndUserId(JSONObject json) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String userId = json.getString("user_id");
            int state = json.getInt("state");
            ArrayList<ComputeTask> computeTasks = SQLHandler.queryComputeTaskByInitiatorIDAndState(userId, state);
            JSONArray jsonArray = new JSONArray();
            for (ComputeTask task : computeTasks) {
                JSONObject tmp = new JSONObject();
                tmp.put("task_id", task.getTask_id());
//                tmp.put("code",task.getCode());
                tmp.put("data_type", task.getData_type());
                tmp.put("cost", task.getCost());
                tmp.put("initiator_id", task.getInitiator_id());
                tmp.put("security_score", task.getSecurity_score());
                tmp.put("state", task.getState());
                tmp.put("task_name", task.getTask_name());
                /**若未绑定group,则返回null**/
                if (task.getGroup_id() == null) tmp.put("group_id", "null");
                else tmp.put("group_id", task.getGroup_id());
                jsonArray.put(tmp);
            }
            jsonObject.put("tasks", jsonArray);
            jsonObject.put("reply", 13);
            jsonObject.put("result", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject(new HashMap().put("result", 0));
        }
        return jsonObject;
    }

    //根据用户id和群组id找到对应的数据集
    public static JSONObject doQueryDataNameByUserIdAndGroupId(JSONObject json) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        try {
            String userId = json.getString("user_id");
            String groupId = json.getString("group_id");
            ArrayList<String> dataNames = SQLHandler.queryDataSetNameByUserIdAndGroupID(userId, groupId);
            JSONArray jsonArray = new JSONArray();
            for (String dataName : dataNames) {
                JSONObject tmp = new JSONObject();
                tmp.put("data_name", dataName);
                jsonArray.put(tmp);
            }
            jsonObject.put("data_names", jsonArray);
            jsonObject.put("reply", 14);
            jsonObject.put("result", 1);

        } catch (JSONException e) {
            e.printStackTrace();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("reply", 14);
            jsonObject1.put("result", 0);
            return jsonObject1;
        }
        return jsonObject;
    }

    /**
     * 添加数据集
     **/
    public static JSONObject doInsertDataNode(JSONObject json) throws org.json.JSONException {
        JSONObject jsonObject = new JSONObject();
        String userID = json.getString("user_id");
        String data_name = json.getString("data_name");
        String data_type = json.getString("data_type");
        int row_num = json.getInt("row_num");
        int attr_num = json.getInt("attr_num");
        String file_path = json.getString("file_path");
        DataNode dataNode = new DataNode(userID, data_name, data_type, row_num, attr_num, file_path);
        if (SQLHandler.insertDataNode(dataNode)) {
            jsonObject.put("reply", 15);
            jsonObject.put("result", 1);

        } else {
            jsonObject.put("reply", 15);
            jsonObject.put("result", 0);
        }
        return jsonObject;
    }

    static JSONObject sendErrorMsg(int reply) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("reply", reply);
        hashMap.put("result", 0);
        JSONObject jsonObject = new JSONObject(hashMap);
        return jsonObject;
    }

    public static JSONObject doAlterTaskBindGroup(JSONObject json) throws org.json.JSONException {
        String task_id = json.getString("task_id");
        String group_id = json.getString("group_id");

        JSONObject jsonObject = new JSONObject();
        if (SQLHandler.alterTaskBindGroup(task_id, group_id)) {
            jsonObject.put("reply", 16);
            jsonObject.put("result", 1);
        } else {
            jsonObject.put("reply", 16);
            jsonObject.put("result", 0);
        }
        return jsonObject;
    }

    /**
     * slave执行结束后，向服务器回传一个消息，服务器收到消息后，向master发送一个消息
     * 发过来的消息purpose为200,确认消息是200，而master监听的是200
     **/
    public static JSONObject doTaskEnd(JSONObject json) {
        JSONObject ack = new JSONObject();
        try {
            String task_id = json.getString("task_id");
            String master_id = json.getString("master_id");
            String slave_id = json.getString("slave_id");
            String data_name = json.getString("slave_data_name");
            /**向master发送消息，然后向salve回传一个确认消息**/
            Socket socket = ServerRun.getUserMap().get(master_id);
            JSONObject informMaster = new JSONObject();
            informMaster.put("task_id", task_id);
            informMaster.put("master_id", master_id);
            informMaster.put("slave_id", slave_id);
            informMaster.put("data_name", data_name);
            /**在works_on表中将对应的项状态改为1**/
            if (SQLHandler.alterWorksOnTupleState(task_id, master_id, slave_id, data_name, 1)) {

                informMaster.put("reply", 201);
                informMaster.put("result", 1);

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                bufferedWriter.write(informMaster.toString() + "\r\n");
                bufferedWriter.flush();

                /**向slave回传消息**/
                ack.put("reply", 200);
                ack.put("result", 1);

                return ack;

            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**出差时返回给slave的**/
        return sendErrorMsg(200);
    }

    /**
     * 根据master_id,查询所有正在工作的slaves,发给master
     **/
    public static JSONObject doQueryWorkingSlaves(JSONObject json) {
        try {
            String master_id = json.getString("master_id");
            ArrayList<TaskPaneItem> slaves = SQLHandler.queryWorkingSlaves(master_id);
            if (slaves != null) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (TaskPaneItem e : slaves) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("task_name", e.getTask_name());
                    tmp.put("slave_data_name", e.getData_name());
                    tmp.put("slave_name", e.getUser_name());
                    tmp.put("slave_id", e.getUser_id());
                    tmp.put("task_id", e.getTask_id());
                    jsonArray.put(tmp);
                }
                jsonObject.put("slaves", jsonArray);
                jsonObject.put("reply", 17);
                jsonObject.put("result", 1);
                return jsonObject;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return sendErrorMsg(17);
    }

    /**
     * slave_id,查询所有正在其上的master_id
     **/
    public static JSONObject doQueryWorkingMasters(JSONObject json) {
        try {
            String slave_id = json.getString("slave_id");
            ArrayList<TaskPaneItem> masters = SQLHandler.queryWorkingMasters(slave_id);
            if (masters != null) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (TaskPaneItem e : masters) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("master_name", e.getUser_name());
                    tmp.put("task_name", e.getTask_name());
                    tmp.put("data_name", e.getData_name());
                    tmp.put("master_id", e.getUser_id());
                    tmp.put("task_id", e.getTask_id());
                    jsonArray.put(tmp);
                }
                jsonObject.put("masters", jsonArray);
                jsonObject.put("reply", 18);
                jsonObject.put("result", 1);
                return jsonObject;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return sendErrorMsg(18);
    }

    /**
     * 插入白名单项
     **/
    public static JSONObject doInsertWhiteRulesItem(JSONObject json) {
        try {
            JSONObject jsonObject = new JSONObject();
            String master_id = json.getString("master_id");
            String slave_id = json.getString("slave_id");
            String slave_data_name = json.getString("slave_data_name");
            if (SQLHandler.insertWhiteRulesItem(master_id, slave_id, slave_data_name)) {
                jsonObject.put("result", 1);
                jsonObject.put("reply", 19);
                return jsonObject;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return sendErrorMsg(19);
    }

    /**
     * 插入黑名单项
     **/
    public static JSONObject doInsertBlackRulesItem(JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        try {
            String master_id = json.getString("master_id");
            String slave_id = json.getString("slave_id");
            String slave_data_name = json.getString("slave_data_name");
            if (SQLHandler.insertBlackRulesItem(master_id, slave_id, slave_data_name)) {
                jsonObject.put("result", 1);
                jsonObject.put("reply", 20);
                return jsonObject;
            }
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }
        return sendErrorMsg(20);
    }
}
