package Dao;

import Controller.DBError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DBConnection
{
	private static DBConnection dbc = null;
	private String username,password;  //指实际连接到数据库的用户
	private Connection conn = null;
	private Statement stat = null;
	private boolean iswrong = false;  //记录某步骤是否出错
	private String wrongmessage = "Not even wrong";  //记录错误信息
	private DBConnection(String username, String password)
	{
		/* 连接数据库 */
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException throwables)
		{
			//e.printStackTrace();
			//System.out.println("与数据库的连接发生错误。");
			iswrong = true;
			wrongmessage = throwables.getMessage();
			return;
		}
		this.username = username; this.password = password;
		try
		{
			conn = DriverManager.getConnection
					("jdbc:sqlserver://127.0.0.1:1433;DatabaseName=school",username,password);
			stat = conn.createStatement();
		} catch (SQLException throwables)
		{
			//e.printStackTrace();
			//System.out.println("用户名或密码错误。");
			iswrong = true;
			wrongmessage = throwables.getMessage();
			return;
		}

		/* 初始化表格 */
		Path path = Paths.get("config/CreateTable.txt");
		String data = null;
		try
		{
			data = Files.readString(path);
		} catch (IOException throwables)
		{
			wrongmessage = throwables.getMessage();
		}
		try
		{
			stat.executeUpdate(data);
		} catch (SQLException throwables)
		{
			wrongmessage = throwables.getMessage();
		}
	}

	public static synchronized DBConnection getDbc(String username, String password)
	{
		if (dbc == null)
			dbc = new DBConnection(username, password);
		return dbc;
	}

	public String getUsername()
	{
		return username;
	}

	public boolean isWrong()
	{
		if (iswrong == true)
		{
			iswrong = false;
			return true;
		}
		else return false;
	}

	public String getMessage()
	{
		return wrongmessage;
	}

	@Override
	public String toString()
	{
		return wrongmessage;
	}

	public ResultSet select(List<String> columns, List<String> tables, Map<String,String> conditions, DBError wrong)  //conditions储存where对应的内容
	{
		StringBuilder sentence = new StringBuilder("use books_management ");
		ResultSet result = null;
		sentence.append("select ");
		if (columns.size() == 0 || columns == null) sentence.append("* ");
		else
		{
			Iterator iter = columns.iterator();
			sentence.append(iter.next());
			while (iter.hasNext())
				sentence.append(","+iter.next());
			sentence.append(" ");
		}

		{
			sentence.append("from ");
			Iterator iter = tables.iterator();
			sentence.append(iter.next());
			while (iter.hasNext())
				sentence.append(","+iter.next());
			sentence.append(" ");
		}

		if (conditions.size() != 0)
		{
			sentence.append("where ");
			Iterator<Map.Entry<String,String>> iter = conditions.entrySet().iterator();  //map.entrySet() 是键值对的集合
			Map.Entry<String,String> entry = iter.next();
			sentence.append(entry.getKey()+"="+entry.getValue()+" ");
			while (iter.hasNext())
			{
				entry = iter.next();
				sentence.append("and "+entry.getKey()+"="+entry.getValue()+" ");
			}
		}

		try
		{
			result = stat.executeQuery(String.valueOf(sentence));
		} catch (SQLException throwables)
		{
			//iswrong = true;
			//wrongmessage = throwables.getMessage();
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
		return result;
	}

	public int insert(String table, Map<String,String> attributes, DBError wrong)
	{
		StringBuilder sentence = new StringBuilder("use books_management ");
		int result = -1;
		sentence.append("insert into "+table);
		StringBuilder keys = new StringBuilder("(");
		StringBuilder values = new StringBuilder("(");
		Iterator<Map.Entry<String,String>> iter = attributes.entrySet().iterator();  //map.entrySet() 是键值对的集合
		Map.Entry<String,String> entry = iter.next();
		keys.append(entry.getKey()+" "); values.append(entry.getValue()+" ");
		while (iter.hasNext())
		{
			entry = iter.next();
			keys.append(","+entry.getKey()+" "); values.append(","+entry.getValue()+" ");
		}
		keys.append(")"); values.append(")");
		sentence.append(" "+keys); sentence.append(" values "+values);
		try
		{
			result = stat.executeUpdate(String.valueOf(sentence));
		} catch (SQLException throwables)
		{
			//iswrong = true;
			//wrongmessage = throwables.getMessage();
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
		return result;
	}

	public int update(String table, Map<String,String> attributes, Map<String, String> conditions, DBError wrong)
	{
		StringBuilder sentence = new StringBuilder("use books_management ");
		int result = -1;
		sentence.append("update "+table);
		Iterator<Map.Entry<String,String>> iter = attributes.entrySet().iterator();  //map.entrySet() 是键值对的集合
		Map.Entry<String,String> entry = iter.next();
		sentence.append(" set "+entry.getKey()+" = "+entry.getValue());
		while (iter.hasNext())
		{
			entry = iter.next();
			sentence.append(", " + entry.getKey() + " = " + entry.getValue());
		}

		iter = conditions.entrySet().iterator();
		entry = iter.next();
		sentence.append(" where "+ entry.getKey() + " = " + entry.getValue());
		while (iter.hasNext())
		{
			entry = iter.next();
			sentence.append(", " + entry.getKey() + " = " + entry.getValue());
		}

		try
		{
			result = stat.executeUpdate(String.valueOf(sentence));
		} catch (SQLException throwables)
		{  wrong.printMessage(throwables.getMessage());  }
		return result;
	}

	public int delete(String table, Map<String, String> conditions, DBError wrong)
	{
		StringBuilder sentence = new StringBuilder("use books_management ");
		int result = -1;
		sentence.append("delete from "+table);
		Iterator<Map.Entry<String,String>> iter = conditions.entrySet().iterator();  //map.entrySet() 是键值对的集合
		Map.Entry<String,String> entry = iter.next();
		sentence.append(" where "+ entry.getKey() + " = " + entry.getValue());
		while (iter.hasNext())
		{
			entry = iter.next();
			sentence.append(", " + entry.getKey() + " = " + entry.getValue());
		}

		try
		{
			result = stat.executeUpdate(String.valueOf(sentence));
		} catch (SQLException throwables)
		{
			//iswrong = true;
			//wrongmessage = throwables.getMessage();
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
		return result;
	}
}
