package sample.SocketConnect;

import org.json.JSONException;
import org.json.JSONObject;
import sample.Entity.UserNode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketHandler extends Thread
{
	private static DataOutputStream dataOutputStream;
	private static DataInputStream dataInputStream;
	private int serverPort;
	private String serverIP = null;
	private Socket socket;
	
	public SocketHandler (String IP, int port)
	{
		this.serverIP = IP;
		this.serverPort = port;
		initSocket ();
	}
	
	public static String sign_in (String email, String password) throws JSONException, IOException
	{
		String ret_user_id = "";
		// 发送请求
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 1);
		sendObject.put ("email", email);
		sendObject.put ("password", password);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
		
		// 监听请求
		try
		{
			ByteArrayOutputStream baos = null;
			byte[] by = new byte[2048];
			int n;
			while ((n = dataInputStream.read (by)) != -1)
			{
				baos = new ByteArrayOutputStream ();
				baos.write (by, 0, n);
				String rcvJsonStr = new String (baos.toByteArray ());
				try
				{
					JSONObject json = new JSONObject (rcvJsonStr);
					if (json.get ("result").equals (1))
					{
						ret_user_id = (String) json.get ("user_id");
						return ret_user_id;
					}
				} catch (JSONException e)
				{
					e.printStackTrace ();
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
		return "NOTFIND";
	}

//	void rcvThread()
//	{
//		try
//		{
//			ByteArrayOutputStream baos = null;
//			byte[] by = new byte[2048];
//			int n;
//			while ((n = dataInputStream.read (by)) != -1)
//			{
//				baos = new ByteArrayOutputStream ();
//				baos.write (by, 0, n);
//				String rcvJsonStr = new String (baos.toByteArray ());
//				try
//				{
//					JSONObject json = new JSONObject (rcvJsonStr);
//					if (json.get ("result").equals (1))
//					{
//						JSONArray jsonArray = json.getJSONArray ("datas");
//						int data_num = jsonArray.length ();
//						String[] filePaths = new String[data_num];
//						String[] dataNames = new String[data_num];
//						String[] userIds = new String[data_num];
//						String[] dataTypes = new String[data_num];
//						int[] rowNums = new int[data_num];
//						int[] attrNums = new int[data_num];
//						for (int i = 0; i < data_num; i++)
//						{
//							JSONObject jsonTemp = jsonArray.getJSONObject (i);
//							filePaths[i] = jsonTemp.getString ("file_path");
//							dataNames[i] = jsonTemp.getString ("data_name");
//							userIds[i] = jsonTemp.getString ("user_id");
//							dataTypes[i] = jsonTemp.getString ("data_type");
//							rowNums[i] = jsonTemp.getInt ("row_nums");
//							attrNums[i] = jsonTemp.getInt ("attr_nums");
//						}
//					}
//				} catch (JSONException e)
//				{
//					e.printStackTrace ();
//				}
//			}
//		} catch (IOException e)
//		{
//			e.printStackTrace ();
//		}
//	}
	
	/**
	 * 根据用户id查询用户，若找到返回UserNode，否则返回null
	 **/
	public static UserNode queryUserByID (String user_id) throws JSONException, IOException
	{
		
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 2);
		sendObject.put ("user_id", user_id);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
		
		// 监听请求
		try
		{
			ByteArrayOutputStream baos = null;
			byte[] by = new byte[2048];
			int n;
			while ((n = dataInputStream.read (by)) != -1)
			{
				baos = new ByteArrayOutputStream ();
				baos.write (by, 0, n);
				String rcvJsonStr = new String (baos.toByteArray ());
				try
				{
					JSONObject json = new JSONObject (rcvJsonStr);
					if (json.get ("result").equals (1))
					{
						UserNode userNode = new UserNode ();
						userNode.setUser_id (json.getString ("user_id"));
						userNode.setUser_name (json.getString ("user_name"));
						userNode.setEmail (json.getString ("email"));
						return userNode;
					}
				} catch (JSONException e)
				{
					e.printStackTrace ();
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
		return null;
	}
	
	public static void send_task ()
	{
	
	}
	
	void initSocket ()
	{
		try
		{
			socket = new Socket (serverIP, serverPort);
			dataOutputStream = new DataOutputStream (socket.getOutputStream ());
			dataInputStream = new DataInputStream (socket.getInputStream ());
		} catch (IOException e)
		{
			e.printStackTrace ();
		}
	}
}
