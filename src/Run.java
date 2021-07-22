import javax.swing.*;

import View.*;

public class Run
{
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("欢迎进入图书管理系统");
		frame.setContentPane(new Login().mainpanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocation(600,300);
		frame.setVisible(true);
	}
}
