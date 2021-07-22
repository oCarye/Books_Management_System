package Controller;

import Dao.DBConnection;

import java.sql.*;
import java.util.*;

public class SuperUser extends BookManager
{
	SuperUser(DBConnection dbc, DBError wrong, String username, String password, ResultSet result)
	{  super(dbc,wrong,username,password,result);  }

	public ResultSet searchUser(Map<String,String> conditions)
	{
		List<String> tables = new ArrayList<String>(); tables.add("Users");
		List<String> columns = new ArrayList<String>();
		columns.add("username");  columns.add("gender");  columns.add("position");  columns.add("credit");
		columns.add("password");
		return dbc.select(columns,tables,conditions,wrong);
	}

	public void addUser(Map<String,String> attributes)
	{
		if (!attributes.get("position").equals("'Reader'")
				&& !attributes.get("position").equals("'LogManagement'")
				&& !attributes.get("position").equals("'BookManagement'")
				&& !attributes.get("position").equals("'SuperUser'"))
		{
			wrong.addMessage("没有该类型。");
			wrong.printMessage();
			return;
		}
		dbc.insert("Users",attributes,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("添加成功。");
	}

	public void changeUser(String username, Map<String,String> attributes)
	{
		if (!attributes.get("position").equals("Reader")
				&& !attributes.get("position").equals("'LogManagement'")
				&& !attributes.get("position").equals("'BookManagement'")
				&& !attributes.get("position").equals("'SuperUser'"))
		{
			wrong.addMessage("没有该类型。");
			wrong.printMessage();
			return;
		}
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("username", "'"+username+"'");
		dbc.update("Users",attributes,conditions,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("修改成功。");
	}

	public void deleteUser(String username)
	{
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("username", "'"+username+"'");
		dbc.delete("Users",conditions,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("删除成功。");
	}
}