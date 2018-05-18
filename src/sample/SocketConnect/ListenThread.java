//package sample.SocketConnect;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.IOException;
//
//public class ListenThread extends Thread
//{
//	private DataInputStream dataInputStream;
//	void rcvThread()
//	{
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
