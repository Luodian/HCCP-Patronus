package sample.Server;

import org.json.JSONException;
import org.json.JSONObject;
import sample.Datebase.SQLHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ServerRun extends Thread {

    private static int serverPort;
    private static ServerSocket serverSocket;

    private static Map<String, Socket> userMap = new HashMap<>();//user_id,socket

    public ServerRun(int port) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        this.serverPort = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SQLHandler.init();
    }

    public static Map<String, Socket> getUserMap() {
        return userMap;
    }

    @Override
    public void run() {
        while (true) {
            Socket s = null;
            String ip = null;
            try {
                s = serverSocket.accept();
                ip = s.getInetAddress().getHostAddress();

                //每个用户都会处理请求，并发送数据
                ServerManiputify serverManiputify = new ServerManiputify(s);
                serverManiputify.start();
            } catch (IOException e) {
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }
}

class ServerManiputify extends Thread {

    private Map<String, Socket> userMap = ServerRun.getUserMap();
    private Socket clientSocket;
    //    private DataOutputStream dataOutputStream;
    private String ip;

    public ServerManiputify(Socket socket) {
        clientSocket = socket;
    }

    //服务器
    @Override
    public void run() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ip = clientSocket.getInetAddress().getHostAddress();
            String rcvJsonStr;
            while ((rcvJsonStr = bufferedReader.readLine()) != null) {
                try {
                    JSONObject json = new JSONObject(rcvJsonStr);

                    System.out.println(rcvJsonStr);

                    int purpose = json.getInt("purpose");
                    JSONObject rtnMsg = null;
                    switch (purpose) {
                        case 0:
                            rtnMsg = ServerAction.doRegister(json);
                            break;
                        case 1:
                            rtnMsg = ServerAction.doLogin(json);
                            /**若登录成功，则记录user_id和socket的键值对**/
                            if (rtnMsg.get("result").equals(1)) userMap.put(rtnMsg.getString("user_id"), clientSocket);

                            break;
                        case 2:
                            rtnMsg = ServerAction.doQueryUser(json);
                            break;
                        case 3:
                            rtnMsg = ServerAction.doCreateGroup(json);
                            break;
                        case 4:
                            rtnMsg = ServerAction.doJoinGroup(json);
                            break;
                        case 5:
                            rtnMsg = ServerAction.doQueryGroupByUserId(json);
                            break;
                        case 6:
                            rtnMsg = ServerAction.doQueryDataNodesByUserId(json);
                            break;
                        case 7:
                            rtnMsg = ServerAction.doInsertGroupDataRegisterRelation(json);
                            break;
                        case 8:
                            rtnMsg = ServerAction.doQueryDataNodesByGroupId(json);
                            break;
                        case 9:
                            rtnMsg = ServerAction.doTask(json);
                            break;
                        case 11:
                            rtnMsg = ServerAction.doProcessMsg(json, userMap);
                            break;
                        case 12:
                            rtnMsg = ServerAction.doStop(json, userMap);
                            break;
                        case 13:
                            rtnMsg = ServerAction.doQueryTaskByStateAndUserId(json);
                            break;
                        case 14:
                            rtnMsg = ServerAction.doQueryDataNameByUserIdAndGroupId(json);
                            break;
                        case 15:
                            rtnMsg = ServerAction.doInsertDataNode(json);
                            break;
                        case 16:
                            rtnMsg = ServerAction.doAlterTaskBindGroup(json);
                            break;
                        case 17:
                            rtnMsg = ServerAction.doQueryWorkingSlaves(json);
                            break;
                        case 18:
                            rtnMsg = ServerAction.doQueryWorkingMasters(json);
                            break;
                        case 19:
                            rtnMsg = ServerAction.doInsertWhiteRulesItem(json);
                            break;
                        case 20:
                            rtnMsg = ServerAction.doInsertBlackRulesItem(json);
                            break;
                        case 100:
                            rtnMsg = ServerAction.doPowerSlaver(json, userMap);
                            break;
                        case 200:
                            rtnMsg = ServerAction.doTaskEnd(json);
                            break;
                        default://请求不合法
                            rtnMsg = new JSONObject().put("result", 0);
                    }
                    /**若无键值对，就不回传了**/
                    if (rtnMsg.keys().hasNext()) {
                        bufferedWriter.write(rtnMsg.toString() + "\r\n");
                        bufferedWriter.flush();
                    }
                } catch (JSONException e) {
                    /**有异常时，返回的也是result：0**/
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("result", 0);
                    hm.put("reply", -1);
                    JSONObject rtnJson = new JSONObject(hm);
                    bufferedWriter.write(rtnJson.toString() + "\r\n");
                    bufferedWriter.flush();
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}