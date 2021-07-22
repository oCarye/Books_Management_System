package Controller;

import Controller.DBError;

public class Check
{
	public static boolean checkId(String string, DBError wrong)
	{
		if (string.equals(""))
		{
			wrong.printMessage("数字类型不能为空！");
			return false;
		}
		try
		{
			Integer.parseInt(string);
		}catch(Exception e)
		{
			wrong.printMessage("数字类型不能有其他字符！");
			return false;
		}
		return true;
	}
}
