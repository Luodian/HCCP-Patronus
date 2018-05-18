//package sample.SocketConnect;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.*;
//import java.net.Socket;
//
//public class ClientRun extends Thread {
//	private int serverPort;
//	private String serverIP;
//	private Socket socket;
//	private DataOutputStream dataOutputStream;
//	private DataInputStream dataInputStream;
//
////	public ClientRun(String IP, int port){
//		this.serverIP = IP;
//		this.serverPort = port;
////		initSocket();
////	}
//
////	void initSocket(){
////		try {
////			socket = new Socket(serverIP, serverPort);
////			dataOutputStream = new DataOutputStream(socket.getOutputStream());
////			dataInputStream = new DataInputStream(socket.getInputStream());
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//
//	@Override
//	public void run(){
//		try {
//			//启动接收数据线程
//			new Thread(){
//				public void run(){
//					rcvThread();
//				}
//			}.start();
//
//			//发送消息
//			while(true){
//				String info = null;
//				if((info=br.readLine())!=null){
//					if(info.equals("over"))
//						break;
//					JSONObject sendObject = new JSONObject();
//					sendObject.put("purpose",6);
//					sendObject.put("user_id","358118125");
//					System.out.println(sendObject);
//					dataOutputStream.write(sendObject.toString().getBytes());
////                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
//					dataOutputStream.flush();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	////接收服务器发送消息的线程
//	void rcvThread(){
//		try{
//			ByteArrayOutputStream baos = null;
//			byte[] by = new byte[2048];
//			int n;
//			while ((n = dataInputStream.read(by)) != -1) {
//				baos = new ByteArrayOutputStream();
//				baos.write(by, 0, n);
//				String rcvJsonStr = new String(baos.toByteArray());
//				try{
//					JSONObject json = new JSONObject(rcvJsonStr);
//					if(json.get("result").equals(1)){
//						JSONArray jsonArray = json.getJSONArray("datas");
//						int data_num = jsonArray.length();
//						String[] filePaths = new String[data_num];
//						String[] dataNames = new String[data_num];
//						String[] userIds = new String[data_num];
//						String[] dataTypes = new String[data_num];
//						int[] rowNums = new int[data_num];
//						int[] attrNums = new int[data_num];
//						for(int i=0;i<data_num;i++){
//							JSONObject jsonTemp = jsonArray.getJSONObject(i);
//							filePaths[i] = jsonTemp.getString("file_path");
//							dataNames[i] = jsonTemp.getString("data_name");
//							userIds[i] = jsonTemp.getString("user_id");
//							dataTypes[i] = jsonTemp.getString("data_type");
//							rowNums[i] = jsonTemp.getInt("row_nums");
//							attrNums[i] = jsonTemp.getInt("attr_nums");
//						}
//						for(int i = 0;i<data_num;i++){
//							System.out.println(dataNames[i]);
//						}
//					}
//				}catch (JSONException e){
//					e.printStackTrace();
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}