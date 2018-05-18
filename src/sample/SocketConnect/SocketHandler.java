package sample.SocketConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Entity.ComputeTask;
import sample.Entity.UserNode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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
	
	/*
		检验完成
	 */
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
	
	public static ArrayList<ComputeTask> queryComputeTaskByInitiatorIDAndState (String initiator_id, int state) throws JSONException, IOException
	{
		ArrayList<ComputeTask> result = new ArrayList<ComputeTask> ();
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 14);
		sendObject.put ("user_id", initiator_id);
		sendObject.put ("state", state);
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
					if (json.get ("result").equals (1) && (json.getInt ("type") == 14))
					{
						ComputeTask computeTask = new ComputeTask ();
						computeTask.setTask_id (json.getString ("task_id"));
						computeTask.setInitiator_id (json.getString ("initiator_id"));
						computeTask.setData_type (json.getString ("data_type"));
						computeTask.setCost (json.getDouble ("cost"));
						computeTask.setSecurity_score (json.getDouble ("security_score"));
						computeTask.setStart_time (json.getString ("start_time"));
						computeTask.setEnd_time (json.getString ("end_time"));
						computeTask.setState (json.getInt ("state"));
						computeTask.setTask_name (json.getString ("task_name"));
						result.add (computeTask);
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
	
	/**
	 * 通过user_id和group_id返回数据集名字
	 **/
	public static ArrayList<String> queryDataSetNameByUserIdAndGroupID (String user_id, String group_id) throws JSONException, IOException
	{
		ArrayList<String> result = new ArrayList<String> ();
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 14);
		sendObject.put ("user_id", user_id);
		sendObject.put ("group_id", group_id);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
		
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
					if (json.get ("result").equals (1) && (json.getInt ("type") == 14))
					{
						JSONArray jsary = json.getJSONArray ("datas");
						int data_num = jsary.length ();
						for (int i = 0; i < data_num; ++i)
						{
							JSONObject jsobj = jsary.getJSONObject (i);
							result.add (jsobj.getString ("data_name"));
						}
						return result;
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
	
	/**
	 * 新添计算任务元组
	 * state = 0 表示未启动
	 * state = 1 表示正在工作
	 * state = 2 表示工作结束
	 **/
	public static boolean insertComputeTask (ComputeTask computeTask) throws JSONException, IOException
	{
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 9);
		sendObject.put ("task_name", computeTask.getTask_name ());
		sendObject.put ("initiator_id", computeTask.getInitiator_id ());
		sendObject.put ("group_id", computeTask.getGroup_id ());
		sendObject.put ("code", computeTask.getCode ());
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
		return true;
	}
	
	public static ArrayList<String> send_task (String task_id) throws JSONException, IOException
	{
		ArrayList<String> result = new ArrayList<> ();
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 10);
		sendObject.put ("task_id", task_id);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
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
					if (json.get ("result").equals (1) && (json.getInt ("type") == 9))
					{
						JSONArray jsarray = json.getJSONArray ("");
						int len = jsarray.length ();
						for (int i = 0; i < len; ++i)
						{
							JSONObject jsobj = jsarray.getJSONObject (i);
							String str = jsobj.getString ("slaver_id");
							result.add (str);
						}
						return result;
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
