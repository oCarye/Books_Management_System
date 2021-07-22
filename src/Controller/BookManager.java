package Controller;

import Dao.DBConnection;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class BookManager extends LogManager
{
	BookManager(DBConnection dbc, DBError wrong, String username, String password, ResultSet result)
	{  super(dbc,wrong,username,password,result);  }

	public void addBook(Map<String,String> attributes)
	{
		dbc.insert("Books",attributes,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("添加成功。");
	}

	public void changeBook(int book_id, Map<String,String> attributes)
	{
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("id", String.valueOf(book_id));
		dbc.update("Books",attributes,conditions,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("修改成功。");
	}

	public void deleteBook(int book_id)
	{
		Map<String,String> conditions = new HashMap<String,String>();
		conditions.put("id", String.valueOf(book_id));
		dbc.delete("Books",conditions,wrong);
		if (wrong.isWrong() == false)
			wrong.printMessage("删除成功。");
	}
}
