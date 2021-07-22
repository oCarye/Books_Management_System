package Controller;

import Dao.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User
{
	protected static DBConnection dbc = DBConnection.getDbc("sa","123456");
	protected int id;
	protected DBError wrong;
	protected String username, password, gender, position, credit;
	protected User(DBConnection dbc, DBError wrong, String username, String password, ResultSet result)
	{
		try
		{
			this.dbc = dbc;
			this.wrong = wrong;
			this.username = username;
			this.password = password;
			id = Integer.parseInt(result.getString("id"));
			gender = result.getString("gender");
			position = result.getString("position");
			credit = result.getString("credit");
		}catch(SQLException throwables)
		{
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
	}

	public static User getUser(DBError wrong, String username, String password)
	{
		try
		{
			Map<String, String> conditions = new HashMap<String, String>();
			conditions.put("username", "'" + username + "'");
			conditions.put("password", "'" + password + "'");
			ArrayList<String> tables = new ArrayList<String>();
			tables.add("Users");
			ResultSet result = dbc.select(new ArrayList(), tables, conditions, wrong);
			if (!result.next())
			{
				wrong.printMessage("用户名或密码错误。");
				return null;
			}

			String authority = result.getString("position").trim();
			if (authority.equals("Reader"))
				return new Reader(dbc,wrong,username,password,result);
			if (authority.equals("SuperUser"))
				return new SuperUser(dbc,wrong,username,password,result);
			if (authority.equals("LogManager"))
				return new LogManager(dbc,wrong,username,password,result);
			if (authority.equals("BookManager"))
				return new BookManager(dbc,wrong,username,password,result);
		}catch(SQLException throwables)
		{
			wrong.printMessage(throwables.getMessage());
		}
		return null;
	}

	public String getAuthority()
	{
		return position;
	}
	public DBError getWrong()  { return wrong; }
	public ResultSet getRecord() { wrong.printMessage("权限不足。");  return null; }
	public ResultSet searchBooks(Map<String,String> conditions)  { wrong.printMessage("权限不足。");  return null; }
	public void borrowBooks(int book_id, int number, String outdate) { wrong.printMessage("权限不足。"); }
	public void returnBooks(int book_id, String indate) { wrong.printMessage("权限不足。"); }
	public ResultSet searchUser(String username)  { wrong.printMessage("权限不足。");  return null; }
	public ResultSet searchUser(int log_id)  { wrong.printMessage("权限不足。");  return null; }
	public ResultSet searchUser(Map<String,String> conditions)  { wrong.printMessage("权限不足。");  return null; }
	public ResultSet searchLogs(Map<String,String> conditions)  { wrong.printMessage("权限不足。");  return null; }
	public void changeBook(int book_id, Map<String,String> attributes)  { wrong.printMessage("权限不足。"); }
	public void addBook(Map<String,String> attributes)  { wrong.printMessage("权限不足。"); }
	public void deleteBook(int book_id)  { wrong.printMessage("权限不足。"); }
	public void addUser(Map<String,String> attributes)  { wrong.printMessage("权限不足。"); }
	public void changeUser(String username, Map<String,String> attributes)  { wrong.printMessage("权限不足。"); }
	public void deleteUser(String username)  { wrong.printMessage("权限不足。"); }
}
