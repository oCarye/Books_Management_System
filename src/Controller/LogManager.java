package Controller;

import Dao.DBConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogManager extends Reader
{
	LogManager(DBConnection dbc, DBError wrong, String username, String password, ResultSet result)
	{  super(dbc,wrong,username,password,result);  }

	public ResultSet searchUser(String username)
	{
		List<String> tables = new ArrayList<String>(); tables.add("Users");
		List<String> columns = new ArrayList<String>();
		columns.add("username");  columns.add("gender");  columns.add("position");  columns.add("credit");
		Map<String,String> conditions = new HashMap<String,String>();
		if (username.equals("") == false)
			conditions.put("username","'"+username+"'");
		return dbc.select(columns,tables,conditions,wrong);
	}

	public ResultSet searchUser(int log_id)
	{
		List<String> columns = new ArrayList<String>();
		columns.add("username");  columns.add("gender");  columns.add("position");  columns.add("credit");
		List<String> tables = new ArrayList<String>();
		tables.add("Users");  tables.add("Logs");
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("Logs.id", String.valueOf(log_id));
		conditions.put("Logs.user_name","Users.username");
		return dbc.select(columns,tables,conditions,wrong);
	}

	public ResultSet searchUser(Map<String,String> conditions)
	{
		List<String> tables = new ArrayList<String>(); tables.add("Users");
		List<String> columns = new ArrayList<String>();
		columns.add("username");  columns.add("gender");  columns.add("position");  columns.add("credit");
		//columns.add("password");
		return dbc.select(columns,tables,conditions,wrong);
	}

	public ResultSet searchLogs(Map<String,String> conditions)
	{
		List<String> tables = new ArrayList<String>(); tables.add("Logs");
		return dbc.select(new ArrayList(),tables,conditions,wrong);
	}
}
