package sample.SocketConnect;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Controller.Task.TaskController;
import sample.Entity.*;
import sample.StartProcess;

import java.io.*;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class SocketHandler
{
	private static DataOutputStream dataOutputStream;
	private static DataInputStream dataInputStream;
    private static OutputStreamWriter outputStreamWriter;
    private static InputStreamReader inputStreamReader;
    private static BufferedWriter bufferedWriter;
    private static BufferedReader bufferedReader;
    private static int serverPort;
    private static String serverIP = null;
    private static Socket socket;
    public static Thread backgroundListner;
    public static Thread endTaskLisner;
    public static Thread readReply;

    /**
     * 每种reply对应一个bucket
     **/
    public static HashMap<Integer, JSONObject> responseBuffer;

	/**
	 * Type-0:注册-检验完成
	 */
    public static void insertUser(UserNode userNode) throws JSONException, IOException {
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 0);
		sendobj.put ("email", userNode.getEmail ());
		sendobj.put ("password", userNode.getPassword ());
		sendobj.put ("user_name", userNode.getUser_name ());
		dataOutputStream.write (sendobj.toString ().getBytes ());
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-1:登录-检验完成
	 */
    public static String isUserExistedByUserNode(String email, String password) {
        JSONObject sendObject = new JSONObject();
        try {
            sendObject.put("purpose", 1);
            sendObject.put("email", email);
            sendObject.put("password", password);
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(1)) continue;
                json = responseBuffer.remove(1);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 1) {
                    if (result == 1) return json.getString("user_id");
                    else if (result == 0) return "NOTFIND";
                } else return "UNKNOWN";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "EXCEPTION";
        } catch (IOException e) {
            e.printStackTrace();
            return "EXCEPTION";
        }
	}
	
	/**
	 * Type-2
	 * 根据用户id查询用户，若找到返回UserNode，否则返回null
	 **/
    public static UserNode queryUserByID(String user_id) {
        try {
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 2);
            sendObject.put("user_id", user_id);
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            String rcvJsonStr = null;
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(2)) continue;
                json = responseBuffer.remove(2);
                int reply = json.getInt("reply");
                int result = json.getInt("result");
                if (reply == 2) {
                    if (result == 1) {
                        UserNode userNode = new UserNode();
                        userNode.setUser_id(json.getString("user_id"));
                        userNode.setUser_name(json.getString("user_name"));
                        userNode.setEmail(json.getString("email"));
                        userNode.setPassword(json.getString("password"));
                        return userNode;
                    } else if (result == 0) return null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * Type-3:新添群组
	 **/
    public static String insertGroup(GroupNode groupNode) {
        try {
            JSONObject sendobj = new JSONObject();
            sendobj.put("purpose", 3);
            sendobj.put("group_name", groupNode.getGroup_name());
            sendobj.put("data_type", groupNode.getType());
            sendobj.put("creator_id", groupNode.getOwner_id());
            sendobj.put("description", groupNode.getDescription());
            sendobj.put("member_num", groupNode.getMember_num());
            bufferedWriter.write(sendobj.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(3)) continue;
                json = responseBuffer.remove(3);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 3) {
                    if (result == 1)
                        return json.getString("group_id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "EXCEPTION";
        } catch (IOException e) {
            e.printStackTrace();
            return "EXCEPTION";
        }
	}
	
	/**
     * Type-4:新添群组用户联系
	 **/
    public static boolean insertGroupUserRelation(String user_id, String group_id) {
        try {
            JSONObject sendobj = new JSONObject();
            sendobj.put("purpose", 4);
            sendobj.put("user_id", user_id);
            sendobj.put("group_id", group_id);
            bufferedWriter.write(sendobj.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(4)) continue;
                json = responseBuffer.remove(4);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 4) return result == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
	
	/**
     * Type-5: 根据用户id查询其所在的若干群组
     * 如果抛异常，返回null
     * 否则至少返回0
	 **/
    public static ArrayList<GroupNode> queryGroupsByUserID(String user_id) {
        try {
            ArrayList<GroupNode> results = new ArrayList<GroupNode>();
            JSONObject sendobj = new JSONObject();
            sendobj.put("purpose", 5);
            sendobj.put("user_id", user_id);
            bufferedWriter.write(sendobj.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(5)) continue;
                json = responseBuffer.remove(5);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 5) {
                    if (result == 1) {
                        JSONArray jsarray = json.getJSONArray("groups");
                        int len = jsarray.length();
                        for (int i = 0; i < len; ++i) {
                            JSONObject jsobj = jsarray.getJSONObject(i);
                            GroupNode temp = new GroupNode();
                            temp.setGroup_name(jsobj.getString("group_name"));
                            temp.setType(jsobj.getString("data_type"));
                            temp.setGroup_id(jsobj.getString("group_id"));
                            String creator = jsobj.getString("creator_id");
                            temp.setOwner_id(creator);
                            temp.setMember_num(jsobj.getInt("member_num"));
                            temp.setDescription(jsobj.getString("description"));
                            temp.setCreat_date(Date.valueOf(jsobj.getString("create_date")));
                            UserNode tempUser = SocketHandler.queryUserByID(creator);
                            if (tempUser != null) temp.setOwner(tempUser);
                            results.add(temp);
                        }
                        return results;
                    } else if (result == 0) return null;//查询失败
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
     * Type-6:查询用户的数据集
	 **/
    public static ArrayList<DataNode> queryDataNodesByID(String user_id) {
        try {
            ArrayList<DataNode> results = new ArrayList<DataNode>();
            JSONObject sendobj = new JSONObject();
            sendobj.put("purpose", 6);
            sendobj.put("user_id", user_id);
            bufferedWriter.write(sendobj.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(6)) continue;
                json = responseBuffer.remove(6);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 6) {
                    if (result == 1) {
                        JSONArray jsarray = json.getJSONArray("datas");
                        int len = jsarray.length();
                        for (int i = 0; i < len; ++i) {
                            JSONObject jsobj = jsarray.getJSONObject(i);
                            String user_id_temp = jsobj.getString("user_id");
                            String data_name = jsobj.getString("data_name");
                            String data_type = jsobj.getString("data_type");
                            int row_nums = jsobj.getInt("row_nums");
                            int attr_nums = jsobj.getInt("attr_nums");
                            String file_path = jsobj.getString("file_path");
                            DataNode temp = new DataNode(user_id_temp, data_name, data_type, row_nums, attr_nums, file_path);
                            results.add(temp);
                        }
                        return results;
                    } else if (result == 0) return null;//查询失败
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Type-7:在群组中注册数据集
     * 新添群组数据注册元组(group_id, user_id, dataset_name)
     * 需要检查type是否一致，若不一致，返回false，并抛出异常
     * 添加成功返回true
     * 实际上只会在dataload界面的choose按钮点击时被使用
     **/
    public static boolean insertGroupDataRegisterRelation(DataNode dataNode, String group_id) {
        try {
            JSONObject sendobj = new JSONObject();
            sendobj.put("purpose", 7);
            sendobj.put("group_id", group_id);
            sendobj.put("data_type", dataNode.getData_type());
            sendobj.put("user_id", dataNode.getUser_id());
            sendobj.put("data_name", dataNode.getData_name());
            bufferedWriter.write(sendobj.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(7)) continue;
                json = responseBuffer.remove(7);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 7) {
                    return result == 1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	/**
	 * Type-8
	 * 根据group_id,获取所有的在群组中注册了的数据
	 * 若查询失败，则返回null
	 * 否则至少返回一个长度为0的数组
	 **/
    public static ArrayList<DataNode> queryRegisterdDataNodesByID(String group_id) {
        try {
            ArrayList<DataNode> results = new ArrayList<>();
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 8);
            sendObject.put("group_id", group_id);
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(8)) continue;
                json = responseBuffer.remove(8);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 8) {
                    if (result == 1) {
                        JSONArray jsarray = json.getJSONArray("datas");
                        int len = jsarray.length();
                        for (int i = 0; i < len; ++i) {
                            JSONObject jsobj = jsarray.getJSONObject(i);
                            String data_name = jsobj.getString("data_name");
                            int row_num = jsobj.getInt("row_nums");
                            int attr_num = jsobj.getInt("attr_nums");
                            String user_name = jsobj.getString(("user_name"));
                            DataNode dtn = new DataNode(data_name, row_num, attr_num, user_name);
                            results.add(dtn);
                        }
                        return results;
                    } else return null;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * Type-9
	 * 新添计算任务元组
	 * state = 0 表示未启动
	 * state = 1 表示正在工作
	 * state = 2 表示工作结束
	 **/
    public static boolean insertComputeTask(ComputeTask computeTask) {
        try {
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 9);
            sendObject.put("task_name", computeTask.getTask_name());
            sendObject.put("initiator_id", computeTask.getInitiator_id());
            sendObject.put("group_id", computeTask.getGroup_id());
            sendObject.put("code", computeTask.getCode());
            sendObject.put("state", computeTask.getState());
            sendObject.put("security_score", computeTask.getSecurity_score());
            sendObject.put("data_type", computeTask.getData_type());
            sendObject.put("cost", computeTask.getCost());
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(9)) continue;
                json = responseBuffer.remove(9);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 9) return result == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	/**
     * Type-10
     * 发起一次任务，暂且就先返回所有slaver_id吧。
     */
    public static boolean runTask(String task_id) {
        try {
            ArrayList<DataNode> results = new ArrayList<>();
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 100);
            sendObject.put("task_id", task_id);
            sendObject.put("action", "run");
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(100)) continue;
                json = responseBuffer.remove(100);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 100) return result == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
	}
	
	/**
	 * Type-11
	 * slaver返回执行过程中的中间消息，服务器转发给master.
	 */
    public static void slaver_send_message(String user_id, String task_id, String text, String group_id) throws JSONException, IOException {
        JSONObject sendObject = new JSONObject();
		sendObject.put ("purpose", 11);
		sendObject.put ("user_id", user_id);
		sendObject.put ("task_id", task_id);
		sendObject.put ("text", text);
		sendObject.put ("group_id", group_id);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-12
	 * master发送终止命令
	 */
    public static void master_send_stop_command(String task_id, String user_id) throws JSONException, IOException {
        JSONObject sendObject = new JSONObject();
		sendObject.put ("purpose", 12);
		sendObject.put ("user_id", user_id);
		sendObject.put ("task_id", task_id);
		dataOutputStream.write (sendObject.toString ().getBytes ());
		dataOutputStream.flush ();
	}

    /**
     * Type-13
     * 通过状态和发起者id查询任务
     * state = 0 表示未启动
     * state = 1 表示正在工作
     * state = 2 表示工作结束
     * state = -1 表示获得所有状态的计算任务
     **/
    public static ArrayList<ComputeTask> queryComputeTaskByInitiatorIDAndState(String initiator_id, int state) {
        try {
            ArrayList<ComputeTask> results = new ArrayList<ComputeTask>();
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 13);
            sendObject.put("user_id", initiator_id);
            sendObject.put("state", state);
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(13)) continue;
                json = responseBuffer.remove(13);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 13) {
                    if (result == 1) {
                        JSONArray jsonArray = json.getJSONArray("tasks");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ComputeTask computeTask = new ComputeTask();
                            computeTask.setTask_id(jsonObject.getString("task_id"));
                            computeTask.setInitiator_id(jsonObject.getString("initiator_id"));
                            computeTask.setData_type(jsonObject.getString("data_type"));
                            computeTask.setCost(jsonObject.getDouble("cost"));
                            computeTask.setSecurity_score(jsonObject.getDouble("security_score"));
                            computeTask.setState(jsonObject.getInt("state"));
                            computeTask.setTask_name(jsonObject.getString("task_name"));
                            String group_id = jsonObject.getString("group_id");
                            if (!group_id.equals("null")) computeTask.setGroup_id(group_id);
//                            computeTask.setCode(jsonObject.getString("code"));
                            /**这里就不要开始和结束时间了，反正没用**/
                            results.add(computeTask);
                        }
                        return results;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	/**
	 * Type-14
	 * 通过user_id和group_id返回数据集名字
     **/
    public static ArrayList<String> queryDataSetNameByUserIdAndGroupID(String user_id, String group_id) {
        try {
            ArrayList<String> results = new ArrayList<String>();
            JSONObject sendObject = new JSONObject();
            sendObject.put("purpose", 14);
            sendObject.put("user_id", user_id);
            sendObject.put("group_id", group_id);
            bufferedWriter.write(sendObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(14)) continue;
                json = responseBuffer.remove(14);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 14) {
                    if (result == 1) {
                        JSONArray jsary = json.getJSONArray("data_names");
                        int data_num = jsary.length();
                        for (int i = 0; i < data_num; ++i) {
                            JSONObject jsobj = jsary.getJSONObject(i);
                            results.add(jsobj.getString("data_name"));
                        }
                        return results;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 插入新的数据集
     **/
    public static boolean insertDataNode(DataNode dataNode) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("purpose", 15);
            jsonObject.put("user_id", dataNode.getUser_id());
            jsonObject.put("data_name", dataNode.getData_name());
            jsonObject.put("data_type", dataNode.getData_type());
            jsonObject.put("row_num", dataNode.getRow_nums());
            jsonObject.put("attr_num", dataNode.getAttr_nums());
            jsonObject.put("file_path", dataNode.getFile_path());
            bufferedWriter.write(jsonObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(15)) continue;
                json = responseBuffer.remove(15);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 15) return result == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 修改task绑定的群组
     **/
    public static boolean alterTaskBindGroup(String task_id, String group_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("purpose", 16);
            jsonObject.put("task_id", task_id);
            jsonObject.put("group_id", group_id);
            bufferedWriter.write(jsonObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(16)) continue;
                json = responseBuffer.remove(16);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 16) return result == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取当前主机正在进行的任务，在works_on表里,type17
     * works_on表中状态为0的表示在运行，状态为1的表示结束了的
     **/
    public static ArrayList<TaskPaneItem> getWorkingSlaves(String user_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("purpose", 17);
            jsonObject.put("master_id", user_id);
            bufferedWriter.write(jsonObject.toString() + "\r\n");
            bufferedWriter.flush();
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(17)) continue;
                json = responseBuffer.remove(17);
                ArrayList<TaskPaneItem> results = new ArrayList<TaskPaneItem>();
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 17) {
                    if (result == 1) {
                        JSONArray jsonArray = json.getJSONArray("slaves");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject tmp = jsonArray.getJSONObject(i);
                            String task_name = tmp.getString("task_name");
                            String slave_name = tmp.getString("slave_name");
                            String slave_data_name = tmp.getString("slave_data_name");
                            TaskPaneItem taskPaneItem = new TaskPaneItem(slave_name, task_name, slave_data_name);
                            results.add(taskPaneItem);
                        }
                    }
                }
                return results;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前主机上面进行的任务，在works_on表里,type18
     * works_on表中状态为0的表示在运行，状态为1的表示结束了的
     **/
    public static ArrayList<TaskPaneItem> getWorkingMasters(String user_id) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("purpose", 18);
            jsonObject.put("slave_id", user_id);
            bufferedWriter.write(jsonObject.toString() + "\r\n");
            JSONObject json = null;
            while (true) {
                if (!responseBuffer.containsKey(18)) continue;
                json = responseBuffer.remove(18);
                ArrayList<TaskPaneItem> results = new ArrayList<TaskPaneItem>();
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 18) {
                    if (result == 1) {
                        JSONArray jsonArray = json.getJSONArray("masters");
                        int len = jsonArray.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject tmp = jsonArray.getJSONObject(i);
                            String task_name = tmp.getString("task_name");
                            String master_name = tmp.getString("master_name");
                            String data_name = tmp.getString("data_name");
                            TaskPaneItem taskPaneItem = new TaskPaneItem(master_name, task_name, data_name);
                            results.add(taskPaneItem);
                        }
                    }
                }
                return results;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新开进程后台监听任务分发
     **/
    public static void startBackgroundListner() {
        backgroundListner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!responseBuffer.containsKey(101)) continue;
                        JSONObject json = responseBuffer.remove(101);
                        int result = json.getInt("result");
                        int reply = json.getInt("reply");
                        if (reply == 101) {
                            if (result == 1) {
                                String task_id = json.getString("task_id");
                                String master_id = json.getString("master_id");
                                String group_id = json.getString("group_id");
                                String task_name = json.getString("task_name");
                                String master_name = json.getString("master_name");
                                String data_name = json.getString("data_name");
                                /**先不要代码了**/
                                ComputeTask computeTask = new ComputeTask();
                                computeTask.setGroup_id(group_id);
                                computeTask.setInitiator_id(master_id);
                                computeTask.setTask_id(task_id);

                                TaskPaneItem taskPaneItem = new TaskPaneItem();
                                taskPaneItem.setData_name(data_name);
                                taskPaneItem.setUser_name(master_name);
                                taskPaneItem.setTask_name(task_name);

                                System.out.println("Task: " + task_name + " start!");
                                /**若task界面存在，则更新task界面*，否则*/
                                if (StartProcess.hashMap.containsKey("tasks")) {
                                    /**不为空，表示当前在task界面里**/
                                    Pane pane = TaskController.newWorkingTask(taskPaneItem);
                                    TaskController.masters.add(pane);
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            ObservableList<Node> masterPanes = TaskController.master_masonry.getChildren();
                                            if (masterPanes.size() < 4) masterPanes.add(pane);
                                        }
                                    });
                                } else {
                                    Pane pane = TaskController.newWorkingTask(taskPaneItem);
                                    TaskController.masters.add(pane);
                                }


                                /**
                                 *
                                 * 执行分配的计算任务
                                 *
                                 * **/

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        backgroundListner.start();
    }

    /**
     * 新开线程监听slave完成任务的消息
     **/
    public static void startSlaveTaskEndListner() {
        endTaskLisner = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!responseBuffer.containsKey(201)) continue;
                    JSONObject json = responseBuffer.remove(201);
                    try {
                        int result = json.getInt("result");
                        int reply = json.getInt("reply");
                        if (reply == 201) {
                            if (result == 1) {

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        endTaskLisner.start();
    }

    /**
     * 终止成功就返回true，否则返回false
     **/
    public static boolean sendToMasterTaskEndMsg(String task_id, String master_id, String slave_id, String slave_data_name) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("purpose", 200);
            jsonObject.put("task_id", task_id);
            jsonObject.put("master_id", master_id);
            jsonObject.put("slave_id", slave_id);
            jsonObject.put("slave_data_name", slave_data_name);
            bufferedWriter.write(jsonObject.toString() + "\r\n");
            bufferedWriter.flush();
            while (true) {
                if (!responseBuffer.containsKey(200)) continue;
                JSONObject json = responseBuffer.remove(200);
                int result = json.getInt("result");
                int reply = json.getInt("reply");
                if (reply == 200) return result == 1;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void startReadResponse() {
        readReply = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String rcvJSonStr = bufferedReader.readLine();

                        System.out.println(rcvJSonStr);

                        if (rcvJSonStr != null) {
                            JSONObject json = new JSONObject(rcvJSonStr);
                            int reply = json.getInt("reply");
                            responseBuffer.put(reply, json);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        readReply.start();
    }

    public static void initSocket(String ip, int port) throws IOException {
        serverIP = ip;
        serverPort = port;
        socket = new Socket(serverIP, serverPort);
        responseBuffer = new HashMap<Integer, JSONObject>();
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
        outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
        inputStreamReader = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
        bufferedWriter = new BufferedWriter(outputStreamWriter);

        startReadResponse();
	}
}
