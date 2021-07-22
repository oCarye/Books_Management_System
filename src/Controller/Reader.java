package Controller;

import Dao.DBConnection;

import java.sql.*;
import java.util.*;

public class Reader extends User
{
	Reader(DBConnection dbc, DBError wrong, String username, String password, ResultSet result)
	{  super(dbc,wrong,username,password,result);  }

	public ResultSet getRecord()
	{
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("user_name","'"+username+"'");
		List<String> tables = new ArrayList<String>(); tables.add("Logs");
		ResultSet result = dbc.select(new ArrayList(),tables,conditions,wrong);
		return result;
	}

	public ResultSet searchBooks(Map<String,String> conditions)
	{
		List<String> tables = new ArrayList<String>(); tables.add("Books");
		return dbc.select(new ArrayList(),tables,conditions,wrong);
	}

	public void borrowBooks(int book_id, int number, String outdate)
	{
		try
		{
			/* 获得书的数量 */
			int book_num = -1;
			List<String> columns = new ArrayList<String>();
			columns.add("number");
			List<String> tables = new ArrayList<String>();
			tables.add("Books");
			Map<String, String> conditions = new HashMap<String, String>();
			conditions.put("id", String.valueOf(book_id));
			ResultSet result = dbc.select(columns, tables, conditions, wrong);
			if (result.next() == false)
			{
				wrong.printMessage("找不到该书籍。");
				return;
			}
			book_num = Integer.parseInt(result.getString("number"));
			if (book_num < number)
			{
				wrong.printMessage("借书数量超过已有数量。");
				return;
			}

			/* 插入借书记录 */
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("user_name", "'" + username + "'");
			attributes.put("book_id", String.valueOf(book_id));
			attributes.put("number", String.valueOf(number));
			attributes.put("outdate", "'" + outdate + "'");
			dbc.insert("Logs", attributes, wrong);

			/* 修改书籍数量 */
			attributes = new HashMap<String, String>();
			attributes.put("number", String.valueOf(book_num-number));
			conditions = new HashMap<String, String>();
			conditions.put("id", String.valueOf(book_id));
			dbc.update("Books",attributes,conditions,wrong);

			if (wrong.isWrong() == false)
				wrong.printMessage("借书成功。");
		}catch(SQLException throwables)
		{
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
	}

	public void returnBooks(int log_id, String indate)
	{
		try
		{
			Map<String, String> conditions = new HashMap<String, String>();
			Map<String, String> attributes = new HashMap<String, String>();
			List<String> columns = new ArrayList<String>();
			List<String> tables = new ArrayList<String>();
			tables.add("Logs");
			conditions.put("id", String.valueOf(log_id));
			ResultSet result = dbc.select(columns, tables, conditions, wrong);
			if (result.next() == false)
			{
				wrong.addMessage("找不到该记录。");
				wrong.printMessage();
				return;
			}
			int number = Integer.parseInt(result.getString("number"));  //借走的数量
			int book_id = Integer.parseInt(result.getString("book_id"));

			attributes.put("indate", "'" + indate + "'");
			conditions.put("id", String.valueOf(log_id));
			dbc.update("Logs", attributes, conditions, wrong);

			/* 修改书籍数量 */
			conditions = new HashMap<String, String>();
			attributes = new HashMap<String, String>();
			attributes.put("number", "number+" + String.valueOf(number));
			conditions = new HashMap<String, String>();
			conditions.put("id", String.valueOf(book_id));
			dbc.update("Books", attributes, conditions, wrong);

			if (wrong.isWrong() == false)
				wrong.printMessage("还书成功。");
		}catch(SQLException throwables)
		{
			wrong.addMessage(throwables.getMessage());
			wrong.printMessage();
		}
	}
}
