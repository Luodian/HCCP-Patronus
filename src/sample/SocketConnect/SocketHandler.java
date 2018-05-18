package sample.SocketConnect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sample.Entity.ComputeTask;
import sample.Entity.DataNode;
import sample.Entity.GroupNode;
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
	
	/**
	 * Type-0:注册-检验完成
	 */
	public static void insertUser (UserNode userNode) throws JSONException, IOException
	{
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 0);
		sendobj.put ("email", userNode.getEmail ());
		sendobj.put ("password", userNode.getPassword ());
		sendobj.put ("user_name", userNode.getUser_name ());
		
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-1:登录-检验完成
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
					if (json.get ("result").equals (1) && json.getString ("reply").equals (1))
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
	
	/**
	 * Type-2
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
	 * Type-3:新添群组
	 **/
	public static void insertGroup (GroupNode groupNode) throws JSONException, IOException
	{
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 3);
		sendobj.put ("group_name", groupNode.getGroup_name ());
		sendobj.put ("data_type", groupNode.getType ());
		sendobj.put ("creator_id", groupNode.getOwner_id ());
		sendobj.put ("description", groupNode.getDescription ());
		sendobj.put ("member_num", groupNode.getMember_num ());
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-4:新添群组
	 **/
	public static void insertGroupUserRelation (String user_id, String group_id) throws JSONException, IOException
	{
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 4);
		sendobj.put ("user_id", user_id);
		sendobj.put ("group_id", group_id);
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-5:查询用户所有分组
	 **/
	
	public static ArrayList<GroupNode> queryGroupsByUserID (String user_id) throws JSONException, IOException
	{
		ArrayList<GroupNode> results = new ArrayList<GroupNode> ();
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 5);
		sendobj.put ("user_id", user_id);
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 5))
					{
						JSONArray jsarray = json.getJSONArray ("datas");
						int len = jsarray.length ();
						for (int i = 0; i < len; ++i)
						{
							JSONObject jsobj = jsarray.getJSONObject (i);
							GroupNode temp = new GroupNode ();
							temp.setGroup_name (json.getString ("group_name"));
							temp.setType (json.getString ("data_type"));
							temp.setGroup_id (json.getString ("group_id"));
							temp.setOwner_id (json.getString ("creator_id"));
							temp.setMember_num (json.getInt ("member_nums"));
							temp.setDescription (json.getString ("description"));
//                if (owner!=null) temp.setOwner(owner);
//                else throw new Throwable("ONT FIND OWNER!");
							results.add (temp);
						}
						return results;
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
	 * Type-6:查询用户注册的数据集
	 **/
	public static ArrayList<DataNode> queryDataNodesByID (String user_id) throws JSONException, IOException
	{
		ArrayList<DataNode> results = new ArrayList<DataNode> ();
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 6);
		sendobj.put ("user_id", user_id);
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 6))
					{
						JSONArray jsarray = json.getJSONArray ("datas");
						int len = jsarray.length ();
						for (int i = 0; i < len; ++i)
						{
							JSONObject jsobj = jsarray.getJSONObject (i);
							String user_id_temp = jsobj.getString ("user_id");
							String data_name = jsobj.getString ("data_name");
							String data_type = jsobj.getString ("data_type");
							int row_nums = jsobj.getInt ("row_nums");
							int attr_nums = jsobj.getInt ("attr_nums");
							String file_path = jsobj.getString ("file_path");
							DataNode temp = new DataNode (user_id_temp, data_name, data_type, row_nums, attr_nums, file_path);
							results.add (temp);
						}
						return results;
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
	 * Type-7:新添数据集
	 */
	public static void insertDataNode (DataNode dataNode, String group_id) throws JSONException, IOException
	{
		JSONObject sendobj = new JSONObject ();
		sendobj.put ("purpose", 7);
		sendobj.put ("group_id", group_id);
		sendobj.put ("data_type", dataNode.getData_type ());
		sendobj.put ("user_id", dataNode.getUser_id ());
		sendobj.put ("data_name", dataNode.getData_name ());
		sendobj.put ("row_nums", dataNode.getRow_nums ());
		sendobj.put ("attr_nums", dataNode.getAttr_nums ());
		sendobj.put ("file_path", dataNode.getFile_path ());
		System.out.println (sendobj);
		dataOutputStream.write (sendobj.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-8
	 * 根据group_id,获取所有的在群组中注册了的数据
	 * 若查询失败，则返回null
	 * 否则至少返回一个长度为0的数组
	 **/
	public static ArrayList<DataNode> queryRegisterdDataNodesByID (String group_id) throws JSONException, IOException
	{
		ArrayList<DataNode> result = new ArrayList<> ();
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 8);
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 8))
					{
						JSONArray jsarray = json.getJSONArray ("datas");
						int len = jsarray.length ();
						for (int i = 0; i < len; ++i)
						{
							JSONObject jsobj = jsarray.getJSONObject (i);
							DataNode dtn = new DataNode (jsobj.getString ("data_name"), jsobj.getInt ("row_nums"), jsobj.getInt ("attr_nums"), jsobj.getString (("user_name")));
							result.add (dtn);
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
	 * Type-9
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
		try
		{
			ByteArrayOutputStream baos = null;
			byte[] by = new byte[2048];
			int n;
			while ((n = dataInputStream.read (by)) != -1)
			{
				baos = new ByteArrayOutputStream ();
				baos.write (by, 0, n);
				String rcvJson = new String (baos.toByteArray ());
				try
				{
					JSONObject json = new JSONObject (rcvJson);
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 9))
					{
					
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
		return false;
	}
	
	/**
	 * Type-10
	 */
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 10))
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
	
	/**
	 * Type-11
	 * slaver返回执行过程中的中间消息，服务器转发给master.
	 */
	public static void slaver_send_message (String user_id, String task_id, String text, String group_id) throws JSONException, IOException
	{
		JSONObject sendObject = new JSONObject ();
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
	
	public static void master_send_stop_command (String task_id, String user_id) throws JSONException, IOException
	{
		JSONObject sendObject = new JSONObject ();
		sendObject.put ("purpose", 12);
		sendObject.put ("user_id", user_id);
		sendObject.put ("task_id", task_id);
		System.out.println (sendObject);
		dataOutputStream.write (sendObject.toString ().getBytes ());
//                    dataOutputStream.write(getJsonByte(Integer.parseInt(info)));
		dataOutputStream.flush ();
	}
	
	/**
	 * Type-13
	 * 根据状态和发起者id查询任务.
	 **/
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 14))
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
	 * Type-14
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
					if (json.get ("result").equals (1) && (json.getInt ("reply") == 14))
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
